// 上传组件 基于https://github.com/Tencent/weui-wxss/tree/master/src/example/uploader
var app = getApp();
var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
Page({
  data: {   
    nickName:'',
    openSelectType: false, 
    distributionType:'',
    distributionIndex: 0,
    selectTypeList:[],
    hasPicture: false,
    picUrls: [],
    files: []
  },

  bindinputName(event) {   
    var nickName = event.detail.value;
    this.setData({
      nickName: nickName
    });
  },

  chooseType(e) {  
    let that = this;
    this.setData({
      openSelectType: !this.data.openSelectType,     
    }); 
  },

  cancelSelectType(e){
    this.setData({
      openSelectType: false     
    });

  },

  selectType(e){
    let that = this;
    var distributionIndex = e.currentTarget.dataset.distributionIndex
    this.setData({     
      distributionType: that.data.selectTypeList[distributionIndex].value,
      openSelectType:false
    });    
  },

  chooseImage: function (e) {
    if (this.data.files.length >= 5) {
      util.showErrorToast('只能上传五张图片')
      return false;
    }
    var that = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: function (res) {
        that.setData({
          files: that.data.files.concat(res.tempFilePaths)
        });
        that.upload(res);
      }
    })
  },
  upload: function (res) {
    var that = this;
    const uploadTask = wx.uploadFile({
      url: api.StorageUpload,
      header: {
        "content-type": "multipart/form-data"
      },
      method: 'POST',
      filePath: res.tempFilePaths[0],
      name: 'file',
      success: function (res) {
        var _res = JSON.parse(res.data);
        if (_res.errno === 0) {
          var url = _res.data.url
          that.data.picUrls.push(url)
          that.setData({
            hasPicture: true,
            picUrls: that.data.picUrls
          })
        }
      },
      fail: function (e) {
        wx.showModal({
          title: '错误',
          content: '上传失败',
          showCancel: false
        })
      },
    })

    uploadTask.onProgressUpdate((res) => {
      console.log('上传进度', res.progress)
      console.log('已经上传的数据长度', res.totalBytesSent)
      console.log('预期需要上传的数据总长度', res.totalBytesExpectedToSend)
    })

  },
  previewImage: function (e) {
    wx.previewImage({
      current: e.currentTarget.id, // 当前显示图片的http链接
      urls: this.data.files // 需要预览的图片http链接列表
    })
  },
  onLoad: function (options) {
    var that = this;
    util.request(api.selectDistributionTypeList, {
      groupCode: 'distribution_type'
    }, 'POST').then(function (res) {
      if (res.errno === 0) {
        that.setData({
          selectTypeList: res.data
        });
      }
    });    
  },
 
  save: function (res) {   
    let that = this;

    if (this.data.files.length <= 0) {
      util.showErrorToast('请选择一张图片')
      return false;
    }

    var flag = res.detail.target.dataset.flag;
    var formId = res.detail.formId;
    if(flag == 'true'){
      util.request(api.saveDistributionApply, {
        createUserId: wx.getStorageSync('userId'),
        nickName: that.data.nickName,
        formId: formId,
        distributionType: that.data.distributionType,
        picUrls: that.data.picUrls
      }, 'POST').then(function (res) {
        if (res.errno === 0) {
          wx.showToast({
            title: '申请成功',
            complete: function () {
              wx.navigateBack();
            }
          })

          wx.navigateTo({ url: "/pages/ucenter/index/index" });
        }
      });
    }else{
      wx.navigateBack();
    }
   
  },
  
  onReady: function () {

  },
  onShow: function () {
    // 页面显示

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  }
})