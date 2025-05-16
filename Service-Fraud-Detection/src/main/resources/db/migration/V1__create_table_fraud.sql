CREATE TABLE frauds (
                                  id SERIAL PRIMARY KEY,
                                  user_id INTEGER,
                                  transaction_id INTEGER,
                                  transactional_code VARCHAR(255),
                                  is_fraud BOOLEAN,
                                  reason VARCHAR(255),
                                  checked_at BIGINT,
                                  total_price DOUBLE PRECISION,
                                  channel VARCHAR(255),
                                  user_ip VARCHAR(255),
                                  device_name VARCHAR(255),
                                  location VARCHAR(255),
                                  transaction_created BIGINT
);
