package com.coongli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Messagefolder;


@Repository
public interface MessagefolderRepository extends JpaRepository<Messagefolder, Integer > {

}
