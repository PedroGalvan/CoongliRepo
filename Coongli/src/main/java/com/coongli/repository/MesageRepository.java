package com.coongli.repository;

import com.coongli.domain.Mesage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mesage entity.
 */
public interface MesageRepository extends JpaRepository<Mesage,Long> {

}
