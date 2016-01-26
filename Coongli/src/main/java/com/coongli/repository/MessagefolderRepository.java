package com.coongli.repository;

import com.coongli.domain.Messagefolder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Messagefolder entity.
 */
public interface MessagefolderRepository extends JpaRepository<Messagefolder,Long> {

}
