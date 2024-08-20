DELIMITER $$

CREATE TRIGGER check_balance_after_insert
    AFTER INSERT ON ledger_entries
    FOR EACH ROW
BEGIN
    DECLARE credit_sum DECIMAL(15,2);
    DECLARE debit_sum DECIMAL(15,2);

    SELECT SUM(amount) INTO credit_sum
    FROM ledger_entries
    WHERE transaction_id = NEW.transaction_id AND TYPE = 'CREDIT';

    SELECT SUM(amount) INTO debit_sum
    FROM ledger_entries
    WHERE transaction_id = NEW.transaction_id AND TYPE = 'DEBIT';

    IF NOT (credit_sum - debit_sum = 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The sum of CREDIT and DEBIT amounts does not balance to zero.';
    end if;
END $$

DELIMITER ;


INSERT INTO ledger_transactions (description, reference_id, reference_type, order_id, idempotency_key)
VALUES ('Test Transaction', 1, 'Test Type', 'test_order_id_1', 'test_idempotency_key_1');

INSERT INTO ledger_entries (amount, account_id, transaction_id, type)
VALUES (100.00, 1, 1, 'CREDIT'), (100.00, 2, 1, 'DEBIT');