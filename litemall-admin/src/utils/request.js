import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(config => {
  // Do something before request is sent
  if (store.getters.token) {
    config.headers['X-Token'] = getToken() // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
  }
  console.log(config)
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    let errmsg = "";
    //从errmsg、errMsg、errormsg、errmsg中找值
    if(res.errmsg!=null && res.errmsg!=null){
      errmsg = res.errmsg;
    }
    if(res.errMsg!=null && res.errMsg!=null){
      errmsg = res.errMsg;
    }
    if(res.errormsg!=null && res.errormsg!=null){
      errmsg = res.errormsg;
    }
    if(res.errorMsg!=null && res.errorMsg!=null){
      errmsg = res.errorMsg;
    }
    if(res.errmsg!=null && res.errmsg!=null){
      errmsg = res.errmsg;
    }

    errmsg = res.errmsg;

    if (res.errno !== 0) {
      if(res.errno==303){
        MessageBox.alert('没有权限登录系统，请输入重新登录', '已退出', {
          confirmButtonText: '重新登录',
          type: 'error'
        }).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload()
          })
        })
      }else if(errmsg!="" && errmsg!=null && res.errno!=401){//401是未登录
        MessageBox.alert(errmsg,errmsg, {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {

        })
      }else{
        MessageBox.alert('超时自动退出系统，请重新登录', '已退出', {
          confirmButtonText: '重新登录',
          type: 'error'
        }).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject('error')
    } else {
      return response
    }
  }, error => {
    console.log('err' + error)// for debug
    Message({
      message: '登录连接超时（后台不能连接，请联系系统管理员）',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  })

export default service
