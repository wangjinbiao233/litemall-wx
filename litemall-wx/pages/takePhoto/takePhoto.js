const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');
const innerAudioContext = wx.createInnerAudioContext();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    width: 0,
    height: 0,
    src: "",
    ishide:false,
    isfanzhuan:false,
    position:'front',
    imagePaths:'',
    audio: '',
    audoSrc: '/static/1.mp3'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var that = this;
      that.boaudio();
  },
  onUnload:function(){
      console.log('退出');
      this.data.audio.stop();
  },
  boaudio: function () {
      this.innerAudioContext = wx.createInnerAudioContext();
      this.setData({
          audio: innerAudioContext
      })
     // this.data.audio.autoplay = true;
      this.data.audio.src = this.data.audoSrc;
      
      this.data.audio.onPlay(function(){
          console.log('播放');
      })
      this.data.audio.onStop(function () {
          console.log('停止');
      })
      this.data.audio.onError((res) => {
          console.log(res.errMsg)
          console.log(res.errCode)
      })
      this.data.audio.play();
  }, 

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  },
  onShow: function () {
      let that =this;
      that.setData({
          ishide: false,
          imagePaths:''
      })
  },
  onReady:function(){
      let that=this;
  },

  /**
   * 拍照
   */
  takePhoto: function() {
      let that = this;
      that.setData({
          ishide: true
      });
      
      const ctx = wx.createCameraContext()
      ctx.takePhoto({
      quality: 'high',
      success: (res) => {
       //res.tempImagePath,
       console.log(res);
          that.setData({
              imagePaths: res.tempImagePath
          })
          wx.showLoading({
              title: '正在识别中',
              mask: true
          });
          that.upload(res);
       
      },
      bindstop:(e)=>{
        console.log(e);
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
          filePath: res.tempImagePath,
          name: 'file',
          success: function (res) {
              console.log("上传图片服务器返回数据" + res);
              console.log(res);
              var _res = JSON.parse(res.data);
              if (_res.errno === 0) {
                  var url = _res.data.url
                  that.facialEvaluateInfo(url, _res.data.id);
              } else {
                  that.showerror();
              }
          },
          fail: function (e) {
              wx.showModal({
                  title: '错误',
                  content: '图片上传失败',
                  showCancel: false
              })
          }
      })
  },
  facialEvaluateInfo: function (url, imgId) {
      let that = this;
      let userId = wx.getStorageSync('userId');//自己
    //   if (that.data.ismyself == 'friend') {
    //       userId = -1;
    //   }

      util.request(api.FacialEvaluate, {
          imageAddress: url, imgId: imgId, userId: userId
      }).then(function (res) {
          console.log("服务端返回人脸识别数据" + res);
          console.log(res);
          if (res.errno === 0) {
              wx.showToast({
                  title: '检测成功',
                  icon: 'succes',
                  duration: 1000,
                  mask: true
              })
              that.navigateToPage(res.data.faceFourdataId);
          } else {
              that.showerror();
          }
      });
  },
  navigateToPage: function (faceFourdataId) {
      console.log('进入facialEvaluate?faceFourdataId=' + faceFourdataId);
      wx.hideLoading();
      wx.navigateTo({
          url: '../facialEvaluate/facialEvaluate?faceFourdataId=' + faceFourdataId + '&userId=' + wx.getStorageSync('userId'),
      })
  },
  showerror: function () {
      wx.hideLoading();
      let that = this;
      wx.showModal({
          title: '提示',
          content: '照片检测失败，是否重新拍照',
          success: function (res) {
              console.log(res)
              if (res.confirm) {
                  console.log('用户点击了确定')
                  that.setData({
                      ishide: false,
                      imagePaths:''
                  });
              } else {
                  console.log('用户点击了取消')
                  wx.navigateBack();
              }
          }
      })
  },
  fanzhuan:function(){
      let that=this;
      let isfanzhuan = !that.data.isfanzhuan;
      if(isfanzhuan){
        that.setData({
            isfanzhuan: isfanzhuan,
            position:'back'
        })
      }else{
          that.setData({
              isfanzhuan: isfanzhuan,
              position: 'front'
          })
      }
  }

})