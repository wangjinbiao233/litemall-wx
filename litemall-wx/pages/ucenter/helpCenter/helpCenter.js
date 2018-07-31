
var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
var user = require('../../../services/user.js');
var app = getApp();

// pages/ucenter/helpCenter/helpCenter.js
Page({
  /**
   * 组件的初始数据
   */
  data: {
    issueList: []
  },

  
  onLoad: function(option){
    this.getHelpQuestion(); //加载帮助中心数据
  },
  getHelpQuestion(){
    let that = this;
    util.request(api.Issue).then(function (res) {
      if (res.errno === 0) {
        console.log(res);
        that.setData({
          issueList: res.data.issue
        })
      }
    })
  },

   onReady: function () {

  },
  onShow: function () {
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  }
})
