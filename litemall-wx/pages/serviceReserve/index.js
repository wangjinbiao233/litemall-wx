// pages/service/index.js
const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');

Page({
    
  /**
   * 页面的初始数据
   */
  data: {
    storeid: undefined,
    name: undefined,
    img: undefined,
    address: undefined,
    dateList: [],
    dateDetailList: [],
    dateSubscript: 0,
    timeSubscript: 999,
    storeId: undefined,
    selectTime:undefined,
    orderGoodsId:undefined,
    goodsId:undefined,
    selectDate:undefined,
    indexDate:undefined
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      wx.setStorageSync('storeid', ''),
      wx.setStorageSync('name', ''),
      wx.setStorageSync('img', ''),
      wx.setStorageSync('address', '')
  
    var date = new Date()
  
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()

    var selectDate = year+'-'+month+'-'+ day
   
    var param = { storeId: wx.getStorageSync('storeid'),currentTime:selectDate}
    this.setData({
      orderGoodsId:options.orderGoodsId,
      goodsId: options.goodsId,
      selectDate: selectDate,
      indexDate: year + '/' + month + '/' + day,
      storeId: wx.getStorageSync('storeid')

    });
    let that = this;

    util.request(api.StoreService, param, 'get').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          dateDetailList: res.data
        });
        console.log('dateDetailList',that.data.dateDetailList)

      }
    });
  
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.dateInit()

   

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({
      storeid: wx.getStorageSync('storeid'),
      name: wx.getStorageSync('name'),
      img: wx.getStorageSync('img'),
      address: wx.getStorageSync('address')
    });

    var date = new Date()

    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()

    var selectDate = year + '-' + month + '-' + day

    var param = { storeId: wx.getStorageSync('storeid'), currentTime: selectDate }
    this.setData({
      // orderGoodsId: options.orderGoodsId,
      selectDate: selectDate,
      indexDate: year + '/' + month + '/' + day,
      storeId: wx.getStorageSync('storeid')

    });
    let that = this;

    util.request(api.StoreService, param, 'get').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          dateDetailList: res.data
        });
        console.log(that.data.dateDetailList)

      }
    });

  
  },
  changeDate: function (e) {
  
    var date = new Date()
    var index = e.currentTarget.dataset.index
    var year = date.getFullYear()
   
    let that = this
    this.setData({
      dateSubscript: index,
      selectDate: year+ '-'+ e.currentTarget.dataset.date,
      indexDate: year + '/' + e.currentTarget.dataset.date.replace("-","/")
      
    })
   
    //查询预约的数据界面
    var param = { storeId: this.data.storeId, currentTime: this.data.selectDate}
    util.request(api.StoreService, param, 'get').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          dateDetailList: res.data
        });
        console.log(that.data.dateDetailList)

      }
    });
  },

  toOrderTime: function (e) {
    var index = e.currentTarget.dataset.index
       //设置当前时间
    this.setData({
      timeSubscript: index,
      selectTime: e.currentTarget.dataset.time
    })

  

  },
  /**
   * 
   */
  dateInit: function(){
    var that = this
    var today = new Date().getTime()
    var dateList = []
    for (let i = 0; i < 60; i++) {
      var dateVal = (i * 86400000) + today
      var dateObj = that.getDateObj(dateVal)
      dateList.push(dateObj)
    }
    that.setData({
      dateList: dateList
    })
  },
  getDateObj: function (date) {
    var weekList = ["日","一","二","三","四","五","六"]
    var date = new Date(date);
    var obj = {month: date.getMonth()+1, day: date.getDate() < 10 ? '0' + date.getDate() : date.getDate(), week: weekList[date.getDay()]}
    return obj
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },
  getIndexData: function () {

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

  toConfirmTime:  function(){
    // console.log(this.data.orderGoodsId);

    // if (this.data.storeId=='') {
    //   wx.showToast({
    //     image: '/static/images/icon_error.png',
    //     title: '请选择门店'
    //   });
    //   return false;
    // }
  
    if (this.data.selectTime == undefined) {
      wx.showToast({
        image: '/static/images/icon_error.png',
        title: '请选择预约时间'
      });
      return;
    }

    wx.navigateTo({
      url: '../reserveGoods/reserveGoods',
    })

    //更新后台数据
    // var param = {
  
    //   userId: wx.getStorageSync('userId'),
    //   orderGoodsId: this.data.orderGoodsId,
    //   reserveTime: this.data.selectTime,
    //   reserveDate: this.data.selectDate,
    //   storeId: this.data.storeId
            
    // }


    // let that = this;
    // util.request(api.StoerServirceInsert, param,'POST').then(function (res) {
    //   if (res.errno === 0) {
       
    //     console.error('预约成功');     
    //       util.redirect('/pages/ucenter/order/index?sort=1&flag=0');
    //   }else{
    //     util.showErrorToast(res.errmsg);
    //   }
    // });
  },

  toSelectStore: function(e){
    var goodsId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: "/pages/serviceStore/store?goodsId=" + goodsId
    })

  }
})