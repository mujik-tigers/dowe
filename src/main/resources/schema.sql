DROP TABLE IF EXISTS `profile`;
DROP TABLE IF EXISTS `team`;
DROP TABLE IF EXISTS `member_token`;
DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `provider` varchar(10),
  `auth_id` varchar(255),
  `name` varchar(25),
  `code` varchar(10)
);

CREATE UNIQUE INDEX provider_auth_id ON `member` (`provider`, `auth_id`);

CREATE TABLE `member_token` (
  `member_id` bigint PRIMARY KEY,
  `refresh_token` varchar(255)
);

CREATE TABLE `team` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(20),
  `description` varchar(30),
  `image` varchar(255),
  `manager_id` bigint
);

CREATE TABLE `profile` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `team_id` bigint,
  `member_id` bigint,
  `image` varchar(255),
  `nickname` varchar(30),
  `description` varchar(30)
);
