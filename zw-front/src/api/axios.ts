import axios from 'axios';
import { showMessage } from "./status";   // 引入状态码文件
import { ElMessage } from 'element-plus'  // 引入el 提示框，这个项目里用什么组件库这里引什么
import store from '@/store'; // Import your Vuex store
import router from '@/router';


// 设置接口超时时间
axios.defaults.timeout = 60000;

// @ts-ignore
axios.defaults.baseURL = import.meta.env.VITE_BASE_API;   

//http request 拦截器
axios.interceptors.request.use(
  config => {
  // 配置请求头
    config.headers = {
      //'Content-Type':'application/x-www-form-urlencoded',   // 传参方式表单
      'Content-Type':'application/json;charset=UTF-8',        // 传参方式json
      'Authorization':localStorage.getItem("USER_TOKEN"),
      'Header_key_user_id':localStorage.getItem("Header_key_user_id"),
    } as any;
    store.commit('startLoading');
    return config;
    
  },
  error => {
    store.commit('stopLoading');
    return Promise.reject(error);
  }
);

//http response 拦截器
axios.interceptors.response.use(
  response => {
    store.commit('stopLoading');
    if(response.data.status != 200){
      ElMessage.warning(showMessage(response.data.message));
      return Promise.reject(response.data);
    }else {
      return response;
    }
  },
  error => {
    store.commit('stopLoading');
    const {response} = error;
    if (response) {
      // 请求已发出，但是不在2xx的范围
     
      if(response.status == 401){
        try {
          router.push({'name':'Login'});
        } catch (error) {
          console.log(error);
        }
        
        ElMessage.warning("Please to login!");
        return Promise.reject(response.data);
      }
      ElMessage.warning(showMessage(response.status));
      // showMessage(response.status);  
      // 传入响应码，匹配响应码对应信息
      return Promise.reject(response.data);
    } else {
      ElMessage.warning('网络连接异常,请稍后再试!');
    }
  }
);

// 封装 GET POST 请求并导出
export function request(url='',params={},type='POST'){
//设置 url params type 的默认值
return new Promise((resolve,reject)=>{
  let promise
  if (type.toUpperCase() === 'GET') {
    promise = axios.get(url, { params });
  } else if (type.toUpperCase() === 'POST') {
    promise = axios.post(url, params);
  } else if (type.toUpperCase() === 'DELETE') {
    promise = axios.delete(url, { params });
  } else if (type.toUpperCase() === 'PUT') {
    promise = axios.put(url, params);
  }
  //处理返回
  promise.then(res=>{
    resolve(res.data)
  }).catch(err=>{
    reject(err)
  })
})
}


