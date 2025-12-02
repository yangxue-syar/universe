<script setup>
import {ref, computed, onMounted} from "vue";
import {useRouter} from "vue-router";
import {message} from "ant-design-vue";

const router = useRouter();

const props = defineProps({
  movie: {
    type: Object,
    required: true,
    default: () => ({})
  }
});

const isFavorite = ref(false);
const imageLoaded = ref(false);
const imageError = ref(false);

// 计算属性：优化性能，避免重复计算
const movieImage = computed(() => {
  return props.movie.cover || props.movie.poster || props.movie.imageUrl || '';
});

const truncatedActors = computed(() => {
  return truncateText(props.movie.actors, 25);
});

const scoreClass = computed(() => {
  const score = props.movie.rating || props.movie.score || 0;
  return getScoreClass(score);
});

const formattedViews = computed(() => {
  if (!props.movie.viewsCount && !props.movie.hot) return '0';
  if (props.movie.viewsCount) {
    return (props.movie.viewsCount / 10000).toFixed(1) + '万';
  }
  return props.movie.hot || '0';
});

// 工具函数
const truncateText = (text, len = 20) => {
  return text?.length > len ? text.slice(0, len) + "..." : text;
};

const getScoreClass = (score) => {
  if (score >= 8) return "high";
  if (score >= 6) return "mid";
  return "low";
};

const playMovie = (m) => message.success("播放：" + m.title);

const addToFavorites = (m) => {
  isFavorite.value = !isFavorite.value;
  if (isFavorite.value) {
    message.success("收藏成功：" + m.title);
  } else {
    message.success("取消收藏：" + m.title);
  }
};

const viewDetails = (m) => {
  if (m.id) {
    router.push(`/movie/detail/${m.id}`);
  } else {
    message.info("前往详情：" + m.title);
  }
};

// 图片加载处理
const handleImageLoad = () => {
  imageLoaded.value = true;
  imageError.value = false;
};

const handleImageError = () => {
  imageError.value = true;
  imageLoaded.value = false;
};
</script>
<template>
  <a-card hoverable class="movie-card" @click="viewDetails(props.movie)">
    <!-- 海报图片区域 -->
    <template #cover>
      <div class="card-image-container">
        <!-- 图片加载占位 -->
        <div v-if="!imageLoaded" class="image-placeholder">
          <div class="loading-spinner"></div>
        </div>
        <!-- 图片错误占位 -->
        <div v-else-if="imageError" class="image-error">
          <span class="error-icon">📷</span>
          <span class="error-text">图片加载失败</span>
        </div>
        <!-- 实际图片 -->
        <img
            v-else
            :src="movieImage"
            alt="电影海报"
            class="movie-image"
            loading="lazy"
            @load="handleImageLoad"
            @error="handleImageError"
        />
        <!-- 图片遮罩层 -->
        <div class="image-overlay">
          <!-- 左上角蓝光标签 -->
          <div v-if="props.movie.tags && props.movie.tags.length" class="quality-tag top-left">
            <span class="tag">{{ props.movie.tags[0] }}</span>
          </div>
          <!-- 右上角新标签 -->
          <div v-if="props.movie.isNew" class="new-tag top-right">
            <span>新</span>
          </div>

          <!-- 底部信息：集数、热度、评分（正常状态和悬停状态都显示） -->
          <div class="image-bottom-info">
            <!-- 集数信息 -->
            <div class="episode-info" v-if="props.movie.episodeInfo">
              {{ props.movie.episodeInfo }}
            </div>
            <!-- 热度和评分 -->
            <div class="stats-row">
              <div class="views-count">
                <span class="views-icon">↑</span>
                <span class="views-text">{{ formattedViews }}</span>
              </div>
              <div class="score-badge" :class="scoreClass">
                <span>{{ props.movie.rating || props.movie.score || '0' }}</span>
              </div>
            </div>
          </div>

          <!-- 悬停时显示的详细信息 -->
          <div class="hover-info-full">
            <!-- 类型信息 -->
            <div class="genres-info" v-if="props.movie.genres && props.movie.genres.length">
              {{ props.movie.genres.join(' ') }}
            </div>
            <!-- 上映时间 -->
            <div class="release-info" v-if="props.movie.releaseDate">
              <span class="label">上映时间：</span>
              <span class="value">{{ props.movie.releaseDate }}</span>
            </div>
            <!-- 完整主演信息 -->
            <div class="actors-full">
              <span class="label">主演：</span>
              <span class="value">{{ props.movie.actors || '暂无' }}</span>
            </div>
            <!-- 简介 -->
            <div class="summary-info" v-if="props.movie.summary || props.movie.description">
              <span class="label">简介：</span>
              <span class="value">{{ props.movie.summary || props.movie.description }}</span>
            </div>
            <!-- 底部栏：热度和收藏按钮 -->
            <div class="hover-bottom-bar">
              <div class="hover-views-count">
                <span class="fire-icon">🔥</span>
                <span class="views-text">{{ props.movie.viewsCount ? (props.movie.viewsCount / 10000).toFixed(1) + '万' : props.movie.hot || '0' }}</span>
              </div>
              <button
                class="favorite-add-btn"
                :class="{ 'favorited': isFavorite }"
                @click.stop="addToFavorites(props.movie)"
              >
                <span v-if="!isFavorite">+ 收藏</span>
                <span v-else>已收藏</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 卡片内容：剧名和主演 -->
    <a-card-meta>
      <template #title>
        <div class="movie-title">{{ props.movie.title }}</div>
      </template>
      <template #description>
        <div class="actors-info" :title="props.movie.actors">
          <span class="label">主演：</span>
          <span class="value">{{ truncatedActors }}</span>
        </div>
      </template>
    </a-card-meta>
  </a-card>
</template>
<style scoped>
/* 卡片基础样式（竖版布局） */
.movie-card {
  border: none;
  border-radius: 4px;
  overflow: visible;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  background: white;
  will-change: transform;
  cursor: pointer;
}

.movie-card:hover {
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

::v-deep(.ant-card-body) {
  padding: 12px 0;
  flex: 1;
  display: flex;
  flex-direction: column;
}

::v-deep(.ant-card) {
  border: none;
  box-shadow: none;
  background: transparent;
}

.movie-card:hover {
  transform: scale(1.05);
  z-index: 100;
}


/* 海报图片容器 */
.card-image-container {
  position: relative;
  width: 100%;
  height: 280px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 0;
  background: #1e293b;
  border-radius: 4px;
}

.movie-card:hover .card-image-container {
  height: 360px;
  margin-bottom: -80px;
  z-index: 10;
}

.movie-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0;
  animation: fadeIn 0.3s ease forwards;
}

/* 图片加载占位 */
.image-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  border-radius: 4px;
}

/* 加载动画 */
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top: 3px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* 图片错误占位 */
.image-error {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: #94a3b8;
  border-radius: 4px;
}

.error-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.error-text {
  font-size: 14px;
}

/* 图片加载占位 */
.image-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
}

/* 加载动画 */
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top: 3px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* 图片错误占位 */
.image-error {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: #94a3b8;
}

.error-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.error-text {
  font-size: 14px;
}

/* 动画 */
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 滚动条样式优化 */
.hover-info-full::-webkit-scrollbar {
  width: 6px;
}

.hover-info-full::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
}

.hover-info-full::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.hover-info-full::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
}

.movie-card:hover .movie-image {
  transform: scale(1.05);
}

/* 图片遮罩层 */
.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7) 0%, transparent 40%);
  padding: 12px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  transition: background 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 4px;
}

.movie-card:hover .image-overlay {
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9) 0%, rgba(0, 0, 0, 0.85) 20%, rgba(0, 0, 0, 0.8) 50%, rgba(0, 0, 0, 0.75) 80%, rgba(0, 0, 0, 0.7) 100%);
}

/* 左上角蓝光标签 */
.quality-tag.top-left {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(59, 130, 246, 0.95);
  color: #fff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
  z-index: 2;
}

/* 右上角新标签 */
.new-tag.top-right {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(239, 68, 68, 0.95);
  color: #fff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
  z-index: 2;
}

/* 底部信息区域（正常状态显示） */
.image-bottom-info {
  width: 100%;
  color: #fff;
  margin-top: auto;
}

/* 集数信息 */
.episode-info {
  font-size: 12px;
  color: #fff;
  margin-bottom: 6px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

/* 统计信息行（观看数和评分） */
.stats-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.views-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.views-icon {
  font-size: 12px;
  color: #fff;
}

.views-text {
  color: #fff;
}

/* 悬停时显示的完整信息 */
.hover-info-full {
  width: 100%;
  color: #fff;
  opacity: 0;
  max-height: 0;
  overflow: hidden;
  transition: opacity 0.3s ease, max-height 0.3s ease, margin-top 0.3s ease;
  margin-top: 0;
  padding: 0 12px;
}

.movie-card:hover .hover-info-full {
  opacity: 1;
  max-height: 240px;
  overflow-y: auto;
  padding-bottom: 48px; /* 为底栏留出空间 */
}

/* 底部栏样式 */
.hover-info-full {
  position: relative;
  z-index: 20;               /* 关键：提高层级 */
  pointer-events: auto;
}

.hover-bottom-bar {
  position: sticky;          /* 使底栏永远贴底可点 */
  bottom: 0;
  z-index: 30;               /* 层级比遮罩更高 */
  backdrop-filter: blur(6px);
  padding: 10px 12px;
  display: flex;
  justify-content: space-between;
  border-top: 1px solid rgba(255,255,255,0.18);
  pointer-events: auto;      /* 关键：允许点击 */
}

/* 遮罩层设置为可穿透点击 */
.image-overlay {
  pointer-events: none;      /* 层改成不阻挡事件 */
}

.hover-info-full * {
  pointer-events: auto;      /* hover 内部全部可互动 */
}

.hover-views-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.fire-icon {
  font-size: 14px;
}

/* 类型信息 */
.genres-info {
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  margin-bottom: 8px;
}

/* 上映时间 */
.release-info {
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  margin-bottom: 8px;
}

.release-info .label {
  color: rgba(255, 255, 255, 0.9);
}

.release-info .value {
  color: #fff;
}

/* 完整主演信息 */
.actors-full {
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  margin-bottom: 8px;
  line-height: 1.5;
}

.actors-full .label {
  color: rgba(255, 255, 255, 0.9);
}

.actors-full .value {
  color: #fff;
}

/* 简介信息 */
.summary-info {
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  margin-bottom: 12px;
  line-height: 1.5;
}

.summary-info .label {
  color: rgba(255, 255, 255, 0.9);
}

.summary-info .value {
  color: #fff;
}

/* 收藏按钮 */
.favorite-add-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 6px 16px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 4px;
  color: #fff;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(4px);
}

.favorite-add-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
}

.favorite-add-btn.favorited {
  background: rgba(239, 68, 68, 0.9);
  border-color: rgba(239, 68, 68, 1);
}

/* 评分徽章 */
.score-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 3px 10px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 600;
  color: #fff;
  background: rgba(255, 193, 7, 0.95);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.score-badge.high {
  background: rgba(72, 187, 120, 0.95);
}

.score-badge.mid {
  background: rgba(255, 193, 7, 0.95);
}

.score-badge.low {
  background: rgba(245, 158, 11, 0.95);
}

/* 卡片内容区域 */
::v-deep(.ant-card-meta) {
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1), transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.movie-card:hover ::v-deep(.ant-card-meta) {
  opacity: 0;
  transform: translateY(-10px);
  pointer-events: none;
}

/* 电影标题 */
.movie-title {
  font-size: 15px;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.4;
}

::v-deep(.ant-card-meta-title) {
  margin-bottom: 0;
}

/* 主演信息 */
.actors-info {
  font-size: 12px;
  color: #4a5568;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.actors-info .label {
  color: #718096;
  margin-right: 4px;
}

.actors-info .value {
  color: #2d3748;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .card-image-container {
    height: 240px;
  }

  .movie-title {
    font-size: 14px;
  }

  .actors-info {
    font-size: 11px;
  }

  .hover-info-full {
    font-size: 11px;
  }
}
</style>