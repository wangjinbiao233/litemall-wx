var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({ 
  data: {
    total: 0,
    sort: 1,
    userId: 0,
    userList: [],
    page: 1,
    subCount: 0,
    subSubCount: 0,

  },
  onLoad: function (options) {
    var that = this;
    let uId = options.userId;
    if (uId){
      that.setData({
        userId: uId
      })
      getProfitUser(1, uId, that.data.page).then((res) => {
        var tiems = res.data.items;
        var total = res.data.total;
        console.log(tiems)
        that.setData({
          'userList': tiems,
          'total': total
        });
      })

      getsubUserCount(uId).then((res) => {
        var subCount = res.data.subUserCount;
        that.setData({
          'subCount': subCount
        });
      })

      getSubSubUserCount(uId).then((res) => {
        var subSubCount = res.data.subSubUserCount;
        that.setData({
          'subSubCount': subSubCount
        });
      })

    }


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

    getProfitUser(this.data.sort, this.data.userId, 1).then((res) => {
      var tiems = res.data.items;
      var total = res.data.total;
      that.setData({
        'userList': tiems,
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
    if (page * 10 < this.data.total){
      getProfitUser(this.data.sort, this.data.userId, this.data.page).then((res) => {
        var tiems = res.data.items;
        var total = res.data.total;
        that.setData({
          'userList': that.data.userList.concat(tiems),
          'total': total
        });
      })
    }
    wx.stopPullDownRefresh();  
  },

  changeTab(e) {
    var that = this;
    var sort = e.currentTarget.dataset.sort - 0;
    this.setData({ 
      'sort': sort,
      'page': 1 
      })
    var userId = this.data.userId;
    getProfitUser(sort, userId, 1).then((res) => {
      var tiems = res.data.items;
      var total = res.data.total;
      that.setData({
        userList: tiems,
        total: total
      });
    })
  },
  orderDetails(e) {
    var subUserId = e.currentTarget.dataset.id
    if (app.globalData.hasLogin) {
      wx.navigateTo({ url: "../distributionOrderDetails/distributionOrderDetails?subUserId=" + subUserId + "&sort=" + this.data.sort });
    } else {
      wx.navigateTo({ url: "/pages/auth/login/login"});
    }
  }

})

function getProfitUser(sort, userId, page) {
  return new Promise(function (resolve, reject) {
    var data = {
      userId: userId,
      page: page,
      size: 10
    };
    
    if (sort == 1) {
      util.request(api.ProfitSubUser, data).then(function (res) {

        if (res.errno === 0) {
          resolve(res)
        }
      });
    } else if (sort == 2) {
      util.request(api.ProfitSubSubUser, data).then(function (res) {
     
        if (res.errno === 0) {
          resolve(res)
        }
      });
    }
  });
  
}

function getsubUserCount(userId) {
  return new Promise(function (resolve, reject) {
    var data = {
      userId: userId
    }
    util.request(api.AuthSubUserCount, data, 'POST').then(function (res) {
     
      if (res.errno === 0) {
        resolve(res)
      }
    });
  });
}

function getSubSubUserCount(userId) {
  return new Promise(function (resolve, reject) {
    var data = {
      userId: userId
    }
    util.request(api.AuthSubSubUserCount, data, 'POST').then(function (res) {

      if (res.errno === 0) {
        resolve(res)
      }
    });
  });
}