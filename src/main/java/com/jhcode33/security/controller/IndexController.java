package com.jhcode33.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhcode33.security.config.auth.PrincipalDetails;
import com.jhcode33.security.model.User;
import com.jhcode33.security.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userReposiroty;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/oauth/login") 
	public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
		System.out.println("/test/login ===============");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("userDetails: "+ oauth2User.getAttributes());
		System.out.println("oauth: " + oauth.getAttributes());
		return "OAuth 세션 정보 확인하기";
	}
	
	@GetMapping("/test/login") //@AuthenticationPrincipal 어노테이션을 통해서 세션 정보에 접근할 수 있다. UserDetails를 구현한 PrincipalDetails 타입으로 받을 수도 있다.
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI를 통해서 PrincipalDetails 객체를 리턴 받을 수 있다.
		System.out.println("/test/login ===============");
		
		// authentication.getPrincipal()이 Object 타입으로 반환되기 때문에 형 변환을 해야한다.
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication: "+ principalDetails.getUsername());
		System.out.println("userDetails: "+ userDetails.getUsername());
		return "세션 정보 확인하기";
	}
	

	// "", / URL으로 들어온 모든 요청을 index View로 요청합니다.
	@GetMapping({ "", "/" })
	public String index() {
		// Mustache 템플릿 엔진을 사용함. src/main/resoureces -> 이 경로로 View가 잡히게 되기 때문에
		// 뷰리졸버 설정을 templates(prefix), mustache(suffix) 설정을 해주어야합니다.
		return "index"; // src/main/resources/templates/index.mustache 기본적으로 mustache 파일을 찾기 때

	}
	
	//OAuth, 일반 로그인 상관 없이 PrincipalDetails를 사용할 수 있음.
	
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: "+principalDetails);
		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}

	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}

	// == 로그인 ==//
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}

	// == 회원가입 ==//
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userReposiroty.save(user); // 이렇게 회원가입하면 비빌번호가 그대로 DB에 저장되어, Security로 로그인할 수 없다. Security는 패스워드 암호화를 해주어야
									// 한다.
		return "redirect:/loginForm"; // redircet: 해당 URL로 재요청함.
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}

	// data()메소드가 실행되기 직전에 발생됨.
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "개인정보";
	}
}
