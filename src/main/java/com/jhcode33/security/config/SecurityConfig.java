package com.jhcode33.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jhcode33.security.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록된다?
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 두 개의 어노테이션 활성화.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
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
			.defaultSuccessUrl("/") //로그인 페이지로 설정한 /loginForm에서 로그인을 진행하면 인덱스페이지로 돌아가지만 특정 URL에서 권한이 필요해서 로그인을 수행하면, 특정 URL로 다시 돌아감, 디폴드로 돌아가는 것이 아님.
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			//로그인이 완료되고 난 후 정보를 가져오는 작업이 필요함.
			// 1. 코드 받기(인증), 2. 엑세스 토큰(권한), 3.사용자프로필 정보를 가져오기 4-1. 그 정보를 토대로 회원가입을 자동으로 진행 시키기.
			// 4-2.(이름, 이메밀, 전화번호, 아이디), 쇼핑몰일 경우 추가적인 정보(주소)가 필요할 수 있음.
			// 코드 x, (엑세스 토큰 + 사용자 프로필 정보를 다 받을 수 있음)
			.userInfoEndpoint()
			.userService(principalOauth2UserService);
	}
}
