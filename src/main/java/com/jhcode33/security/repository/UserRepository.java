package com.jhcode33.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhcode33.security.model.User;

// JpaRepository가 기본적인 CRUD SQL 메서드를 가지고 있음
// @Reposiroty 어노테이션이 없어도 빈 객체로 사용 가능함.
public interface UserRepository extends JpaRepository<User, Integer> {

	//SELECT * FROM user WHERE username = ?, ?에는 파라미터로 넘어간 username의 값으로 들어간다.
	public User findByUsername(String username);
}
