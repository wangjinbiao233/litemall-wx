var app = getApp();
var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
  data: {
    comments: [],
    allCommentList: [],
    picCommentList: [],
    typeId: 0,
    valueId: 0,
    showType: 0,
    allCount: 0,
    hasPicCount: 0,
    allPage: 1,
    picPage: 1,
    size: 20,
    userId:'',
    imgList: {}
  },
  getCommentCount: function () {
    let that = this;
    util.request(api.CommentCount, { valueId: that.data.valueId, typeId: that.data.typeId}).then(function (res) {
      if (res.errno === 0) {
        that.setData({
          allCount: res.data.allCount,
          hasPicCount: res.data.hasPicCount
        });
      }
    });
  },
  getCommentList: function(){
    let that = this;
    util.request(api.CommentListdata, { 
      valueId: that.data.valueId, 
      typeId: that.data.typeId, 
      size: that.data.size,
      page: (that.data.showType == 0 ? that.data.allPage : that.data.picPage),
      showType: that.data.showType,
      userId: that.data.userId
      }).then(function (res) {
      if (res.errno === 0) {

        if (that.data.showType == 0) {
          that.setData({
            allCommentList: that.data.allCommentList.concat(res.data.data),
            allPage: res.data.currentPage,
            comments: that.data.allCommentList.concat(res.data.data),
            imgList: res.data.imgList
          });
        } else {
          that.setData({
            picCommentList: that.data.picCommentList.concat(res.data.data),
            picPage: res.data.currentPage,
            comments: that.data.picCommentList.concat(res.data.data),
            imgList: res.data.imgList
          });
        }
      }
    });
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      typeId: options.typeId,
      valueId: options.valueId,
      userId:wx.getStorageSync('userId')
    });
    this.getCommentCount();
    this.getCommentList();
  },
  imgYu: function (event) {
      let src = event.currentTarget.dataset.src;//获取data-src
      let imgList = this.data.imgList;//获取data-list
      //图片预览
      wx.previewImage({
          current: src, // 当前显示图片的http链接
          urls: imgList // 需要预览的图片http链接列表
      })
  },
  changehide: function (e) {//一级点赞
      //var bgColor = this.data.pageBackgroundColor == 'red' ? '#5cb85c' : 'red';
      let index = e.currentTarget.dataset.text;
      let status = this.data.comments[index].islike == true ? false : true;
      let islike = "comments[" + index + "].islike";//渣渣微信
      let wlikenum = "comments[" + index + "].likenum";//渣渣微信;
      let dcount = 0;
      if (status == false) {//取消赞 减少
          dcount = this.data.comments[index].likenum - 1;
      } else {
          dcount = this.data.comments[index].likenum + 1;
      }
      this.setData({//刷新数量和图标
          [islike]: status,
          [wlikenum]: dcount
      })
      let commentid = this.data.comments[index].id;
      // let islike = this.data.comments[index].islike;
      let userid = wx.getStorageSync('userId');
      this.liketag(userid, commentid, status);//后台更新
  },
  zchangehide: function (e) {//二级点赞
      let index = e.currentTarget.dataset.text;
      let subindex = e.currentTarget.dataset.textb;
      let status = this.data.comments[index].subcomment[subindex].islike == true ? false : true;
      let islike = "comments[" + index + "].subcomment[" + subindex + "].islike";
      let wlikenum = "comments[" + index + "].subcomment[" + subindex + "].likenum";
      let dcount = 0;
      if (status == false) {//取消赞 减少
          dcount = this.data.comments[index].subcomment[subindex].likenum - 1;
      } else {
          dcount = this.data.comments[index].subcomment[subindex].likenum + 1;
      }
      this.setData({//刷新数量和图标
          [islike]: status,
          [wlikenum]: dcount
      })
      let commentid = this.data.comments[index].subcomment[subindex].id;
      let userid = wx.getStorageSync('userId');
      this.liketag(userid, commentid, status);//后台更新
  },
  liketag(userid, commentid, islike) {
      util.request(api.Knowledgeliketag,
          { userId: userid, commentId: commentid, islike: islike }).then((res) => {
              console.log(res.errmsg);
          })
  },
  onReady: function () {
    // 页面渲染完成

  },
  onShow: function () {
    // 页面显示

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  switchTab: function () {
    let that = this;
    if (that.data.showType == 0) {
      that.setData({
        allCommentList: [],
        allPage: 1,
        comments: [],
        showType: 1
      });
    } else {
      that.setData({
        picCommentList: [],
        picPage: 1,
        comments: [],
        showType: 0
      });
    }
    this.getCommentList();
  },
  onReachBottom: function(){
    console.log('onPullDownRefresh');
    if ( this.data.showType == 0) {

      if (this.data.allCount / this.data.size < this.data.allPage) {
        return false;
      }

      this.setData({
        'allPage' : this.data.allPage + 1
      });
    } else {
      if (this.data.hasPicCount / this.data.size < this.data.picPage) {
        return false;
      }

      this.setData({
        'picPage': this.data.picPage + 1
      });
    }

    this.getCommentList();
  }
})