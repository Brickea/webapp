package zixiaowangfall2020.webapp.controller;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

import com.amazonaws.services.appsync.model.LogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zixiaowangfall2020.webapp.pojo.UpdateUser;
import zixiaowangfall2020.webapp.pojo.User;
import zixiaowangfall2020.webapp.service.UserService;
import zixiaowangfall2020.webapp.util.RegexHelper;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping(value = "/v1/user",produces="application/json;charset=UTF-8")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);

    @Autowired
    UserService userService;

    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param: 
    * @return: org.springframework.http.ResponseEntity<java.util.Set<java.lang.Object>>
    * @description:
     * Get all users' information, except password.
    **/
    @GetMapping("/all")
    public ResponseEntity<Set<Object>> getAll(){
        LOG.info("API CALL GET /v1/user/all get all users");

        List<User> list = userService.getAllUsers();

        Set<Object> set = new HashSet <Object>();

        for(User user : list){
            Map<String,Object> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("user_name",user.getUserName());
            map.put("first_name",user.getFirstName());
            map.put("last_name",user.getLastName());
            map.put("account_created",user.getAccountCreated());
            map.put("account_updated",user.getAccountUpdated());
            set.add(map);
        }
        return new ResponseEntity(set,HttpStatus.OK);
    }

    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: id
    * @return: org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    * @description:
    **/
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getUserByUserId(@PathVariable("id") String id){
        LOG.info("API CALL GET /v1/user/{id} get user by id");

        Map<String,Object> map = new HashMap<>();

        User user = userService.getByUserId(id);
        if(user==null){
            map.put("message","No user found!");
            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
        }

        map.put("id",user.getId());
        map.put("user_name",user.getUserName());
        map.put("first_name",user.getFirstName());
        map.put("last_name",user.getLastName());
        map.put("account_created",user.getAccountCreated());
        map.put("account_updated",user.getAccountUpdated());

        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param: 
    * @return: org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    * @description:
     * Get self information by using token.
    **/
    @GetMapping("/self")
    public ResponseEntity<Map<String,Object>> getSelfUserInformation(HttpServletRequest request){
        LOG.info("API CALL GET /v1/user/self get user self information");

        String token = null;
        Map<String,Object> map = null;

        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            User currentUser = userService.getByUserName(currentUserName);

            map = new HashMap<>();

            map.put("id",currentUser.getId());
            map.put("user_name",currentUser.getUserName());
            map.put("first_name",currentUser.getFirstName());
            map.put("last_name",currentUser.getLastName());
            map.put("account_created",currentUser.getAccountCreated());
            map.put("account_updated",currentUser.getAccountUpdated());
            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.CREATED);
        }
        return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
    }

    /**
    * @author: Zixiao Wang
    * @date: 10/6/20
     * @param: newUser
     * @param: request
    * @return: org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    * @description:
    **/
    @PutMapping("/self")
    public ResponseEntity<Map<String,Object>> updateUserInformation(@NotNull @Valid @RequestBody UpdateUser updateUser, HttpServletRequest request){
        String token = null;
        Map<String,Object> map = null;

        // Check if the format is correct
        if(updateUser.getUserName()!=null){
            map.put("Message","Email shall not be changed!");
            return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
        }

        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            User currentUser = userService.getByUserName(currentUserName);
            if(currentUser==null){
                return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
            }

            currentUser.setPassword(updateUser.getPassword());
            currentUser.setFirstName(updateUser.getFirstName());
            currentUser.setLastName(updateUser.getLastName());

            userService.updateUser(currentUser);

            map = new HashMap<>();

            map.put("id", currentUser.getId());
            map.put("user_name", currentUser.getUserName());
            map.put("first_name", currentUser.getFirstName());
            map.put("last_name", currentUser.getLastName());
            map.put("account_created", currentUser.getAccountCreated());
            map.put("account_updated", currentUser.getAccountUpdated());
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NO_CONTENT);
    }
    
    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param: newUser
    * @return: org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    * @description:
    **/
    @PostMapping
    public ResponseEntity<Map<String,Object>> insertNewUser(@RequestBody User newUser){
        // if the newUser has the same emailAddress with someone in database
        String newUserUserName = newUser.getUserName();
        Map<String,Object> map = new HashMap<String,Object>();

        // Check if the format is correct
//        if(newUser.getUserName()!=null){
//            map.put("Message","Email shall not be changed!");
//            return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
//        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        if(!newUser.getPassword().matches(regex)){
            map.put("Message","password's length should longer than 8 and contain at least one char and one number!");
            return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
        }

        if(userService.getByUserName(newUserUserName)==null){
            // new user
            map.put("password_strength",RegexHelper.checkPassordStrength(newUser.getPassword()));
            userService.insertNewUser(newUser);

            map.put("id",newUser.getId());
            map.put("user_name",newUser.getUserName());
            map.put("first_name",newUser.getFirstName());
            map.put("last_name",newUser.getLastName());
            map.put("account_created",newUser.getAccountCreated());
            map.put("account_updated",newUser.getAccountUpdated());


            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.CREATED);
        }else{
            map.put("error message","the email address has been used!");
            return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
        }

    }
}
