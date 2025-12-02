<script setup>
import { message } from 'ant-design-vue';
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from "vue-router";

const route = useRoute()
const router = useRouter()

// 用户登录状态管理
const isLoggedIn = ref(false);
const userInfo = ref(null);

// 解析 JWT token 的函数
const parseJwtToken = (token) => {
  try {
    // 检查 token 是否存在
    if (!token || typeof token !== 'string') {
      console.error('❌ Token 不存在或格式不正确:', token);
      return null;
    }

    // JWT 格式：header.payload.signature
    const parts = token.split('.');
    if (parts.length !== 3) {
      console.error('❌ Token 格式不正确，应该有3部分，实际有:', parts.length);
      return null;
    }

    // 解码 payload (Base64Url)
    const payload = parts[1];
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split('')
            .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
            .join('')
    );

    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error('❌ 解析 JWT token 失败:', e);
    return null;
  }
};

// 获取用户信息
const getUserInfo = () => {
  try {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token');

    console.log('🔍 从 localStorage 获取的 token:', token ? token.substring(0, 50) + '...' : 'null');

    if (!token) {
      console.warn('⚠️ 未找到 token');
      return null;
    }

    // 解析 token
    const payload = parseJwtToken(token);
    if (!payload) {
      console.warn('⚠️ Token 解析失败');
      return null;
    }

    console.log('✅ Token 解析成功:', payload);
    return {
      id: payload.id,
      username: payload.username
    };
  } catch (e) {
    console.error('❌ 获取用户信息失败:', e);
    return null;
  }
};


// 计算用户头像地址
const userAvatar = computed(() => {
  if (userInfo.value?.avatar) {
    return userInfo.value.avatar;
  }
  return 'https://via.placeholder.com/32/667eea/ffffff?text=U';
});

// 同步登录状态的函数
const syncLoginState = () => {
  const loginStatus = localStorage.getItem('isLoggedIn') === 'true';
  console.log('🔄 同步登录状态:', loginStatus);

  if (loginStatus) {
    const user = getUserInfo();
    if (user) {
      isLoggedIn.value = true;
      userInfo.value = user;
      console.log('✅ 用户信息加载成功:', user);
    } else {
      // Token 无效，清除登录状态
      isLoggedIn.value = false;
      userInfo.value = null;
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('token');
      console.warn('⚠️ Token 无效，已清除登录状态');
    }
  } else {
    isLoggedIn.value = false;
    userInfo.value = null;
  }
};


// 监听页面加载和 storage 事件
onMounted(() => {
  syncLoginState();
  console.log('📱 头部组件加载完成，登录状态:', isLoggedIn.value);

  window.addEventListener('storage', syncLoginState);
  window.addEventListener('loginStateChange', syncLoginState);

  window.addEventListener('scroll', () => {
    isScrolled.value = window.scrollY > 10;
  });
});

// 监听路由变化，确保状态同步
watch(() => route.path, () => {
  syncLoginState();
  console.log('🔄 路由变化，重新同步登录状态');
});

// 登录功能 - 跳转到登录页面
const handleLogin = () => {
  router.push('/login');
};

// 点击头像 - 跳转到个人中心
const handleAvatarClick = () => {
  if (userInfo.value?.id) {
    router.push(`/profile/${userInfo.value.id}`);
  } else {
    router.push('/profile');
  }
};

// 退出登录功能
const handleLogout = () => {
  isLoggedIn.value = false;
  userInfo.value = null;
  localStorage.removeItem('isLoggedIn');
  localStorage.removeItem('userInfo');

  window.dispatchEvent(new Event('loginStateChange'));

  message.success('已退出登录');
  if (route.path.startsWith('/profile')) {
    router.push('/');
  }
};

/**
 * 🔧 修复：导航菜单数据列表（移除个人中心）
 */
const navList = [
  { title: '首页', path: '/' },
  { title: '电影', path: '/movie' },
  { title: '电视剧', path: '/tv' },
  { title: '动漫', path: '/animation' },
  { title: '综艺', path: '/variety' },
];

const isScrolled = ref(false);
// const isActive = (navKey) => {
//   // 使用路由meta判断
//   if (route.meta.activeNav) {
//     return route.meta.activeNav === navKey;
//   }
//   return route.path.startsWith(`/${navKey}`);
// };

const handleNavClick = (path) => {
  console.log('点击导航:', path)
  router.push(path)
}
</script>

<template>
  <!-- 页面头部组件 -->
  <header class="header sticky" :class="{ scrolled: isScrolled }">
    <div class="header-inner">
      <!-- 左侧区域：Logo 和 导航菜单 -->
      <div class="left">
        <!-- Logo 区域 -->
        <div class="logo" @click="router.push('/')">LOGO</div>

        <!-- 主导航菜单 -->
        <nav class="nav">
          <router-link
              v-for="item in navList"
              :key="item.path"
              :to="item.path"
              class="nav-item"
              :class="{active: route.path === item.path }"
              @click="handleNavClick(item.path)"
          >
            {{ item.title }}
          </router-link>
        </nav>
      </div>

      <!-- 右侧区域：搜索框与功能图标 -->
      <div class="right">
        <!-- 椭圆搜索框 -->
        <div class="search-box">
          <input type="text" placeholder="搜索影片 / 电视剧…" />
          <i class="iconfont icon-sousuo icon-search"></i>
        </div>

        <!-- SVG 图标组 -->
        <div class="icons">
          <!-- 历史记录图标 -->
          <svg class="icon-item" viewBox="0 0 1024 1024">
            <path fill="currentColor" d="M512 128c212.077 0 384 171.923 384 384s-171.923 384-384 384S128 724.077 128 512h64c0 194.109 157.891 352 352 352s352-157.891-352-352-157.891-352-352-352-352 157.891-352 352H128c0-212.077 171.923-384 384-384zm32 192v192l160 96-32 53-192-112V320h64z"/>
          </svg>

          <!-- 私信图标 -->
          <svg class="icon-item" viewBox="0 0 1024 1024">
            <path fill="currentColor" d="M128 224h704c35.2 0 64 28.8 64 64v448c0 35.2-28.8 64-64 64H128c-35.2 0-64-28.8-64-64V288c0-35.2 28.8-64 64-64zm32 96v384h640V320L544 544c-19.2 12.8-44.8 12.8-64 0L192 320zm640-64H224l288 192L832 256z"/>
          </svg>

          <!-- 🔧 修复：用户登录状态显示 -->
          <!-- 未登录时显示登录按钮 -->
          <button v-if="!isLoggedIn" class="login-btn" @click="handleLogin">
            登录
          </button>

          <!-- 登录后显示用户头像和下拉菜单 -->
          <div v-else class="user-menu">
            <div class="user-avatar">
              <img :src="userAvatar" :alt="userInfo?.username || '用户头像'" />
            </div>

            <!-- 🔧 修复：下拉菜单 -->
            <div class="dropdown-menu">
              <div class="user-info">
                <img :src="userAvatar" :alt="userInfo?.username || '用户'" />
                <div class="info">
                  <div class="nickname">{{ userInfo?.nickname || userInfo?.username || '用户' }}</div>
                  <div class="user-id">ID: {{ userInfo?.id }}</div>
                </div>
              </div>
              <div class="menu-divider"></div>
              <div class="menu-item" @click.stop="handleAvatarClick">
                <svg class="menu-icon" viewBox="0 0 1024 1024">
                  <path fill="currentColor" d="M512 512a192 192 0 1 0 0-384 192 192 0 0 0 0 384zm0 64a256 256 0 1 1 0-512 256 256 0 0 1 0 512zm320 320v-96a96 96 0 0 0-96-96H288a96 96 0 0 0-96 96v96a32 32 0 1 1-64 0v-96a160 160 0 0 1 160-160h448a160 160 0 0 1 160 160v96a32 32 0 1 1-64 0z"/>
                </svg>
                个人中心
              </div>
              <div class="menu-item" @click.stop="handleLogout">
                <svg class="menu-icon" viewBox="0 0 1024 1024">
                  <path fill="currentColor" d="M868 732h-70.3c-4.8 0-9.3 2.1-12.3 5.8-7 8.5-14.5 16.7-22.4 24.5a353.84 353.84 0 0 1-112.7 75.9A352.8 352.8 0 0 1 512.4 866c-47.9 0-94.3-9.4-137.9-27.8a353.84 353.84 0 0 1-112.7-75.9 353.28 353.28 0 0 1-76-112.5C167.3 606.2 158 559.9 158 512s9.4-94.2 27.8-137.8c17.8-42.1 43.4-80 76-112.5s70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8 47.9 0 94.3 9.3 137.9 27.8 42.2 17.8 80.1 43.4 112.7 75.9 7.9 7.9 15.3 16.1 22.4 24.5 3 3.7 7.6 5.8 12.3 5.8H868c6.3 0 10.2-7 6.7-12.3C798 160.5 663.8 81.6 511.3 82 271.7 82.6 79.6 277.1 82 516.4 84.4 751.9 276.2 942 512.4 942c152.1 0 285.7-78.8 362.3-197.7 3.4-5.3-.4-12.3-6.7-12.3zm88.9-226.3L815 393.7c-5.3-4.2-13-.4-13 6.3v76H488c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h314v76c0 6.7 7.8 10.5 13 6.3l141.9-112a8 8 0 0 0 0-12.6z"/>
                </svg>
                退出登录
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </header>
</template>

<style scoped>
:root {
  --color-primary: #003974;
  --color-primary-light: #0a57b3;
  --color-text: #fff;
  --color-bg: #071a39;
  --color-border: #0f2a5c;
}

.header {
  width: 100%;
  background: var(--color-bg);
  color: var(--color-text);
  transition: box-shadow 0.25s, background-color 0.25s;
  pointer-events: auto;
}

.sticky {
  position: sticky;
  top: 0;
  z-index: 999;
}

.scrolled {
  box-shadow: 0 2px 12px rgba(0,0,0,0.3);
}

.header-inner {
  max-width: 1228px;
  height: 58px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  pointer-events: auto;
}

.left {
  display: flex;
  align-items: center;
  gap: 20px;
  pointer-events: auto;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: var(--color-primary-light);
  cursor: pointer;
}

.nav {
  display: flex;
  gap: 18px;
  pointer-events: auto;
}

.nav-item {
  color: var(--color-text);
  font-size: 15px;
  padding: 6px 16px;
  position: relative;
  cursor: pointer;
  transition: all 0.25s;
  text-decoration: none;
  border-radius: 20px;
  pointer-events: auto;
  display: inline-block;
  z-index: 10;
}

.nav-item.active {
  color: #fff;
  background-color: red;
}

.nav-item:hover:not(.active) {
  color: #e0e0e0;
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-item::after {
  display: none;
}

.right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-box {
  position: relative;
}

.search-box input {
  width: 200px;
  height: 36px;
  border: none;
  border-radius: 999px;
  padding: 0 40px 0 12px;
  font-size: 14px;
  outline: none;
  background: #f0f0f0;
  color: #333;
  transition: all 0.2s;
}

.search-box input::placeholder {
  color: #999;
}

.search-box input:focus {
  box-shadow: none;
  background: #f0f0f0;
}

.icon-search {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #999;
  cursor: pointer;
}

.icons {
  display: flex;
  align-items: center;
  gap: 20px;
}

.icon-item {
  width: 22px;
  height: 22px;
  fill: #fff;
  cursor: pointer;
  transition: fill 0.25s;
}

.icon-item:hover {
  fill: var(--color-primary-light);
}

.login-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 20px;
  background-color: #e41e25;
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.25s;
}

.login-btn:hover {
  background-color: #fd0428;
}

/* 🔧 用户菜单容器 */
.user-menu {
  position: relative;
}

/* 🔧 用户头像 */
.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid #fff;
  transition: border-color 0.25s;
}

.user-avatar:hover {
  border-color: var(--color-primary-light);
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 🔧 下拉菜单 */
.dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  width: 240px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.25s;
  z-index: 1000;
}

.user-menu:hover .dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

/* 🔧 用户信息区域 */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
}

.user-info img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info .info {
  flex: 1;
  min-width: 0;
}

.user-info .nickname {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-info .user-id {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 🔧 分割线 */
.menu-divider {
  height: 1px;
  background: #f0f0f0;
  margin: 0 16px;
}

/* 🔧 菜单项 */
.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  color: #333;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.menu-item:hover {
  background-color: #f5f5f5;
}

.menu-item:last-child {
  border-radius: 0 0 8px 8px;
}

.menu-icon {
  width: 18px;
  height: 18px;
  fill: #666;
}
</style>