package com.backend.autoapply.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// represents a historical email sending

@Entity
@Table(name = "application_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(length = 1000)
    private String errorMessage;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();

    public enum ApplicationStatus {
        PENDING,
        SENT,
        FAILED,
        RETRYING
    }
}
