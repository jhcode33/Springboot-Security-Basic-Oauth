package com.jhcode33.security.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhcode33.security.model.User;
import com.jhcode33.security.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 메소드가 실행된다.

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	//loadUserByUsername() 메소드에서 받는 username은 View에서 넘어오는 username 파라미터를 받는다. 파라미터의 명을 바꾸고 싶다면
	//SecurityConfig에서 설정해야한다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//기본적인 CRUD만 들고 있기 때문에 fingByUsername() 메소드를 만들어주어야합니다.
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}
	//-> PrincipalDetails가 리턴되면 Authentication 내부에 저장된다.
	// 그런 후 Authentication이 스프링 시큐리티의 session 내부에 들어가게 된다.
	
}
