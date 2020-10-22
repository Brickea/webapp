package zixiaowangfall2020.webapp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import zixiaowangfall2020.webapp.pojo.User;
import zixiaowangfall2020.webapp.pojo.WebappFile;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/


public interface WebappFileMapper {
    @Insert("insert into WebappFile (fileId,fileName,s3ObjectName,createdTimestamp)" +
            " values (#{fileId}, #{fileName},#{s3ObjectName},#{createdTimestamp})")
    @SelectKey(statement = "select replace(uuid(), '-', '') as id from dual", keyProperty = "fileId", resultType = String.class, before = true)
    public int insertNewFile(WebappFile webappFile);

    @Select("select * from WebappFile where fileId=#{fileId}")
    public WebappFile getOneByFileId(String fileId);

    @Delete("delete from WebappFile where fileId=#{fildId}")
    public void deleteFileByFileId(final String fileId);
}
