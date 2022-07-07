package com.bosonit.EJ3.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    @Bean
    protected SecurityFilterChain configure (HttpSecurity http) throws Exception{
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                //Solo puedes modificar los usuarios con rol ADMIN
                .antMatchers(HttpMethod.POST,"/person/add").permitAll()
                .antMatchers(HttpMethod.GET,"/person/id/{id}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.GET,"/person/name/{name}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE,"/person/delete/{id}}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/person/update/{id}").hasRole("ADMIN")
                //Endpoint teacher
                .antMatchers(HttpMethod.POST,"/teacher/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/teacher/id/{id}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.GET,"/teacher/name/{name}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE,"/teacher/delete/{id}}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/teacher/update/{id}").hasRole("ADMIN")
                //Endpoint student
                .antMatchers(HttpMethod.POST,"/student/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/student/id/{id}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.GET,"/student/name/{name}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE,"/student/delete/{id}}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/student/update/{id}").hasRole("ADMIN")
                //Endpoint subject
                .antMatchers(HttpMethod.POST,"/subject/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/subject/id/{id}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.GET,"/subject/name/{name}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE,"/subject/delete/{id}}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/subject/update/{id}").hasRole("ADMIN")
                .anyRequest().authenticated();
        return http.build();
    }

}
