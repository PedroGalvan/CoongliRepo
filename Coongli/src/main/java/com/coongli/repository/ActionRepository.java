package com.coongli.repository;

import com.coongli.domain.Action;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Action entity.
 */
public interface ActionRepository extends JpaRepository<Action,Long> {

    @Query("select action from Action action where action.user.login = ?#{principal.username}")
    List<Action> findByUserIsCurrentUser();

}
