var util = require('../../utils/util.js');
var api = require('../../config/api.js');
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


  getIndexData: function (goodsId) {
    let that = this;
    var param = {}
    param.id = goodsId
    util.request(api.GoodsStore, param, 'get').then(function (res) {
      console.log(res)
      if (res.errno === 0) {
        that.setData({
          storeList: res.data.stores, 
        });   
      }
    });

    wx.getLocation({
      //定位类型 wgs84, gcj02
      type: 'gcj02',
      success: function (res) {
        console.log(res)
        var latitude = res.latitude
        var longitude = res.longitude
        var store = that.data.storeList;
        for (var i in store) {         
          var storeCoordinate = that.data.storeList[i].storeCoordinate
          var distant = that.getDistance(latitude, longitude, storeCoordinate.split(",")[0], storeCoordinate.split(",")[1]) / 1000
          store[i].distant = Math.floor(distant * 100) / 100;
        }
        store.sort(function (a, b) {
          return a.distant - b.distant;
        })      
        that.setData({
          storeList: store
        });

      },
      fail: function (err) {
        console.log(err)
      },
      complete: function (info) {
        console.log(info)
      },
    })  
  },


  getDistance: function (lat1, lng1, lat2, lng2) {
    lat1 = lat1 || 0;
    lng1 = lng1 || 0;
    lat2 = lat2 || 0;
    lng2 = lng2 || 0;
    var rad1 = lat1 * Math.PI / 180.0;
    var rad2 = lat2 * Math.PI / 180.0;
    var a = rad1 - rad2;
    var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
    var r = 6378137;
    return (r * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)))).toFixed(0)

  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var goodsId = options.goodsId;
    this.getIndexData(goodsId)
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