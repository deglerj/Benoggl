CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_uid VARCHAR(38) NOT NULL,
    data CLOB NOT NULL
);