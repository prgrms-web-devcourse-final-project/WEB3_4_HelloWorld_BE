package org.helloworld.gymmate.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GymmateUserDetails implements UserDetails {

	private final Long userId;
	private final String username;
	private final String userType; // 예: MEMBER, TRAINER
	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;  // 계정이 만료되지 않았다는 조건
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;  // 계정이 잠기지 않았다는 조건
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;  // 자격 증명이 만료되지 않았다는 조건
	}

	@Override
	public boolean isEnabled() {
		return true;  // 계정이 활성화되었는지 여부
	}

}
