CREATE TABLE drone (
  id BIGINT NOT NULL AUTO_INCREMENT,
  serial_number VARCHAR(100) UNIQUE NOT NULL ,
  model VARCHAR(255),
  weight_limit DOUBLE PRECISION,
  battery_capacity INTEGER,
  state VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE medication (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(255),
  name VARCHAR(100) NOT NULL,
  weight DOUBLE PRECISION,
  image BLOB,
  drone_id BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (drone_id) REFERENCES drone (id)
);

CREATE TABLE delivery (
  id BIGINT NOT NULL AUTO_INCREMENT,
  drone_id VARCHAR(100),
  medication_id VARCHAR(255),
  status VARCHAR(255),
  delivery_date TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (drone_id) REFERENCES drone (id),
  FOREIGN KEY (medication_id) REFERENCES medication (id)
);

CREATE TABLE drone_history (
  id BIGINT NOT NULL AUTO_INCREMENT,
  drone_id BIGINT NOT NULL,
  battery_capacity INTEGER NOT NULL,
  state VARCHAR(255) NOT NULL,
  battery_state VARCHAR(255),
  timestamp TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (drone_id) REFERENCES drone (id)
);
