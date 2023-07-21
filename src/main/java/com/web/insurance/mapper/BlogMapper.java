package com.web.insurance.mapper;

import com.web.insurance.po.Blog;
import com.web.insurance.po.Type;
import com.web.insurance.po.User;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created on 2020/4/5
 * Package com.web.insurance.mapper
 *
 * @author dsy
 */
public interface BlogMapper extends Mapper<Blog>{

    @Select("select * from blog order by update_time desc")
    @Results(id = "blogMap",
            value = {
            @Result(column = "title",property = "title"),
            @Result(column = "content",property = "content"),
                    @Result(column = "first_picture", property = "firstPicture"),
                    @Result(column = "flag", property = "flag"),
                    @Result(column = "views", property = "views"),
                    @Result(column = "appreciation", property = "appreciation"),
                    @Result(column = "share_statement", property = "shareStatement"),
                    @Result(column = "comment", property = "comment"),
                    @Result(column = "publish", property = "publish"),
                    @Result(column = "recommend", property = "recommend"),
                    @Result(column = "update_time", property = "updateTime"),
                    @Result(column = "create_time", property = "createTime"),
                    @Result(column = "type_id", property = "type", javaType = Type.class,
                            one = @One(select = "com.web.insurance.mapper.TypeMapper.selectTypeById")),
                    @Result(column = "user_id", property = "user", javaType = User.class,
                            one = @One(select = "com.web.insurance.mapper.UserMapper.selectUserByUserId"))
            })
    List<Blog> findBlogAll();


    List<Blog> findBlogByKeyWords(@Param(value = "title") String title);


    @Select("select * from blog where title = #{title}")
    @ResultMap(value = "blogMap")
    List<Blog> findBlogByTitle(@Param(value = "title") String title);


    @Select("select * from blog where blog_id = #{id}")
    @ResultMap(value = "blogMap")
    Blog selectBlogByBlogId(@Param(value = "id") String id);

    @Select("select * from blog where title = #{title} limit 1")
    @ResultMap(value = "blogMap")
    Blog selectBlogByTitle(@Param(value = "title") String title);

    @Select("select * from blog where type_id = #{typeId}")
    @ResultMap(value = "blogMap")
    List<Blog> selectBlogByTypeId(@Param(value = "typeId") Integer typeId);

    @Select("select date_format(b.update_time,'%Y') as year from blog b group by year order by year desc")
    List<String> findYearsGroupByYear();

    @Select("select  * from blog b where date_format(b.update_time,'%Y') = #{year}")
    List<Blog> selectBlogByYear(@Param(value = "year") String year);
}
