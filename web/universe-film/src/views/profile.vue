<template>
  <div class="profile-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">个人中心</h2>
    </div>

    <div class="profile-wrapper">
      <!-- 左侧导航栏 -->
      <div class="profile-sidebar">
        <div class="user-card">
          <div class="user-avatar">
            <img
                :src="userProfile?.avatar || defaultAvatar"
                :alt="userProfile?.nickname || userProfile?.username"
                class="avatar-img"
            />
          </div>
          <div class="user-info">
            <div class="user-nickname">
              {{ userProfile?.nickname || userProfile?.username || '未登录用户' }}
            </div>
            <div class="user-status">
              最近在线：{{ lastOnlineTime }}
            </div>
          </div>
        </div>

        <nav class="sidebar-menu">
          <div
              class="menu-item"
              :class="{ active: activeTab === 'profile' }"
              @click="activeTab = 'profile'"
          >
            <svg class="menu-icon" viewBox="0 0 1024 1024">
              <path d="M512 512a192 192 0 1 0 0-384 192 192 0 0 0 0 384zm0 64a256 256 0 1 1 0-512 256 256 0 0 1 0 512zm320 320v-96a96 96 0 0 0-96-96H288a96 96 0 0 0-96 96v96a32 32 0 1 1-64 0v-96a160 160 0 0 1 160-160h448a160 160 0 0 1 160 160v96a32 32 0 1 1-64 0z" fill="currentColor"/>
            </svg>
            <span>个人资料</span>
          </div>

          <div
              class="menu-item"
              :class="{ active: activeTab === 'edit' }"
              @click="activeTab = 'edit'"
          >
            <svg class="menu-icon" viewBox="0 0 1024 1024">
              <path d="M880 836H144c-17.7 0-32 14.3-32 32v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-17.7-14.3-32-32-32zm-622.3-84c2 0 4-.2 6-.5L431.9 722c2-.4 3.9-1.3 5.3-2.8l423.9-423.9a9.96 9.96 0 0 0 0-14.1L694.9 114.9c-1.9-1.9-4.4-2.9-7.1-2.9s-5.2 1-7.1 2.9L256.8 538.8c-1.5 1.5-2.4 3.3-2.8 5.3l-29.5 168.2a33.5 33.5 0 0 0 9.4 29.8c6.6 6.4 14.9 9.9 23.8 9.9z" fill="currentColor"/>
            </svg>
            <span>修改信息</span>
          </div>

          <div
              class="menu-item"
              :class="{ active: activeTab === 'phone' }"
              @click="activeTab = 'phone'"
          >
            <svg class="menu-icon" viewBox="0 0 1024 1024">
              <path d="M877.1 238.7L770.6 132.3c-13-13-30.4-20.3-48.8-20.3s-35.8 7.2-48.8 20.3L558.3 246.8c-13 13-20.3 30.5-20.3 48.9 0 18.5 7.2 35.8 20.3 48.9l89.6 89.7a405.46 405.46 0 0 1-86.4 127.3c-36.7 36.9-79.6 66-127.2 86.6l-89.6-89.7c-13-13-30.4-20.3-48.8-20.3a68.2 68.2 0 0 0-48.8 20.3L132.3 673c-13 13-20.3 30.5-20.3 48.9 0 18.5 7.2 35.8 20.3 48.9l106.4 106.4c22.2 22.2 52.8 34.9 84.2 34.9 6.5 0 12.8-.5 19.2-1.6 132.4-21.8 263.8-92.3 369.9-198.3C818 606 888.4 474.6 910.4 342.1c6.3-37.6-6.3-76.3-33.3-103.4z" fill="currentColor"/>
            </svg>
            <span>修改手机号</span>
          </div>

          <div
              class="menu-item"
              :class="{ active: activeTab === 'password' }"
              @click="activeTab = 'password'"
          >
            <svg class="menu-icon" viewBox="0 0 1024 1024">
              <path d="M832 464h-68V240c0-70.7-57.3-128-128-128H388c-70.7 0-128 57.3-128 128v224h-68c-17.7 0-32 14.3-32 32v384c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 32-32V496c0-17.7-14.3-32-32-32zM332 240c0-30.9 25.1-56 56-56h248c30.9 0 56 25.1 56 56v224H332V240zm460 600H232V536h560v304zM484 701v53c0 4.4 3.6 8 8 8h40c4.4 0 8-3.6 8-8v-53a48.01 48.01 0 1 0-56 0z" fill="currentColor"/>
            </svg>
            <span>修改密码</span>
          </div>

          <router-link to="/favorites" class="menu-item">
            <svg class="menu-icon" viewBox="0 0 1024 1024">
              <path d="M923 283.6a260.04 260.04 0 0 0-56.9-82.8 264.4 264.4 0 0 0-84-55.5A265.34 265.34 0 0 0 679.7 125c-49.3 0-97.4 13.5-139.2 39-10 6.1-19.5 12.8-28.5 20.1-9-7.3-18.5-14-28.5-20.1-41.8-25.5-89.9-39-139.2-39-35.5 0-69.9 6.8-102.4 20.3-31.4 13-59.7 31.7-84 55.5a258.44 258.44 0 0 0-56.9 82.8c-13.9 32.3-21 66.6-21 101.9 0 33.3 6.8 68 20.3 103.3 11.3 29.5 27.5 60.1 48.2 91 32.8 48.9 77.9 99.9 133.9 151.6 92.8 85.7 184.7 144.9 188.6 147.3l23.7 15.2c10.5 6.7 24 6.7 34.5 0l23.7-15.2c3.9-2.5 95.7-61.6 188.6-147.3 56-51.7 101.1-102.7 133.9-151.6 20.7-30.9 37-61.5 48.2-91 13.5-35.3 20.3-70 20.3-103.3.1-35.3-7-69.6-20.9-101.9z" fill="currentColor"/>
            </svg>
            <span>收藏记录</span>
          </router-link>
        </nav>

        <button class="logout-btn" @click="handleLogout">
          <svg class="logout-icon" viewBox="0 0 1024 1024">
            <path d="M868 732h-70.3c-4.8 0-9.3 2.1-12.3 5.8-7 8.5-14.5 16.7-22.4 24.5a353.84 353.84 0 0 1-112.7 75.9A352.8 352.8 0 0 1 512.4 866c-47.9 0-94.3-9.4-137.9-27.8a353.84 353.84 0 0 1-112.7-75.9 353.28 353.28 0 0 1-76-112.5C167.3 606.2 158 559.9 158 512s9.4-94.2 27.8-137.8c17.8-42.1 43.4-80 76-112.5s70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8 47.9 0 94.3 9.3 137.9 27.8 42.2 17.8 80.1 43.4 112.7 75.9 7.9 7.9 15.3 16.1 22.4 24.5 3 3.7 7.6 5.8 12.3 5.8H868c6.3 0 10.2-7 6.7-12.3C798 160.5 663.8 81.6 511.3 82 271.7 82.6 79.6 277.1 82 516.4 84.4 751.9 276.2 942 512.4 942c152.1 0 285.7-78.8 362.3-197.7 3.4-5.3-.4-12.3-6.7-12.3zm88.9-226.3L815 393.7c-5.3-4.2-13-.4-13 6.3v76H488c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h314v76c0 6.7 7.8 10.5 13 6.3l141.9-112a8 8 0 0 0 0-12.6z" fill="currentColor"/>
          </svg>
          退出登录
        </button>
      </div>

      <!-- 右侧内容区域 -->
      <div class="profile-content">
        <!-- 个人资料 -->
        <div v-if="activeTab === 'profile'" class="content-card">
          <div class="section-header">
            <h3 class="section-title">个人资料</h3>
          </div>

          <div class="profile-form">
            <div class="form-row">
              <label class="form-label">昵称：</label>
              <div class="form-value">{{ userProfile?.nickname || userProfile?.username || '未设置' }}</div>
            </div>

            <div class="form-row">
              <label class="form-label">性别：</label>
              <div class="form-value">
                <span v-if="userProfile?.gender === 1">男</span>
                <span v-else-if="userProfile?.gender === 2">女</span>
                <span v-else>保密</span>
              </div>
            </div>

            <div class="form-row">
              <label class="form-label">账号：</label>
              <div class="form-value">{{ userProfile?.username || '未设置' }}</div>
            </div>

            <div class="form-row">
              <label class="form-label">绑定手机号：</label>
              <div class="form-value">
                <span v-if="userProfile?.phone">
                  {{ formatPhoneNumber(userProfile.phone) }}
                </span>
                <span v-else>未绑定</span>
              </div>
            </div>

            <div class="form-row">
              <label class="form-label">邮箱：</label>
              <div class="form-value">{{ userProfile?.email || '未设置' }}</div>
            </div>

            <div class="form-row">
              <label class="form-label">注册时间：</label>
              <div class="form-value">{{ formatCreateTime }}</div>
            </div>

            <div class="form-row">
              <label class="form-label">收藏数量：</label>
              <div class="form-value">{{ userProfile?.favoriteCount || 0 }} 部</div>
            </div>

            <div class="form-row">
              <label class="form-label">评论数量：</label>
              <div class="form-value">{{ userProfile?.commentCount || 0 }} 条</div>
            </div>
          </div>
        </div>

        <!-- 修改信息 -->
        <div v-if="activeTab === 'edit'" class="content-card">
          <div class="section-header">
            <h3 class="section-title">修改个人信息</h3>
          </div>

          <form @submit.prevent="handleUpdateInfo" class="edit-form">
            <div class="form-group">
              <label class="form-label-edit">昵称</label>
              <input
                  v-model="editForm.nickname"
                  type="text"
                  class="form-input"
                  placeholder="请输入昵称"
                  maxlength="50"
              />
            </div>

            <div class="form-group">
              <label class="form-label-edit">性别</label>
              <div class="radio-group">
                <label class="radio-label">
                  <input type="radio" v-model="editForm.gender" :value="0" />
                  <span>保密</span>
                </label>
                <label class="radio-label">
                  <input type="radio" v-model="editForm.gender" :value="1" />
                  <span>男</span>
                </label>
                <label class="radio-label">
                  <input type="radio" v-model="editForm.gender" :value="2" />
                  <span>女</span>
                </label>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label-edit">用户名</label>
              <input
                  v-model="editForm.username"
                  type="text"
                  class="form-input"
                  placeholder="请输入用户名（3-50个字符）"
                  pattern="^[a-zA-Z0-9_]+$"
                  minlength="3"
                  maxlength="50"
              />
              <div class="form-tip">只能包含字母、数字和下划线</div>
            </div>

            <div class="form-group">
              <label class="form-label-edit">邮箱</label>
              <input
                  v-model="editForm.email"
                  type="email"
                  class="form-input"
                  placeholder="请输入邮箱"
                  maxlength="100"
              />
            </div>

            <div class="form-group">
              <label class="form-label-edit">头像URL</label>
              <input
                  v-model="editForm.avatar"
                  type="text"
                  class="form-input"
                  placeholder="请输入头像URL"
                  maxlength="255"
              />
            </div>

            <div class="form-actions">
              <button type="submit" class="btn-primary" :disabled="updateLoading">
                {{ updateLoading ? '保存中...' : '保存修改' }}
              </button>
              <button type="button" class="btn-secondary" @click="resetEditForm">
                重置
              </button>
            </div>
          </form>
        </div>

        <!-- 修改手机号 -->
        <div v-if="activeTab === 'phone'" class="content-card">
          <div class="section-header">
            <h3 class="section-title">修改手机号</h3>
          </div>

          <form @submit.prevent="handleUpdatePhone" class="edit-form">
            <div class="form-group">
              <label class="form-label-edit">当前手机号</label>
              <input
                  type="text"
                  class="form-input"
                  :value="formatPhoneNumber(userProfile?.phone)"
                  disabled
              />
            </div>

            <div class="form-group">
              <label class="form-label-edit">新手机号</label>
              <input
                  v-model="phoneForm.phone"
                  type="tel"
                  class="form-input"
                  placeholder="请输入新手机号"
                  pattern="^1[3-9]\d{9}$"
                  maxlength="11"
              />
              <div class="form-tip">请输入11位手机号码</div>
            </div>

            <div class="form-actions">
              <button type="submit" class="btn-primary" :disabled="phoneLoading">
                {{ phoneLoading ? '保存中...' : '确认修改' }}
              </button>
              <button type="button" class="btn-secondary" @click="phoneForm.phone = ''">
                取消
              </button>
            </div>
          </form>
        </div>

        <!-- 修改密码 -->
        <div v-if="activeTab === 'password'" class="content-card">
          <div class="section-header">
            <h3 class="section-title">修改密码</h3>
          </div>

          <form @submit.prevent="handleUpdatePassword" class="edit-form">
            <div class="form-group">
              <label class="form-label-edit">原密码</label>
              <input
                  v-model="passwordForm.oldPassword"
                  type="password"
                  class="form-input"
                  placeholder="请输入原密码"
                  required
              />
            </div>

            <div class="form-group">
              <label class="form-label-edit">新密码</label>
              <input
                  v-model="passwordForm.newPassword"
                  type="password"
                  class="form-input"
                  placeholder="请输入新密码（6-20个字符）"
                  minlength="6"
                  maxlength="20"
                  required
              />
              <div class="form-tip">密码长度为6-20个字符</div>
            </div>

            <div class="form-group">
              <label class="form-label-edit">确认新密码</label>
              <input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  class="form-input"
                  placeholder="请再次输入新密码"
                  required
              />
            </div>

            <div class="form-actions">
              <button type="submit" class="btn-primary" :disabled="passwordLoading">
                {{ passwordLoading ? '保存中...' : '确认修改' }}
              </button>
              <button type="button" class="btn-secondary" @click="resetPasswordForm">
                取消
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// API基础地址
const API_BASE_URL = 'http://192.168.0.80:9102';

// 数据状态
const userProfile = ref(null);
const loading = ref(true);
const activeTab = ref('profile');
const updateLoading = ref(false);
const phoneLoading = ref(false);
const passwordLoading = ref(false);

// 默认头像
const defaultAvatar = 'https://ui-avatars.com/api/?name=User&background=667eea&color=fff&size=200';

// 编辑表单
const editForm = ref({
  nickname: '',
  gender: 0,
  username: '',
  email: '',
  avatar: ''
});

// 手机号表单
const phoneForm = ref({
  phone: ''
});

// 密码表单
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 获取Token
const getToken = () => {
  return localStorage.getItem('token');
};

// 获取用户ID
const getUserId = () => {
  const token = getToken();
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.id || payload.userId;
    } catch (e) {
      console.error('解析token失败:', e);
      return null;
    }
  }
  return null;
};

// 获取用户信息
const fetchUserProfile = async () => {
  const userId = getUserId();
  const token = getToken();

  if (!userId || !token) {
    showMessage('请先登录', 'error');
    router.push('/login');
    return;
  }

  loading.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/user/profile/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        userProfile.value = result.data;
        // 初始化编辑表单
        editForm.value = {
          nickname: result.data.nickname || '',
          gender: result.data.gender || 0,
          username: result.data.username || '',
          email: result.data.email || '',
          avatar: result.data.avatar || ''
        };
      } else {
        showMessage(result.message || '获取用户信息失败', 'error');
      }
    } else {
      throw new Error('请求失败');
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
    showMessage('获取用户信息失败', 'error');
  } finally {
    loading.value = false;
  }
};

// 更新个人信息
const handleUpdateInfo = async () => {
  const token = getToken();
  if (!token) {
    showMessage('请先登录', 'error');
    return;
  }

  // 验证用户名格式
  if (editForm.value.username) {
    const usernamePattern = /^[a-zA-Z0-9_]+$/;
    if (!usernamePattern.test(editForm.value.username)) {
      showMessage('用户名只能包含字母、数字和下划线', 'error');
      return;
    }
    if (editForm.value.username.length < 3 || editForm.value.username.length > 50) {
      showMessage('用户名长度必须在3-50个字符之间', 'error');
      return;
    }
  }

  // 验证邮箱格式
  if (editForm.value.email) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(editForm.value.email)) {
      showMessage('邮箱格式不正确', 'error');
      return;
    }
  }

  updateLoading.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/user/update`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(editForm.value)
    });

    const result = await response.json();
    if (result.code === 200) {
      showMessage('信息更新成功', 'success');
      await fetchUserProfile();
      activeTab.value = 'profile';
    } else {
      showMessage(result.message || '更新失败', 'error');
    }
  } catch (error) {
    console.error('更新信息失败:', error);
    showMessage('更新失败，请稍后重试', 'error');
  } finally {
    updateLoading.value = false;
  }
};

// 更新手机号
const handleUpdatePhone = async () => {
  const token = getToken();
  if (!token) {
    showMessage('请先登录', 'error');
    return;
  }

  // 验证手机号格式
  const phonePattern = /^1[3-9]\d{9}$/;
  if (!phonePattern.test(phoneForm.value.phone)) {
    showMessage('手机号格式不正确', 'error');
    return;
  }

  phoneLoading.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/user/update`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        phone: phoneForm.value.phone
      })
    });

    const result = await response.json();
    if (result.code === 200) {
      showMessage('手机号修改成功', 'success');
      await fetchUserProfile();
      phoneForm.value.phone = '';
      activeTab.value = 'profile';
    } else {
      showMessage(result.message || '修改失败', 'error');
    }
  } catch (error) {
    console.error('修改手机号失败:', error);
    showMessage('修改失败，请稍后重试', 'error');
  } finally {
    phoneLoading.value = false;
  }
};

// 更新密码
const handleUpdatePassword = async () => {
  const token = getToken();
  if (!token) {
    showMessage('请先登录', 'error');
    return;
  }

  // 验证密码
  if (!passwordForm.value.oldPassword) {
    showMessage('请输入原密码', 'error');
    return;
  }

  if (!passwordForm.value.newPassword) {
    showMessage('请输入新密码', 'error');
    return;
  }

  if (passwordForm.value.newPassword.length < 6 || passwordForm.value.newPassword.length > 20) {
    showMessage('密码长度必须在6-20个字符之间', 'error');
    return;
  }

  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    showMessage('两次输入的密码不一致', 'error');
    return;
  }

  passwordLoading.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/user/update`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        oldPassword: passwordForm.value.oldPassword,
        newPassword: passwordForm.value.newPassword
      })
    });

    const result = await response.json();
    if (result.code === 200) {
      showMessage('密码修改成功，请重新登录', 'success');
      resetPasswordForm();
      setTimeout(() => {
        handleLogout();
      }, 1500);
    } else {
      showMessage(result.message || '修改失败', 'error');
    }
  } catch (error) {
    console.error('修改密码失败:', error);
    showMessage('修改失败，请稍后重试', 'error');
  } finally {
    passwordLoading.value = false;
  }
};

// 重置编辑表单
const resetEditForm = () => {
  if (userProfile.value) {
    editForm.value = {
      nickname: userProfile.value.nickname || '',
      gender: userProfile.value.gender || 0,
      username: userProfile.value.username || '',
      email: userProfile.value.email || '',
      avatar: userProfile.value.avatar || ''
    };
  }
};

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  };
};

// 退出登录
const handleLogout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('isLoggedIn');
  showMessage('已退出登录', 'success');
  router.push('/login');
};

// 格式化手机号
const formatPhoneNumber = (phone) => {
  if (phone && phone.length === 11) {
    return phone.substring(0, 3) + '****' + phone.substring(7);
  }
  return phone || '未绑定';
};

// 计算最近在线时间
const lastOnlineTime = computed(() => {
  if (userProfile.value?.updateTime) {
    const updateTime = new Date(userProfile.value.updateTime);
    const now = new Date();
    const diffHours = Math.floor((now - updateTime) / (1000 * 60 * 60));

    if (diffHours < 1) {
      return '刚刚';
    } else if (diffHours < 24) {
      return `${diffHours}小时前`;
    } else {
      return `${Math.floor(diffHours / 24)}天前`;
    }
  }
  return '未知';
});

// 格式化注册时间
const formatCreateTime = computed(() => {
  if (userProfile.value?.createTime) {
    const date = new Date(userProfile.value.createTime);
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  }
  return '未知';
});

// 简单的消息提示
const showMessage = (message, type = 'info') => {
  alert(message);
};

// 页面加载时获取用户信息
onMounted(() => {
  fetchUserProfile();
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.profile-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.page-header {
  margin-bottom: 24px;
  text-align: center;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #fff;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-wrapper {
  display: flex;
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 左侧导航栏样式 */
.profile-sidebar {
  width: 280px;
  flex-shrink: 0;
}

.user-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 32px 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
  backdrop-filter: blur(10px);
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto 20px;
  border: 4px solid #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  text-align: center;
}

.user-nickname {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-status {
  font-size: 13px;
  color: #666;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 6px 16px;
  border-radius: 20px;
  display: inline-block;
}

.sidebar-menu {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 12px 0;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
  backdrop-filter: blur(10px);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  color: #333;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  border-left: 4px solid transparent;
}

.menu-item:hover {
  background: linear-gradient(90deg, rgba(102, 126, 234, 0.1) 0%, transparent 100%);
  color: #667eea;
}

.menu-item.active {
  background: linear-gradient(90deg, rgba(102, 126, 234, 0.15) 0%, transparent 100%);
  color: #667eea;
  border-left-color: #667eea;
  font-weight: 600;
}

.menu-icon {
  width: 20px;
  height: 20px;
  margin-right: 12px;
  flex-shrink: 0;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #ff4d4f;
  border-radius: 16px;
  color: #ff4d4f;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.logout-btn:hover {
  background: #ff4d4f;
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.3);
}

.logout-icon {
  width: 18px;
  height: 18px;
  margin-right: 8px;
}

/* 右侧内容区域样式 */
.profile-content {
  flex: 1;
  min-width: 0;
}

.content-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.section-header {
  margin-bottom: 32px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
}

.section-title {
  font-size: 22px;
  font-weight: 700;
  color: #333;
  margin: 0;
}

/* 个人资料展示 */
.profile-form {
  max-width: 600px;
}

.form-row {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.form-row:last-child {
  border-bottom: none;
}

.form-label {
  width: 140px;
  font-size: 15px;
  font-weight: 600;
  color: #666;
  flex-shrink: 0;
}

.form-value {
  flex: 1;
  font-size: 15px;
  color: #333;
  padding: 0 16px;
}

/* 编辑表单样式 */
.edit-form {
  max-width: 600px;
}

.form-group {
  margin-bottom: 24px;
}

.form-label-edit {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  font-size: 15px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: #fff;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input:disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
}

.form-tip {
  margin-top: 8px;
  font-size: 13px;
  color: #999;
}

.radio-group {
  display: flex;
  gap: 24px;
}

.radio-label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.radio-label input[type="radio"] {
  width: 18px;
  height: 18px;
  margin-right: 8px;
  cursor: pointer;
  accent-color: #667eea;
}

.radio-label span {
  font-size: 15px;
  color: #333;
}

.form-actions {
  display: flex;
  gap: 16px;
  margin-top: 32px;
}

.btn-primary {
  flex: 1;
  padding: 14px 32px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  flex: 1;
  padding: 14px 32px;
  font-size: 16px;
  font-weight: 600;
  color: #666;
  background: #fff;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background: #f5f5f5;
  border-color: #d0d0d0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-wrapper {
    flex-direction: column;
  }

  .profile-sidebar {
    width: 100%;
  }

  .user-card {
    padding: 24px;
  }

  .content-card {
    padding: 24px;
  }

  .form-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .form-label {
    width: 100%;
    margin-bottom: 8px;
  }

  .form-value {
    padding: 0;
  }

  .form-actions {
    flex-direction: column;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
  }
}
</style>