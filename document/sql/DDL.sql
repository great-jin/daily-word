-- daily_word.auth_user definition

CREATE TABLE `auth_user`
(
    `id`                      int(11) NOT NULL AUTO_INCREMENT,
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


-- daily_word.invite_code definition

CREATE TABLE `invite_code`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `user_id`       int(11) DEFAULT NULL,
    `code`          varchar(100) DEFAULT NULL,
    `generate_date` date         DEFAULT NULL,
    `expire_date`   date         DEFAULT NULL,
    `status`        int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- daily_word.match_detail definition

CREATE TABLE `match_detail`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `rank_mode`   varchar(100) DEFAULT NULL,
    `room_number` varchar(100) DEFAULT NULL,
    `rank_type`   int(11) DEFAULT NULL,
    `catalog`     varchar(50)  DEFAULT NULL,
    `word_count`  varchar(100) DEFAULT NULL,
    `word_indies` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- daily_word.match_record definition

CREATE TABLE `match_record`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `user_id`       int(11) DEFAULT NULL,
    `season`        int(11) DEFAULT NULL,
    `match_id`      int(11) NOT NULL,
    `correct_count` int(11) DEFAULT NULL,
    `cost_second`   int(11) DEFAULT NULL,
    `score`         int(11) DEFAULT NULL,
    `finished`      tinyint(1) DEFAULT '0',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- daily_word.rank_board definition

CREATE TABLE `rank_board`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `user_id`     int(11) DEFAULT NULL,
    `user_name`   varchar(100) DEFAULT NULL,
    `season`      int(11) DEFAULT NULL,
    `catalog`     varchar(100) DEFAULT NULL,
    `score`       int(11) DEFAULT NULL,
    `match_count` int(11) DEFAULT NULL,
    `match_win`   int(11) DEFAULT NULL,
    `match_lost`  int(11) DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB EFAULT CHARSET=utf8mb4;


-- daily_word.user_detail definition

CREATE TABLE `user_detail`
(
    `user_id`          int(11) NOT NULL,
    `user_name`        varchar(100) DEFAULT NULL,
    `avatar`           varchar(100) DEFAULT NULL,
    `email`            varchar(100) DEFAULT NULL,
    `invite_code`      varchar(100) DEFAULT NULL,
    `register_time`    datetime     DEFAULT CURRENT_TIMESTAMP,
    `last_online_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- daily_word.user_friend definition

CREATE TABLE `user_friend`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `user_id`     int(11) DEFAULT NULL,
    `friend_id`   int(11) DEFAULT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- daily_word.user_invitation definition

CREATE TABLE `user_invitation`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `from_user`      int(11) DEFAULT NULL,
    `target_user`    int(11) DEFAULT NULL,
    `process_status` int(11) DEFAULT NULL,
    `create_time`    datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;