package com.universe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.universe.entity.Announcement;
import com.universe.service.AnnouncementService;
import com.universe.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;

/**
* @author mac
* @description 针对表【announcement(公告表)】的数据库操作Service实现
* @createDate 2025-12-11 09:11:17
*/
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService{

}




