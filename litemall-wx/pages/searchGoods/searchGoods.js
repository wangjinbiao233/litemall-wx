var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();
Page({
  data: {
    goodsList: [],
    id: -1,
    scrollLeft: 0,
    scrollTop: 0,
    scrollHeight: 0,
    page: 1,
    size: 100,
    inputValue: ''
  },
  bindInput: function(e) {
    this.setData({
      inputValue: e.detail.value
    })
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        that.setData({
          scrollHeight: res.windowHeight
        });
      }
    });

  },

  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示

  },
  onHide: function() {
    // 页面隐藏
  },
  getGoodsList: function() {
    var that = this;
    if (that.data.inputValue == null || that.data.inputValue == '') {
      util.showErrorToast("请输入关键字");
      return;
    }
    util.request(api.GoodsList, {
        keyword: that.data.inputValue,
        page: that.data.page,
        size: that.data.size
      })
      .then(function(res) {
        console.log(res)
        if (res.data.goodsList.length > 0) {
          that.setData({
            goodsList: res.data.goodsList,
          });
        }else{
          util.showErrorToast("没有相关商品");
        }
      });
  },
  onUnload: function() {
    // 页面关闭
  },

})