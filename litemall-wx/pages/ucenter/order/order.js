var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({
  data:{
    orderList: [],
    showType: 0
  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    this.getOrderList();
  },
  getOrderList(){
    let that = this;    
    var data = {
      userId: wx.getStorageSync('userId'),
      flag:1,   
      showType: 2
    }
    util.request(api.OrderList, data, 'POST').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          orderList: res.data.data
        });
      }
    });

  },
  switchTab: function (event) {
    let showType = event.currentTarget.dataset.index;
    this.setData({
      showType: showType
    });
    this.getOrderList();
  },
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})