package com.backend.autoapply.repository;

import com.backend.autoapply.model.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationLogRepository extends JpaRepository<ApplicationLog, Long> {

    List<ApplicationLog> findByJobOfferId(Long jobOfferId);

    List<ApplicationLog> findByStatus(ApplicationLog.ApplicationStatus status);
}
