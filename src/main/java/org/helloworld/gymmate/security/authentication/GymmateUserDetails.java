package org.helloworld.gymmate.security.authentication;

import java.util.Collection;

import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.model.GymmateUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class GymmateUserDetails implements UserDetails {

	final String USER_ROLE = "ROLE_USER";

	private final Member member;

	public GymmateUserDetails(Member member) {
		this.member = member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(USER_ROLE);
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return member.getMemberName();
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
