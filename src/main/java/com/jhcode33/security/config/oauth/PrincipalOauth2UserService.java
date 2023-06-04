package com.jhcode33.security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	//구글로부터 받은 userRequest 데이터에 대해서 후처리 되는 메소드
	//OAuth2UserRequest는 AccessToken과 User 데이터가 모두 담긴 객체임.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("UserRequest: "+userRequest);
		System.out.println("getClientRegistration: "+userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인 했는지 알 수 있음.
		System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
		
		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴받고 OAuth-Client 라이브러리가 처리함 -> AccessToken 요청
		// userRequest 정보는 여기까지임.
		// userRequest를 통해서 구글회원의 프로필을 받아햐하는데 그때 사용하는 메소드가 loadUser() 메소드임.
		
		System.out.println("getAttributes: "+super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		
		//Attributes 정보를 통해서 회원가입을 강제로 진행함.
		
		return super.loadUser(userRequest);
	}

	
	
}
