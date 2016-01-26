package com.coongli.repository;

import com.coongli.domain.Actor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Actor entity.
 */
public interface ActorRepository extends JpaRepository<Actor,Long> {

}
