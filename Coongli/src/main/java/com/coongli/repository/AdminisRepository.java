package com.coongli.repository;

import com.coongli.domain.Adminis;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Adminis entity.
 */
public interface AdminisRepository extends JpaRepository<Adminis,Long> {

}
