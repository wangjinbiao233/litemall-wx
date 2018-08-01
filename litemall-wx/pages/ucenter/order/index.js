const util = require('../../../utils/util.js');
const api = require('../../../config/api.js');

//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sort: 1,   
    flag:1,
    orderId:1, 
    orderList:{}
  },
  changeTab: function (e) {
    var sort = e.currentTarget.dataset.sort - 0  
    var flag = e.currentTarget.dataset.flag   
    console.log("sort =="+sort+"---- flag =="+flag) 
    this.setData({ 'sort': sort })
    this.setData({ 'flag': flag })
    let that = this;
    var data = {
      userId:  wx.getStorageSync('userId'),
      flag:flag,
      showType: sort
    }
    util.request(api.OrderList, data, 'POST').then(function (res) {     
      if (res.errno === 0) {
        that.setData({
          orderList: res.data.data      
        });
      }
    });

  },
  // “取消订单”点击效果
  cancelOrder: function (e) { 
    let that = this;
    let orderId = e.currentTarget.dataset.orderid;
    wx.showModal({
      title: '',
      content: '确定要取消此订单？',
      success: function (res) {
        if (res.confirm) {
          util.request(api.OrderCancel, {
            orderId: orderId, userId:  wx.getStorageSync('userId')
          }, 'POST').then(function (res) {
            if (res.errno === 0) {
              wx.showToast({
                title: '取消订单成功'
              });
              util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
            }
            else {
              util.showErrorToast(res.errmsg);
            }
          });
        }
      }
    });
  },

  // “取消订单”点击效果
  cancelReserve: function (e) {
    let that = this;
    let reserveId = e.currentTarget.dataset.reserveid;
    wx.showModal({
      title: '',
      content: '确定要取消此预约？',
      success: function (res) {
        if (res.confirm) {
          util.request(api.ReserveCancel, {
            id: reserveId, userId: wx.getStorageSync('userId')
          }, 'POST').then(function (res) {
            if (res.errno === 0) {
              wx.showToast({
                title: '取消预约成功'
              });
              util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
            }
            else {
              util.showErrorToast(res.errmsg);
            }
          });
        }
      }
    });
  },

  // “取消订单并退款”点击效果
  refundOrder: function () {
    let that = this;
    let orderInfo = that.data.orderInfo;

    wx.showModal({
      title: '',
      content: '确定要取消此订单？',
      success: function (res) {
        if (res.confirm) {
          util.request(api.PayRefund, {
            orderId: orderId, userId: wx.getStorageSync('userId')
          }, 'POST').then((res) => {
            console.log(res)
            if (res.errno === 0) {
              util.request(api.OrderRefund, {
                orderId: orderInfo.id
              }, 'POST').then(function (res) {
                if (res.errno === 0) {
                  wx.showToast({
                    title: '取消订单成功'
                  });
                  util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
                }
                else {
                  util.showErrorToast(res.errmsg);
                }
              });
            } else {
            
            }
          });


        }
      }
    });
  },  

  // “去付款”按钮点击效果
  payOrder: function (e) {  
    wx.navigateTo({
      url: "../checkout/checkout?orderId=" + e.currentTarget.dataset.orderid + "&actualPrice=" + e.currentTarget.dataset.actualprice 
    });
  },
  comment: function (e) {    
    wx.navigateTo({ 
      url: "../../commentPost/commentPost?orderId=" + e.currentTarget.dataset.orderid
    });
  },

  // 订单详情点击效果
  orderDetail: function (e) {   
    wx.navigateTo({
      url: "../orderDetail/orderDetail?id=" + e.currentTarget.dataset.orderid + "&showType=" + e.currentTarget.dataset.sort + "&flag=" + e.currentTarget.dataset.flag + "&reserveId=" + e.currentTarget.dataset.reserveid
    });

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var sort = options.sort - 0
    var flag = options.flag 
    this.setData({ 'sort': sort })
    this.setData({ 'flag': flag })
    let that = this;
    var data = {
      userId :  wx.getStorageSync('userId'),
      flag: flag,
      showType: sort
    }
    util.request(api.OrderList,data,'POST').then(function (res) { 
      if (res.errno === 0) {
        that.setData({
          orderList: res.data.data        
        });       
      }
    });


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
    var sort = this.data.sort;
    var flag = this.data.flag;    
    let that = this;
    var data = {
      userId: wx.getStorageSync('userId'),
      flag: flag,
      showType: sort
    }
    util.request(api.OrderList, data, 'POST').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          orderList: res.data.data
        });
      }
    });
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
  
  }
})