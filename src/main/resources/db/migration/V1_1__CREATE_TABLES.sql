CREATE SCHEMA IF NOT EXISTS se;

CREATE TABLE se.ACCOUNTS
(
    ACCOUNT_ID             NUMERIC(16)                     NOT NULL,
    NAME                   character varying(255 CHAR)     NOT NULL,
    EMAIL                  character varying(255 CHAR)     NOT NULL,
    BALANCE                NUMERIC(10, 2)                  NOT NULL default 200.00
);

CREATE TABLE se.TRANSACTIONS
(
    TRANSACTION_ID             NUMERIC(16)  NOT NULL,
    FROM_ACCOUNT_ID            NUMERIC(16)  NOT NULL,
    TO_ACCOUNT_ID              NUMERIC(16)  NOT NULL,
    AMOUNT                     NUMERIC(10, 2) NOT NULL,
    LAST_UPDATE_TIME           timestamp(0)   NOT NULL
);

CREATE SEQUENCE se.PK_GENERATOR
    START WITH 10001
    INCREMENT BY 1
    MAXVALUE 1019999999999999
    MINVALUE 10001;