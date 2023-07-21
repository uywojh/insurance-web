package com.web.insurance.service;

import com.web.insurance.modelEntity.TypeTops;
import com.web.insurance.po.Type;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created on 2020/4/4
 * Package com.web.insurance.service
 *
 * @author dsy
 */
public interface TypeService {

    Type saveType(Type type);

    Type getType(Integer typeId);

    Page<Type> listType();

    Type updateType(Type type);

    int deleteType(Integer id);

    Type getTypeByName(String typeName);

    List<Type> allType();

    List<TypeTops> findSeveralTypes(Integer number);

    Integer findCount();
}

