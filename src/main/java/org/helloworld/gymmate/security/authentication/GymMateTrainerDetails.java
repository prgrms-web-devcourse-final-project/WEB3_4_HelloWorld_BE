package org.helloworld.gymmate.security.authentication;

import java.util.Collection;

import org.helloworld.gymmate.domain.user.trainer.model.Trainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class GymMateTrainerDetails implements UserDetails {

	final String TRAINER_ROLE = "ROLE_TRAINER";

	private final Trainer trainer;

	public GymMateTrainerDetails(Trainer trainer) {
		this.trainer = trainer;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return getTrainer().getIsAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(TRAINER_ROLE);
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return trainer.getTrainerName();
	}
}
