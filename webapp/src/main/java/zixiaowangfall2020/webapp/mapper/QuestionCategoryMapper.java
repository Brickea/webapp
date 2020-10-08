package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import zixiaowangfall2020.webapp.pojo.QuestionCategory;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface QuestionCategoryMapper {

    @Insert("insert into QuestionCategory (questionId,categoryId)" +
            " values (#{questionId},#{categoryId})")
    public int interQuestionCategory(QuestionCategory questionCategory);

    @Select("select * from QuestionCategory where questionId=#{questionId}")
    public List<QuestionCategory> getAllCategoryByQuestionId(String questionId);

    @Delete("delete from QuestionCategory where questionId=#{questionId}")
    public void deleteQuestionCategoryByQuestionId(String questionId);
}
