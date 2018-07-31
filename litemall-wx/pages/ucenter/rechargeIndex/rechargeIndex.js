var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: "",
    rechargeMoney: 0,
    rechargeList:{}  
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
  util.request(api.selectRechargeRecord, { userId: userId }, 'POST').then(function (res) {
    if (res.errno === 0) {
      console.log(res)
      that.setData({
        rechargeMoney: res.data.rechargeMoney,
        rechargeList: res.data.items
      });
    }
  });

}