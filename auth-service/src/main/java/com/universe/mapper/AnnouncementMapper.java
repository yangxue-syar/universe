package com.universe.mapper;

import com.universe.entity.Announcement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mac
* @description 针对表【announcement(公告表)】的数据库操作Mapper
* @createDate 2025-12-11 09:11:17
* @Entity com.universe.entity.Announcement
*/
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

}




