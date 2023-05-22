package com.jhcode33.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jhcode33.security.model.User;
import com.jhcode33.security.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userReposiroty;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// "", / URL으로 들어온 모든 요청을 index View로 요청합니다.
	@GetMapping({"","/"})
	public String index() {
		//Mustache 템플릿 엔진을 사용함. src/main/resoureces -> 이 경로로 View가 잡히게 되기 때문에
		//뷰리졸버 설정을 templates(prefix), mustache(suffix) 설정을 해주어야합니다.
		return "index"; // src/main/resources/templates/index.mustache 기본적으로 mustache 파일을 찾기 때
		
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
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
	
	//== 로그인 ==//
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	//== 회원가입 ==//
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
		userReposiroty.save(user); //이렇게 회원가입하면 비빌번호가 그대로 DB에 저장되어, Security로 로그인할 수 없다. Security는 패스워드 암호화를 해주어야 한다.
		return "redirect:/loginForm"; //redircet: 해당 URL로 재요청함.
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
