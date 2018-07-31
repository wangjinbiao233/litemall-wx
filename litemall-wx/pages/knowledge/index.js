// pages/knowledge/index.js
var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    scrollLeft: 0,
    scrollTop: 0,
    scrollHeight: 0,
    //scrollHeight:500,
    kCategory:-1,
    page: 1,
    size: 100,
    knowledgeList:[],
    kCategoryList:[],
    kCategoryName:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this
    let knowledgeId = app.knowledgeId;
    if (knowledgeId){
      that.setData({
        kCategory: parseInt(knowledgeId)
      })
    }
    wx.getSystemInfo({
      success: (res)=> {
        that.setData({
          scrollHeight: res.windowHeight*750/res.windowWidth-280
        });
      }
    });
    this.getkCategoryInfoList()
  },
  getkCategoryInfoList:function(){//获取知识分类信息
    let that = this;
    console.log(that.data.kCategory);
    wx.showLoading({
      title: '加载中...',
    });
    util.request(api.kCategoryInfoList, { kCategoryId: that.data.kCategory}).then(function (res) {
      console.log(res.data.kCategoryList);
      that.setData({
        kCategoryList: res.data.kCategoryList,
        kCategory: res.data.kCategory
      });
      wx.hideLoading();
      that.getKnowledgeByClsId();
    });
  },
  getKnowledgeByClsId:function(){//获取某个知识分类的知识列表信息
    let that = this;
    util.request(api.KnowledgeList, { kCategoryId: that.data.kCategory, page: that.data.page, size: that.data.size })
      .then(function (res) {
        that.setData({
          knowledgeList: res.data.knowledgeList,
          kCategory: res.data.kCategory
        });
        for (var i = 0; i < that.data.knowledgeList.length;i++){
          that.setData({
            ['knowledgeList[' + i + '].isShow']: false, 
          });
        }
        console.log(res.data);
      });
  },
  touchChange: function (e) {//知识点赞    
    let index = e.currentTarget.dataset.text;
    let status = this.data.knowledgeList[index].isShow == true ? false : true;
    let isShow = "knowledgeList[" + index + "].isShow";
    let praisenum = "knowledgeList[" + index + "].praiseCount";
    let dcount = 0;
    console.log("status ==" + status);
    if (status == false) {//取消赞 减少
      dcount = this.data.knowledgeList[index].praiseCount - 1;
    } else {
      dcount = this.data.knowledgeList[index].praiseCount + 1;
    }
    this.setData({//刷新数量和图标
      [isShow]: status,
      [praisenum]: dcount
    })
    let kid = this.data.knowledgeList[index].id;    
    let userid = wx.getStorageSync('userId');
    this.liketag(userid, kid, status);//后台更新
  },
  liketag(userid, kid, islike) {//后台更新数据库点赞数
    util.request(api.KnowledgePointliketag,
      { userId: userid, knowldegeId: kid, islike: islike }).then((res) => {
        console.log(res.errmsg);
      })
  },
  switchKnowledge: function(event){//切换头部tab标签
    var currentTarget = event.currentTarget;
    if (this.data.kCategory == currentTarget.dataset.kcategoryid) {
      return false;
    }
    this.setData({
      kCategory: event.currentTarget.dataset.kcategoryid
    });
    this.getKnowledgeByClsId();
  },

  changeSort: function (e) {
    var sort = e.currentTarget.dataset.sort - 0
    this.setData({'sort': sort})
    this.setData({'sortName': this.data.sortList[sort]})
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
    let that = this
    let kc = that.data.kCategory
    let knowledgeId = app.knowledgeId;
    if (knowledgeId && kc != knowledgeId){
      that.setData({
        kCategory: knowledgeId
      })
      that.getkCategoryInfoList()
    }
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