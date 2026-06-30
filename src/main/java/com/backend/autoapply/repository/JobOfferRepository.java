package com.backend.autoapply.repository;

import com.backend.autoapply.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    Optional<JobOffer> findByUrl(String url);

    List<JobOffer> findByAppliedFalseAndMatchesProfileTrue();

    List<JobOffer> findBySource(String source);

    @Query("SELECT j FROM JobOffer j WHERE j.createdAt >= :startDate")
    List<JobOffer> findByCreatedAtAfter(LocalDateTime startDate);

    @Query("SELECT j FROM JobOffer j WHERE j.applied = false AND j.matchesProfile = true ORDER BY j.createdAt DESC")
    List<JobOffer> findPendingApplications();
}
