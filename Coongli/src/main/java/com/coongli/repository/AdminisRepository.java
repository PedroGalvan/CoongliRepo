package com.coongli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Adminis;


@Repository
public interface AdminisRepository extends JpaRepository<Adminis, Integer>{

	@Query("select a from Adminis a where a.userAccount.id=?1")
	Adminis findOneByPrincipal(int id);
}