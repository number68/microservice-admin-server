/**
 * Copyright (C), 2015-2019, 辰森世纪
 * FileName: ChoiceCrmApplication.java
 * Author: zwl
 * Date: 2019/03/26 12:07:57
 * Description: <功能描述>
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间            版本号                描述
 */
package com.choice.cloud.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;

/**
 * <功能描述><br/>
 *
 * @author zwl
 * @create 2019/03/26 12:07:57
 * @since 0.1
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
@Slf4j
public class ChoiceAdminCenterApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChoiceAdminCenterApplication.class, args);

        log.info("------------choice-admin-center started successfully-------------");
    }

    @Configuration
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setAlwaysUseDefaultTargetUrl(true);
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl(adminContextPath + "/");

            http.authorizeRequests()
                    .antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                    .logout().logoutUrl(adminContextPath + "/logout").and()
                    .httpBasic().and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringAntMatchers(adminContextPath + "/instances", adminContextPath + "/actuator/**");
            http.headers().frameOptions().sameOrigin();
        }
    }
}
