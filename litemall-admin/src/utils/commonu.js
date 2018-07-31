/**  * */

export function browserEquipmentCheck() {
       var u = navigator.userAgent,app = navigator.appVersion;
      //移动终端浏览器版本信息
      var trident=u.indexOf('Trident') > -1;//IE内核
      var presto=u.indexOf('Presto') > -1; //opera内核
      var webKit=u.indexOf('AppleWebKit') > -1; //苹果、谷歌内核
      var gecko=u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1; //火狐内核
      var mobile=!!u.match(/AppleWebKit.*Mobile.*/); //是否为移动终端
      var ios=!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
      var android=u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或uc浏览器
      var iPhone=u.indexOf('iPhone') > -1; //是否为iPhone或者QQHD浏览器
      var iPad=u.indexOf('iPad') > -1; //是否iPad
      var webApp=u.indexOf('Safari') == -1; //是否web应该程序，没有头部与底部
      var language=(navigator.browserLanguage || navigator.language).toLowerCase();
      if(mobile){//判断是否是移动设备打开
          var ua = navigator.userAgent.toLowerCase(); //获取判断用的对象
          if(iPad){
            return "iPad"
          }
          if (ios) {//移动设备为IOS
            return "ios";
          }
          if (android) {//移动设备为android
            return "android";
          }
          if (ua.match(/MicroMessenger/i) == "micromessenger") {//是否在微信打开
            return "MicroMessenger";
          }
          if (ua.match(/WeiBo/i) == "weibo") {//是否是在微博中打开
            return "WeiBo";
          }
          if (ua.match(/QQ/i) == "qq") {//是否在QQ打开
              return "qq";
          }
          return "mobile";          
      }else {//否则就是PC浏览器打开
          //if(trident) return "trident";//IE内核
          //if(presto) return "presto";//opera内核
          //if(webKit) return "webKit";//苹果、谷歌内核
          //if(gecko) return "gecko";//火狐内核
          return "pc";
      }
}