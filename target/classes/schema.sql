CREATE TABLE supplier (
                          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          name VARCHAR(255),
                          start_date DATE
);