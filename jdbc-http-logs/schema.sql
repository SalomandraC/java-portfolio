DROP TABLE IF EXISTS http_logs;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    ip VARCHAR(45) NOT NULL UNIQUE
);

CREATE TABLE http_logs (
    id        SERIAL PRIMARY KEY,
    client_id INT          NOT NULL REFERENCES clients (id),
    ts        TIMESTAMP    NOT NULL,
    method    VARCHAR(10)  NOT NULL,
    path      VARCHAR(500) NOT NULL,
    status    INT          NOT NULL CHECK (status BETWEEN 100 AND 599),
    size      INT          NOT NULL
);

INSERT INTO clients (ip) VALUES
    ('192.168.0.1'),
    ('10.0.0.5');

INSERT INTO http_logs (client_id, ts, method, path, status, size) VALUES
    (1, '2026-09-01 08:00:00', 'GET',  '/index.html', 200, 5120),
    (1, '2026-09-01 08:05:00', 'GET',  '/missing',    404,  512),
    (2, '2026-09-01 08:10:00', 'POST', '/login',      500, 1024);
