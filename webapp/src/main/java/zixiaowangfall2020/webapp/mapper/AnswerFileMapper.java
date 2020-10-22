package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import zixiaowangfall2020.webapp.pojo.AnswerFile;
import zixiaowangfall2020.webapp.pojo.QuestionFile;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface AnswerFileMapper {

    @Insert("insert into AnswerFile (answerId,fileId)" +
            " values (#{answerId},#{fileId})")
    public int insertAnswerFile(AnswerFile answerFile);

    @Delete("delete from AnswerFile where fileId=#{fileId}")
    public void deleteAnswerFileByFileId(final String fileId);

    @Select("select * from AnswerFile where answerId=#{answerId}")
    public List<AnswerFile> getAllAnswerFileByAnswerId(final String answerId);
}
