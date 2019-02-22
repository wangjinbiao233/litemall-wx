var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: '',
    cashNum: '',
    hint: '',
    isShow: 'hide',
    disabled: true,
    money: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if (app.globalData.hasLogin) {
      let userId = wx.getStorageSync('userId');
      that.setData({
        userId: userId
      });
      var data = {
        userId: userId
      }
      util.request(api.cashNum, data).then(function (res) {
        console.log(res)
        if (res.errno === 0) {
          that.setData({
            cashNum: res.data
          });
        }
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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },
  /**
   * 监听用户输入
   */
  inputFunc: function (event) {
    let inputVal = event.detail.value;
    if (inputVal < 2){
      this.setData({
        hint: "提现金额不能小于2.00元",
        isShow: 'none',
        disabled: true
      });
    } else if (inputVal > this.data.cashNum) {
      this.setData({
        hint: "提现金额超过可提现金额",
        isShow: 'none',
        disabled: true
      });
    } else {
      this.setData({
        hint: "",
        isShow: 'hide',
        disabled: false,
        money: inputVal
      });
    }
  },


  /**
   * 提交申请
   */
  submit: function (e){
    var that = this;
    this.setData({
      disabled: true
    });
    console.log("申请提现")
    var data = {
      userId: this.data.userId,
      money: this.data.money
    }
    util.request(api.withdrawDeposit, data).then(function (res) {
      console.log(res)
      if (res.errno === 0 && res.data === 1) {
        wx.showToast({
          title: '提现成功',
          icon: 'success',
          duration: 2000,
          success:function(){
            console.log("提现成功")
            setTimeout(function () {
              //要延时执行的代码
              wx.navigateBack({
              })
            }, 2200) //延迟时间 
          }
        })
      } else {
        wx.showToast({
          title: '提现失败',
          icon: 'success',
          duration: 2000,
          success: function () {
            console.log("提现失败")
            that.setData({
              hint: "提现失败，请稍后再试",
              isShow: 'none',
            });
          }
        })
        
      }
    })
  }


})