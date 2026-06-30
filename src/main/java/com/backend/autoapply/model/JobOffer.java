package com.backend.autoapply.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// represent job offer from a website

@Entity
@Table(name = "job_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double salaryMin;

    @Column(nullable = false)
    private Double salaryMax;

    @Column(nullable = false)
    @Builder.Default
    private Boolean applied = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean matchesProfile = true;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
