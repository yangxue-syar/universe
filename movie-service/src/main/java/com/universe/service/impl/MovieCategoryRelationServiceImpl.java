package com.universe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.universe.entity.MovieCategoryRelation;
import com.universe.service.MovieCategoryRelationService;
import com.universe.mapper.MovieCategoryRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author mac
* @description 针对表【movie_category_relation(电影分类关联表)】的数据库操作Service实现
* @createDate 2025-11-30 14:41:24
*/
@Service
public class MovieCategoryRelationServiceImpl extends ServiceImpl<MovieCategoryRelationMapper, MovieCategoryRelation>
    implements MovieCategoryRelationService {
    @Autowired
    private MovieCategoryRelationMapper movieCategoryRelationMapper;
    @Override
    @Transactional
    public void bind(Long movieId, List<Long> categoryIds) {
        this.remove(new QueryWrapper<MovieCategoryRelation>().eq("movie_id", movieId));
        List<MovieCategoryRelation> list = categoryIds.stream().map(cid -> {
            MovieCategoryRelation r = new MovieCategoryRelation();
            r.setMovieId(movieId);
            r.setCategoryId(cid);
            return r;
        }).collect(Collectors.toList());
        this.saveBatch(list);
    }

    @Override
    public void unbind(Long movieId, Long categoryId) {
        movieCategoryRelationMapper.deleteByMovieIdAndCategoryId(movieId, categoryId);

    }

    @Override
    public List<Long> getCategoryIdsByMovieId(Long movieId) {
        return this.list(new QueryWrapper<MovieCategoryRelation>().eq("movie_id", movieId))
                .stream().map(MovieCategoryRelation::getCategoryId).collect(Collectors.toList());
    }
}




