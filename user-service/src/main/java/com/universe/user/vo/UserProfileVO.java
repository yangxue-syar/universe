package com.universe.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserProfileVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer gender;                  // 性别(0未知,1男,2女)
    private Integer status;
    private Integer isAdmin;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 统计数据
    private Integer favoriteCount;           // 收藏数量
    private Integer commentCount;            // 评论数量
    private Integer watchHistoryCount;       // 观看历史数量
}