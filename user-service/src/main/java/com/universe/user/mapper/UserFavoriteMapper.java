package com.universe.user.mapper;

import com.universe.user.entity.UserFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
* @author mac
* @description 针对表【user_favorite(用户收藏表)】的数据库操作Mapper
* @createDate 2025-12-08 15:30:38
* @Entity com.universe.user.entity.UserFavorite
*/
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {

}




