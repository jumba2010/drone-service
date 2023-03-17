-- Insert 10 drones
INSERT INTO drone (id, serial_number, model, weight_limit, battery_capacity, state)
VALUES
  (100, 'SN001', 'LIGHTWEIGHT', 300, 100, 'IDLE'),
  (101, 'SN002', 'MIDDLEWEIGHT', 400, 22, 'IDLE'),
  (102, 'SN003', 'CRUISERWEIGHT', 450, 25, 'LOADING'),
  (103, 'SN004', 'HEAVYWEIGHT', 500, 40, 'DELIVERING'),
  (104, 'SN005', 'LIGHTWEIGHT', 350, 20, 'DELIVERED'),
  (105, 'SN006', 'MIDDLEWEIGHT', 450, 35, 'RETURNING'),
  (106, 'SN007', 'CRUISERWEIGHT', 400, 30, 'RETURNING'),
  (107, 'SN008', 'HEAVYWEIGHT', 500, 15, 'IDLE'),
  (108, 'SN009', 'LIGHTWEIGHT', 250, 10, 'IDLE'),
  (109, 'SN010', 'CRUISERWEIGHT', 400, 35, 'LOADING');

CREATE SEQUENCE medication_seq START WITH 1;
INSERT INTO MEDICATION (ID, CODE, NAME, WEIGHT)
SELECT
NEXTVAL('medication_seq'),
CONCAT('CODE', NEXTVAL('medication_seq')),
CONCAT('Medication ', NEXTVAL('medication_seq')),
FLOOR(RAND() * 100) + 1
FROM SYSTEM_RANGE(1, 50);
