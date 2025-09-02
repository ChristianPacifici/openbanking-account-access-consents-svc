DROP TABLE IF EXISTS account_access_consents;

CREATE TABLE account_access_consents (
    consent_id VARCHAR(255) PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    creation_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status_update_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    expiration_date_time TIMESTAMP WITH TIME ZONE,
    permissions TEXT NOT NULL,
    request_body TEXT NOT NULL
);
