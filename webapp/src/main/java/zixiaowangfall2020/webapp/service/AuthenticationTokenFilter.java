package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description: request中获取token，并把token转换成用户，放置到当前的spring context内。
 * 这个类必须写一个@Service注解，否则spring不会加载它作为filter
 * Obtain the token from the request, convert the token into a user, and place it in the current spring context.
 * This class must write a @Service annotation, otherwise spring will not load it as a filter
 **/


@Service
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authToken = null;

        // 下面的代码从Http Header的Authorization中获取token，也可以从其他header,cookie等中获取，看客户端怎么传递token
        // The following code gets the token from the Authorization of the Http Header, or from other headers, cookies, etc., depending on how the client passes the token
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
        }

        if (authToken != null) {
            UserDetails user = null;
            //查询token对应的用户
            //searching follows token->user
            user = tokenService.getUserFromToken(authToken);
            if (user != null) {
                // 把user设置到SecurityContextHolder内，以spring使用
                // set user into SecurityContextHolder for spring
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}