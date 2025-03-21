CREATE SCHEMA IF NOT EXISTS `franchises`;

USE `franchises`;

CREATE TABLE IF NOT EXISTS `franchise` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `branch` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `franchise_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_branch_franchise` FOREIGN KEY (`franchise_id`) REFERENCES `franchise` (`id`) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS `product` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `product_branch` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `product_id` INT NOT NULL,
     `branch_id` INT NOT NULL,
     `stock` INT NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_product_branch_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
     CONSTRAINT `fk_product_branch_branch` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`id`) ON DELETE CASCADE
    );