package com.jhcode33.security.config.oauth.provider;

public interface OAuth2UserInfo {
	//디자인 패턴에서 전략 패턴을 사용하고, 각각을 캡슐화함.
	//facebook과 google의 id값을 가져오는 key가 다르기 때문에 작성하게 됨.
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
}
