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

    @Select("select * from User where id=#{id}")
    public User getOneByUserId(String id);

    @Select("select * from User where userName=#{userName}")
    public User getOneByUserName(String userName);

    @Insert("insert into User (id,userName,password,firstName,lastName,accountCreated,accountUpdated)" +
            " values (#{id}, #{userName},#{password},#{firstName},#{lastName},#{accountCreated},#{accountUpdated})")
    @SelectKey(statement = "select replace(uuid(), '-', '') as id from dual", keyProperty = "id", resultType = String.class, before = true)
    public int insertNewUser(User user);

    @Update("update User set userName=#{userName},password=#{password},firstName=#{firstName},lastName=#{lastName},accountUpdated=#{accountUpdated} where id=#{id}")
    public int updateUser(User user);


}
