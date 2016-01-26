package com.coongli.repository;

import com.coongli.domain.Resourcecategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Resourcecategory entity.
 */
public interface ResourcecategoryRepository extends JpaRepository<Resourcecategory,Long> {

    @Query("select distinct resourcecategory from Resourcecategory resourcecategory left join fetch resourcecategory.userss")
    List<Resourcecategory> findAllWithEagerRelationships();

    @Query("select resourcecategory from Resourcecategory resourcecategory left join fetch resourcecategory.userss where resourcecategory.id =:id")
    Resourcecategory findOneWithEagerRelationships(@Param("id") Long id);

}
