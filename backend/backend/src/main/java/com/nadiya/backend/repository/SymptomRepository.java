package com.nadiya.backend.repository;

import com.nadiya.backend.entity.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {

    List<Symptom> findByNameContainingIgnoreCase(String keyword);

}