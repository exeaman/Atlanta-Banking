-- ==========================================================
-- Atlanta Banking - Initial Database Setup
-- ==========================================================

CREATE DATABASE identity_db;
CREATE DATABASE customer_db;
CREATE DATABASE accounts_db;
CREATE DATABASE ledger_db;
CREATE DATABASE payments_db;
CREATE DATABASE statement_db;
CREATE DATABASE notification_db;
CREATE DATABASE audit_db;

       -- ==========================================================
-- Atlanta Banking - Schema Initialization
-- ==========================================================

\connect identity_db;
CREATE SCHEMA IF NOT EXISTS identity;

\connect customer_db;
CREATE SCHEMA IF NOT EXISTS customer;

\connect accounts_db;
CREATE SCHEMA IF NOT EXISTS accounts;

\connect ledger_db;
CREATE SCHEMA IF NOT EXISTS ledger;

\connect payments_db;
CREATE SCHEMA IF NOT EXISTS payments;

\connect statement_db;
CREATE SCHEMA IF NOT EXISTS statement;

\connect notification_db;
CREATE SCHEMA IF NOT EXISTS notification;

\connect audit_db;
CREATE SCHEMA IF NOT EXISTS audit;