package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Mesage;


@Repository
public interface MesageRepository extends JpaRepository<Mesage, Integer> {
	
	@Query("select m from Mesage m where m.messageFolder.id=?1 order by m.sentMoment desc")
	Collection<Mesage> findMesageByMFOrder(int mesageFolderId);
	
}
