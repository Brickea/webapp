package zixiaowangfall2020.webapp.service;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.UserMapper;
import zixiaowangfall2020.webapp.pojo.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired UserMapper userMapper;

    @Autowired TokenService tokenService;

    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param:
    * @return: java.util.List<zixiaowangfall2020.webapp.pojo.User>
    * @description: Get all user information
    **/
    public List<User> getAllUsers(){
        return userMapper.getAll();
    }

    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param: userName
    * @return: zixiaowangfall2020.webapp.pojo.User
    * @description: Get a particular user information by using userName
    **/
    public User getByUserName(String userName){
        return userMapper.getOneByUserName(userName);
    }

//    public User getByUserToken(String token){
//        String userName = tokenService.getUserFromToken(token).getUsername();
//        return userMapper.getOneByUserName(userName);
//    }

    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param: newUser
    * @return: void
    * @description: Insert a new user information into database
    **/
    public void insertNewUser(User newUser){
        // Get current time and save it as accountCreated, accountUpdated
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        newUser.setAccountCreated(ft.format(dNow));
        newUser.setAccountUpdated(ft.format(dNow));

        // Use BCrypt encoding tech to encode the password
        String rowPassword = newUser.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rowPassword);
        newUser.setPassword(encodedPassword);


        userMapper.insertNewUser(newUser);
    }

    public void updateUser(User user){
        // Get current time and save it as accountCreated, accountUpdated
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        user.setAccountUpdated(ft.format(dNow));

        // Use BCrypt encoding tech to encode the password
        String rowPassword = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rowPassword);
        user.setPassword(encodedPassword);

        userMapper.updateUser(user);
    }
}
