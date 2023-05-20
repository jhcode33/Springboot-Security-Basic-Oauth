package com.jhcode33.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jhcode33.security.model.User;

//Security가 /login controller를 대신해서 로그인을 진행합니다.
//로그인 진행이 완료가 되면 session을 만들어 로그인 상태를 저장해야합니다.
//Security는 자신만의 session을 가지고 있다.
//Security ContextHolder을 key 값으로 가지고 정보를 구분한다.
//Authentication 객체만 security session의 정보로 사용할 수 있다.
//Authentication 안에 User 정보가 있어야 한다.
//User 객체의 타입 = UserDetails 타입 객체이여야만 한다.

//Security Session은 Authentication 객체를 사용해야하고
//Authentication 객체는 User 정보를 UserDetails 타입의 객체로 저장해야함.

public class PrincipalDetails implements UserDetails {

	private User user; // User 객체를 주입받음, 생성자를 통해서

	public PrincipalDetails(User user) {
		this.user = user;
	}

	// 해당 User의 권한을 리턴하는 메소드임
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
	
			//String 타입인 User의 권한을 GrantedAuthority 타입으로 변경
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	//패스워드 리턴
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	//계정이 만료가 안 되었는지? True는 만료되지 않았다.
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	//계정이 잠겼는지? True는 잠겼지 않았다.
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//비밀번호의 유효기간이 지났는지, True는 안 지났다.
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//계정이 활성화 되어있니? True는 활성화되어있다.
	@Override
	public boolean isEnabled() {
		
		//우리 사이트에서 1년동안 회원이 로그인을 안 하면 휴면 계정으로 하기로 했다면
		//현재 시간 - 로그인 시간 => 1년을 초과하면 return False; 이런식으로 작성하면 됨
		
		
		return true;
	}

}
