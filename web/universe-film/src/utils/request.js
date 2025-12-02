import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建 axios 实例
const request = axios.create({
    baseURL: '/api', // 根据您的实际情况修改
    timeout: 10000, // 请求超时时间
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
});

// 请求拦截器
request.interceptors.request.use(
    config => {
        // 在发送请求之前做些什么
        // 例如添加 token
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        // 对请求错误做些什么
        console.error('请求错误:', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(
    response => {
        console.log('收到响应:', response.status, response.config.url);
        console.log('响应数据:', response.data);
        // 对响应数据做点什么
        const res = response.data;

        // 如果返回的状态码不是 200，则认为是错误
        if (res.code !== 200) {
            ElMessage.error(res.message || '请求失败');

            // 特殊状态码处理
            if (res.code === 401) {
                // 未授权，跳转到登录页
                ElMessage.error('请先登录');
                // router.push('/login');
            }

            return Promise.reject(new Error(res.message || '请求失败'));
        }

        return res;
    },
    error => {
        // 对响应错误做点什么
        console.error('响应错误:', error);

        let message = '网络错误，请稍后重试';

        if (error.response) {
            // 服务器返回了错误状态码
            switch (error.response.status) {
                case 400:
                    message = '请求参数错误';
                    break;
                case 401:
                    message = '未授权，请登录';
                    break;
                case 403:
                    message = '没有权限访问';
                    break;
                case 404:
                    message = '请求的资源不存在';
                    break;
                case 500:
                    message = '服务器错误';
                    break;
                default:
                    message = `错误: ${error.response.status}`;
            }
        } else if (error.request) {
            // 请求已发出，但没有收到响应
            message = '网络连接失败，请检查网络';
        }

        ElMessage.error(message);
        return Promise.reject(error);
    }
);

export default request;