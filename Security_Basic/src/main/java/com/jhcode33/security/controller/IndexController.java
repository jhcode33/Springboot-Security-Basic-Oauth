package com.jhcode33.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
	
	@GetMapping("/login")
	public @ResponseBody String login() {
		return "login";
	}
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "회원가입 완료됨!";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "joinProc";
	}
}
