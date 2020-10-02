package zixiaowangfall2020.webapp.mapper;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

import org.apache.ibatis.annotations.*;
import zixiaowangfall2020.webapp.pojo.User;

import java.util.List;

public interface UserMapper {

    @Select("select * from User")
    public List<User> getAll();

    @Select("select * from User where userName=#{userName}")
    public User getOneByUserName(String userName);

    @Insert("insert into User (userName,password,firstName,lastName,accountCreated,accountUpdated)"+
            " values (#{userName},#{password},#{firstName},#{lastName},#{accountCreated},#{accountUpdated})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    public int insertNewUser(User user);

    @Update("update User set userName=#{userName},password=#{password},firstName=#{firstName},lastName=#{lastName},accountUpdated=#{accountUpdated} where id=#{id}")
    public int updateUser(User user);


}
