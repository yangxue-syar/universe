/**
 * 扁平化树形分类数据
 * @param {Array} tree - 树形分类数据
 * @returns {Array} - 扁平化后的分类数组
 */
export function flattenCategoryTree(tree) {
    const result = [];

    function flatten(nodes) {
        if (!nodes || !Array.isArray(nodes)) return;

        nodes.forEach(node => {
            result.push({
                id: node.id,
                name: node.name,
                parentId: node.parentId || null
            });

            if (node.children && node.children.length > 0) {
                flatten(node.children);
            }
        });
    }

    flatten(tree);
    return result;
}

/**
 * 根据分类名称过滤分类
 * @param {Array} categories - 分类数组
 * @param {Array} filterNames - 需要的分类名称数组
 * @returns {Array} - 过滤后的分类数组
 */
export function filterCategoriesByNames(categories, filterNames) {
    if (!categories || !Array.isArray(categories)) return [];
    if (!filterNames || !Array.isArray(filterNames)) return categories;

    return categories.filter(cat => filterNames.includes(cat.name));
}

/**
 * 电影页面的分类过滤器
 * 包含：喜剧、动作、科幻、爱情、悬疑、奇幻、剧情、恐怖、犯罪、动画等
 */
export const MOVIE_CATEGORIES = [
    '喜剧', '动作', '科幻', '爱情', '悬疑', '奇幻',
    '剧情', '恐怖', '犯罪', '动画', '惊悚', '战争',
    '冒险', '灾难', '传记', '其他'
];

/**
 * 电视剧页面的分类过滤器
 */
export const TV_CATEGORIES = [
    '都市', '古装', '家庭', '悬疑', '谍战', '军旅',
    '青春', '偶像', '言情', '武侠', '历史', '农村',
    '科幻', '喜剧', '其他'
];

/**
 * 动漫页面的分类过滤器
 */
export const ANIMATION_CATEGORIES = [
    '热血', '搞笑', '恋爱', '冒险', '校园', '奇幻',
    '科幻', '运动', '机战', '治愈', '悬疑', '推理',
    '美食', '日常', '萌系', '其他'
];

/**
 * 综艺页面的分类过滤器
 */
export const VARIETY_CATEGORIES = [
    '真人秀', '脱口秀', '访谈', '音乐', '舞蹈', '选秀',
    '游戏', '美食', '旅游', '情感', '竞技', '文化',
    '喜剧', '益智', '养成', '其他'
];

/**
 * 获取页面对应的分类
 * @param {String} pageType - 页面类型: movie, tv, animation, variety
 * @param {Array} allCategories - 所有分类数据
 * @returns {Array} - 过滤后的分类数组
 */
export function getCategoriesForPage(pageType, allCategories) {
    const categoryMap = {
        'movie': MOVIE_CATEGORIES,
        'tv': TV_CATEGORIES,
        'animation': ANIMATION_CATEGORIES,
        'variety': VARIETY_CATEGORIES
    };

    const filterNames = categoryMap[pageType];
    if (!filterNames) return allCategories;

    // 扁平化树形结构
    const flatCategories = flattenCategoryTree(allCategories);

    // 根据页面类型过滤
    return filterCategoriesByNames(flatCategories, filterNames);
}

/**
 * 获取地区分类
 */
export const REGION_CATEGORIES = [
    '中国大陆', '中国香港', '中国台湾', '美国', '日本',
    '韩国', '英国', '法国', '印度', '泰国'
];

/**
 * 从所有分类中提取地区分类
 */
export function getRegionCategories(allCategories) {
    const flatCategories = flattenCategoryTree(allCategories);
    return filterCategoriesByNames(flatCategories, REGION_CATEGORIES);
}