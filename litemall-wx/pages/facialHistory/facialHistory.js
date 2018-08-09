var app = getApp();
var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
  data: {
    allList: [],
    allCount: 0,
    allPage: 1,
    size: 20,
    userId:''
  },
  getDataList: function(){
    let that = this;
    util.request(api.FacialEvaluatelistData, { 
        userId: that.data.userId, page:that.data.allPage,
        size:that.data.size
      }).then(function (res) {
          console.log(res);
      if (res.errno === 0) {
          that.setData({
              allList: that.data.allList.concat(res.data.facedata),
              allPage: res.data.currentPage,
              allCount: res.data.allcount
          });
      }
    });
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      userId:wx.getStorageSync('userId')
    });
   // this.getDataList();
  },
  onReady: function () {
    // 页面渲染完成
      //this.getDataList()
  },
  onShow: function () {
      this.setData({
          allList: [],
          allPage:1,
          allCount:0
      });
      this.getDataList();
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  toTenD:function(e){
    wx.navigateTo({
      url: '../ucenter/windowchooseProfessional/windowchooseProfessional',
    })
  },
  navigateToPage: function (e) {
      let id = e.currentTarget.dataset.text;
      wx.navigateTo({
          url: '../facialEvaluate/facialEvaluate?faceFourdataId=' + id,
          success: function (res) {
              // success
          },
          fail: function () {
              // fail
          },
          complete: function () {
              // complete
          }
      })
  },
  onReachBottom: function(){
    console.log('onPullDownRefresh');
      if (this.data.allCount / this.data.size < this.data.allPage) {
        return false;
      }

      this.setData({
        'allPage' : this.data.allPage + 1
      });

      this.getDataList();
  }
})