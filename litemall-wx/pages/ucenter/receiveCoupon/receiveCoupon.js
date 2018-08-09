const app = getApp()

var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    allCouponList: '',
    userId: wx.getStorageSync('userId'),
    currentIndex: 1,
  },
  receiveCoupon: function (e) {
    var that = this
    let userid = this.data.userId
    var data = {
      key: e.currentTarget.dataset.key,
      userId: userid
    }
    util.request(api.receiveCoupon, data).then(function (res) {
      if (res.data === 1) {
        console.log(res)
        wx.showToast({
          title: '领取成功',
        })
        getAllCoupon(userid).then((res) => {
          console.log(res)
          var tiems = res.data.items;
          that.setData({
            allCouponList: res.data.items
          });
        })
      } else {
        wx.showToast({
          title: '领取失败',
        })
      }
    })
  },
  search: function (e) {
    var searchInfo = e.detail.value.searchInfo;
    if (searchInfo == '') {
      wx.showToast({
        icon: 'loading',
        title: '请输入优惠券名称'
      })
    } else {
      var that = this;
      var data = {
        userId: this.data.userId,
        page: 1,
        size: 10,
        discountName: searchInfo
      }
      util.request(api.allCoupon, data).then(function (res) {
        if (res.errno === 0) {
          console.log(res.data.items)
          that.setData({
            allCouponList: res.data.items
          });
        }
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var discountType = options.id;   
    getAllCoupon(this.data.userId, discountType).then((res) => {
      console.log(res)
      var tiems = res.data.items;
      that.setData({
        allCouponList: res.data.items
      });
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
    var that = this;
    var data = {
      userId: this.data.userId,
      page: 1,
      size: 10
    }
    util.request(api.allCoupon, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        that.setData({
          allCouponList: res.data.items,
          currentIndex: 1
        });
      }
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    var that = this;
    var currentIndex = this.data.currentIndex += 1;
    var data = {
      userId: this.data.userId,
      page: currentIndex,
      size: 10
    }
    util.request(api.allCoupon, data).then(function (res) {
      if (res.errno === 0) {
        console.log(res)
        if (currentIndex > res.data.pages) {
          wx.showToast({
            title: '没有更多了',
          })
        } else {
          that.setData({
            allCouponList: that.data.allCouponList.concat(res.data.items),
            currentIndex: currentIndex
          });
        }
      }
    })
  }
})
function getAllCoupon(userId,discountType) {
  return new Promise(function (resolve, reject) {
    var data = {
      userId: userId,
      discountType:discountType,
      page: 1,
      size: 10
    }
    util.request(api.allCoupon, data).then(function (res) {
      if (res.errno === 0) {
        resolve(res)
      }
    })

  });
}