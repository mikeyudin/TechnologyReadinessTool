SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';



-- -----------------------------------------------------
-- Table `snapshot_window`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `snapshot_window` (
  `snapshot_window_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(100) NOT NULL ,
  `visible` TINYINT(1) NULL ,
  `request_user` VARCHAR(100) NULL ,
  `request_date` DATETIME NULL ,
  `execute_date` DATETIME NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`snapshot_window_id`) )
ENGINE = InnoDB;

CREATE UNIQUE INDEX `snapshotwindow_name_scopeid_uc` ON `snapshot_window` (`name` ASC, `scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `snapshot_summary`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `snapshot_summary` (
  `snapshot_summary_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `snapshot_window_id` INT UNSIGNED NOT NULL ,
  `org_id` INT UNSIGNED NOT NULL ,
  `create_date` DATETIME NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`snapshot_summary_id`) ,
  CONSTRAINT `snapshotsummary_snapshotwindow_fk`
    FOREIGN KEY (`snapshot_window_id` )
    REFERENCES `snapshot_window` (`snapshot_window_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `snapshotsummary_snapshotwindow_fki` ON `snapshot_summary` (`snapshot_window_id` ASC) ;

CREATE UNIQUE INDEX `snapshotsummary_snapshotwindowid_orgid_uc` ON `snapshot_summary` (`snapshot_window_id` ASC, `org_id` ASC) ;


-- -----------------------------------------------------
-- Table `snapshot_summary_value`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `snapshot_summary_value` (
  `snapshot_summary_value_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `snapshot_summary_id` INT UNSIGNED NOT NULL ,
  `type` VARCHAR(100) NOT NULL ,
  `value` VARCHAR(100) NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`snapshot_summary_value_id`) ,
  CONSTRAINT `snapshotsummaryvalue_snapshotsummary_fk`
    FOREIGN KEY (`snapshot_summary_id` )
    REFERENCES `snapshot_summary` (`snapshot_summary_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `snapshotsummaryvalue_snapshotsummary_fki` ON `snapshot_summary_value` (`snapshot_summary_id` ASC) ;

CREATE UNIQUE INDEX `snapshotsummaryvalue_snapshotsummaryid_type_uc` ON `snapshot_summary_value` (`snapshot_summary_id` ASC, `type` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
