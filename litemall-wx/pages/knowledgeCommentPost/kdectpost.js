var app = getApp();
var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
    data: {
        //orderId: 0,
        userId: wx.getStorageSync('userId'),
        valueId: 0,//知识文章的id
        typeId: 0,//2
        userId: 0,
        title:'',
        content: '',
        hasPicture: false,
        picUrls: [],
        files: [],
        isImg: true,
        commentId:null,
        replayId:null
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
        that.setData({
            valueId: options.valueId,
            typeId: options.typeId,
            title:options.title,
            isImg: options.isImg,
            commentId: options.commentId,
            replayId: options.replayId
        });
    },
    onClose: function () {
        wx.navigateBack();
    },
    onPost: function () {
        let that = this;

        if (!this.data.content) {
            util.showErrorToast('请填写评论')
            return false;
        }

        util.request(api.CommentPost, {
            userId: wx.getStorageSync('userId'),
            typeId: that.data.typeId,
            valueId: that.data.valueId,
            content: that.data.content,
            //star: that.data.star,
            hasPicture: that.data.hasPicture,
            picUrls: that.data.picUrls,
            commentId: that.data.commentId,
            replayId: that.data.replayId
        }, 'POST').then(function (res) {
            if (res.errno === 0) {
                wx.showToast({
                    title: '评论成功',
                    complete: function () {
                        wx.navigateBack();
                    }
                })
            }
        });
    },
    bindInputValue(event) {

        let value = event.detail.value;

        //判断是否超过140个字符
        if (value && value.length > 140) {
            return false;
        }

        this.setData({
            content: event.detail.value,
        })
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