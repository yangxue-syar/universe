package com.universe.dto;

import lombok.Data;

import java.util.List;

@Data
/**
 * 类别树形结构数据传输对象
 *
 * 用于表示具有层级关系的类别信息，支持无限级分类，
 * 每个节点包含自身的标识、名称以及子节点列表
 */
public class CategoryTreeDTO {
    /** 类别唯一标识符 */
    private Long id;

    /** 类别名称 */
    private String name;
    private Integer sort;

    /** 子类别列表，用于构建树形结构 */
    private List<CategoryTreeDTO> children;
}

