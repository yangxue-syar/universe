import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import ElementPlus from 'element-plus' // 导入 Element Plus
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import 'ant-design-vue/dist/reset.css';
import axios from 'axios';

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.use(Antd)
app.config.globalProperties.$axios = axios;
app.mount('#app')

