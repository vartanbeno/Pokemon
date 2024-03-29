CREATE TABLE IF NOT EXISTS User (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	username VARCHAR(64) NOT NULL,
	password VARCHAR(64) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Deck (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	player BIGINT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Challenge (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	challenger BIGINT NOT NULL,
	challengee BIGINT NOT NULL,
	status INT NOT NULL,
	challenger_deck BIGINT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Card (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	deck BIGINT NOT NULL,
	type VARCHAR(1) NOT NULL,
	name VARCHAR(30) NOT NULL,
	basic VARCHAR(30) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Game (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	challenger BIGINT NOT NULL,
	challengee BIGINT NOT NULL,
	challenger_deck BIGINT NOT NULL,
	challengee_deck BIGINT NOT NULL,
	current_turn BIGINT NOT NULL,
	turn BIGINT NOT NULL,
	status INT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Hand (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	game BIGINT NOT NULL,
	player BIGINT NOT NULL,
	deck BIGINT NOT NULL,
	card BIGINT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Discard (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	game BIGINT NOT NULL,
	player BIGINT NOT NULL,
	deck BIGINT NOT NULL,
	card BIGINT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Bench (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	game BIGINT NOT NULL,
	player BIGINT NOT NULL,
	deck BIGINT NOT NULL,
	card BIGINT NOT NULL,
	predecessor BIGINT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS AttachedEnergy (
	id BIGINT NOT NULL,
	version BIGINT NOT NULL,
	game BIGINT NOT NULL,
	game_turn BIGINT NOT NULL,
	player BIGINT NOT NULL,
	energy_card BIGINT NOT NULL,
	pokemon_card BIGINT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;
