var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
var user = require('../../../services/user.js');

var app = getApp();
Page({
  data: {
    username: '',
    password: '',
    code: '',
    loginErrorCount: 0,
    showNameModal: false,
    pId: '',
    labelId: '',
    send: false,
    alreadySend: false,
    second: 60,
    disabled: true,
    buttonType: 'default',
    phoneNum: '',
    userInfo: '',
    codeC: ''
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    let pId = options.pId;
    let labelId = options.labelId;
    if (pId && pId != 'undefined') {
      console.log('pId  = ' + pId)
      this.setData({
        pId: pId,
        labelId: labelId
      });
    }
    if (app.globalData.hasLogin) {
      wx.switchTab({
        url: '/pages/ucenter/index/index'
      });
    }
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  wxLogin: function(e) {
    console.log(e)
    var userInfo = this.data.userInfo;
    if (userInfo == undefined || userInfo == '') {
      userInfo = e.detail.userInfo;
    }
    this.setData({
      userInfo: userInfo
    });
    let that = this;
    user.checkLogin().catch(() => {

      user.loginByWeixin(that.data.pId, that.data.labelId, userInfo).then(res => {
        console.log(res.data.userInfo.mobile);
        var mobile = res.data.userInfo.mobile;
        if (mobile == undefined || mobile == '') {
          this.setData({
            showNameModal: true
          });
        } else {
          app.globalData.hasLogin = true;
          if (that.data.pId) {
            wx.switchTab({
              url: '/pages/ucenter/index/index'
            });
          } else {
            wx.navigateBack({
              delta: 1
            })
          }
        }

      }).catch((err) => {
        console.log(err);
        if (err.errno === -2) {
          app.globalData.hasLogin = false;
          util.showErrorToast('该用户已被禁用');
        } else {
          app.globalData.hasLogin = false;
          util.showErrorToast('微信登录失败');
        }
      });
    });
  },
  accountLogin: function() {
    var that = this;

    if (this.data.password.length < 1 || this.data.username.length < 1) {
      wx.showModal({
        title: '错误信息',
        content: '请输入用户名和密码',
        showCancel: false
      });
      return false;
    }

    wx.request({
      url: api.AuthLoginByAccount,
      data: {
        username: that.data.username,
        password: that.data.password
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function(res) {
        if (res.data.errno == 0) {
          that.setData({
            loginErrorCount: 0
          });
          app.globalData.hasLogin = true;
          wx.setStorageSync('userInfo', res.data.data.userInfo);
          wx.setStorage({
            key: "token",
            data: res.data.data.token,
            success: function() {
              wx.switchTab({
                url: '/pages/ucenter/index/index'
              });
            }
          });
        } else {
          that.setData({
            loginErrorCount: that.data.loginErrorCount + 1
          });
          app.globalData.hasLogin = false;
          util.showErrorToast('账户登录失败');
        }
      }
    });
  },
  bindUsernameInput: function(e) {

    this.setData({
      username: e.detail.value
    });
  },
  hideModal: function() {
    this.setData({
      showNameModal: false,
      alreadySend: false,
      send: false,
      disabled: true
    });
  },
  bindPasswordInput: function(e) {

    this.setData({
      password: e.detail.value
    });
  },
  bindCodeInput: function(e) {

    this.setData({
      code: e.detail.value
    });
  },
  clearInput: function(e) {
    switch (e.currentTarget.id) {
      case 'clear-username':
        this.setData({
          username: ''
        });
        break;
      case 'clear-password':
        this.setData({
          password: ''
        });
        break;
      case 'clear-code':
        this.setData({
          code: ''
        });
        break;
    }
  },
  inputPhoneNum: function(e) {
    let phoneNum = e.detail.value
    if (phoneNum.length === 11) {
      let checkedNum = this.checkPhoneNum(phoneNum)
      if (checkedNum) {
        this.setData({
          phoneNum: phoneNum
        })
        console.log('phoneNum' + this.data.phoneNum)
        this.showSendMsg()
        this.activeButton()
      }
    } else {
      this.setData({
        phoneNum: ''
      })
      this.hideSendMsg()
    }
  },
  checkPhoneNum: function(phoneNum) {
    let str = /^1\d{10}$/
    if (str.test(phoneNum)) {
      return true
    } else {
      util.showErrorToast('电话号码错误')
      return false
    }
  },
  showSendMsg: function() {
    if (!this.data.alreadySend) {
      this.setData({
        send: true
      })
    }
  },
  hideSendMsg: function() {
    this.setData({
      send: false,
      disabled: true,
      buttonType: 'default'
    })
  },
  sendMsg: function() {
    var phoneNum = this.data.phoneNum;
    wx.request({
      url: api.SetCodeByPhone,
      data: {
        phoneNum: phoneNum
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function(res) {
        console.log(res)
      }
    })
    this.setData({
      alreadySend: true,
      send: false
    })
    this.timer()
  },
  timer: function() {
    let promise = new Promise((resolve, reject) => {
      let setTimer = setInterval(
        () => {
          this.setData({
            second: this.data.second - 1
          })
          if (this.data.second <= 0) {
            this.setData({
              second: 60,
              alreadySend: false,
              send: true
            })
            resolve(setTimer)
          }
        }, 1000)
    })
    promise.then((setTimer) => {
      clearInterval(setTimer)
    })
  },

  // 验证码

  addCode: function(e) {
    this.setData({
      codeC: e.detail.value
    })
    this.activeButton()
    console.log('code' + this.data.code)
  },

  // 按钮
  activeButton: function() {
    let {
      phoneNum,
      codeC
    } = this.data
    if (phoneNum && codeC) {
      this.setData({
        disabled: false,
        buttonType: 'primary'
      })
    } else {
      this.setData({
        disabled: true,
        buttonType: 'default'
      })
    }
  },
  verifyCodeSubmit: function() {
    var userId = wx.getStorageSync('userId');
    var authCode = this.data.codeC;
    var mobile = this.data.phoneNum;
    var userInfo = this.data.userInfo;
    var that = this;
    wx.request({
      url: api.verifyCodeAndSave,
      data: {
        mobile: mobile,
        userId: userId,
        authCode: authCode
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function(res) {
        console.log(res)
        if (res.data.errno === 0) {
          //说明验证成功

          console.log(userInfo);
          that.wxLogin(userInfo)
        } else if (res.data.errno === 403) {
          //验证失败
          util.showErrorToast(res.data.errmsg + '');
        }
      }
    })


  }

})