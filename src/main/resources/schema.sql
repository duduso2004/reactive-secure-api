CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS member (
    member_id SERIAL PRIMARY KEY,
    member_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    member_id INTEGER,
    order_name VARCHAR(255),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

INSERT INTO customer (first_name, last_name) VALUES ('JOSÉ', 'INOCÊNCIO');

INSERT INTO member (member_name) VALUES ('MEMBER 01');
INSERT INTO member (member_name) VALUES ('MEMBER 02');
INSERT INTO member (member_name) VALUES ('MEMBER 03');

INSERT INTO orders (member_id, order_name) VALUES (1, 'ORDER FOR MEMBER 01');
INSERT INTO orders (member_id, order_name) VALUES (2, 'ORDER FOR MEMBER 02');
INSERT INTO orders (member_id, order_name) VALUES (3, 'ORDER FOR MEMBER 03');