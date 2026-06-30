-- Create job_offers table
CREATE TABLE IF NOT EXISTS job_offers (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    company VARCHAR NOT NULL,
    location VARCHAR NOT NULL,
    url VARCHAR NOT NULL UNIQUE,
    source VARCHAR NOT NULL,
    description TEXT NOT NULL,
    salary_min DOUBLE PRECISION NOT NULL,
    salary_max DOUBLE PRECISION NOT NULL,
    applied BOOLEAN NOT NULL DEFAULT FALSE,
    matches_profile BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create scraping_configs table
CREATE TABLE IF NOT EXISTS scraping_configs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE,
    base_url VARCHAR NOT NULL,
    search_path VARCHAR NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    cron_hours INTEGER NOT NULL DEFAULT 24,
    last_run TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create application_logs table
CREATE TABLE IF NOT EXISTS application_logs (
    id BIGSERIAL PRIMARY KEY,
    job_offer_id BIGINT NOT NULL,
    recipient_email VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    error_message VARCHAR(1000),
    sent_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_application_logs_job_offer
        FOREIGN KEY (job_offer_id)
        REFERENCES job_offers(id)
        ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_job_offers_url ON job_offers(url);
CREATE INDEX IF NOT EXISTS idx_job_offers_applied ON job_offers(applied);
CREATE INDEX IF NOT EXISTS idx_job_offers_matches_profile ON job_offers(matches_profile);
CREATE INDEX IF NOT EXISTS idx_job_offers_source ON job_offers(source);
CREATE INDEX IF NOT EXISTS idx_job_offers_created_at ON job_offers(created_at);

CREATE INDEX IF NOT EXISTS idx_scraping_configs_active ON scraping_configs(active);
CREATE INDEX IF NOT EXISTS idx_scraping_configs_name ON scraping_configs(name);

CREATE INDEX IF NOT EXISTS idx_application_logs_job_offer_id ON application_logs(job_offer_id);
CREATE INDEX IF NOT EXISTS idx_application_logs_status ON application_logs(status);
CREATE INDEX IF NOT EXISTS idx_application_logs_sent_at ON application_logs(sent_at);
