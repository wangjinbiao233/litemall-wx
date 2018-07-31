/**
 * 支付相关服务
 */

const util = require('../utils/util.js');
const api = require('../config/api.js');

/**
 * 判断用户是否登录
 */
function payOrder(orderId) {
  return new Promise(function (resolve, reject) {
    util.request(api.PayPrepayId, {
      orderId: orderId,userId:  wx.getStorageSync('userId')
    },'POST').then((res) => {
      console.log(res)
      if (res.errno === 0) {
        const payParam = res.data;
        console.log(payParam);
        //发起支付请求
        wx.requestPayment({
          'timeStamp': payParam.timeStamp,
          'nonceStr': payParam.nonceStr,
          'package': payParam.package,
          'signType': 'MD5',
          'paySign': payParam.paySign,
          'success': function (res) {
            resolve(res);
          },
          'fail': function (res) {
            reject(res);
          },
          'complete': function (res) {
            reject(res);
          }
        });
      } else {
        reject(res);
      }
    });
  });
}


module.exports = {
  payOrder,
};











