CREATE KEYSPACE IF NOT EXISTS lineServer WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE lineServer;

CREATE TABLE line (
	line_index int PRIMARY KEY,
	line_text text
);