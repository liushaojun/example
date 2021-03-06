package com.brook.example.security.config;

import com.brook.example.security.filter.CsrfFilter.AfterCsrfFilter;
import com.brook.example.security.filter.CsrfFilter.BeforeLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public WebSecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * 匹配 "/" 路径，不需要权限即可访问
     * 匹配 "/user" 及其以下所有路径，都需要 "USER" 权限
     * 登录地址为 "/login"，登录成功默认跳转到页面 "/user"
     * 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
     * 默认启用 CSRF
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/user/**").hasRole("USER")
            .and()
            .formLogin().loginPage("/login").defaultSuccessUrl("/user")
            .and()
            .rememberMe()
            .key("brook")
            .tokenValiditySeconds(60 * 60 *24 * 7)
            .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        // 在UsernamePasswordAuthenticationFilter 之前添加
        http.addFilterAfter(new BeforeLoginFilter(),
            UsernamePasswordAuthenticationFilter.class)
            // CsrfFilter 后添加AfterCsrfFilter
            .addFilterAfter(new AfterCsrfFilter(),
                org.springframework.security.web.csrf.CsrfFilter.class);

    }

    /**
     * 添加 UserDetailsService， 实现自定义登录校验
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 密码加密
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}