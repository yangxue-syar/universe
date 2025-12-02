import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {
          path: '/', // 根路径
          name: 'home',
          component: () => import('../views/home.vue')
      },
      {
          path: '/animation', // 电影详情页
          name: 'animation',
          component: () => import('../views/videos/animation.vue')
      },{
          path: '/movie', // 电影列表页
          name: "movie",
          component: () => import('../views/videos/movie.vue')
      },{
          path: '/tv', // 音乐详情页
          name: "tv",
          component: () => import('../views/videos/tv.vue')
      },{
      path: '/movie/filter',
          name: 'movie-filter',
          component: () => import('../views/videos/MovieFilter.vue')
      },{
          path: '/tv/filter',
          name: 'TvFilter',
          component: () => import('../views/videos/TvFilter.vue')
      },{
          path: '/variety',
          name: 'Variety',
          component: () => import('../views/videos/variety.vue')
      },
      {
          path: '/variety/filter',
          name: 'VarietyFilter',  // ✅ 综艺筛选
          component: () => import('../views/videos/VarietyFilter.vue')
      },{
        path: '/animation/filter',
          name: 'AnimationFilter',
          component: () => import('../views/videos/AnimatonFilter.vue')
      },{
          path: '/movie/detail/:id',
          name: 'MovieDetail',
          component: () => import('../views/videos/MovieDetial.vue')
      },
      {
            path: '/login',
             name: 'login',
            component: () => import('../views/login.vue')
      },{
            path: '/register',
            name: 'register',
            component: () => import('../views/register.vue')
      },{
             path: '/profile/:id?',
            name: 'profile',
            component: () => import('../views/profile.vue')
      },
      {
          path: '/favorites',
          name: 'Favorites',
          component: () => import('../views/favorite.vue'),
          meta: { requiresAuth: true }
      }

  ],
})

export default router
