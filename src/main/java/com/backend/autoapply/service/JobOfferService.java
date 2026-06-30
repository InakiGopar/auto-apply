package com.backend.autoapply.service;

import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final EmailService emailService;


    @Transactional
    public JobOffer saveJobOffer(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }

    @Transactional
    public JobOffer createIfNotExists(JobOffer jobOffer) {
        return jobOfferRepository.findByUrl(jobOffer.getUrl())
                .orElseGet(() -> jobOfferRepository.save(jobOffer));
    }

    @Transactional
    public void markAsApplied(Long jobOfferId) {
        jobOfferRepository.findById(jobOfferId).ifPresent(jobOffer -> {
            jobOffer.setApplied(true);
            jobOfferRepository.save(jobOffer);
            log.info("Job offer marked as applied: {}", jobOffer.getTitle());
        });
    }

    @Transactional
    public void applyToJobOffer(Long jobOfferId, String recipientEmail) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));

        emailService.sendApplicationEmail(jobOffer, recipientEmail);
        markAsApplied(jobOfferId);
    }

    @Transactional
    public void deleteJobOffer(Long id) {
        jobOfferRepository.deleteById(id);
    }


    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public Optional<JobOffer> getJobOfferById(Long id) {
        return jobOfferRepository.findById(id);
    }

    public Optional<JobOffer> getJobOfferByUrl(String url) {
        return jobOfferRepository.findByUrl(url);
    }

    public List<JobOffer> getPendingApplications() {
        return jobOfferRepository.findPendingApplications();
    }
}
