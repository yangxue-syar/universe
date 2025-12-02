<template>
  <div class="favorite-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">我的收藏</h2>
      <div class="page-subtitle">共收藏 {{ favorites.length }} 部影片</div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 空状态 -->
    <div v-else-if="favorites.length === 0" class="empty-state">
      <svg class="empty-icon" viewBox="0 0 1024 1024">
        <path d="M923 283.6a260.04 260.04 0 0 0-56.9-82.8 264.4 264.4 0 0 0-84-55.5A265.34 265.34 0 0 0 679.7 125c-49.3 0-97.4 13.5-139.2 39-10 6.1-19.5 12.8-28.5 20.1-9-7.3-18.5-14-28.5-20.1-41.8-25.5-89.9-39-139.2-39-35.5 0-69.9 6.8-102.4 20.3-31.4 13-59.7 31.7-84 55.5a258.44 258.44 0 0 0-56.9 82.8c-13.9 32.3-21 66.6-21 101.9 0 33.3 6.8 68 20.3 103.3 11.3 29.5 27.5 60.1 48.2 91 32.8 48.9 77.9 99.9 133.9 151.6 92.8 85.7 184.7 144.9 188.6 147.3l23.7 15.2c10.5 6.7 24 6.7 34.5 0l23.7-15.2c3.9-2.5 95.7-61.6 188.6-147.3 56-51.7 101.1-102.7 133.9-151.6 20.7-30.9 37-61.5 48.2-91 13.5-35.3 20.3-70 20.3-103.3.1-35.3-7-69.6-20.9-101.9z" fill="currentColor"/>
      </svg>
      <h3>还没有收藏任何影片</h3>
      <p>快去收藏喜欢的电影吧！</p>
      <button class="btn-primary" @click="goToHome">去首页逛逛</button>
    </div>

    <!-- 收藏列表 -->
    <div v-else class="favorites-grid">
      <div
          v-for="item in favorites"
          :key="item.favoriteId"
          class="movie-card"
      >
        <div class="movie-poster-wrapper" @click="goToMovie(item.movieId)">
          <img
              :src="item.poster || defaultPoster"
              :alt="item.title"
              class="movie-poster"
              @error="handleImageError"
          />
          <div class="movie-overlay">
            <button class="btn-play">
              <svg viewBox="0 0 24 24" class="play-icon">
                <path d="M8 5v14l11-7z" fill="currentColor"/>
              </svg>
              立即观看
            </button>
          </div>
        </div>

        <div class="movie-info">
          <h3 class="movie-title" @click="goToMovie(item.movieId)">
            {{ item.title }}
          </h3>

          <div class="movie-meta">
            <div class="movie-rating">
              <svg class="star-icon" viewBox="0 0 1024 1024">
                <path d="M908.1 353.1l-253.9-36.9L540.7 86.1c-3.1-6.3-8.2-11.4-14.5-14.5-15.8-7.8-35-1.3-42.9 14.5L369.8 316.2l-253.9 36.9c-7 1-13.4 4.3-18.3 9.3a32.05 32.05 0 0 0 .6 45.3l183.7 179.1-43.4 252.9a31.95 31.95 0 0 0 46.4 33.7L512 754l227.1 119.4c6.2 3.3 13.4 4.4 20.3 3.2 17.4-3 29.1-19.5 26.1-36.9l-43.4-252.9 183.7-179.1c5-4.9 8.3-11.3 9.3-18.3 2.7-17.5-9.5-33.7-27-36.3z" fill="currentColor"/>
              </svg>
              <span>{{ item.rating || 'N/A' }}</span>
            </div>

            <div class="movie-duration">
              <svg class="clock-icon" viewBox="0 0 1024 1024">
                <path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="currentColor"/>
                <path d="M686.7 638.6L544.1 535.5V288c0-4.4-3.6-8-8-8h-48c-4.4 0-8 3.6-8 8v275.4c0 2.6 1.2 5 3.3 6.5l165.4 120.6c3.6 2.6 8.6 1.8 11.2-1.7l28.6-39c2.6-3.7 1.8-8.7-1.9-11.2z" fill="currentColor"/>
              </svg>
              <span>{{ item.duration ? `${item.duration}分钟` : '未知' }}</span>
            </div>
          </div>

          <p class="movie-description">
            {{ truncateText(item.description, 60) }}
          </p>

          <div class="movie-footer">
            <div class="favorite-time">
              收藏于：{{ formatDate(item.createTime) }}
            </div>

            <button
                class="btn-remove"
                @click.stop="handleRemoveFavorite(item.movieId)"
                :disabled="removingIds.includes(item.movieId)"
            >
              <svg class="remove-icon" viewBox="0 0 1024 1024">
                <path d="M864 256H736v-80c0-35.3-28.7-64-64-64H352c-35.3 0-64 28.7-64 64v80H160c-17.7 0-32 14.3-32 32v32c0 4.4 3.6 8 8 8h60.4l24.7 523c1.6 34.1 29.8 61 63.9 61h454c34.2 0 62.3-26.8 63.9-61l24.7-523H888c4.4 0 8-3.6 8-8v-32c0-17.7-14.3-32-32-32zm-504-72h304v72H360v-72zm371.3 656H292.7l-24.2-512h487l-24.2 512z" fill="currentColor"/>
              </svg>
              {{ removingIds.includes(item.movieId) ? '取消中...' : '取消收藏' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// API基础地址
const API_BASE_URL = 'http://192.168.0.80:9102';

// 数据状态
const favorites = ref([]);
const loading = ref(true);
const removingIds = ref([]);

// 默认海报
const defaultPoster = 'https://via.placeholder.com/300x450/667eea/ffffff?text=No+Image';

// 获取Token
const getToken = () => {
  return localStorage.getItem('token');
};

// 获取收藏列表
const fetchFavorites = async () => {
  const token = getToken();
  if (!token) {
    showMessage('请先登录', 'error');
    router.push('/login');
    return;
  }

  loading.value = true;
  try {
    const response = await fetch(`${API_BASE_URL}/favorite/list`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        favorites.value = result.data || [];
      } else {
        showMessage(result.message || '获取收藏列表失败', 'error');
      }
    } else {
      throw new Error('请求失败');
    }
  } catch (error) {
    console.error('获取收藏列表失败:', error);
    showMessage('获取收藏列表失败', 'error');
  } finally {
    loading.value = false;
  }
};

// 取消收藏
const handleRemoveFavorite = async (movieId) => {
  const token = getToken();
  if (!token) {
    showMessage('请先登录', 'error');
    return;
  }

  if (!confirm('确定要取消收藏这部电影吗？')) {
    return;
  }

  removingIds.value.push(movieId);

  try {
    const response = await fetch(`${API_BASE_URL}/favorite/${movieId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    const result = await response.json();
    if (result.code === 200) {
      showMessage('已取消收藏', 'success');
      // 从列表中移除
      favorites.value = favorites.value.filter(item => item.movieId !== movieId);
    } else {
      showMessage(result.message || '取消收藏失败', 'error');
    }
  } catch (error) {
    console.error('取消收藏失败:', error);
    showMessage('取消收藏失败，请稍后重试', 'error');
  } finally {
    removingIds.value = removingIds.value.filter(id => id !== movieId);
  }
};

// 跳转到电影详情
const goToMovie = (movieId) => {
  router.push(`/movie/${movieId}`);
};

// 跳转到首页
const goToHome = () => {
  router.push('/');
};

// 图片加载失败处理
const handleImageError = (event) => {
  event.target.src = defaultPoster;
};

// 截断文本
const truncateText = (text, maxLength) => {
  if (!text) return '暂无简介';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知';
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
};

// 简单的消息提示
const showMessage = (message, type = 'info') => {
  alert(message);
};

// 页面加载时获取收藏列表
onMounted(() => {
  fetchFavorites();
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.favorite-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

/* 页面标题 */
.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 12px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  color: #fff;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-container p {
  margin-top: 16px;
  font-size: 16px;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 60px 40px;
  max-width: 500px;
  margin: 0 auto;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  width: 120px;
  height: 120px;
  color: #d0d0d0;
  margin-bottom: 24px;
}

.empty-state h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 12px;
}

.empty-state p {
  font-size: 16px;
  color: #666;
  margin-bottom: 32px;
}

/* 收藏列表 */
.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

/* 电影卡片 */
.movie-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.movie-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
}

.movie-poster-wrapper {
  position: relative;
  width: 100%;
  padding-top: 150%;
  overflow: hidden;
  cursor: pointer;
  background: #f0f0f0;
}

.movie-poster {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.movie-card:hover .movie-poster {
  transform: scale(1.05);
}

.movie-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.movie-poster-wrapper:hover .movie-overlay {
  opacity: 1;
}

.btn-play {
  padding: 14px 28px;
  background: rgba(255, 255, 255, 0.95);
  color: #667eea;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.btn-play:hover {
  background: #fff;
  transform: scale(1.05);
}

.play-icon {
  width: 20px;
  height: 20px;
}

.movie-info {
  padding: 20px;
}

.movie-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
  transition: color 0.3s ease;
}

.movie-title:hover {
  color: #667eea;
}

.movie-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.movie-rating,
.movie-duration {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #666;
}

.star-icon,
.clock-icon {
  width: 16px;
  height: 16px;
  color: #ffa500;
}

.clock-icon {
  color: #999;
}

.movie-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.movie-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.favorite-time {
  font-size: 13px;
  color: #999;
}

.btn-remove {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: #fff;
  border: 1px solid #ff4d4f;
  border-radius: 8px;
  color: #ff4d4f;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-remove:hover:not(:disabled) {
  background: #fff2f0;
}

.btn-remove:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.remove-icon {
  width: 16px;
  height: 16px;
}

.btn-primary {
  padding: 14px 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .favorite-container {
    padding: 20px 12px;
  }

  .page-title {
    font-size: 28px;
  }

  .page-subtitle {
    font-size: 16px;
  }

  .favorites-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 16px;
  }

  .empty-state {
    padding: 40px 20px;
  }

  .movie-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .btn-remove {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .favorites-grid {
    grid-template-columns: 1fr;
  }
}
</style>