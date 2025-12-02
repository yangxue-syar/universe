<template>
  <div class="home-container">
    <!-- 轮播图区域 -->
    <div class="carousel-wrapper">
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>

      <!-- 轮播图主体 -->
      <div v-else class="carousel-container">
        <transition-group name="fade" tag="div">
          <div
              v-for="(movie, index) in movies"
              :key="movie.id"
              v-show="index === currentIndex"
              class="carousel-item"
              :style="{ backgroundImage: `url(${movie.poster || movie.cover})` }"
          >
            <!-- 渐变遮罩 -->
            <div class="carousel-overlay"></div>

            <!-- 电影信息 -->
            <div class="movie-info">
              <div class="movie-content">
                <!-- 电影标题 -->
                <h1 class="movie-title">{{ movie.title }}</h1>

                <!-- 评分和观看次数 -->
                <div class="movie-meta">
                  <span class="rating" v-if="movie.rating">
                    <i class="star-icon">⭐</i>
                    {{ movie.rating }}
                  </span>
                  <span class="views" v-if="movie.viewsCount">
                    {{ formatViews(movie.viewsCount) }}人观看
                  </span>
                </div>

                <!-- 导演和演员信息 -->
                <div class="movie-details" v-if="movie.director || movie.actors">
                  <p v-if="movie.director">
                    <span class="label">导演：</span>{{ movie.director }}
                  </p>
                  <p v-if="movie.actors" class="actors-text">
                    <span class="label">主演：</span>{{ movie.actors }}
                  </p>
                </div>

                <!-- 简介 -->
                <p class="movie-description" v-if="movie.description">
                  {{ movie.description }}
                </p>

                <!-- 操作按钮 -->
                <div class="action-buttons">
                  <button class="btn-play" @click="playMovie(movie)">
                    <span class="play-icon">▶</span>
                    立即播放
                  </button>
                  <button class="btn-favorite" @click="toggleFavorite(movie)">
                    <span class="heart-icon">♥</span>
                  </button>
                </div>
              </div>

              <!-- 右侧推荐列表 -->
              <div class="recommended-list" v-if="movies.length > 0">
                <div
                    v-for="(item, idx) in movies"
                    :key="item.id"
                    :class="['recommended-item', { active: idx === currentIndex }]"
                    @click="jumpToMovie(idx)"
                >
                  <div class="item-title">{{ item.title }}</div>
                  <div class="item-meta">
                    <span v-if="item.rating">{{ item.rating }}/10</span>
                    <span v-else>暂无评分</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </transition-group>

        <!-- 轮播指示器 -->
        <div class="carousel-indicators" v-if="movies.length > 1">
          <span
              v-for="(movie, index) in movies"
              :key="'indicator-' + movie.id"
              :class="['indicator', { active: index === currentIndex }]"
              @click="goToSlide(index)"
          ></span>
        </div>

        <!-- 左右切换按钮 -->
        <button
            v-if="movies.length > 1"
            class="carousel-control prev"
            @click="prevSlide"
        >‹</button>
        <button
            v-if="movies.length > 1"
            class="carousel-control next"
            @click="nextSlide"
        >›</button>
      </div>
    </div>

    <!-- 公告展示模块 -->
    <div class="announcement-wrapper">
      <div class="announcement-container">
        <div class="announcement-header">
          <h2 class="announcement-title">最新公告</h2>
          <span class="announcement-update-time" v-if="announcement.updateTime">
            更新时间：{{ announcement.updateTime }}
          </span>
        </div>
        <div class="announcement-content">
          <div v-if="announcementLoading" class="announcement-loading">
            <div class="loading-spinner"></div>
            <p>加载公告中...</p>
          </div>
          <div v-else-if="announcement.content" class="announcement-text">
            {{ announcement.content }}
          </div>
          <div v-else class="announcement-empty">
            暂无公告
          </div>
        </div>
      </div>
    </div>

    <!-- ========== 🆕 热门内容区块 ========== -->

    <!-- 1. 正在热播 -->
    <div class="content-section">
      <div class="section-header">
        <h2 class="section-title">正在热播</h2>
        <div class="section-actions">
          <button class="btn-refresh" @click="refreshTrending">换一换</button>
          <button class="btn-more" @click="goToMore('trending')">更多 ›</button>
        </div>
      </div>
      <div class="movie-carousel">
        <div class="movie-scroll-container" ref="trendingScroll">
          <div
              v-for="item in trendingList"
              :key="item.id"
              class="movie-card"
              @click="goToDetail(item.id)"
          >
            <div class="card-poster">
              <img :src="item.poster" :alt="item.title" loading="lazy" />
              <div class="card-badge blue">蓝光</div>
              <div class="card-tag new" v-if="isNew(item.releaseDate)">新</div>
              <div class="card-overlay">
                <button class="play-button" @click.stop="playMovie(item)">
                  <span class="play-icon">▶</span>
                </button>
              </div>
            </div>
            <div class="card-info">
              <div class="card-title">{{ item.title }}</div>
              <div class="card-meta">
                <span class="views">🔥 {{ formatViews(item.viewsCount) }}</span>
                <span class="rating" v-if="item.rating">{{ item.rating }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 2. 最新电影 -->
    <div class="content-section">
      <div class="section-header">
        <h2 class="section-title">最新电影</h2>
        <div class="section-actions">
          <button class="btn-refresh" @click="refreshLatestMovies">换一换</button>
          <button class="btn-more" @click="goToMore('movie')">更多 ›</button>
        </div>
      </div>
      <div class="movie-carousel">
        <div class="movie-scroll-container" ref="moviesScroll">
          <div
              v-for="item in latestMovies"
              :key="item.id"
              class="movie-card"
              @click="goToDetail(item.id)"
          >
            <div class="card-poster">
              <img :src="item.poster" :alt="item.title" loading="lazy" />
              <div class="card-badge blue">蓝光</div>
              <div class="card-tag new" v-if="isNew(item.releaseDate)">新</div>
              <div class="card-overlay">
                <button class="play-button" @click.stop="playMovie(item)">
                  <span class="play-icon">▶</span>
                </button>
              </div>
            </div>
            <div class="card-info">
              <div class="card-title">{{ item.title }}</div>
              <div class="card-meta">
                <span class="views">🔥 {{ formatViews(item.viewsCount) }}</span>
                <span class="rating" v-if="item.rating">{{ item.rating }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 3. 最新电视剧 -->
    <div class="content-section">
      <div class="section-header">
        <h2 class="section-title">最新电视剧</h2>
        <div class="section-actions">
          <button class="btn-refresh" @click="refreshLatestTv">换一换</button>
          <button class="btn-more" @click="goToMore('tv')">更多 ›</button>
        </div>
      </div>
      <div class="movie-carousel">
        <div class="movie-scroll-container" ref="tvScroll">
          <div
              v-for="item in latestTv"
              :key="item.id"
              class="movie-card"
              @click="goToDetail(item.id)"
          >
            <div class="card-poster">
              <img :src="item.poster" :alt="item.title" loading="lazy" />
              <div class="card-badge blue">蓝光</div>
              <div class="card-tag new" v-if="isNew(item.releaseDate)">新</div>
              <div class="card-overlay">
                <button class="play-button" @click.stop="playMovie(item)">
                  <span class="play-icon">▶</span>
                </button>
              </div>
            </div>
            <div class="card-info">
              <div class="card-title">{{ item.title }}</div>
              <div class="card-meta">
                <span class="views">🔥 {{ formatViews(item.viewsCount) }}</span>
                <span class="rating" v-if="item.rating">{{ item.rating }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 4. 最新综艺 -->
    <div class="content-section">
      <div class="section-header">
        <h2 class="section-title">最新综艺</h2>
        <div class="section-actions">
          <button class="btn-refresh" @click="refreshLatestVariety">换一换</button>
          <button class="btn-more" @click="goToMore('variety')">更多 ›</button>
        </div>
      </div>
      <div class="movie-carousel">
        <div class="movie-scroll-container" ref="varietyScroll">
          <div
              v-for="item in latestVariety"
              :key="item.id"
              class="movie-card"
              @click="goToDetail(item.id)"
          >
            <div class="card-poster">
              <img :src="item.poster" :alt="item.title" loading="lazy" />
              <div class="card-badge blue">蓝光</div>
              <div class="card-tag new" v-if="isNew(item.releaseDate)">新</div>
              <div class="card-overlay">
                <button class="play-button" @click.stop="playMovie(item)">
                  <span class="play-icon">▶</span>
                </button>
              </div>
            </div>
            <div class="card-info">
              <div class="card-title">{{ item.title }}</div>
              <div class="card-meta">
                <span class="views">🔥 {{ formatViews(item.viewsCount) }}</span>
                <span class="rating" v-if="item.rating">{{ item.rating }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 5. 最新动漫 -->
    <div class="content-section">
      <div class="section-header">
        <h2 class="section-title">最新动漫</h2>
        <div class="section-actions">
          <button class="btn-refresh" @click="refreshLatestAnimation">换一换</button>
          <button class="btn-more" @click="goToMore('animation')">更多 ›</button>
        </div>
      </div>
      <div class="movie-carousel">
        <div class="movie-scroll-container" ref="animationScroll">
          <div
              v-for="item in latestAnimation"
              :key="item.id"
              class="movie-card"
              @click="goToDetail(item.id)"
          >
            <div class="card-poster">
              <img :src="item.poster" :alt="item.title" loading="lazy" />
              <div class="card-badge blue">蓝光</div>
              <div class="card-tag new" v-if="isNew(item.releaseDate)">新</div>
              <div class="card-overlay">
                <button class="play-button" @click.stop="playMovie(item)">
                  <span class="play-icon">▶</span>
                </button>
              </div>
            </div>
            <div class="card-info">
              <div class="card-title">{{ item.title }}</div>
              <div class="card-meta">
                <span class="views">🔥 {{ formatViews(item.viewsCount) }}</span>
                <span class="rating" v-if="item.rating">{{ item.rating }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HomePage',
  data() {
    return {
      // 后端 API 地址
      apiBaseUrl: 'http://192.168.0.80:9103',

      // 轮播图数据
      movies: [],
      announcement: {},

      // 🆕 热门内容数据
      trendingList: [],      // 正在热播
      latestMovies: [],      // 最新电影
      latestTv: [],          // 最新电视剧
      latestVariety: [],     // 最新综艺
      latestAnimation: [],   // 最新动漫

      // 轮播状态
      currentIndex: 0,
      autoPlayTimer: null,
      loading: true,
      announcementLoading: true
    };
  },
  mounted() {
    this.initPage();
  },
  beforeUnmount() {
    this.stopAutoPlay();
  },
  methods: {
    // ========== 初始化 ==========
    async initPage() {
      console.log('🚀 初始化首页...');

      // 并行加载所有数据
      await Promise.all([
        this.fetchCarouselMovies(),
        this.fetchAnnouncement(),
        this.fetchTrending(),
        this.fetchLatestMovies(),
        this.fetchLatestTv(),
        this.fetchLatestVariety(),
        this.fetchLatestAnimation()
      ]);

      this.loading = false;
      this.startAutoPlay();

      console.log('✅ 首页初始化完成');
    },

    // ========== Axios 请求方法 ==========
    async request(config) {
      try {
        const response = await axios({
          baseURL: this.apiBaseUrl,
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          },
          ...config
        });

        console.log('API 响应:', response.data);
        return response.data;
      } catch (error) {
        console.error('API 请求失败:', error);

        if (error.response) {
          const status = error.response.status;
          const errorMsg = this.getErrorMessage(status);
          console.error(errorMsg);
          alert(errorMsg);
        } else if (error.request) {
          console.error('网络连接失败，请检查网络');
          alert('网络连接失败，请检查网络');
        } else {
          console.error('请求配置错误:', error.message);
          alert('请求失败: ' + error.message);
        }

        throw error;
      }
    },

    getErrorMessage(status) {
      const errorMessages = {
        400: '请求参数错误',
        401: '未授权，请登录',
        403: '拒绝访问',
        404: '请求的资源不存在',
        500: '服务器内部错误',
        502: '网关错误',
        503: '服务不可用'
      };
      return errorMessages[status] || `请求失败 (状态码: ${status})`;
    },

    // ========== 数据获取方法 ==========

    // 获取轮播图电影
    async fetchCarouselMovies() {
      try {
        console.log('📡 获取轮播图电影...');
        const result = await this.request({
          method: 'GET',
          url: '/movie/carousel/latest',
          params: { limit: 5 }
        });

        if (result.code === 200 && result.data) {
          this.movies = result.data;
          console.log('✅ 成功加载轮播图电影:', this.movies.length, '条');
        }
      } catch (error) {
        console.error('❌ 获取轮播图电影失败:', error);
        this.movies = [];
      }
    },

    // 获取公告
    async fetchAnnouncement() {
      try {
        console.log('📡 获取公告...');
        this.announcementLoading = true;

        // TODO: 替换为实际的公告API
        // const result = await this.request({
        //   method: 'GET',
        //   url: '/announcement/latest'
        // });

        // 模拟公告数据
        this.announcement = {
          content: '欢迎来到影视平台！最新电影、电视剧、综艺、动漫持续更新中...',
          updateTime: new Date().toLocaleString()
        };

        this.announcementLoading = false;
        console.log('✅ 成功加载公告');
      } catch (error) {
        console.error('❌ 获取公告失败:', error);
        this.announcementLoading = false;
      }
    },

    // 🆕 获取正在热播
    async fetchTrending() {
      try {
        console.log('🔥 获取正在热播...');
        const result = await this.request({
          method: 'GET',
          url: '/movie/list/trending',
          params: { limit: 10 }
        });

        if (result.code === 200 && result.data) {
          this.trendingList = result.data;
          console.log('✅ 成功加载正在热播:', this.trendingList.length, '条');
        }
      } catch (error) {
        console.error('❌ 获取正在热播失败:', error);
        this.trendingList = [];
      }
    },

    // 🆕 获取最新电影
    async fetchLatestMovies() {
      try {
        console.log('🎬 获取最新电影...');
        const result = await this.request({
          method: 'GET',
          url: '/movie/list/latest-movies',
          params: { limit: 10 }
        });

        if (result.code === 200 && result.data) {
          this.latestMovies = result.data;
          console.log('✅ 成功加载最新电影:', this.latestMovies.length, '条');
        }
      } catch (error) {
        console.error('❌ 获取最新电影失败:', error);
        this.latestMovies = [];
      }
    },

    // 🆕 获取最新电视剧
    async fetchLatestTv() {
      try {
        console.log('📺 获取最新电视剧...');
        const result = await this.request({
          method: 'GET',
          url: '/movie/list/latest-tv',
          params: { limit: 10 }
        });

        if (result.code === 200 && result.data) {
          this.latestTv = result.data;
          console.log('✅ 成功加载最新电视剧:', this.latestTv.length, '条');
        }
      } catch (error) {
        console.error('❌ 获取最新电视剧失败:', error);
        this.latestTv = [];
      }
    },

    // 🆕 获取最新综艺
    async fetchLatestVariety() {
      try {
        console.log('🎤 获取最新综艺...');
        const result = await this.request({
          method: 'GET',
          url: '/movie/list/latest-variety',
          params: { limit: 10 }
        });

        if (result.code === 200 && result.data) {
          this.latestVariety = result.data;
          console.log('✅ 成功加载最新综艺:', this.latestVariety.length, '条');
        }
      } catch (error) {
        console.error('❌ 获取最新综艺失败:', error);
        this.latestVariety = [];
      }
    },

    // 🆕 获取最新动漫
    async fetchLatestAnimation() {
      try {
        console.log('🎨 获取最新动漫...');
        const result = await this.request({
          method: 'GET',
          url: '/movie/list/latest-animation',
          params: { limit: 10 }
        });

        if (result.code === 200 && result.data) {
          this.latestAnimation = result.data;
          console.log('✅ 成功加载最新动漫:', this.latestAnimation.length, '条');
        }
      } catch (error) {
        console.error('❌ 获取最新动漫失败:', error);
        this.latestAnimation = [];
      }
    },

    // ========== 刷新方法 ==========
    refreshTrending() {
      console.log('🔄 刷新正在热播');
      this.fetchTrending();
    },

    refreshLatestMovies() {
      console.log('🔄 刷新最新电影');
      this.fetchLatestMovies();
    },

    refreshLatestTv() {
      console.log('🔄 刷新最新电视剧');
      this.fetchLatestTv();
    },

    refreshLatestVariety() {
      console.log('🔄 刷新最新综艺');
      this.fetchLatestVariety();
    },

    refreshLatestAnimation() {
      console.log('🔄 刷新最新动漫');
      this.fetchLatestAnimation();
    },

    // ========== 导航方法 ==========

    // 跳转到详情页
    goToDetail(id) {
      console.log('🎬 跳转到详情页, ID:', id);
      this.$router.push(`/movie/detail/${id}`);
    },

    // 跳转到更多页面
    goToMore(type) {
      console.log('📋 跳转到更多页面, 类型:', type);
      const routes = {
        'trending': '/movie/filter',
        'movie': '/movie/filter?categoryId=1',
        'tv': '/tv/filter',
        'variety': '/variety/filter',
        'animation': '/animation/filter'
      };
      this.$router.push(routes[type] || '/movie/filter');
    },

    // 播放影片
    playMovie(movie) {
      console.log('▶️ 播放影片:', movie.title);
      // TODO: 跳转到播放页面
      this.$router.push(`/movie/play/${movie.id}`);
    },

    // 收藏影片
    toggleFavorite(movie) {
      console.log('❤️ 收藏/取消收藏:', movie.title);
      // TODO: 调用收藏API
      alert('收藏功能开发中...');
    },

    // ========== 轮播图控制 ==========
    startAutoPlay() {
      if (this.movies.length <= 1) return;

      this.autoPlayTimer = setInterval(() => {
        this.nextSlide();
      }, 5000);
    },

    stopAutoPlay() {
      if (this.autoPlayTimer) {
        clearInterval(this.autoPlayTimer);
        this.autoPlayTimer = null;
      }
    },

    nextSlide() {
      this.currentIndex = (this.currentIndex + 1) % this.movies.length;
    },

    prevSlide() {
      this.currentIndex = (this.currentIndex - 1 + this.movies.length) % this.movies.length;
    },

    goToSlide(index) {
      this.currentIndex = index;
      this.stopAutoPlay();
      this.startAutoPlay();
    },

    jumpToMovie(index) {
      this.goToSlide(index);
    },

    // ========== 工具方法 ==========

    // 格式化观看次数
    formatViews(count) {
      if (!count) return '0';
      if (count >= 10000) {
        return (count / 10000).toFixed(1) + '万';
      }
      return count.toString();
    },

    // 判断是否为新内容(30天内)
    isNew(releaseDate) {
      if (!releaseDate) return false;

      const release = new Date(releaseDate);
      const now = new Date();
      const daysDiff = (now - release) / (1000 * 60 * 60 * 24);

      return daysDiff <= 30;
    }
  }
};
</script>

<style scoped>
/* ========== 基础样式 ========== */
.home-container {
  min-height: 100vh;
  background: #0a0e1a;
  color: #fff;
}

/* ========== 轮播图样式 ========== */
.carousel-wrapper {
  position: relative;
  width: 100%;
  height: 600px;
  background: #000;
  overflow: hidden;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.carousel-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.carousel-item {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}

.carousel-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
      90deg,
      rgba(0, 0, 0, 0.9) 0%,
      rgba(0, 0, 0, 0.6) 50%,
      rgba(0, 0, 0, 0.3) 100%
  );
}

.movie-info {
  position: relative;
  z-index: 10;
  display: flex;
  height: 100%;
  padding: 60px 80px;
}

.movie-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 700px;
}

.movie-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
}

.movie-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 16px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5px;
}

.movie-details {
  margin-bottom: 20px;
  font-size: 14px;
  line-height: 1.8;
}

.label {
  color: #aaa;
}

.actors-text {
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.movie-description {
  font-size: 14px;
  line-height: 1.8;
  color: #ccc;
  margin-bottom: 30px;
  max-height: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.btn-play,
.btn-favorite {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 30px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-play {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.btn-play:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.btn-favorite {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  color: #fff;
  padding: 12px;
}

.btn-favorite:hover {
  background: rgba(255, 255, 255, 0.2);
}

.recommended-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-left: 40px;
  padding: 20px;
}

.recommended-item {
  padding: 15px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.1);
  min-width: 200px;
}

.recommended-item:hover,
.recommended-item.active {
  background: rgba(255, 255, 255, 0.15);
  transform: translateX(-5px);
}

.item-title {
  font-size: 14px;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-meta {
  font-size: 12px;
  color: #aaa;
}

.carousel-indicators {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 20;
}

.indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  cursor: pointer;
  transition: all 0.3s;
}

.indicator.active {
  width: 24px;
  border-radius: 4px;
  background: #fff;
}

.carousel-control {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 50px;
  height: 50px;
  border: none;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  color: #fff;
  font-size: 30px;
  cursor: pointer;
  border-radius: 50%;
  transition: all 0.3s;
  z-index: 20;
}

.carousel-control:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-50%) scale(1.1);
}

.carousel-control.prev {
  left: 30px;
}

.carousel-control.next {
  right: 30px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* ========== 公告样式 ========== */
.announcement-wrapper {
  padding: 40px 80px;
  background: linear-gradient(180deg, #0a0e1a 0%, #121824 100%);
}

.announcement-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.announcement-title {
  font-size: 24px;
  font-weight: bold;
  color: #fff;
}

.announcement-update-time {
  font-size: 14px;
  color: #888;
}

.announcement-content {
  min-height: 60px;
}

.announcement-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  padding: 20px;
}

.announcement-loading .loading-spinner {
  width: 20px;
  height: 20px;
}

.announcement-text {
  font-size: 16px;
  line-height: 1.8;
  color: #ccc;
}

.announcement-empty {
  text-align: center;
  padding: 20px;
  color: #666;
}

/* ========== 🆕 热门内容区块样式 ========== */
.content-section {
  padding: 40px 80px;
  background: #0a0e1a;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.section-title {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
}

.section-actions {
  display: flex;
  gap: 15px;
}

.btn-refresh,
.btn-more {
  padding: 8px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-refresh {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.btn-refresh:hover {
  background: rgba(255, 255, 255, 0.1);
}

.btn-more {
  background: transparent;
  color: #888;
}

.btn-more:hover {
  color: #fff;
}

/* 电影横向滚动容器 */
.movie-carousel {
  max-width: 1400px;
  margin: 0 auto;
  overflow: hidden;
}

.movie-scroll-container {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding-bottom: 20px;
  scroll-behavior: smooth;
}

.movie-scroll-container::-webkit-scrollbar {
  height: 6px;
}

.movie-scroll-container::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
}

.movie-scroll-container::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.movie-scroll-container::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 电影卡片 */
.movie-card {
  flex-shrink: 0;
  width: 200px;
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
  transition: all 0.3s;
}

.movie-card:hover .card-poster img {
  transform: scale(1.05);
}

/* 卡片徽章 */
.card-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  z-index: 2;
}

.card-badge.blue {
  background: #00a1d6;
  color: #fff;
}

/* 新标签 */
.card-tag {
  position: absolute;
  top: 8px;
  right: 8px;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  z-index: 2;
}

.card-tag.new {
  background: #ff6b6b;
  color: #fff;
}

/* 悬停遮罩 */
.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s;
}

.movie-card:hover .card-overlay {
  opacity: 1;
}

/* 播放按钮 */
.play-button {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.play-button:hover {
  background: #fff;
  transform: scale(1.1);
}

.play-icon {
  font-size: 18px;
  color: #333;
  margin-left: 3px;
}

/* 卡片信息 */
.card-info {
  padding: 0 4px;
}

.card-title {
  font-size: 15px;
  font-weight: 500;
  color: #fff;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.card-meta .views {
  color: #ff6b6b;
}

.card-meta .rating {
  color: #ffd700;
  font-weight: 500;
}

/* ========== 响应式设计 ========== */
@media (max-width: 1200px) {
  .movie-info {
    padding: 40px;
  }

  .movie-content {
    max-width: 600px;
  }

  .movie-title {
    font-size: 36px;
  }

  .content-section {
    padding: 30px 40px;
  }
}

@media (max-width: 768px) {
  .carousel-wrapper {
    height: 400px;
  }

  .movie-info {
    padding: 20px;
  }

  .recommended-list {
    display: none;
  }

  .movie-title {
    font-size: 28px;
  }

  .movie-description {
    max-height: 60px;
    -webkit-line-clamp: 3;
  }

  .carousel-control {
    width: 40px;
    height: 40px;
    font-size: 24px;
  }

  .carousel-control.prev {
    left: 10px;
  }

  .carousel-control.next {
    right: 10px;
  }

  .announcement-wrapper {
    padding: 20px;
  }

  .announcement-container {
    padding: 20px;
  }

  .content-section {
    padding: 20px;
  }

  .section-title {
    font-size: 22px;
  }

  .movie-card {
    width: 150px;
  }
}
</style>