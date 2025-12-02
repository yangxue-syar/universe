<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <!-- 登录表单卡片 -->
    <div class="login-card">
      <!-- 头部 Logo 和标题 -->
      <div class="login-header">
        <div class="logo">LOGO</div>
        <h2 class="title">欢迎回来</h2>
        <p class="subtitle">登录您的账户以继续</p>
      </div>

      <!-- 登录表单 -->
      <form class="login-form" @submit.prevent="handleLogin">
        <!-- 用户名输入框 -->
        <div class="form-group">
          <label class="form-label">
            <i class="icon">👤</i>
            用户名
          </label>
          <input
              v-model="loginForm.username"
              type="text"
              class="form-input"
              placeholder="请输入用户名"
              :class="{ 'input-error': errors.username }"
              @input="clearError('username')"
          />
          <span v-if="errors.username" class="error-message">
            {{ errors.username }}
          </span>
        </div>

        <!-- 密码输入框 -->
        <div class="form-group">
          <label class="form-label">
            <i class="icon">🔒</i>
            密码
          </label>
          <div class="password-wrapper">
            <input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                class="form-input"
                placeholder="请输入密码"
                :class="{ 'input-error': errors.password }"
                @input="clearError('password')"
            />
            <button
                type="button"
                class="toggle-password"
                @click="togglePassword"
            >
              {{ showPassword ? '👁️' : '👁️‍🗨️' }}
            </button>
          </div>
          <span v-if="errors.password" class="error-message">
            {{ errors.password }}
          </span>
        </div>

        <!-- 记住我 & 忘记密码 -->
        <div class="form-options">
          <label class="checkbox-label">
            <input v-model="rememberMe" type="checkbox" />
            <span>记住我</span>
          </label>
          <a href="#" class="forgot-password">忘记密码?</a>
        </div>

        <!-- 登录按钮 -->
        <button
            type="submit"
            class="login-button"
            :disabled="loading"
            :class="{ 'button-loading': loading }"
        >
          <span v-if="!loading">登录</span>
          <span v-else class="loading-content">
            <span class="spinner"></span>
            登录中...
          </span>
        </button>

        <!-- 注册链接 -->
        <div class="register-link">
          还没有账户？
          <router-link to="/register" class="link">立即注册</router-link>
        </div>
      </form>

      <!-- 第三方登录（可选） -->
      <div class="divider">
        <span>或使用以下方式登录</span>
      </div>

      <div class="social-login">
        <button class="social-button wechat" title="微信登录">
          💬
        </button>
        <button class="social-button qq" title="QQ登录">
          🐧
        </button>
        <button class="social-button weibo" title="微博登录">
          📱
        </button>
      </div>
    </div>

    <!-- 返回首页按钮 -->
    <button class="back-home" @click="goHome">
      ← 返回首页
    </button>
  </div>
</template>

<script>
import axios from 'axios';
import { message } from 'ant-design-vue';

export default {
  name: 'LoginPage',
  data() {
    return {
      // 后端 API 地址
      apiBaseUrl: 'http://192.168.0.80:9102',

      // 登录表单数据
      loginForm: {
        username: '',
        password: ''
      },

      // 表单验证错误信息
      errors: {
        username: '',
        password: ''
      },

      // UI 状态
      showPassword: false,
      rememberMe: false,
      loading: false
    };
  },

  mounted() {
    // 检查是否有记住的用户名
    const savedUsername = localStorage.getItem('savedUsername');
    if (savedUsername) {
      this.loginForm.username = savedUsername;
      this.rememberMe = true;
    }
  },

  methods: {
    /**
     * 表单验证
     */
    validateForm() {
      let isValid = true;
      this.errors = { username: '', password: '' };

      // 验证用户名
      if (!this.loginForm.username.trim()) {
        this.errors.username = '请输入用户名';
        isValid = false;
      } else if (this.loginForm.username.length < 3) {
        this.errors.username = '用户名长度至少为3个字符';
        isValid = false;
      }

      // 验证密码
      if (!this.loginForm.password) {
        this.errors.password = '请输入密码';
        isValid = false;
      } else if (this.loginForm.password.length < 6) {
        this.errors.password = '密码长度至少为6个字符';
        isValid = false;
      }

      return isValid;
    },

    /**
     * 清除指定字段的错误信息
     */
    clearError(field) {
      this.errors[field] = '';
    },

    /**
     * 切换密码显示/隐藏
     */
    togglePassword() {
      this.showPassword = !this.showPassword;
    },

    /**
     * 处理登录
     */
    async handleLogin() {
      // 表单验证
      if (!this.validateForm()) {
        return;
      }

      this.loading = true;

      try {
        // 发送登录请求
        const response = await axios.post(
            `${this.apiBaseUrl}/auth/login`,
            {
              username: this.loginForm.username,
              password: this.loginForm.password
            },
            {
              headers: {
                'Content-Type': 'application/json'
              },
              timeout: 10000
            }
        );

        console.log('✅ 登录响应:', response.data);

        // 检查响应状态
        if (response.data.code === 200 && response.data.data) {
          const token = response.data.data;

          // 保存 token 到 localStorage
          localStorage.setItem('token', token);
          localStorage.setItem('isLoggedIn', 'true');

          // 如果勾选了"记住我"，保存用户名
          if (this.rememberMe) {
            localStorage.setItem('savedUsername', this.loginForm.username);
          } else {
            localStorage.removeItem('savedUsername');
          }

          // 触发登录状态变化事件（供其他组件监听）
          window.dispatchEvent(new Event('loginStateChange'));

          // 显示成功消息
          message.success('登录成功！');

          // 延迟跳转，让用户看到成功消息
          setTimeout(() => {
            // 检查是否有重定向参数
            const redirect = this.$route.query.redirect || '/';
            this.$router.push(redirect);
          }, 500);

        } else {
          // 登录失败
          message.error(response.data.message || '登录失败，请重试');
        }

      } catch (error) {
        console.error('❌ 登录失败:', error);

        // 处理错误
        if (error.response) {
          const status = error.response.status;
          const errorData = error.response.data;

          if (status === 401) {
            message.error('用户名或密码错误');
          } else if (status === 403) {
            message.error('账户已被禁用，请联系管理员');
          } else if (errorData && errorData.message) {
            message.error(errorData.message);
          } else {
            message.error(`登录失败 (${status})`);
          }
        } else if (error.request) {
          message.error('网络连接失败，请检查网络');
        } else {
          message.error('登录失败: ' + error.message);
        }
      } finally {
        this.loading = false;
      }
    },

    /**
     * 返回首页
     */
    goHome() {
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
  padding: 2rem;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  left: -150px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  right: -100px;
  animation-delay: 7s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 80%;
  animation-delay: 14s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  33% {
    transform: translate(30px, -30px) scale(1.1);
  }
  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }
}

/* 登录卡片 */
.login-card {
  width: 100%;
  max-width: 440px;
  background: white;
  border-radius: 20px;
  padding: 3rem 2.5rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
}

/* 头部 */
.login-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.logo {
  display: inline-block;
  font-size: 2rem;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 1rem;
}

.title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 0.5rem;
}

.subtitle {
  font-size: 0.95rem;
  color: #718096;
  margin: 0;
}

/* 表单 */
.login-form {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  font-weight: 600;
  color: #4a5568;
  margin-bottom: 0.5rem;
}

.icon {
  font-size: 1.1rem;
}

.form-input {
  width: 100%;
  padding: 0.9rem 1rem;
  font-size: 0.95rem;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  outline: none;
  transition: all 0.3s ease;
  background: #f7fafc;
}

.form-input:focus {
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input.input-error {
  border-color: #fc8181;
  background: #fff5f5;
}

.error-message {
  display: block;
  margin-top: 0.4rem;
  font-size: 0.85rem;
  color: #e53e3e;
}

/* 密码输入框 */
.password-wrapper {
  position: relative;
}

.toggle-password {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  padding: 0.3rem;
  transition: opacity 0.2s;
}

.toggle-password:hover {
  opacity: 0.7;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  color: #4a5568;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.forgot-password {
  font-size: 0.9rem;
  color: #667eea;
  text-decoration: none;
  transition: color 0.2s;
}

.forgot-password:hover {
  color: #764ba2;
  text-decoration: underline;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  padding: 1rem;
  font-size: 1rem;
  font-weight: 600;
  color: white;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 1.5rem;
  font-size: 0.9rem;
  color: #718096;
}

.link {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.link:hover {
  color: #764ba2;
  text-decoration: underline;
}

/* 分割线 */
.divider {
  position: relative;
  text-align: center;
  margin: 2rem 0 1.5rem;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e2e8f0;
}

.divider span {
  position: relative;
  display: inline-block;
  padding: 0 1rem;
  background: white;
  font-size: 0.85rem;
  color: #a0aec0;
}

/* 第三方登录 */
.social-login {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.social-button {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 2px solid #e2e8f0;
  background: white;
  font-size: 1.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.social-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.social-button.wechat:hover {
  border-color: #09bb07;
}

.social-button.qq:hover {
  border-color: #12b7f5;
}

.social-button.weibo:hover {
  border-color: #e6162d;
}

/* 返回首页按钮 */
.back-home {
  position: absolute;
  top: 2rem;
  left: 2rem;
  padding: 0.8rem 1.5rem;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 10;
}

.back-home:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateX(-5px);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-container {
    padding: 1rem;
  }

  .login-card {
    padding: 2rem 1.5rem;
  }

  .title {
    font-size: 1.5rem;
  }

  .back-home {
    top: 1rem;
    left: 1rem;
    padding: 0.6rem 1rem;
    font-size: 0.85rem;
  }
}
</style>