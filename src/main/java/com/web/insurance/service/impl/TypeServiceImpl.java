package com.web.insurance.service.impl;

import com.web.insurance.exception.NotFoundException;
import com.web.insurance.mapper.BlogMapper;
import com.web.insurance.mapper.TypeMapper;
import com.web.insurance.modelEntity.TypeTops;
import com.web.insurance.po.Blog;
import com.web.insurance.po.Type;
import com.web.insurance.service.TypeService;
import com.web.insurance.util.RedisKeyUtils;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created on 2020/4/4
 * Package com.web.insurance.service.impl
 *
 * @author dsy
 */
@Service
@Slf4j
public class TypeServiceImpl implements TypeService {


    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private BlogMapper blogMapper;


    @Transactional
    @Override
    public Type saveType(Type type) {
        int integer = typeMapper.insert(type);
        return integer == 1 ? type : null;
    }

    @Override
    public Type getType(Integer typeId) {
        return typeMapper.selectByPrimaryKey(typeId);
    }

    @Transactional
    @Override
    public Page<Type> listType() {
        return (Page<Type>) typeMapper.selectAll();
    }

    @Transactional
    @Override
    public Type updateType(Type type) {
        Type t = typeMapper.selectByPrimaryKey(type.getTypeId());
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        } else {
            typeMapper.updateByPrimaryKey(type);
        }
        return typeMapper.selectByPrimaryKey(type.getTypeId());
    }

    @Transactional
    @Override
    public int deleteType(Integer id) {
        //先查询blog是否引用过该typeId 若没有则删除，否则删除失败
        Example example = new Example(Blog.class);
        example.createCriteria().andEqualTo("typeId", id);
        List<Blog> blogs = blogMapper.selectByExample(example);
        if (blogs.size() > 0) {
            return 0;
        }
        int i = typeMapper.deleteByPrimaryKey(id);
        //从redis中删除BlogTypes对应的数据
        log.info("删除redis中与BlogTypes对应的类型的数据，类型id为：" + id);
        return i;
    }

    @Override
    public Type getTypeByName(String typeName) {
        Example example = new Example(Type.class);
        example.createCriteria().andEqualTo("name", typeName);
        return typeMapper.selectOneByExample(example);
    }

    @Override
    public List<Type> allType() {
        return typeMapper.selectAll();
    }

    @Override
    public List<TypeTops> findSeveralTypes(Integer number) {
        //没有再从mysql数据库中获得并放到redis中
        List<TypeTops> typeTops = typeMapper.selectSeveralTopTypes(number);
        return typeTops;
    }


    @Override
    public Integer findCount() {
        return typeMapper.selectCount(null);
    }

}
