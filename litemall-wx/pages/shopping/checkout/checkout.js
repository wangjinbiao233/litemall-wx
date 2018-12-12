var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const pay = require('../../../services/pay.js');
const user = require('../../../services/user.js');
var interval = null //倒计时函数

var app = getApp();
Page({
  data: {
    checkedGoodsList: [],
    checkedAddress: {},
    checkedCoupon: [],
    couponList: [],
    goodsTotalPrice: 0.00, //商品总价
    freightPrice: 0.00,    //快递费
    couponPrice: 0.00,     //优惠券的价格
    orderTotalPrice: 0.00,  //订单总价
    actualPrice: 0.00,     //实际需要支付的总价
    cartId: 0,
    addressId: 0,
    couponId: 0,
    payType: 1,
    radioFlag: 1,
    verificationCode: null,
    verificationCodeInput: null,
    money: 0.00,
    rechargeMoney: 0.00,
    showModalStatus: false,
    time: "获取验证码",
    currentTime: 60,
    disabled: false,
    isGetPhone: 0,
    mobile: '',
    code: '',
    pageFlag:1,
    storeName: [],
    storeId: [],
    storeIndex: 0
  },
  onLoad: function (options) {
    var that = this
    // 页面初始化 options为页面跳转所带来的参数
    console.log(wx.getStorageSync('userId'));
    util.request(api.getUserInfo, { userId: wx.getStorageSync('userId') }, 'POST').then(function (res) {
      that.setData({
        money: res.user.money,
        rechargeMoney: res.user.rechargeMoney,
        pageFlag:options.flag
      });
    });
    wx.setStorageSync('couponId', 0);
  },
  getCheckoutInfo: function () {
    let that = this;
    util.request(api.CartCheckout, { cartId: that.data.cartId, addressId: that.data.addressId, couponId: that.data.couponId, userId: wx.getStorageSync('userId') }, 'POST').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          checkedGoodsList: res.data.checkedGoodsList,
          checkedAddress: res.data.checkedAddress,
          actualPrice: res.data.actualPrice,
          checkedCoupon: res.data.checkedCoupon,
          couponList: res.data.couponList,
          couponPrice: res.data.couponPrice,
          freightPrice: res.data.freightPrice,
          goodsTotalPrice: res.data.goodsTotalPrice,
          orderTotalPrice: res.data.orderTotalPrice,
          addressId: res.data.addressId,
          pageFlag: res.data.flag
        });
      }
      wx.hideLoading();
    });
  },
  selectAddress() {
    wx.navigateTo({
      url: '/pages/shopping/address/address',
    })
  },

  selectCoupon() {
    wx.navigateTo({
      url: '/pages/shopping/coupon/coupon?goodsTotalPrice=' + this.data.goodsTotalPrice,
    })
  },

  addAddress() {
    wx.navigateTo({
      url: '/pages/shopping/addressAdd/addressAdd',
    })
  },

  //单选按钮触发事件
  radioChange: function (e) {
    this.data.payType = e.detail.value;
  },
  //地址选取和店面自取的单选触发事件
  radioChoose: function(e){
    this.setData({
      'radioFlag': e.detail.value
    });
    if(e.detail.value == 2){
      //此处选取店面自取
      var storeName=[];
      var storeId=[];
      var that=this;
      wx.request({
        url: api.ServiceStore,
        header: {
          "content-type": "application/x-www-form-urlencoded"
        },
        method: 'GET',
        success: function (res) {
          var data = res.data
          var stores = data.data.stores;
          console.log(stores);
          if (stores != undefined) {
            for (var i = 0; i < stores.length; i++) {
              storeName.push(stores[i].storeName)
              storeId.push(stores[i].id)
            }

            that.setData({ storeName: storeName, storeId: storeId});
       
          }

          console.log(storeName,storeId);
        }
      })
    }else{
      var that = this;
      that.setData({
        storeName:[],
        storeId:[],
        storeIndex: 0
      })
    }
  },

  storePickerChange:function(e){
    this.setData({
      storeIndex: e.detail.value
    })
  },

  onReady: function () {
    // 页面渲染完成

  },
  onShow: function () {
    // 页面显示
    wx.showLoading({
      title: '加载中...',
    })
    try {
      var cartId = wx.getStorageSync('cartId');
      console.log(cartId);
      if (cartId) {
        this.setData({
          'cartId': cartId
        });
      }

      var addressId = wx.getStorageSync('addressId');
      if (addressId) {
        this.setData({
          'addressId': addressId
        });
      }

      var couponId = wx.getStorageSync('couponId');
      if (couponId) {
        this.setData({
          'couponId': couponId
        });
      }
    } catch (e) {
      // Do something when catch error
      console.log(e);
    }
    this.getCheckoutInfo();
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  submitOrder: function () {
    var that = this;
    if (this.data.addressId <= 0 && this.data.pageFlag == 1 && this.data.radioFlag == 1) {
      util.showErrorToast('请选择收货地址');
      return false;
    }
    if (this.data.storeId.length <= 0 && this.data.pageFlag == 1 && this.data.radioFlag == 2){
      util.showErrorToast('没有门店，不能自取');
      return false;
    }

    if (this.data.payType == 1) {
      var storeId = wx.getStorageSync('storeid')
      console.log(wx.getStorageSync('storeid'))
      var radioFlag = parseInt(that.data.radioFlag);
      //storeId: storeId,
      util.request(api.OrderSubmit, { storeId: storeId, cartId: that.data.cartId, radioFlag: radioFlag, pageFlag:that.data.pageFlag,storeIds: that.data.storeId,storeIndex:that.data.storeIndex,addressId: that.data.addressId, couponId: that.data.couponId, payType: that.data.payType, userId: wx.getStorageSync('userId') }, 'POST').then(res => {
        if (res.errno === 0) {
          const orderId = res.data.orderInfo.id;

          // 微信统一下单，获取支付参数
          pay.payOrder(orderId).then(res => {
            //支付成功后

            wx.redirectTo({
              url: '/pages/payResult/payResult?status=1&orderId=' + orderId
            });
          }).catch(res => {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=0&orderId=' + orderId
            });
          });
        }
      });
    } else {
      //余额支付或存储金支付
      var phoneNo = that.data.mobile;
      var actualPrice = this.data.actualPrice;

      if (this.data.payType == 2) {
        var money = this.data.money;
        if (money < actualPrice) {
          util.showErrorToast('余额不足');
          return false;
        }
      } else {
        var rechargeMoney = this.data.rechargeMoney;
        if (rechargeMoney < actualPrice) {
          util.showErrorToast('余额不足');
          return false;
        }
      }

      if (phoneNo == '' || phoneNo == null || phoneNo == undefined) {
        that.setData({
          isGetPhone: 1
        })
        setTimeout(function () {
          that.setData({ showModalStatus: true })
        }, 500);
        return new Promise(function (resolve, reject) {
          return user.login().then((res) => {
            console.log(res);
            that.setData({
              code: res.code
            })
          })
        })
      } else {
        that.setData({ showModalStatus: true });
      }
    }
  },
  powerDrawer: function (e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.util(currentStatu)
  },
  util: function (currentStatu) {
    /* 动画部分 */
    // 第1步：创建动画实例 
    var animation = wx.createAnimation({
      duration: 200, //动画时长 
      timingFunction: "linear", //线性 
      delay: 0 //0则不延迟 
    });

    // 第2步：这个动画实例赋给当前的动画实例 
    this.animation = animation;

    // 第3步：执行第一组动画 
    animation.opacity(0).rotateX(-100).step();

    // 第4步：导出动画对象赋给数据对象储存 
    this.setData({
      animationData: animation.export()
    })

    // 第5步：设置定时器到指定时候后，执行第二组动画 
    setTimeout(function () {
      // 执行第二组动画 
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
      this.setData({
        animationData: animation
      })

      //关闭 
      if (currentStatu == "close") {
        this.setData(
          {
            showModalStatus: false
          }
        );
      }
    }.bind(this), 200)
  },
  getCode: function () {
    var that = this;
    var currentTime = that.data.currentTime
    interval = setInterval(function () {
      currentTime--;
      that.setData({
        time: currentTime + '秒'
      })
      if (currentTime <= 0) {
        clearInterval(interval)
        that.setData({
          time: '获取验证码',
          currentTime: 60,
          disabled: false
        })
      }
    }, 1000)
  },
  getVerificationCode: function () {
    this.getCode();
    var that = this
    that.setData({
      disabled: true
    })
    util.request(api.getVerificationCode, { mobile: that.data.mobile }, 'POST').then(res => {
      if (res.errno === 0) {
        this.data.verificationCode = res.data.verificationCode;
      } else {
        util.showErrorToast(res.errmsg);
      }
    });
  },
  getNumber: function (e) {
    var that = this;
    var iv = e.detail.iv;
    var encryptedData = e.detail.encryptedData;
    var data = {
      code: this.data.code,
      iv: e.detail.iv,
      encryptedData: e.detail.encryptedData
    };
    util.request(api.getPhoneNo, data, 'POST').then(res => {
      console.log(res);
      if (res.phoneNumber) {
        that.setData({
          isGetPhone: 0,
          mobile: res.phoneNumber
        })
        that.getVerificationCode();
      } else {
        util.showErrorToast(res.errmsg);
      }
    });
  },
  sendCode: function (e) {
    var that = this;
    if (e.detail.value.verificationCode == '') {
      wx.showToast({
        title: '请输入验证码'
      })
    } else {
      util.request(api.verifyCode, { mobile: that.data.mobile, authCode: e.detail.value.verificationCode }, 'POST').then(function (res) {
        if (res.errno === 0) {
          var radioFlag = parseInt(that.data.radioFlag);
          var storeId = wx.getStorageSync('storeid');
          util.request(api.OrderSubmit, { payType: that.data.payType, storeId: storeId, cartId: that.data.cartId, radioFlag: radioFlag, pageFlag: that.data.pageFlag,storeIds: that.data.storeId, storeIndex: that.data.storeIndex, addressId: that.data.addressId, couponId: that.data.couponId, userId: wx.getStorageSync('userId') }, 'POST').then(res => {
            if (res.errno === 0) {
              const orderId = res.data.orderInfo.id;
              util.request(api.moneyPay, {
                payType: that.data.payType,
                orderId: orderId, 
                userId: wx.getStorageSync('userId'),
                actualPrice: that.data.actualPrice,
                money: that.data.money
              }, 'POST').then((res) => {

                if (res.return_code == 'SUCCESS') {
                  //支付成功后
                  wx.redirectTo({
                    url: '/pages/payResult/payResult?status=1&orderId=' + orderId
                  });
                } else {
                  util.showErrorToast(res.errmsg);
                }

              }).catch(res => {
                wx.redirectTo({
                  url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                });
              });

            }
          });
        } else {
          util.showErrorToast(res.errmsg);
        }
      })
    }
  }
})