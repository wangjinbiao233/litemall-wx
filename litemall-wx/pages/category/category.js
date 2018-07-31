var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();
Page({
  data: {
    navList: [],
    goodsList: [],
    id: -1,
    currentCategory: {},
    scrollLeft: 0,
    scrollTop: 0,
    scrollHeight: 0,
    page: 1,
    size: 100,
    cartGoodsCount: 0   //购物车中的商品数量
  },
  getCartGoodsCount: function () {
    let that = this;
    if (app.globalData.hasLogin) {
      let userId = wx.getStorageSync('userId');
      util.request(api.CartGoodsCount, { 'userId': userId }).then(function (res) {
        that.setData({
          cartGoodsCount: res.data,
        });
      });
    } else {
      that.setData({
        cartGoodsCount: 0,
      });
    }
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    var that = this;
    if (options.id) {
      that.setData({
        id: parseInt(options.id)
      });
    }
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          scrollHeight: res.windowHeight
        });
      }
    });
    this.getCategoryInfo();

    // that.getGoodsList();
  },
  getCategoryInfo: function () {
    let that = this;
    util.request(api.GoodsCategory, { id: this.data.id })
      .then(function (res) {
        if (res.errno == 0) {
          console.log(res)
          that.setData({
            navList: res.data.categoryList,
            currentCategory: res.data.category,
            id: res.data.category.id
          });
          wx.setNavigationBarTitle({
            title: res.data.category.name
          })
          //nav位置
          let currentIndex = 0;
          let navListCount = that.data.navList.length;
          for (let i = 0; i < navListCount; i++) {
            currentIndex += 1;
            if (that.data.navList[i].id == that.data.id) {
              break;
            }
          }
          if (currentIndex > navListCount / 2 && navListCount > 5) {
            that.setData({
              scrollLeft: currentIndex * 60
            });
          }
          that.getGoodsList();
        } else {
          //显示错误信息
        }     
      });
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.getCartGoodsCount();
  },
  onHide: function () {
    // 页面隐藏
  },
  getGoodsList: function () {
    var that = this;
    util.request(api.GoodsList, {categoryId: that.data.id, page: that.data.page, size: that.data.size})
      .then(function (res) {
        console.log(res)
        that.setData({
          goodsList: res.data.goodsList,
        });
      });
  },
  onUnload: function () {
    // 页面关闭
  },
  switchCate: function (event) {
    if (this.data.id == event.currentTarget.dataset.id) {
      return false;
    }
    var that = this;
    var clientX = event.detail.x;
    var currentTarget = event.currentTarget;
    if (clientX < 60) {
      that.setData({
        scrollLeft: currentTarget.offsetLeft - 60
      });
    } else if (clientX > 330) {
      that.setData({
        scrollLeft: currentTarget.offsetLeft
      });
    }
    this.setData({
      id: event.currentTarget.dataset.id
    });

    this.getCategoryInfo();
  }
})