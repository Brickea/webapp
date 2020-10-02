package zixiaowangfall2020.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zixiaowangfall2020.webapp.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    TokenService tokenService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> userInfo) {
        String userName = userInfo.get("username");
        String password = userInfo.get("password");

        HashMap<String, Object> result = new HashMap<>();
        String token = tokenService.login(userName, password);
        if (token == null) {
            result.put("message", "invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else {
            result.put("role",userName);
            result.put("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @DeleteMapping
    public Map<String, Object> logout(HttpServletRequest request) {
        String token = null;
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            tokenService.logout(token);
        }
        HashMap<String, Object> result = new HashMap<>();
        return result;
    }
}
