INSERT INTO USERS(id, username, password) VALUES(1, 'user', '$2a$04$N7JXgEmKL2gncKe.8EcH/eCxgE061PBmAP9b/SlXNsmsXvR8Pju8u');
INSERT INTO ROLES(id, name) VALUES(1, 'ROLE_USER');
INSERT INTO USER_ROLES(user_id, role_id) VALUES(1, 1);
