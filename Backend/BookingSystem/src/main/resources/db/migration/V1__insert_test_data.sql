INSERT INTO users (id, user_type, email, password)
VALUES (1, 'EMPLOYEE', 'admin@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (2, 'EMPLOYEE', 'employee@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (3, 'EMPLOYEE', 'employee2@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (4, 'EMPLOYEE', 'employee3@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (5, 'EMPLOYEE', 'employee4@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');


INSERT INTO users (id, user_type, email, password)
VALUES (100, 'CLIENT', 'rclient1@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (101, 'CLIENT', 'rclient2@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (102, 'CLIENT', 'rclient3@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (103, 'CLIENT', 'rclient4@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (104, 'CLIENT', 'rclient5@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');

INSERT INTO users (id, user_type, email, password)
VALUES (105, 'CLIENT', 'rclient6@mail.com', '$2a$12$smugtmuAporcBU48i05crucP6vQPPDRBtRJakW/G6Rj4O/fRMoa4e');


INSERT INTO employees (id , first_name, last_name, rate, role, status)
VALUES (1, 'admin', 'admin', NULL, 'ADMIN', 'BUSY');

INSERT INTO employees (id , first_name, last_name, rate, role, status)
VALUES (2, 'employee', 'employee', 30, 'EMPLOYEE', 'ACTIVE');

INSERT INTO employees (id , first_name, last_name, rate, role, status)
VALUES (3, 'employee_two', 'employee_two', 40, 'EMPLOYEE', 'ACTIVE');

INSERT INTO employees (id , first_name, last_name, rate, role, status)
VALUES (4, 'employee_three', 'employee_three', 50, 'EMPLOYEE', 'ACTIVE');

INSERT INTO employees (id , first_name, last_name, rate, role, status)
VALUES (5, 'employee_four', 'employee_four', 60, 'EMPLOYEE', 'ACTIVE');



INSERT INTO clients (id, name, phone, address, type)
VALUES (100, 'client one', '123456789', 'client_address', 'REGISTERED');

INSERT INTO clients (id, name, phone, address, type)
VALUES (101, 'client two', '123456789', 'client_address', 'REGISTERED');

INSERT INTO clients (id, name, phone, address, type)
VALUES (102, 'client three', '123456789', 'client_address', 'REGISTERED');

INSERT INTO clients (id, name, phone, address, type)
VALUES (103, 'client four', '123456789', 'client_address', 'REGISTERED');

INSERT INTO clients (id, name, phone, address, type)
VALUES (104, 'client five', '123456789', 'client_address', 'REGISTERED');

INSERT INTO clients (id, name, phone, address, type)
VALUES (105, 'client six', '123456789', 'client_address', 'REGISTERED');


INSERT INTO languages (id, language_name)
VALUES (1, 'ENGLISH');

INSERT INTO languages (id, language_name)
VALUES (2, 'SPANISH');

INSERT INTO languages (id, language_name)
VALUES (3, 'GERMAN');

INSERT INTO languages (id, language_name)
VALUES (4, 'FRENCH');

INSERT INTO languages (id, language_name)
VALUES (5, 'ARABIC');

INSERT INTO employee_languages (employee_id, language_id)
VALUES (2, 1);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (2, 2);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (3, 3);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (3, 1);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (4, 4);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (4, 3);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (5, 1);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (5, 3);

INSERT INTO employee_languages (employee_id, language_id)
VALUES (5, 5);

INSERT INTO job_requests (id, client_id, employee_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, actual_duration_in_minutes, price, start_time, is_deleted, is_active)
VALUES (1, 100, 2, 'IN_PERSON', 'COMPLETED', 1, 2, '2025-04-20', '15:00:00', 60, 'client_address', '2025-05-01T12:00:00', 60, 30, '15:00:00', false, false);

INSERT INTO job_requests (id, client_id, employee_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, actual_duration_in_minutes, price, start_time, is_deleted, is_active)
VALUES (2, 100, 3, 'IN_PERSON', 'COMPLETED', 1, 3, '2025-04-20', '16:00:00', 60, 'client_address', '2025-05-01T12:00:00', 60, 40, '16:00:00', false, false);

INSERT INTO job_requests (id, client_id, employee_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, actual_duration_in_minutes, price, start_time, is_deleted, is_active)
VALUES (3, 101, 4, 'IN_PERSON', 'COMPLETED', 4, 3, '2025-04-20', '17:00:00', 60, 'client_address', '2025-05-01T12:00:00', 60, 50, '17:00:00', false, false);

INSERT INTO job_requests (id, client_id, employee_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, actual_duration_in_minutes, price, start_time, is_deleted, is_active)
VALUES (4, 102, 5, 'IN_PERSON', 'COMPLETED', 1, 5, '2025-04-21', '18:00:00', 60, 'client_address', '2025-05-01T12:00:00', 60, 60, '18:00:00', false, false);

INSERT INTO job_requests (id, client_id, employee_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, actual_duration_in_minutes, price, start_time, is_deleted, is_active)
VALUES (5, 100, 5, 'IN_PERSON', 'COMPLETED', 1, 3, '2025-04-21', '17:50:00', 60, 'client_address', '2025-05-01T12:00:00', 60, 60, '17:50:00', false, false);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (6, 105, 'VIDEO_CALL', 'PENDING', 1, 5, '2025-05-20', '08:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (10, 105, 'VIDEO_CALL', 'PENDING', 3, 5, '2025-05-21', '09:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (12, 105, 'PHONE_CALL', 'PENDING', 1, 2, '2025-05-20', '10:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (7, 103, 'PHONE_CALL', 'PENDING', 1, 3, '2025-05-10', '08:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (8, 104, 'PHONE_CALL', 'PENDING', 4, 3, '2025-06-1', '08:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (9, 102, 'VIDEO_CALL', 'PENDING', 3, 4, '2025-06-23', '08:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (11, 101, 'VIDEO_CALL', 'PENDING', 1, 5, '2025-05-22', '08:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);

INSERT INTO job_requests (id, client_id, service_type, status, source_language_id, target_language_id, service_date, service_time, planned_duration_in_minutes, address, created_at, is_deleted, is_active)
VALUES (13, 100, 'VIDEO_CALL', 'PENDING', 3, 4, '2025-05-21', '08:00:00', 60, 'client_address', '2025-05-01T12:00:00', false, true);
