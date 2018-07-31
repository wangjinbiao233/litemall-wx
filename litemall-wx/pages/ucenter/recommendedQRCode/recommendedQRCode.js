var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    qrcodeUrl: ""
  },
  onLoad: function(options) {
    var that = this;

    if (app.globalData.hasLogin) {
      util.request(api.getUserInfo, {
        userId: wx.getStorageSync('userId')
      }, 'POST').then(function(res) {
        console.log(res);     
        that.setData({
          qrcodeUrl: res.user.qrcodeUrl
        });
      });
    }
  },
  previewImage: function(e) {
    var that = this 
    var imgalist = [that.data.qrcodeUrl]  
    wx.previewImage({
      current: '', // 当前显示图片的http链接
      urls: imgalist // 需要预览的图片http链接列表
    })
 

    /*  
    wx.getSetting({
      success: (res) => {     
        if (!res.authSetting['scope.writePhotosAlbum']) {
          console.log('没有获取授权');
          wx.authorize({
            scope: 'scope.writePhotosAlbum',
            success: (res) => {            
              console.log('获取授权')
              console.log('path = ' + that.data.qrcodeUrl)
              wx.downloadFile({
                url: that.data.qrcodeUrl, //仅为示例，并非真实的资源
                success: function (res) {
                  wx.saveImageToPhotosAlbum({
                    filePath: res.tempFilePath,
                    success: (res) => {
                      wx.showToast({
                        title: '保存成功'
                      });
                      console.log('保存到相册');
                      console.log(res)
                    },
                    fail: (res) => {
                      util.showErrorToast('保存失败');
                      console.log('保存失败');
                      console.log(res)
                    },
                    complete: (res) => {
                      console.log('调用完成')
                    }
                  })
                }
              })              
            }
          })
        }else{
          wx.downloadFile({
            url: that.data.qrcodeUrl, //仅为示例，并非真实的资源
            success: function (res) {
              wx.saveImageToPhotosAlbum({
                filePath: res.tempFilePath,
                success: (res) => {
                  wx.showToast({
                    title: '保存成功'
                  });
                  console.log('保存到相册');
                  console.log(res)
                },
                fail: (res) => {
                  util.showErrorToast('保存失败');
                  console.log('保存失败');
                  console.log(res)
                },
                complete: (res) => {
                  console.log('调用完成')
                }
              })
            }
          })             

        }
      }
    })*/
  },
  onReady: function() {

  },
  onShow: function() {

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  },


})