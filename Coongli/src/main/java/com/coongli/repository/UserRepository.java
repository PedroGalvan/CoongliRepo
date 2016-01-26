package com.coongli.repository;

import com.coongli.domain.User;

import java.time.ZonedDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);

    @Query("select u from User u where u.id=?1")
	User findOne(int id);
    
    @Query("select u from User u where u.userAccount.id=?1")
	User findOneByPrincipal(int id);
	
	@Query("select u from User u where u.userAccount.id=?1")
	User findByUserAccount(int id);
	
	@Query("select distinct u from User u  where (u.name like %?1% or u.surname like %?1% " +
			"or u.email like %?1% or u.userAccount.username like %?1% " +
			"or u.city like %?1% or u.nationality like %?1%)")
	Collection<User> findAllByKeyWord(String kw);
	
	@Query("select distinct si.recipient from Session s join s.invitations si where s.id=?1")
	Collection<User> findUsersBySession(long sessionId);

    
    @Override
    void delete(User t);

}
