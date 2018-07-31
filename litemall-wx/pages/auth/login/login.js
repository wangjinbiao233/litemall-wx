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
    pId: ''
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    let pId = options.pId;
    if (pId && pId != 'undefined') {
      console.log('pId  = ' + pId)
      this.setData({
        pId: pId
      });
    }
    if (app.globalData.hasLogin) {
      wx.switchTab({
        url: '/pages/ucenter/index/index'
      });
    }
  },
  onReady: function () {

  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  wxLogin: function (e) {
    console.log(e)
    var userInfo = e.detail.userInfo;
    let that = this;
    user.checkLogin().catch(() => {

      user.loginByWeixin(that.data.pId, userInfo).then(res => {
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
  accountLogin: function () {
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
      success: function (res) {
        if (res.data.errno == 0) {
          that.setData({
            loginErrorCount: 0
          });
          app.globalData.hasLogin = true;
          wx.setStorageSync('userInfo', res.data.data.userInfo);
          wx.setStorage({
            key: "token",
            data: res.data.data.token,
            success: function () {
              wx.switchTab({
                url: '/pages/ucenter/index/index'
              });
            }
          });
        }
        else {
          that.setData({
            loginErrorCount: that.data.loginErrorCount + 1
          });
          app.globalData.hasLogin = false;
          util.showErrorToast('账户登录失败');
        }
      }
    });
  },
  bindUsernameInput: function (e) {

    this.setData({
      username: e.detail.value
    });
  },
  bindPasswordInput: function (e) {

    this.setData({
      password: e.detail.value
    });
  },
  bindCodeInput: function (e) {

    this.setData({
      code: e.detail.value
    });
  },
  clearInput: function (e) {
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
  }
})