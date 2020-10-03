package zixiaowangfall2020.webapp.controller;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zixiaowangfall2020.webapp.pojo.User;
import zixiaowangfall2020.webapp.service.TokenService;
import zixiaowangfall2020.webapp.service.UserService;
import zixiaowangfall2020.webapp.util.RegexHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@RestController
@RequestMapping(value = "/v1/user",produces="application/json;charset=UTF-8")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    /**
    * @author: Zixiao Wang
    * @date: 10/1/20
     * @param: 
    * @return: org.springframework.http.ResponseEntity<java.util.Set<java.lang.Object>>
    * @description:
     * Get all users' information, except password.
    **/
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Set<Object>> getAll(){
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
    * @date: 10/1/20
     * @param: 
    * @return: org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    * @description:
     * Get self information by using token.
    **/
    @GetMapping("/self")
    @PreAuthorize("hasAuthority('selfUser')")
    public ResponseEntity<Map<String,Object>> getSelfUserInformation(HttpServletRequest request){
        String token = null;
        Map<String,Object> map = null;

        // Get the token
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
        }
        if (token != null) {
            UserDetails user = null;
            //查询token对应的用户
            //searching follows token->user
            user = tokenService.getUserFromToken(token);
            User currentUser = userService.getByUserName(user.getUsername());

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

    @PutMapping("/self")
    @PreAuthorize("hasAuthority('selfUser')")
    public ResponseEntity<Map<String,Object>> updateUserInformation(@NotNull @Valid @RequestBody User newUser, HttpServletRequest request){
        String token = null;
        Map<String,Object> map = null;

        // Get the token
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
        }
        if (token != null) {
            UserDetails user = null;
            //查询token对应的用户
            //searching follows token->user
            user = tokenService.getUserFromToken(token);
            User currentUser = userService.getByUserName(user.getUsername());

            currentUser.setUserName(newUser.getUserName());
            currentUser.setPassword(newUser.getPassword());
            currentUser.setFirstName(newUser.getFirstName());
            currentUser.setLastName(newUser.getLastName());

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
    public ResponseEntity<Map<String,Object>> insertNewUser(@Valid @RequestBody User newUser){
        // if the newUser has the same emailAddress with someone in database
        String newUserUserName = newUser.getUserName();
        Map<String,Object> map = new HashMap<String,Object>();

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
