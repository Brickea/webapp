package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import zixiaowangfall2020.webapp.pojo.Category;
import zixiaowangfall2020.webapp.pojo.User;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface CategoryMapper {
    @Insert("insert into Category (categoryId,category)" +
            " values (#{categoryId},#{category})")
    @SelectKey(statement = "select replace(uuid(), '-', '') as id from dual", keyProperty = "categoryId",resultType = String.class,before = true)
    public int insertNewCategory(Category category);

    @Select("select * from Category")
    public List<Category> getAllCategory();

    @Select("select * from Category " +
            "where category=#{category}")
    public Category getCategoryByCategory(String category);

    @Select("select * from Category where categoryId=#{categoryId}")
    public Category getCategoryByCategoryId(String categoryId);
}
