package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.*;
import zixiaowangfall2020.webapp.pojo.Answer;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface AnswerMapper {

    @Select("select * from Answer where answerId=#{answerId}")
    public Answer getAnswerById(String answerId);

    @Insert("insert into Answer (answerId,createdTimestamp,updatedTimestamp,userId,answerText) values(#{answerId},#{createdTimestamp},#{updatedTimestamp},#{userId},#{answerText})")
    @SelectKey(statement = "select replace(uuid(), '-', '') as id from dual", keyProperty = "answerId", resultType = String.class, before = true)
    public int insertNewAnswer(Answer answer);

    @Update("update Answer set answerText=#{answerText},updatedTimestamp=#{updatedTimestamp} where answerId=#{answerId}")
    public int updateAnswerByAnswer(Answer answer);

    @Delete("delete from Answer where answerId=#{answerId}")
    public void deleteAnswerByAnswerId(String answerId);


}
