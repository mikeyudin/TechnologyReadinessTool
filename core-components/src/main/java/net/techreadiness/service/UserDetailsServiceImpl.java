package net.techreadiness.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.techreadiness.persistence.dao.UserDAO;
import net.techreadiness.persistence.domain.UserDO;
import net.techreadiness.persistence.domain.UserRoleDO;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Inject
	private UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserDO userDO = userDAO.findByUsername(username, false);
		if (userDO == null) {
			throw new UsernameNotFoundException(username);
		}

		return new org.springframework.security.core.userdetails.User(userDO.getUsername(), "", true, true, true, true,
				getAuthorities(userDO));
	}

	private static List<GrantedAuthority> getAuthorities(UserDO userDO) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (userDO == null || userDO.getUserRoles() == null) {
			return authorities;
		}

		for (UserRoleDO userRole : userDO.getUserRoles()) {
			authorities.add(new SimpleGrantedAuthority(String.valueOf(userRole.getRole().getRoleId())));
		}

		return authorities;
	}

}
