package com.jhcode33.security.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jhcode33.security.config.auth.PrincipalDetails;
import com.jhcode33.security.config.oauth.provider.FacebookUserInfo;
import com.jhcode33.security.config.oauth.provider.GoogleUserInfo;
import com.jhcode33.security.config.oauth.provider.NaverUserInfo;
import com.jhcode33.security.config.oauth.provider.OAuth2UserInfo;
import com.jhcode33.security.model.User;
import com.jhcode33.security.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//구글로부터 받은 userRequest 데이터에 대해서 후처리 되는 메소드
	//OAuth2UserRequest는 AccessToken과 User 데이터가 모두 담긴 객체임.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		System.out.println("UserRequest: "+userRequest);
//		System.out.println("getClientRegistration: "+userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인 했는지 알 수 있음.
//		System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
//		
//		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴받고 OAuth-Client 라이브러리가 처리함 -> AccessToken 요청
//		// userRequest 정보는 여기까지임.
//		// userRequest를 통해서 구글회원의 프로필을 받아햐하는데 그때 사용하는 메소드가 loadUser() 메소드임.
//		
//		OAuth2User oauth2User = super.loadUser(userRequest);
//		System.out.println("getAttributes: "+oauth2User.getAttributes());
//		
//		String provider = userRequest.getClientRegistration().getClientName(); //ClientName이 google인지, facebook인지 확인할 수 있는거임.
//		String providerId = oauth2User.getAttribute("sub"); //google
//	//	String providerId = oauth2User.getAttribute("id"); //facebook
//		String username = provider+"_"+providerId; //google_구글의 primary key(숫자)
//		String password = "";
//		String email = oauth2User.getAttribute("email");
//		String role = "ROLE_USER";
//		
//		User userEntity = userRepository.findByUsername(username);
//		
//		if(userEntity == null) {
//			userEntity = User.builder()
//							 .username(username)
//							 .password(password)
//							 .email(email)
//							 .role(role)
//							 .provider(provider)
//							 .providerId(providerId)
//							 .build();
//			userRepository.save(userEntity);
//		}

		//전략패턴을 사용해서, OAuth2User의 정보를 Provider에 따라 구분하는 코드.
		OAuth2User oauth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo= null;
		
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인을 진행합니다.");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
			
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인을 진행합니다.");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
			
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			System.out.println("네이버 로그인을 진행합니다.");
			oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
			
		} else {
			System.out.println("현재는 구글과 페이스북과 네이버만 지원합니다.");
		}
		
		
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;
		String password = null;
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println("OAuth 자동 회원가입 진행합니다.");
			userEntity = User.builder()
					 .username(username)
					 .password(password)
					 .email(email)
					 .role(role)
					 .provider(provider)
					 .providerId(providerId)
					 .build();
			userRepository.save(userEntity);
		} else {
			System.out.println("회원가입을 이미 진행한 적이 있습니다.");
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}

	
	
}
