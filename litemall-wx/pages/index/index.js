const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');

//获取应用实例
const app = getApp()
Page({
  data: {
    picUrl: api.picUrl,
    newGoods: [],
    hotGoods: [],
    topics: [],
    brands: [],
    floorGoods: [],
    banner: [],
    store: [],
    channel: [],
    knowledges: [],
    kcategories: [],
    isSearch: 1,
    showModalStatus: false,
    isfirstlogin:false    
  },
  toSearch: function (e) {
    this.setData({
      isSearch: 2
    })
  },
  btngif: function (e) {
    wx.showToast({
      title: '这是悬浮按钮',
    })
  },
  onShareAppMessage: function () {
    return {
      title: 'PHILAB梵朗',
      desc: '微信小程序商城',
      path: '/pages/index/index'
    }
  },
  goCategory:function(){
    wx.switchTab({
      url: '../category/category',
    })
  },


  getDistance: function (lat1, lng1, lat2, lng2) {
    lat1 = lat1 || 0;
    lng1 = lng1 || 0;
    lat2 = lat2 || 0;
    lng2 = lng2 || 0;
    var rad1 = lat1 * Math.PI / 180.0;
    var rad2 = lat2 * Math.PI / 180.0;
    var a = rad1 - rad2;
    var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
    var r = 6378137;
    return (r * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)))).toFixed(0)

  },

  getIndexData: function () {
    let that = this;
    util.request(api.IndexUrl).then(function (res) {
      if (res.errno === 0) {
        console.log(res);
        that.setData({
          newGoods: res.data.newGoodsList,
          hotGoods: res.data.hotGoodsList,
          topics: res.data.topicList,
          brands: res.data.brandList,
          floorGoods: res.data.floorGoodsList,
          banner: res.data.banner,
          store: res.data.storeList,
          channel: res.data.channel,
          knowledges: res.data.knowledgeList,
          kcategories: res.data.kCategoryList
        });        
      }
    });

    wx.getLocation({
      //定位类型 wgs84, gcj02
      type: 'gcj02',
      success: function (res) {
        console.log(res)
        var latitude = res.latitude
        var longitude = res.longitude        
        var store = that.data.store;
        for (var i in store){
          var storeCoordinate = that.data.store[i].storeCoordinate
          var distant = that.getDistance(latitude, longitude, storeCoordinate.split(",")[0], storeCoordinate.split(",")[1]) / 1000  
          store[i].distant = Math.floor(distant * 100) / 100;
        }
        that.setData({
          store: store
        }); 

      },
      fail: function (err) {
        console.log(err)
      },
      complete: function (info) {
        console.log(info)
      },
    })  

  },
  goKnowledge: function (e) {
    let id = e.currentTarget.dataset.id;
    app.knowledgeId = id;
    wx.switchTab({ url: '../knowledge/index' })
  },
  getFirstface:function(){//是否第一次检测
   let that=this;
      util.request(api.FacialFirstfaceCheck, {
          userId: wx.getStorageSync('userId')
      }).then(function (res) {
          console.log(res);
          if (res.errno === 0) {
              that.setData({
                  isfirstlogin: res.data==0?true:false,
              });
          }
      });
  },
  onLoad: function (options) {
    let that = this;     
    this.getIndexData();
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
      this.getFirstface();
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },

  toStoreDetail: function (res) {
    var id = res.currentTarget.dataset.id
    wx.navigateTo({
      url: '../store/index?id='+id,
    })
  },
  windowchoose: function (e) {
        let that = this;
        let currentStatu = e.currentTarget.dataset.statu;
        that.yz_util(currentStatu);
  },
  chooseImages:function(e){
      let that=this;
      that.yz_util('close');

      let ismyself = e.currentTarget.dataset.statu;
    
      if(ismyself=='myself'){
          console.log('takePhoto ismyself=' + ismyself);
            wx.navigateTo({
                url: '/pages/takePhoto/takePhoto'
            })
      }else{
          console.log('friend ismyself=' + ismyself);
          wx.navigateTo({
              url: '/pages/takePhoto/friend'
          })
      }
  },
  powerDrawer: function (e) {
      let currentStatu = e.currentTarget.dataset.statu;
      this.yz_util(currentStatu)
  },
  yz_util: function (currentStatu) {
      /* 动画部分 */
      // 第1步：创建动画实例 
      var animation = wx.createAnimation({
          duration: 200, //动画时长 
          timingFunction: "linear", //线性 
          delay: 0 //0则不延迟 
      });

      // 第2步：这个动画实例赋给当前的动画实例 
      this.animation = animation;

      // 第3步：执行第一组动画 
      animation.opacity(0).rotateX(-100).step();

      // 第4步：导出动画对象赋给数据对象储存 
      this.setData({
          animationData: animation.export()
      })

      // 第5步：设置定时器到指定时候后，执行第二组动画 
      setTimeout(function () {
          // 执行第二组动画 
          animation.opacity(1).rotateX(0).step();
          // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
          this.setData({
              animationData: animation
          })

          //关闭 
          if (currentStatu == "close") {
              this.setData(
                  {
                      showModalStatus: false
                  }
              );
          }
      }.bind(this), 200)

      // 显示 
      if (currentStatu == "open") {
          this.setData(
              {
                  showModalStatus: true
              }
          );
      }
  } 


})
