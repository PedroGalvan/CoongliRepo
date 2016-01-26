package com.coongli.repository;

import com.coongli.domain.Textable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Textable entity.
 */
public interface TextableRepository extends JpaRepository<Textable,Long> {

}
