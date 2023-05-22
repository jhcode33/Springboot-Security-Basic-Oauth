package com.jhcode33.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록된다?
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 두 개의 어노테이션 활성화.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean //해당 메소드로 리턴되는 오브젝트를 IoC 컨테이너가 관리함.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();  //Security 기본 login 페이지 사용 안함.
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") // /login URL이 호출이 되면 Security가 중간에 개입해서 로그인을 진행해줍니다, Controller에 /login 부분을 구성하지 않아도 됩니다.
			.defaultSuccessUrl("/"); //로그인 페이지로 설정한 /loginForm에서 로그인을 진행하면 인덱스페이지로 돌아가지만 특정 URL에서 권한이 필요해서 로그인을 수행하면, 특정 URL로 다시 돌아감, 디폴드로 돌아가는 것이 아님.
	}
}
