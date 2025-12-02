<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import TopContainer from "@/components/Top-container.vue";
// import Container from "@/components/Container.vue";


/**
 * 控制头部背景透明度和滚动状态的逻辑
 *
 * headerOpacity: 控制头部背景的透明度值（0-1），初始为完全透明
 * isScrolled: 标记页面是否已滚动超过阈值，用于切换样式类名
 */

/** 头部背景透明与否 **/
const headerOpacity = ref(0);            // 初始透明
const isScrolled = ref(false);           // 用于控制是否加白色背景与阴影

/**
 * 处理页面滚动事件
 *
 * 计算当前滚动位置，并据此更新头部透明度和滚动状态
 * 当滚动距离超过100px时，开始渐变显示背景并应用阴影效果
 */
const handleScroll = () => {
  const scrollY = window.scrollY || document.documentElement.scrollTop;

  // 滚动超过 100px 加背景
  const fadeStart = 100;
  headerOpacity.value = Math.min(scrollY / fadeStart, 1);

  isScrolled.value = scrollY > fadeStart;
}

/**
 * 组件挂载完成后添加滚动监听器
 *
 * 在组件加载完成之后绑定window的scroll事件处理器
 */
onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

/**
 * 组件卸载前移除滚动监听器
 *
 * 防止内存泄漏，在组件销毁之前清理绑定的scroll事件
 */
onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<template>
  <!-- 页面整体布局容器 -->
  <div class="common-layout">
    <!-- 固定定位的头部区域 -->
    <div
        class="fixed-header"
        :class="{ 'scrolled': isScrolled }"
        :style="{ backgroundColor: `rgba(255,255,255,${headerOpacity})` }"
    >
      <!-- 顶部内容组件，根据滚动状态传递dark属性以调整文字颜色 -->
      <top-container />
    </div>
    <!-- Element Plus 布局容器 -->
    <el-container>
      <!-- 占位用的头部元素，确保主体内容不会被固定头部遮挡 -->
      <el-header class="header-placeholder"></el-header>
      <container/>
      <!-- 主体内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
      <!-- 底部区域 -->
      <el-footer class="footer">
        <div>Footer</div>
        <div class="footer-content"></div>
      </el-footer>
    </el-container>

  </div>
</template>

<style scoped>
:root {
  --primary-color: #003974;
  --primary-color-light: #0059b2;
  --text-dark: #222222;
  --text-light: #ffffff;
  --bg-light: #ffffff;
  --bg-gray: #f9f9f9;
}

.common-layout {
  min-height: 100vh;
  background-color: var(--bg-light);
  color: var(--text-dark);
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 69px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  z-index: 1000;
  pointer-events: auto;
  transition: all 0.25s ease;
  background-color: rgba(255,255,255,0);
  backdrop-filter: blur(8px);
  color: var(--text-light);
}

.fixed-header.scrolled {
  background-color: var(--bg-light) !important;
  color: var(--primary-color) !important;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.header-placeholder {
  height: 69px;
  visibility: hidden;
}

/* 主体内容区域 */
.main-content {
  min-height: calc(100vh - 69px - 60px); /* 视口高度 - 头部 - 底部 */
  padding: 20px;
}

.footer {
  background: #f1f1f1;
  height: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #555;
}

.footer-content {
  margin-top: 5px;
  font-size: 14px;
}
</style>
