package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import zixiaowangfall2020.webapp.pojo.QuestionCategory;
import zixiaowangfall2020.webapp.pojo.QuestionFile;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public interface QuestionFileMapper {

    @Insert("insert into QuestionFile (questionId,fileId)" +
            " values (#{questionId},#{fileId})")
    public int insertQuestionFile(QuestionFile questionFile);

    @Delete("delete from QuestionFile where fileId=#{fileId}")
    public void deleteQuestionFileByFileId(final String fileId);

    @Select("select * from QuestionFile where questionId=#{questionId}")
    public List<QuestionFile> getAllQuestionFileByQuestionId(final String questionId);
}
