package com.backend.autoapply.repository;

import com.backend.autoapply.model.ScrapingConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapingConfigRepository extends JpaRepository<ScrapingConfig, Long> {

    Optional<ScrapingConfig> findByName(String name);

    List<ScrapingConfig> findByActiveTrue();
}
