CREATE TABLE outboxes (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          idempotency_key VARCHAR(255) UNIQUE NOT NULL,
                          status ENUM ('INIT', 'FAILURE', 'SUCCESS') DEFAULT 'INIT',
                          type VARCHAR(40),
                          partition_key INT DEFAULT 0,
                          payload JSON,
                          metadata JSON,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wallets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                         version INT NOT NULL DEFAULT 0,
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         UNIQUE (user_id)
);

CREATE TABLE wallet_transactions (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     wallet_id BIGINT NOT NULL,
                                     amount DECIMAL(15,2) NOT NULL,
                                     type ENUM('CREDIT', 'DEBIT') NOT NULL,
                                     reference_id BIGINT NOT NULL,
                                     reference_type VARCHAR(50) NOT NULL,
                                     order_id VARCHAR(255),
                                     idempotency_key VARCHAR(255) UNIQUE NOT NULL,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
#     FOREIGN KEY (wallet_id) REFERENCES wallets(id) 외래키 지정시 갭스락?
);

CREATE TABLE ledger_transaction
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    description     VARCHAR(100)        NOT NULL,
    reference_id    BIGINT              NOT NULL,
    reference_type  VARCHAR(50),
    order_id        VARCHAR(255),
    idempotency_key VARCHAR(255) UNIQUE NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL

);

CREATE TABLE ledger_entries
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(15, 2) NOT NULL,
    account_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    type ENUM('CREDIT', 'DEBIT') NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES ledger_transaction(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);


