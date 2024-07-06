DROP TABLE IF EXISTS `member_token`;
DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `provider` varchar(10),
  `auth_id` varchar(255),
  `name` varchar(25),
  `code` varchar(10) UNIQUE
);

CREATE UNIQUE INDEX provider_auth_id ON `member` (`provider`, `auth_id`);

CREATE TABLE `member_token` (
  `member_id` bigint PRIMARY KEY,
  `refresh_token` varchar(255)
);
