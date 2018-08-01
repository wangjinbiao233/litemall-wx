var wxCharts = require('../../utils/wxcharts.js');
var app = getApp();
var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var ringCharta = null;//肌龄
var ringChartb = null;//总分
var radarChart = null;//4方雷达图
Page({
    data: {
        templatenum:1, //1,2,3,4 对应4个模板,初始化选择分数最低的对应
        sharpicture: 1,//最maxSkintypeId
        faceFourdataId:'',
        facedata:[],
        goods:[],
        facedefault:{},
        maokong:{},
        redArea:{},
        sesu:{},
        wenli:{},
        showModalStatus: false,
        ismyself: '',
        userId:''
    },
    touchHandler: function (e) {
        //console.log(ringCharta.getCurrentDataIndex(e));
    },
    onLoad: function (options) {
        let that = this;
        let ud=options.userId;
        if(ud==undefined){
            ud = wx.getStorageSync('userId');
        }
        that.setData({
            faceFourdataId: options.faceFourdataId,
            userId: ud
        });
         that.faceEvaluateData();
    },
    faceEvaluateData:function(){
        let that = this;
        util.request(api.FacialEvaluateData, {
            faceFourdataId: that.data.faceFourdataId, userId: that.data.userId
        }).then(function (res) {
            that.setData({
                templatenum: res.data.defaultdata.minSkintypeId,
                sharpicture: res.data.defaultdata.maxSkintypeId,
                facedata: res.data,
                goods: res.data.hotGoods,
                facedefault: res.data.defaultdata,
                maokong: res.data.maokong,
                redArea: res.data.redArea,
                sesu: res.data.sesu,
                wenli: res.data.wenli
            });
            that.chartload(ringCharta, 'ringCanvasa', 'jl');
            that.chartload(ringChartb, 'ringCanvasb', 'zf');
            that.radarChartload();
        });
    }, 
    onReady: function (e) {
    },
    choosetmp:function(e){
        let num = e.currentTarget.dataset.text;
        this.setData({//刷新数量和图标
            templatenum: num
        })
    },
    radarChartload:function(){
        var windowWidth = 320;
        try {
            var res = wx.getSystemInfoSync();
            windowWidth = res.windowWidth;
        } catch (e) {
            console.error('getSystemInfoSync failed!');
        }

        radarChart = new wxCharts({
            canvasId: 'radarCanvas',
            type: 'radar',
            categories: ['', '','',''],
            series: [{
                name: '',
                data: [this.data.facedefault.maokong, this.data.facedefault.redArea, this.data.facedefault.sesu, this.data.facedefault.wenli]
            }],
            width: windowWidth,
            height: 220,
            extra: {
                radar: {
                    max: 100
                }
            }
        });
    },
    chartload: function (ringChart,domid,types){
        let ots=0;let ot=100;
        let that=this;
        if(types=='jl'){
            ots = that.data.facedefault.skinAge;
            ot = 100 - that.data.facedefault.skinAge;
        }
        if(types=='zf'){
            ots = that.data.facedefault.score;
            ot = 100 - that.data.facedefault.score;
        }
        var windowWidth = 320;
        try {
            var res = wx.getSystemInfoSync();
            windowWidth = res.windowWidth;
        } catch (e) {
            console.error('getSystemInfoSync failed!');
        }
        ringChart = new wxCharts({
            animation: true,
            canvasId: domid,
            type: 'ring',
            extra: {
                ringWidth: 10,
                pie: {
                    offsetAngle: -25
                }
            },
            title: {
                //name: '70%',
                color: 'black',
                // fontSize: 25
            },
            subtitle: {
                name: ots,
                color: '#1E90FF',
                fontSize: 30
            },
            series: [{
                name: 'XXX',
                data: ot,
                color: '#E0FFFF',
                stroke: false
            }, {
                name: '正确值',
                data: ots,
                color: '#1E90FF',
                stroke: false
            }],
            disablePieStroke: true,
            width: windowWidth,
            height: 170,
            dataLabel: false,
            legend: false,
            background: 'white',
            padding: 0
        });
        ringChart.addEventListener('renderComplete', () => {
            console.log('renderComplete');
        });
        setTimeout(() => {
            ringChart.stopAnimation();
        }, 500);
    },
    windowchoose: function (e) {
        let that = this;
        let currentStatu = e.currentTarget.dataset.statu;
        that.yz_util(currentStatu);
    },
    chooseImages: function (e) {
        let that = this;
        that.yz_util('close');

        let ismyself = e.currentTarget.dataset.statu;

        if (ismyself == 'myself') {
            console.log('takePhoto ismyself=' + ismyself);
            wx.navigateTo({
                url: '/pages/takePhoto/takePhoto'
            })
        } else {
            console.log('friend ismyself=' + ismyself);
            wx.navigateTo({
                url: '/pages/takePhoto/friend'
            })
        }
    },
    tiaoz:function(e){
        let ids = e.currentTarget.dataset.text;
        
    },
    onShareAppMessage: function (ops) {
        console.log('分享了');

        if (ops.from === 'button') {
            // 来自页面内转发按钮
            console.log(ops.target)
        }
        return {
            title: 'PHILAB梵朗人脸检测分享',
            path: '/pages/facialEvaluate/facialEvaluate?faceFourdataId=' + this.data.faceFourdataId +'&userId='+this.data.userId,
           //path:'',
            success: function (res) {
                // 转发成功
                console.log("转发成功:" + JSON.stringify(res));
            },
            fail: function (res) {
                // 转发失败
                console.log("转发失败:" + JSON.stringify(res));
            }
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
            animation.opacity(0.9).rotateX(0).step();
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

});