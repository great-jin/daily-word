CREATE TABLE `auth_user`
(
    `id`                      varchar(36) NOT NULL,
    `user_name`               varchar(100) DEFAULT NULL,
    `password`                varchar(100) DEFAULT NULL,
    `role`                    varchar(100) DEFAULT NULL,
    `authority`               varchar(100) DEFAULT NULL,
    `account_non_expired`     tinyint(1) unsigned NOT NULL DEFAULT '0',
    `account_non_locked`      tinyint(1) unsigned NOT NULL DEFAULT '0',
    `credentials_non_expired` tinyint(1) unsigned NOT NULL DEFAULT '0',
    `is_enabled`              tinyint(1) unsigned NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf32;