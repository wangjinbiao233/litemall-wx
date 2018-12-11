const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');

//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {  
   id:'',
   store: [],
   distant:'',
   goods:[],
   storeCoordinate:''  
  },


  getIndexData: function (id) {
    let that = this;
    var param={id:''}
    param.id = id
    param.userId = wx.getStorageSync('userId')
    util.request(api.Store, param,'POST').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          store: res.data.store,
          goods: res.data.goods
        });

        wx.getLocation({
          //定位类型 wgs84, gcj02
          type: 'gcj02',
          success: function (res) {
            console.log(res)
            var latitude = res.latitude
            var longitude = res.longitude
            var storeCoordinate = that.data.store[0].storeCoordinate
            var distant = that.getDistance(latitude, longitude, storeCoordinate.split(",")[0], storeCoordinate.split(",")[1]) / 1000
            that.setData({
              distant: Math.floor(distant * 100) / 100,
              storeCoordinate: storeCoordinate
            });
          },
          fail: function (err) {
            console.log(err)
          },
          complete: function (info) {
            console.log(info)
          },
        })  
        
        console.log(res.data.store)
        console.log(res.data.goods)      
      }
    });
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
    let that = this
    this.setData({
      id:options.id
    }) 
   
    this.getIndexData(this.data.id);  

  },

  toMap:function (e){
    var that = this
    let storeAddress = e.currentTarget.dataset.storeaddress;
    let storeName = e.currentTarget.dataset.storename;

      wx.openLocation({       
        //当前经纬度
        latitude: Number(that.data.storeCoordinate.split(",")[0]),
        longitude: Number(that.data.storeCoordinate.split(",")[1]),
        //缩放级别默认28
        scale: 28,
        //位置名
        name: storeName,
        //详细地址
        address: storeAddress,
        //成功打印信息
        success: function (res) {
          console.log(res)
        },
        //失败打印信息
        fail: function (err) {
          console.log(err)
        },
        //完成打印信息
        complete: function (info) {
          console.log(info)
        },
      })
     
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

  toGoodsDetail: function (res) {
   
    var id = Number(res.currentTarget.dataset.id) 
   
    wx.navigateTo({
      url: '../goods/goods?id='+id
    })

  },
  toReserve: function (res) {
    wx.navigateTo({
      url: '../serviceReserve/index'
    })
  },
})