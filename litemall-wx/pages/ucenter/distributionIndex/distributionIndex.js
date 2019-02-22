var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: "",
    subUserTotal: 0,
    noCashNum: '',
    cashNum: '',
    cashDetailNum: ''
  },

  //分享链接
  onShareAppMessage: function (options) {
    var that = this;
    // 设置菜单中的转发按钮触发转发事件时的转发内容
    var shareObj = {
      title: "微商城",
      path: '/pages/auth/login/login?pId=' + that.data.userId,
      imageUrl:'http://mall.philab.net/images/system/distribution_share.jpg',
      success: function (res) {
        // 转发成功之后的回调
        if (res.errMsg == 'shareAppMessage:ok') {
          console.log('分享成功')
        }
      },
      fail: function (res) {
        // 转发失败之后的回调
        if (res.errMsg == 'shareAppMessage:fail cancel') {
          console.log('取消分享')
        } else if (res.errMsg == 'shareAppMessage:fail') {
          console.log('err 转发失败')
        }
      },
      complete: function (res) {

        // 转发结束之后的回调（转发成不成功都会执行）
      }
    };
    // 来自页面内的按钮的转发
    if (options.from == 'button') {
      console.log(options)
      var eData = options.target.dataset;
      // 此处可以修改 shareObj 中的内容
      //shareObj.path = '/pages/btnname/btnname?
      console.log('path = ' + shareObj.path)
    }
    // 返回shareObj
    return shareObj;


  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //获取用户的登录信息
    var that = this;
    let userId = wx.getStorageSync('userId');
    if (app.globalData.hasLogin) {
      that.setData({
        userId: userId
      });
      getDataInfo(this);
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    getDataInfo(this);
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  }
  
})

function getDataInfo(that) {
  let userId = wx.getStorageSync('userId');
  util.request(api.AuthSubUserTotalCount, { userId: userId }, 'POST').then(function (res) {
    if (res.errno === 0) {
      console.log(res)
      that.setData({
        subUserTotal: res.data.subUserCount
      });
    }
  });

  util.request(api.noCashNum, { userId: userId }).then(function (res) {
    if (res.errno === 0) {
      console.log(res)
      that.setData({
        noCashNum: res.data.toFixed(2)
      });
    }
  })
  util.request(api.cashNum, { userId: userId }).then(function (res) {
    console.log(res)
    if (res.errno === 0) {
      that.setData({
        cashNum: res.data.toFixed(2)
      });
    }
  })
  util.request(api.cashDetailNum, { userId: userId }).then(function (res) {
    if (res.errno === 0) {
        that.setData({
          cashDetailNum: parseFloat(res.data).toFixed(2)
        });
    }
  })

}