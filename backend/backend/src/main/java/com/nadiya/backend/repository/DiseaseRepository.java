package com.nadiya.backend.repository;

import com.nadiya.backend.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {

}