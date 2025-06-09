CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` enum('ROLE_USER',
'ROLE_ADMIN') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


OR

CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


INSERT INTO crmDBtest.roles (id, name) VALUES(1, 'ROLE_ADMIN');
INSERT INTO crmDBtest.roles (id, name) VALUES(2, 'ROLE_USER');
INSERT INTO crmDBtest.roles (id, name) VALUES(3, 'ROLE_CUSTOMER');
INSERT INTO crmDBtest.roles (id, name) VALUES(3, 'ROLE_CUSTOMER');
INSERT INTO crmDBtest.roles (id, name) VALUES(4, 'ROLE_CUSTOMER_DELETE');
INSERT INTO crmDBtest.roles (id, name) VALUES(5, 'ROLE_CUSTOMER_ADD');
INSERT INTO crmDBtest.roles (id, name) VALUES(6, 'ROLE_CUSTOMER_EDIT');

INSERT INTO crmDBtest.user_roles(user_id, role_id) VALUES(1, 1); ADMIN
INSERT INTO crmDBtest.user_roles(user_id, role_id) VALUES(1, 2); USER
INSERT INTO crmDBtest.user_roles(user_id, role_id) VALUES(1, 3); CUSTOMER
INSERT INTO crmDBtest.user_roles(user_id, role_id) VALUES(1, 6); CUSTOMER_EDIT
--------------------------------------------------------------------
-- crmDBtest.user_roles definition
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,
`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

