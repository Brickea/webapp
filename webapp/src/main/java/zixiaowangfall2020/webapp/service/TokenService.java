package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.pojo.User;

import java.util.*;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Service
public class TokenService {
    // 一般是把token和用户对应关系放在数据库或高速缓存(例如readis/memcache等），放在一个单例类的成员变量里仅适合很小规模的情形
    // Usually we put the map of (user,token) into redis/memcache. This case is only suit for small scale.
    private Map<String, UserDetails> tokenMap = new HashMap<>();

    @Autowired
    UserService userService;

    public UserDetails getUserFromToken(String token) {
        if (token == null) {
            return null;
        }
        return tokenMap.get(token);
    }

    /**
     * @author: Zixiao Wang
     * @date: 10/1/20
     * @param: userName
     * @param: password
     * @return: java.lang.String
     * @description: Login and return the token
     **/
    public String login(String userName, String password) {
        UserDetails ud = null;

        // Get Registered user and authorize them with grant
        User loginUser = userService.getByUserName(userName);
        if (loginUser != null) {
            // normal user and check if the password is right
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, loginUser.getPassword())) {
                // password is correct
                ud = createUser(userName, password, "selfUser");
            }

        } else {
            if ("author".equals(userName) && "password".equals(password)) {
                ud = createUser(userName, password, "author");
            } else if ("reader".equals(userName) && "password".equals(password)) {
                ud = createUser(userName, password, "reader");
            } else if ("admin".equals(userName) && "password".equals(password)) {
                ud = createUser(userName, password, "admin");
            }
        }

//        // 此例中支持三个用户： author/reader/admin 三个用户的密码都是password; author具有author角色；rader具有reader角色；admin则2个角色都有
//        // In this example, three users are supported: author/reader/admin. The passwords of the three users are password; author has the author role; radar has the reader role; admin has both roles
//        if("author".equals(userName) && "password".equals(password)) {
//            ud = createUser(userName, password, "author");
//        }else if("reader".equals(userName) && "password".equals(password)) {
//            ud = createUser(userName, password, "reader");
//        }else if("admin".equals(userName) && "password".equals(password)) {
//            ud = createUser(userName, password, "admin");
//        }
        if (ud != null) {
            String token = UUID.randomUUID().toString();
            tokenMap.put(token, ud);
            return token;
        } else {
            return null;
        }
    }

    /**
     * @author: Zixiao Wang
     * @date: 10/1/20
     * @param: token
     * @return: void
     * @description: Logout and delete the token
     **/
    public void logout(String token) {
        tokenMap.remove(token);
    }

    private UserDetails createUser(String userName, String password, String privillage) {
        return new UserDetails() {

            private static final long serialVersionUID = 6905138725952656074L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

                //这是增加了一种名为query的权限，可以使用 @hasAuthority("query") 来判断
                //This is an additional permission called query, which can be judged by @hasAuthority("query")
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(privillage);
                authorities.add(authority);

                //这是增加到xxx角色，可以用hasRole("xxx")来判断；需要注意所有的角色在这里增加时必须以ROLE_前缀，使用时则没有ROLE_前缀
                //This is added to the role of xxx, which can be judged by hasRole("xxx"); note that all roles must be prefixed with ROLE_ when added here, and there is no ROLES_ prefix when used
//                for(String role : roles) {
//                    SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + role);
//                    authorities.add(sga);
//                }
                return authorities;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUsername() {
                return userName;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

}
