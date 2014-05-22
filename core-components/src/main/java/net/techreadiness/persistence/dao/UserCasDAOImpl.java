package net.techreadiness.persistence.dao;

import javax.persistence.NoResultException;

import net.techreadiness.persistence.domain.UserCasDO;
import net.techreadiness.persistence.domain.UserDO;

import org.springframework.stereotype.Repository;

@Repository
public class UserCasDAOImpl extends BaseDAOImpl<UserCasDO> implements UserCasDAO {

	@Override
	public UserCasDO getByUsername(final String username) {
		StringBuilder sb = new StringBuilder();
		sb.append("select u ");
		sb.append("from UserCasDO u  ");
		sb.append("where u.username=:username ");

		UserCasDO userCasDO;
		try {
			userCasDO = em.createQuery(sb.toString(), UserCasDO.class).setParameter("username", username).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
		return userCasDO;
	}

	@Override
	public void create(final UserDO userDO, String initialPassword) {
		UserCasDO userCasDO = new UserCasDO();
		userCasDO.setPassword(initialPassword);
		userCasDO.setUsername(userDO.getUsername());
		persist(userCasDO);
	}
}
