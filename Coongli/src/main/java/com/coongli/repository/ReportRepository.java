package com.coongli.repository;

import com.coongli.domain.Report;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Report entity.
 */
public interface ReportRepository extends JpaRepository<Report,Long> {

}
