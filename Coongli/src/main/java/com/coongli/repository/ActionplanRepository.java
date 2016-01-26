package com.coongli.repository;

import com.coongli.domain.Actionplan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Actionplan entity.
 */
public interface ActionplanRepository extends JpaRepository<Actionplan,Long> {

}
