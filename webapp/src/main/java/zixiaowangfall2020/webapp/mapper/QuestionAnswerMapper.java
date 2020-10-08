package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import zixiaowangfall2020.webapp.pojo.Question;
import zixiaowangfall2020.webapp.pojo.QuestionAnswer;
import zixiaowangfall2020.webapp.pojo.QuestionCategory;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface QuestionAnswerMapper {

    @Insert("insert into QuestionAnswer (questionId,answerId)" +
            " values (#{questionId},#{answerId})")
    public int interQuestionAnswer(QuestionAnswer questionAnswer);

    @Select("select * from QuestionAnswer where questionId=#{questionId}")
    public List<QuestionAnswer> getAllQuestionAnswerByQuestionId(String questionId);

    @Select("select * from QuestionAnswer where questionId=#{questionId} and answerId=#{answerId}")
    public QuestionAnswer getQuestionAnswerByQuestionIdAnswerId(String questionId,String answerId);

    @Delete("delete from QuestionAnswer where questionId=#{questionId} and answerId=#{answerId}")
    public void deleteQuestionAnswerByQuestionIdAnswerId(String questionId,String answerId);

    @Delete("delete from QuestionAnswer where questionId=#{questionId}")
    public void deleteQuestionAnswerByQuestionId(String questionId);
}
