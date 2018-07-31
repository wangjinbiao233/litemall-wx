var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const pay = require('../../../services/pay.js');
const user = require('../../../services/user.js');
var interval = null //倒计时函数

var app = getApp();

Page({
  data: {
    orderId: null,
    actualPrice: 0.0,
    payType: 1,
    verificationCode: null,
    verificationCodeInput: null,
    money: 0.00,
    rechargeMoney:0.00,
    showModalStatus: false,
    time: "获取验证码",
    currentTime: 60,
    disabled: false,
    isGetPhone: 0,
    mobile: '',
    code: ''
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数  

    var that = this
    // 页面初始化 options为页面跳转所带来的参数
    util.request(api.getUserInfo, { userId: wx.getStorageSync('userId') },'POST').then(function (res) {
      console.log(res);
      that.setData({
        money: res.user.money,
        rechargeMoney: res.user.rechargeMoney
      });
    });

    this.setData({
      orderId: options.orderId,
      actualPrice: options.actualPrice
    });

  },

  //单选按钮触发事件

  radioChange: function (e) {
    this.data.payType = e.detail.value;
  },

  onReady: function () {
    // 页面渲染完成

  },
  onShow: function () {

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  submitOrder: function () {
    var that = this;
    if (this.data.payType == 1) {
      let that = this;
      util.request(api.PayPrepayId, {
        orderId: this.data.orderId, payType: that.data.payType, userId: wx.getStorageSync('userId')
      }, 'POST').then(function (res) {
        if (res.errno === 0) {

          const payParam = res.data;
          wx.requestPayment({
            'timeStamp': payParam.timeStamp,
            'nonceStr': payParam.nonceStr,
            'package': payParam.package,
            'signType': payParam.signType,
            'paySign': payParam.paySign,
            'success': function (res) {
              wx.redirectTo({
                url: '/pages/payResult/payResult?status=1&orderId=' + that.data.orderId
              });             
            },
            'fail': function (res) {
              wx.redirectTo({
                url: '/pages/payResult/payResult?status=0&orderId=' + that.data.orderId
              });
            }
          });
        }
      });
    } else {
      //余额支付
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
  sendCode: function (e) {
    var that = this;
    if (e.detail.value.verificationCode == '') {
      wx.showToast({
        title: '请输入验证码'
      })
    } else {
      util.request(api.verifyCode, { mobile: that.data.mobile, authCode: e.detail.value.verificationCode }, 'POST').then(function (res) {
        if (res.errno === 0) {

          util.request(api.moneyPay, {
            orderId: that.data.orderId, userId: wx.getStorageSync('userId'), payType: that.data.payType, actualPrice: that.data.actualPrice, money: that.data.money
          }, 'POST').then((res) => {

            if (res.return_code == 'SUCCESS') {
              //支付成功后
              wx.redirectTo({
                url: '/pages/payResult/payResult?status=1&orderId=' + that.data.orderId
              });
            } else {
              util.showErrorToast(res.errmsg);
            }

          }).catch(res => {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=0&orderId=' + that.data.orderId
            });
          });

        }
      })
    }
  }
})