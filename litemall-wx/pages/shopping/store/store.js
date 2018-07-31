var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id:undefined,
    storeList: []
   },


  getIndexData: function (id) {
    let that = this;
    var param = { id: ''}
    param.id = id
    util.request(api.GoodsStore, param, 'get').then(function (res) {
      console.log(res)
      if (res.errno === 0) {
        that.setData({
          storeList: res.data.stores,
        });
    
 
      }
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
 
    this.setData({
     
      id: options.id
    })

    this.getIndexData(this.data.id);




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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },


  backGoodsDetail: function (res) {
   
    wx.setStorageSync('storeid', res.currentTarget.dataset.storeid);
    wx.setStorageSync('address', res.currentTarget.dataset.address);
    wx.setStorageSync('img', res.currentTarget.dataset.img);
    wx.setStorageSync('name', res.currentTarget.dataset.name);
 
    wx.navigateBack();//返回前面一个界面
  },


})