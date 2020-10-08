package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.*;
import zixiaowangfall2020.webapp.pojo.Question;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface QuestionMapper {

    @Select("select * from Question")
    public List<Question> getAllQuestions();

    @Select("select * from Question where " +
            "questionId=#{questionId}")
    public Question getQuestionById(String questionId);

    @Insert("insert into Question (questionId,createdTimestamp,updatedTimestamp,userId,questionText)" +
            " values (#{questionId},#{createdTimestamp},#{updatedTimestamp},#{userId},#{questionText})")
    @SelectKey(statement = "select replace(uuid(), '-', '') as id from dual", keyProperty = "questionId", resultType = String.class, before = true)
    public int insertNewQuestion(Question question);

    @Delete("delete from Question where questionId=#{questionId}")
    public void deleteQuestionByQuestionId(String questionId);

    @Update("update Question set questionText=#{questionText},updatedTimestamp=#{updatedTimestamp} where questionId=#{questionId}")
    public void updateQuestionByQuestionId(Question question);
}
