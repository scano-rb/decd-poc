-- Users schema

-- !Ups

DROP TABLE IF EXISTS "user";

CREATE TABLE IF NOT EXISTS "user" (
                                      id BIGSERIAL,
                                      username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
    );

DROP TABLE IF EXISTS "transaction";

CREATE TABLE IF NOT EXISTS "transaction" (
                                             id BIGSERIAL,
                                             amount NUMERIC(22,0) NOT NULL,
    card_last_digits VARCHAR(255) NOT NULL,
    date_time VARCHAR(255) NOT NULL,
    installments SMALLINT NOT NULL,
    card_type VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    status  VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
    );