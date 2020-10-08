package zixiaowangfall2020.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import zixiaowangfall2020.webapp.service.AuthUserService;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthUserService authUserService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserService).passwordEncoder(new BCryptPasswordEncoder()); //user Details Service验证
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable().authorizeRequests()
                // User Api
                .antMatchers(HttpMethod.GET,"/v1/user/all").hasRole("MANAGE")
                .antMatchers(HttpMethod.GET,"/v1/user/self").hasRole("NORMAL")
                .antMatchers(HttpMethod.PUT,"/v1/user/self").hasRole("NORMAL")
                // Category Api
                .antMatchers(HttpMethod.GET,"/v1/category").hasRole("MANAGE")
                // Question Api
                .antMatchers(HttpMethod.POST,"/v1/question").hasRole("NORMAL")
                .antMatchers(HttpMethod.PUT,"/v1/question/**").hasRole("NORMAL")
                .antMatchers(HttpMethod.DELETE,"/v1/question/**").hasRole("NORMAL")
                // Answer Api
                .antMatchers(HttpMethod.POST,"/v1/question/**/answer").hasRole("NORMAL")
                .antMatchers(HttpMethod.PUT,"/v1/question/**/answer/**").hasRole("NORMAL")
                .antMatchers(HttpMethod.DELETE,"/v1/question/**/answer/**").hasRole("NORMAL")
                .antMatchers(HttpMethod.PUT,"/question/**").hasRole("NORMAL")
                .anyRequest().permitAll();


    }
}
