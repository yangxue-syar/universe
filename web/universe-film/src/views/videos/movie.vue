<template>
  <div class="movie-page">
    <!-- 轮播图区域 -->
    <div class="carousel-section">
      <div class="carousel-container">
        <el-carousel
            :interval="5000"
            height="600px"
            arrow="always"
            indicator-position="none"
        >
          <el-carousel-item v-for="movie in carouselMovies" :key="movie.id">
            <div class="carousel-item" @click="goToDetail(movie.id)">
              <!-- 背景图片 -->
              <img
                  :src="movie.cover || movie.poster"
                  :alt="movie.title"
                  class="carousel-bg"
              />

              <!-- 渐变遮罩 -->
              <div class="carousel-overlay"></div>

              <!-- 右侧推荐列表 -->
              <div class="carousel-sidebar">
                <div class="sidebar-item"
                     v-for="item in sideRecommendMovies.slice(0, 5)"
                     :key="item.id"
                     @click.stop="goToDetail(item.id)"
                >
                  <div class="sidebar-text">{{ item.title }}</div>
                </div>
              </div>

              <!-- 左侧内容 -->
              <div class="carousel-content">
                <h1 class="movie-title">{{ movie.title }}</h1>

                <div class="movie-info-row">
                  <span class="info-badge">电影</span>
                  <span class="info-badge">剧情</span>
                </div>

                <div class="action-buttons">
                  <button class="btn-primary" @click.stop="playMovie(movie.id)">
                    <svg class="icon" viewBox="0 0 24 24" fill="currentColor">
                      <path d="M8 5v14l11-7z"/>
                    </svg>
                    立即播放
                  </button>
                  <button class="btn-secondary" @click.stop="addToFavorite(movie.id)">
                    <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

    <!-- 分类区域 - 只显示类型分类 -->
    <div class="category-section">
      <!-- 主分类标签 -->
      <div class="main-category">
        <div class="category-icon">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M18 4l2 4h-3l-2-4h-2l2 4h-3l-2-4H8l2 4H7L5 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V4h-4z"/>
          </svg>
        </div>
        <span class="category-text">电影</span>
      </div>

      <!-- 类型分类 - 只显示一行 -->
      <div class="category-list">
        <div
            v-for="category in typeCategories"
            :key="category.id"
            class="category-item"
            @click="goToFilter(category.id)"
        >
          {{ category.name }}
        </div>
      </div>
    </div>

    <!-- 热门推荐区域 -->
    <div class="content-section">
      <div class="section-header">
        <h3 class="section-title">热门推荐</h3>
        <div class="section-actions">
          <button class="btn-text" @click="refreshHot">换一换</button>
          <button class="btn-text" @click="viewMore">更多</button>
        </div>
      </div>

      <div class="movie-grid">
        <div
            v-for="movie in hotMovies"
            :key="movie.id"
            class="movie-card"
            @click="goToDetail(movie.id)"
        >
          <div class="card-poster">
            <img :src="movie.poster" :alt="movie.title" />
            <div class="card-overlay">
              <button class="play-btn" @click.stop="playMovie(movie.id)">
                <svg viewBox="0 0 24 24" fill="currentColor">
                  <path d="M8 5v14l11-7z"/>
                </svg>
              </button>
            </div>
          </div>
          <div class="card-info">
            <div class="card-title">{{ movie.title }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import {
  getCarouselLatest,
  getRecommendMovies,
  getCategoryTree
} from '@/api/movie';

const router = useRouter();

// 数据定义
const carouselMovies = ref([]);
const sideRecommendMovies = ref([]);
const categories = ref([]);
const hotMovies = ref([]);

// 只显示类型分类 (sort: 1-19)
const typeCategories = computed(() => {
  return categories.value.filter(cat => cat.sort >= 1 && cat.sort < 20);
});

// 获取轮播图电影
const getCarouselMoviesData = async () => {
  try {
    const res = await getCarouselLatest(5);
    carouselMovies.value = res.data || [];
  } catch (error) {
    console.error('获取轮播图电影失败:', error);
  }
};

// 获取侧边推荐电影
const getSideRecommendMoviesData = async () => {
  try {
    const res = await getRecommendMovies(8);
    sideRecommendMovies.value = res.data || [];
  } catch (error) {
    console.error('获取推荐电影失败:', error);
  }
};

// 获取分类列表
const getCategoriesData = async () => {
  try {
    const res = await getCategoryTree();
    const movieCategory = (res.data || []).find(cat => cat.name === '电影');
    if (movieCategory && movieCategory.children) {
      categories.value = movieCategory.children.sort((a, b) => (a.sort || 0) - (b.sort || 0));
    }
  } catch (error) {
    console.error('获取分类列表失败:', error);
  }
};

// 获取热门电影
const getHotMoviesData = async () => {
  try {
    const res = await getRecommendMovies(12);
    hotMovies.value = res.data || [];
  } catch (error) {
    console.error('获取热门电影失败:', error);
  }
};

// 刷新热门
const refreshHot = () => {
  getHotMoviesData();
};

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/movie/detail/${id}`);
};

// 跳转到筛选页 - 带上分类ID
const goToFilter = (categoryId) => {
  router.push({
    path: '/movie/filter',
    query: { categoryId }
  });
};

// 查看更多 - 不带分类ID
const viewMore = () => {
  router.push('/movie/filter');
};

// 播放电影
const playMovie = (id) => {
  router.push(`/movie/play/${id}`);
};

// 添加到收藏
const addToFavorite = (id) => {
  ElMessage.success('已添加到收藏');
};

// 页面加载时获取数据
onMounted(async () => {
  await getCategoriesData();
  await getCarouselMoviesData();
  await getSideRecommendMoviesData();
  await getHotMoviesData();
});
</script>

<style scoped>
/* 整体布局 */
.movie-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #0f1c3f 0%, #1a2744 50%, #0f1c3f 100%);
  color: #fff;
}

/* ========================================
   轮播图区域
   ======================================== */
.carousel-section {
  position: relative;
  width: 100%;
  height: 600px;
}

.carousel-container {
  width: 100%;
  height: 100%;
}

.carousel-item {
  position: relative;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.carousel-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
      90deg,
      rgba(0, 0, 0, 0.9) 0%,
      rgba(0, 0, 0, 0.7) 40%,
      rgba(0, 0, 0, 0.5) 60%,
      rgba(0, 0, 0, 0.3) 100%
  );
}

/* 右侧推荐列表 */
.carousel-sidebar {
  position: absolute;
  right: 40px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: 10;
}

.sidebar-item {
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-item:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateX(-5px);
}

.sidebar-text {
  font-size: 14px;
  color: #fff;
  white-space: nowrap;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 左侧内容 */
.carousel-content {
  position: absolute;
  left: 80px;
  top: 50%;
  transform: translateY(-50%);
  max-width: 600px;
  z-index: 10;
}

.movie-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  line-height: 1.2;
}

.movie-info-row {
  display: flex;
  gap: 12px;
  margin-bottom: 30px;
}

.info-badge {
  padding: 6px 16px;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  font-size: 14px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

/* 按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
  align-items: center;
}

.btn-primary,
.btn-secondary {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 14px;
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.icon {
  width: 20px;
  height: 20px;
}

/* ========================================
   分类区域 - 简化版,只显示一行
   ======================================== */
.category-section {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px 20px;
}

/* 主分类标签 */
.main-category {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.06);
  border-radius: 50px;
  width: fit-content;
  margin-bottom: 24px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.category-icon {
  width: 24px;
  height: 24px;
  color: #667eea;
}

.category-icon svg {
  width: 100%;
  height: 100%;
}

.category-text {
  font-size: 16px;
  font-weight: 500;
}

/* 分类列表 - 只有一行 */
.category-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.category-item {
  padding: 10px 24px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50px;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.25s;
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.category-item:hover {
  background: rgba(255, 255, 255, 0.12);
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.2);
}

/* ========================================
   内容区域
   ======================================== */
.content-section {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-title {
  font-size: 28px;
  font-weight: bold;
}

.section-actions {
  display: flex;
  gap: 20px;
}

.btn-text {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  padding: 8px 16px;
  border-radius: 4px;
}

.btn-text:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.05);
}

/* 电影网格 */
.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
}

.movie-card {
  cursor: pointer;
  transition: all 0.3s;
}

.movie-card:hover {
  transform: translateY(-8px);
}

.card-poster {
  position: relative;
  aspect-ratio: 2/3;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 12px;
  background: rgba(255, 255, 255, 0.05);
}

.card-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s;
}

.movie-card:hover .card-overlay {
  opacity: 1;
}

.play-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.play-btn:hover {
  transform: scale(1.1);
  background: #fff;
}

.play-btn svg {
  width: 24px;
  height: 24px;
  color: #333;
  margin-left: 3px;
}

.card-info {
  padding: 0 4px;
}

.card-title {
  font-size: 15px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: rgba(255, 255, 255, 0.9);
}

/* ========================================
   响应式设计
   ======================================== */
@media (max-width: 1200px) {
  .carousel-content {
    left: 40px;
    max-width: 500px;
  }

  .carousel-sidebar {
    right: 20px;
  }

  .movie-title {
    font-size: 36px;
  }
}

@media (max-width: 768px) {
  .carousel-sidebar {
    display: none;
  }

  .carousel-content {
    left: 20px;
    right: 20px;
    max-width: none;
  }

  .movie-title {
    font-size: 28px;
  }

  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 16px;
  }

  .category-list {
    gap: 8px;
  }

  .category-item {
    padding: 8px 18px;
    font-size: 14px;
  }
}

/* Element Plus 轮播图样式覆盖 */
:deep(.el-carousel__arrow) {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

:deep(.el-carousel__arrow:hover) {
  background: rgba(255, 255, 255, 0.2);
}
</style>