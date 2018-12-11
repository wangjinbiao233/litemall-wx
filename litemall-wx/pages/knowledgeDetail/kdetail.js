var WxParse = require('../../lib/wxParse/wxParse.js');
var util = require('../../utils/util.js');
var api = require('../../config/api.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    knowledge: {},
    id: 0,
    count: 0,
    comments: [],
    imgList: {},
    goodsList: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let that = this;
    if (options.id) {
      that.setData({
        id: parseInt(options.id)
      })
    }
    this.getKnowledge();
  },
  getKnowledge: function() {
    let that = this;
    util.request(api.KnowledgeDetail, {
      id: this.data.id
    }).then((res) => {
      that.setData({
        knowledge: res.data.knowledge,
        goodsList: res.data.knowledge.goodsList
      })
      wx.setNavigationBarTitle({
        title: res.data.knowledge.title
      })
      WxParse.wxParse('detail', 'html', res.data.knowledge.content, that);
    })
  },
  getComment: function() {
    let that = this;
    util.request(api.KnowledgeComment, {
      userId: wx.getStorageSync('userId'),
      valueId: this.data.id,
      typeId: 2
    }).then((res) => {
      console.log(res);
      that.setData({
        count: res.data.count,
        comments: res.data.datas,
        imgList: res.data.imgList
      })
    })
  },
  wxParseTagATap: function(e) {
    console.log('执行了wxParseTagATap');
    let link = e.currentTarget.dataset.src
    if (link.indexOf('/pages') === 0) {
      wx.navigateTo({
        url: link,
        fail: (res) => {
          console.log('跳转失败');
        }
      })
    } else {
      console.log('这是外链，无法跳转');
    }

  },
  imgYu: function(event) {
    let src = event.currentTarget.dataset.src; //获取data-src
    let imgList = this.data.imgList; //获取data-list
    //图片预览
    wx.previewImage({
      current: src, // 当前显示图片的http链接
      urls: imgList // 需要预览的图片http链接列表
    })
  },
  changehide: function(e) { //一级点赞
    //var bgColor = this.data.pageBackgroundColor == 'red' ? '#5cb85c' : 'red';
    let index = e.currentTarget.dataset.text;
    let status = this.data.comments[index].islike == true ? false : true;
    let islike = "comments[" + index + "].islike"; //渣渣微信
    let wlikenum = "comments[" + index + "].likenum"; //渣渣微信;
    let dcount = 0;
    if (status == false) { //取消赞 减少
      dcount = this.data.comments[index].likenum - 1;
    } else {
      dcount = this.data.comments[index].likenum + 1;
    }
    this.setData({ //刷新数量和图标
      [islike]: status,
      [wlikenum]: dcount
    })
    let commentid = this.data.comments[index].id;
    // let islike = this.data.comments[index].islike;
    let userid = wx.getStorageSync('userId');
    this.liketag(userid, commentid, status); //后台更新
  },
  zchangehide: function(e) { //二级点赞
    let index = e.currentTarget.dataset.text;
    let subindex = e.currentTarget.dataset.textb;
    let status = this.data.comments[index].subcomment[subindex].islike == true ? false : true;
    let islike = "comments[" + index + "].subcomment[" + subindex + "].islike";
    let wlikenum = "comments[" + index + "].subcomment[" + subindex + "].likenum";
    let dcount = 0;
    if (status == false) { //取消赞 减少
      dcount = this.data.comments[index].subcomment[subindex].likenum - 1;
    } else {
      dcount = this.data.comments[index].subcomment[subindex].likenum + 1;
    }
    this.setData({ //刷新数量和图标
      [islike]: status,
      [wlikenum]: dcount
    })
    let commentid = this.data.comments[index].subcomment[subindex].id;
    let userid = wx.getStorageSync('userId');
    this.liketag(userid, commentid, status); //后台更新
  },
  liketag(userid, commentid, islike) {
    util.request(api.Knowledgeliketag, {
      userId: userid,
      commentId: commentid,
      islike: islike
    }).then((res) => {
      console.log(res.errmsg);
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    this.getComment();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})