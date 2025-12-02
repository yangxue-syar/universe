import request from '@/utils/request';

/**
 * 电影相关 API
 */

// 获取最新轮播图电影
export const getCarouselLatest = (limit = 8) => {
    return request({
        url: '/movie/carousel/latest',
        method: 'get',
        params: { limit }
    });
};

// 获取热门轮播图电影
export const getCarouselHot = (limit = 8) => {
    return request({
        url: '/movie/carousel/hot',
        method: 'get',
        params: { limit }
    });
};

// 获取推荐电影列表
export const getRecommendMovies = (limit = 10) => {
    return request({
        url: '/movie/recommend',
        method: 'get',
        params: { limit }
    });
};

// 分页查询电影（支持多条件筛选）
export const getMoviePage = (params) => {
    return request({
        url: '/movie/page',
        method: 'get',
        params
    });
};

// 获取电影详情
export const getMovieDetail = (id) => {
    return request({
        url: `/movie/detail/${id}`,
        method: 'get'
    });
};

/**
 * 分类相关 API
 */

// 获取分类树
export const getCategoryTree = () => {
    return request({
        url: '/category/tree',
        method: 'get'
    });
};

/**
 * 关系相关 API
 */

// 绑定电影和分类
export const bindMovieCategory = (movieId, categoryIds) => {
    return request({
        url: '/relation/bind',
        method: 'post',
        params: { movieId },
        data: categoryIds
    });
};

// 解绑电影和分类
export const unbindMovieCategory = (movieId, categoryId) => {
    return request({
        url: '/relation/unbind',
        method: 'delete',
        params: { movieId, categoryId }
    });
};

// 获取电影的所有分类
export const getMovieCategories = (movieId) => {
    return request({
        url: `/relation/movie/${movieId}`,
        method: 'get'
    });
};