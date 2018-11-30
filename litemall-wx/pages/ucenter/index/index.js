var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var user = require('../../../services/user.js');
var app = getApp();

Page({
  data: {
    pId: '',
    labelId: '',
    userInfo: {
      nickname: '点击登录',
      avatar: 'http://yanxuan.nosdn.127.net/8945ae63d940cc42406c3f67019c5cb6.png',
      showModalStatus: false
    },
    hasLogin: false,
    showmodal: false,
    taglist: '',
    tagarray: [],
    tagindex: 0
  },
  showDialogBtn: function() {
    var that = this;
    util.request(api.getDistributionTag, {
      pId: that.data.pId
    }, 'GET').then(function(res) {
      var labelList = []
      for (var i = 0; i < res.data.labelList.length; i++) {
        var labelName = res.data.labelList[i].labelName
        labelList.push(labelName)
      }
      console.log(res);
      that.setData({
        tagarray: labelList
      })
    })
    this.setData({
      showmodal: true
    })
  },
  hidemodal: function() {
    this.setData({
      showmodal: false
    })
  },
  onCancel: function() {
    this.hidemodal()
  },
  onConfirm: function() {
    var that = this;
    for (var i = 0; i < this.data.taglist.length; i++) {
      if (that.data.tagarray[that.data.tagindex] == that.data.taglist[i].labelName) {
        that.setData({
          labelId: that.data.taglist[i].labelId
        })
        wx.showToast({
          title: '选择成功',
        })
        that.hidemodal();
      }
    }
  },
  bindtagChange: function(e) {
    this.setData({
      tagindex: e.detail.value
    })
  },
  onLoad: function(options) {
    var that = this;
    // 页面初始化 options为页面跳转所带来的参数
    // options 中的 scene 需要使用 decodeURIComponent 才能获取到生成二维码时传入的 scene
    if (options) {
      var scene = decodeURIComponent(options.scene)
      console.log('pId == ' + scene);
      if (scene && scene != 'undefined') {
        if (scene.indexOf('labelId' != -1)) {
          that.setData({
            labelId: scene.substring(8)
          })
        } else {
          that.setData({
            'pId': scene
          })
          that.showDialogBtn();
        }
      }
    }
  },
  onReady: function() {

  },
  onShow: function() {

    //获取用户的登录信息
    if (app.globalData.hasLogin) {
      let userInfo = wx.getStorageSync('userInfo');
      this.setData({
        userInfo: userInfo,
        hasLogin: app.globalData.hasLogin
      });
    }
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  },
  goLogin() {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId + "&labelId=" + this.data.labelId
      });
    }
  },
  goReservation() {
    wx.navigateTo({
      url: "/pages/ucenter/reservation/index"
    });
  },
  goOrderList(e) {
    var sort = e.currentTarget.dataset.sort - 0
    var flag = e.currentTarget.dataset.flag - 0
    wx.navigateTo({
      url: "/pages/ucenter/order/index?sort=" + sort + "&flag=" + flag
    });
  },
  goOrder() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/order/index"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    }
  },
  goCoupon() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/coupon/coupon"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };

  },
  goCollect() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/collect/collect"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };
  },
  goFootprint() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/footprint/footprint"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };
  },
  goDistribution() {
    var that = this
    if (app.globalData.hasLogin) {
      util.request(api.getUserInfo, {
        userId: wx.getStorageSync('userId')
      }, 'POST').then(function(res) {
        console.log(res);
        var distributionPartner = res.user.distributionPartner
        if (distributionPartner === true) {
          wx.navigateTo({
            url: "/pages/ucenter/distributionIndex/distributionIndex"
          });
        } else {
          util.request(api.selectApplyByUserId, {
            userId: wx.getStorageSync('userId')
          }, 'POST').then(function(res) {
            if (res.data != null && res.data.auditStatus == 0) {
              util.showErrorToast('审核中')
            } else if (res.data != null && res.data.auditStatus == 2) {
              wx.navigateTo({
                url: "/pages/ucenter/distributionApply/distributionApply"
              });
            } else {
              wx.showModal({
                title: '提示',
                content: '是否成为分销合作伙伴',
                success: function(res) {
                  if (res.confirm) {
                    wx.navigateTo({
                      url: "/pages/ucenter/distributionApply/distributionApply"
                    });
                  } else if (res.cancel) {
                    console.log('用户点击取消')
                  }
                }
              })
            }

          })


        }
      });

    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };
  },

  goRecharge() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/rechargeIndex/rechargeIndex"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };
  },
  goAddress() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/address/address"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };
  },
  goHelpCenter() {
    if (app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/ucenter/helpCenter/helpCenter"
      });
    } else {
      wx.navigateTo({
        url: "/pages/auth/login/login?pId=" + this.data.pId
      });
    };
  },

  goConnectCenter() {
    var phoneNumber;
    util.request(api.selectDistributionTypeList, {
      groupCode: 'phone'
    }, 'POST').then(function(res) {
      if (res.errno === 0) {
        phoneNumber = res.data[0].value;
        wx.showActionSheet({
          itemList: [phoneNumber, '呼叫'],
          success: function(res) {
            if (res.tapIndex === 0) {

            } else if (res.tapIndex === 1) {
              wx.makePhoneCall({
                phoneNumber: phoneNumber,
              })
            }
          },
          fail: function(res) {
            console.log(res.errMsg)
          }
        })
      }
    });



  },
  exitLogin: function() {
    var that = this;
    var nickname = 'userInfo.nickname'
    var avatar = 'userInfo.avatar'
    wx.showModal({
      title: '',
      confirmColor: '#b4282d',
      content: '退出登录？',
      success: function(res) {
        if (res.confirm) {
          app.globalData.hasLogin = false;

          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.removeStorageSync('userId');
          wx.removeStorageSync('isDistributionPartner');

          that.setData({
            [nickname]: '点击登录',
            [avatar]: 'http://yanxuan.nosdn.127.net/8945ae63d940cc42406c3f67019c5cb6.png'
          })

          wx.switchTab({
            url: '/pages/index/index'
          });
        }
      }
    })
  },
  windowchoose: function(e) {
    let that = this;
    let currentStatu = e.currentTarget.dataset.statu;
    that.yz_util(currentStatu);
  },
  chooseImages: function(e) {
    let that = this;
    that.yz_util('close');

    let ismyself = e.currentTarget.dataset.statu;

    if (ismyself == 'myself') {
      console.log('takePhoto ismyself=' + ismyself);
      wx.navigateTo({
        url: '/pages/takePhoto/takePhoto'
      })
    } else {
      console.log('friend ismyself=' + ismyself);
      wx.navigateTo({
        url: '/pages/takePhoto/friend'
      })
    }
  },
  navigateToPages: function() {
    console.log('进入../../facialHistory/facialHistory');
    wx.navigateTo({
      url: '../../facialHistory/facialHistory'
    })
  },
  powerDrawer: function(e) {
    let currentStatu = e.currentTarget.dataset.statu;
    this.yz_util(currentStatu)
  },
  yz_util: function(currentStatu) {
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
    setTimeout(function() {
      // 执行第二组动画 
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
      this.setData({
        animationData: animation
      })

      //关闭 
      if (currentStatu == "close") {
        this.setData({
          showModalStatus: false
        });
      }
    }.bind(this), 200)

    // 显示 
    if (currentStatu == "open") {
      this.setData({
        showModalStatus: true
      });
    }
  }


})