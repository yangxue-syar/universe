<template>
  <div class="filter-page">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <!-- 类型选择 -->
      <div class="filter-row">
        <div class="filter-label">类型</div>
        <div class="filter-options">
          <div
              class="filter-option"
              :class="{ active: selectedCategory === null }"
              @click="selectCategory(null)"
          >
            全部
          </div>
          <div
              v-for="category in varietyCategories"
              :key="category.id"
              class="filter-option"
              :class="{ active: selectedCategory === category.id }"
              @click="selectCategory(category.id)"
          >
            {{ category.name }}
          </div>
        </div>
      </div>

      <!-- 地区选择 -->
      <div class="filter-row">
        <div class="filter-label">地区</div>
        <div class="filter-options">
          <div
              class="filter-option"
              :class="{ active: selectedRegion === null }"
              @click="selectRegion(null)"
          >
            全部
          </div>
          <div
              v-for="region in regions"
              :key="region"
              class="filter-option"
              :class="{ active: selectedRegion === region }"
              @click="selectRegion(region)"
          >
            {{ region }}
          </div>
        </div>
      </div>

      <!-- 年份选择 -->
      <div class="filter-row">
        <div class="filter-label">年份</div>
        <div class="filter-options">
          <div
              class="filter-option"
              :class="{ active: selectedYear === null }"
              @click="selectYear(null)"
          >
            全部
          </div>
          <div
              v-for="year in years"
              :key="year"
              class="filter-option"
              :class="{ active: selectedYear === year }"
              @click="selectYear(year)"
          >
            {{ year }}
          </div>
        </div>
      </div>
    </div>

    <!-- 排序栏 -->
    <div class="sort-bar">
      <div
          class="sort-option"
          :class="{ active: sortBy === 'time' }"
          @click="sortBy = 'time'"
      >
        上映时间↓
      </div>
      <div
          class="sort-option"
          :class="{ active: sortBy === 'views' }"
          @click="sortBy = 'views'"
      >
        人气高低↓
      </div>
      <div
          class="sort-option"
          :class="{ active: sortBy === 'rating' }"
          @click="sortBy = 'rating'"
      >
        评分高低↓
      </div>
    </div>

    <!-- 综艺列表 -->
    <div class="movie-list">
      <div
          v-for="variety in filteredVarietyShows"
          :key="variety.id"
          class="movie-item"
          @click="goToDetail(variety.id)"
      >
        <div class="movie-poster">
          <img :src="variety.poster" :alt="variety.title" />
          <div class="movie-badge">综艺</div>
        </div>
        <div class="movie-info">
          <div class="movie-title">{{ variety.title }}</div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="totalPages > 1">
      <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getCategoryTree, getMoviePage } from '@/api/movie';

const route = useRoute();
const router = useRouter();

// 综艺类型ID
const VARIETY_TYPE_ID = 3;

// 数据定义
const varietyCategories = ref([]);
const selectedCategory = ref(null);
const selectedRegion = ref(null);
const selectedYear = ref(null);
const sortBy = ref('time');

const filteredVarietyShows = ref([]);
const currentPage = ref(1);
const pageSize = ref(20);
const total = ref(0);
const totalPages = ref(0);

// 地区、年份选项
const regions = ref(['国产综艺', '日本综艺', '韩国综艺', '欧美综艺']);
const years = ref([]);

// 生成年份选项
const generateYears = () => {
  const currentYear = new Date().getFullYear();
  const yearList = [];
  for (let year = currentYear; year >= 2000; year--) {
    yearList.push(year.toString());
  }
  years.value = yearList;
};

// 获取综艺分类
const getVarietyCategoriesData = async () => {
  try {
    const res = await getCategoryTree();
    const varietyType = (res.data || []).find(cat => cat.id === VARIETY_TYPE_ID);

    if (varietyType && varietyType.children) {
      varietyCategories.value = varietyType.children.sort((a, b) => (a.sort || 0) - (b.sort || 0));
    }

    // 从URL参数获取categoryId
    const categoryId = route.query.categoryId;
    if (categoryId) {
      selectedCategory.value = Number(categoryId);
    }
  } catch (error) {
    console.error('获取综艺分类失败:', error);
  }
};

// 获取综艺列表
const getVarietyShowsData = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      sortBy: sortBy.value
    };

    if (selectedCategory.value) {
      params.categoryId = selectedCategory.value;
    } else {
      params.categoryId = VARIETY_TYPE_ID;
    }

    if (selectedYear.value) {
      params.year = selectedYear.value;
    }
    if (selectedRegion.value) {
      params.region = selectedRegion.value;
    }

    const res = await getMoviePage(params);
    filteredVarietyShows.value = res.data?.records || [];
    total.value = res.data?.total || 0;
    totalPages.value = res.data?.pages || 0;
  } catch (error) {
    console.error('获取综艺列表失败:', error);
  }
};

// 选择分类
const selectCategory = (categoryId) => {
  selectedCategory.value = categoryId;
  currentPage.value = 1;
  getVarietyShowsData();
};

// 选择地区
const selectRegion = (region) => {
  selectedRegion.value = region;
  currentPage.value = 1;
  getVarietyShowsData();
};

// 选择年份
const selectYear = (year) => {
  selectedYear.value = year;
  currentPage.value = 1;
  getVarietyShowsData();
};

// 分页变化
const handlePageChange = (page) => {
  currentPage.value = page;
  getVarietyShowsData();
  window.scrollTo(0, 0);
};

// 跳转到详情
const goToDetail = (id) => {
  router.push(`/variety/detail/${id}`);
};

// 监听排序变化
watch(sortBy, () => {
  currentPage.value = 1;
  getVarietyShowsData();
});

// 页面加载
onMounted(async () => {
  generateYears();
  await getVarietyCategoriesData();
  await getVarietyShowsData();
});
</script>

<style scoped>
.filter-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px;
}

.filter-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  align-items: flex-start;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.filter-row:last-child {
  border-bottom: none;
}

.filter-label {
  width: 80px;
  font-size: 15px;
  font-weight: 500;
  color: #333;
  flex-shrink: 0;
  padding-top: 8px;
}

.filter-options {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-option {
  padding: 8px 20px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.filter-option:hover {
  background: #e8e8e8;
  color: #333;
}

.filter-option.active {
  background: #ff5a5f;
  color: #fff;
}

.sort-bar {
  background: #fff;
  padding: 15px 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  display: flex;
  gap: 20px;
}

.sort-option {
  padding: 8px 16px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 4px;
}

.sort-option:hover {
  background: #f5f5f5;
}

.sort-option.active {
  background: #ff5a5f;
  color: #fff;
}

.movie-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.movie-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.movie-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.movie-poster {
  position: relative;
  aspect-ratio: 2/3;
  overflow: hidden;
  background: #f0f0f0;
}

.movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  padding: 4px 8px;
  background: #ffa500;
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
}

.movie-info {
  padding: 12px;
}

.movie-title {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 20px;
}

@media (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    gap: 12px;
  }

  .filter-label {
    width: 100%;
    padding-top: 0;
  }

  .movie-list {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 12px;
  }
}
</style>