var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({
  data: {
    page: 1,
    list: '',
    total: 0,
    sUserId: 0,
    sort: 0,
    userId: 0

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //获取用户的登录信息
    var that = this;
    let userId = wx.getStorageSync('userId');
    let sort = options.sort;
    let subUserId = options.subUserId;
    console.log(subUserId)
    console.log(sort)
    if (app.globalData.hasLogin) { 
      that.setData({
        'sUserId': subUserId,
        'sort': sort,
        'userId': userId
      });
      getDataInfo(sort, userId, subUserId, this.data.page).then((res) => {
        var tiems = res.data.items;
        var total = res.data.total;
        that.setData({
          'list': tiems,
          'total': total
        });
      })
    }
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
    console.log('下拉下拉')
    var that = this;
    this.setData({
      'page': 1
    })
    getDataInfo(this.data.sort, this.data.userId, this.data.sUserId, this.data.page).then((res) => {
      var tiems = res.data.items;
      var total = res.data.total;
      that.setData({
        'list': tiems,
        'total': total
      });
    })
   
    wx.stopPullDownRefresh();  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log('上拉上拉')
    var that = this;
    var page = this.data.page;
    this.setData({
      'page': page + 1
    })
    if (page * 10 < this.data.total) {
      getDataInfo(this.data.sort, this.data.userId, this.data.sUserId, this.data.page).then((res) => {
        var tiems = res.data.items;
        var total = res.data.total;
        that.setData({
          'list': that.data.list.concat(tiems),
          'total': total
        });
      })
    }
    wx.stopPullDownRefresh();  
  }
})

function getDataInfo(sort, userId, sUserId, page){
  return new Promise(function (resolve, reject) {
    var data = {
      'userId': userId,
      'sUserId': sUserId,
      'page': page,
      'size': 10
    };
    if (sort == 1) {
      util.request(api.SubUserOrderGoodsInfo, data).then(function (res) {
        if (res.errno === 0) {
          console.log(res)
          resolve(res)
        }
      });
    } else if (sort == 2) {
      util.request(api.SubSubUserOrderGoodsInfo, data).then(function (res) {
        if (res.errno === 0) {
          console.log(res)
          resolve(res)
        }
      });
    }
  });
}