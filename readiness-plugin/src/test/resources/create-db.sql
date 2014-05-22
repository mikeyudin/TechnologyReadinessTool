SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `core` DEFAULT CHARACTER SET utf8 ;
CREATE SCHEMA IF NOT EXISTS `core_batch` DEFAULT CHARACTER SET utf8 ;
USE `core` ;

-- -----------------------------------------------------
-- Table `scope_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scope_type` ;

CREATE  TABLE IF NOT EXISTS `scope_type` (
  `scope_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `parent_scope_type_id` INT UNSIGNED NULL DEFAULT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier (within a hierarchy type) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name (within a hierarchy type) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `allow_student` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Are scopes of this type allowed to own students?' ,
  `allow_student_reg` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Can a student be registered for scopes of this type.' ,
  `allow_student_enroll` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Can students be enrolled in organizations for this type of scope.' ,
  `allow_org` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Are scopes of this type allowed to own organizations?' ,
  `allow_org_part` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Can organizations participate in scopes of this type?' ,
  `allow_group` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Are scopes of this type allowed to own groups?' ,
  `allow_staff` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Are scopes of this type allowed to own staff?' ,
  `allow_user` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Can users be associated to scopes of this type?' ,
  `description` VARCHAR(1000) NULL DEFAULT NULL COMMENT 'Detailed description of the scope type to be displayed as context sensitive help to the user.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`scope_type_id`) ,
  CONSTRAINT `scopetype_self_fk`
    FOREIGN KEY (`parent_scope_type_id` )
    REFERENCES `scope_type` (`scope_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 14;

CREATE UNIQUE INDEX `scopetype_code_parentscopetypeid_uc` ON `scope_type` (`code` ASC, `parent_scope_type_id` ASC) ;

CREATE UNIQUE INDEX `scopetype_name_parentscopetypeid_uc` ON `scope_type` (`name` ASC, `parent_scope_type_id` ASC) ;

CREATE INDEX `scopetype_self_fki` ON `scope_type` (`parent_scope_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `scope`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scope` ;

CREATE  TABLE IF NOT EXISTS `scope` (
  `scope_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `parent_scope_id` INT UNSIGNED NULL DEFAULT NULL ,
  `scope_type_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier (within parent scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique identifier (within parent scope) which is displayed to the users.' ,
  `description` VARCHAR(1000) NULL DEFAULT NULL COMMENT 'Optional description of the scope.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`scope_id`) ,
  CONSTRAINT `scope_scopetype_fk`
    FOREIGN KEY (`scope_type_id` )
    REFERENCES `scope_type` (`scope_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `scope_self_fk`
    FOREIGN KEY (`parent_scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 37;

CREATE UNIQUE INDEX `scope_code_parentscopeid_uc` ON `scope` (`code` ASC, `parent_scope_id` ASC) ;

CREATE UNIQUE INDEX `scope_name_parentscopeid_uc` ON `scope` (`name` ASC, `parent_scope_id` ASC) ;

CREATE INDEX `scope_scopetype_fki` ON `scope` (`scope_type_id` ASC) ;

CREATE INDEX `scope_self_fki` ON `scope` (`parent_scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `org_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_type` ;

CREATE  TABLE IF NOT EXISTS `org_type` (
  `org_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `parent_org_type_id` INT UNSIGNED NULL DEFAULT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `allow_student` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Student can only be enrolled into organization which have \"true\" for the \"allow_student\" flag.' ,
  `allow_group` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Groups can only be created for organizations which have \"true\" for the \"allow_group\" flag.' ,
  `allow_device` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Devices can only be created/modified by orgs which have the allow_device flag.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`org_type_id`) ,
  CONSTRAINT `orgtype_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `orgtype_self_fk`
    FOREIGN KEY (`parent_org_type_id` )
    REFERENCES `org_type` (`org_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5;

CREATE UNIQUE INDEX `orgtype_code_scopeid_uc` ON `org_type` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `orgtype_name_parentorgtypeid_uc` ON `org_type` (`name` ASC, `parent_org_type_id` ASC) ;

CREATE INDEX `orgtype_scope_fki` ON `org_type` (`scope_id` ASC) ;

CREATE INDEX `orgtype_self_fki` ON `org_type` (`parent_org_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `org`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org` ;

CREATE  TABLE IF NOT EXISTS `org` (
  `org_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_type_id` INT UNSIGNED NOT NULL ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `parent_org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Name which is displayed to the user...for example \"Grant Wood Elementary School\". This descriptive name can change and should not be be used as a key.' ,
  `local_code` VARCHAR(50) NULL DEFAULT NULL COMMENT 'The customer provided identifier for this organization.  This field might be used by the customer to \"help\" identify an organization, however is not guaranteed to be unique.' ,
  `address_line1` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Address line #1' ,
  `address_line2` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Address line #2' ,
  `city` VARCHAR(50) NULL DEFAULT NULL COMMENT 'City' ,
  `state` VARCHAR(2) NULL DEFAULT NULL COMMENT 'State' ,
  `zip` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Zip code' ,
  `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Phone number' ,
  `phone_extension` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Extension' ,
  `fax` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Fax number' ,
  `inactive` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Indicates that the organization should no longer be active for this scope and is therefore not relevant to any future tasks.  Similar to a logical delete.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`org_id`) ,
  CONSTRAINT `org_orgtype_fk`
    FOREIGN KEY (`org_type_id` )
    REFERENCES `org_type` (`org_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `org_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `org_self_fk`
    FOREIGN KEY (`parent_org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 13;

CREATE UNIQUE INDEX `org_code_scopeid_uc` ON `org` (`code` ASC, `scope_id` ASC) ;

CREATE INDEX `org_orgtype_fki` ON `org` (`org_type_id` ASC) ;

CREATE INDEX `org_scope_fki` ON `org` (`scope_id` ASC) ;

CREATE INDEX `org_self_fki` ON `org` (`parent_org_id` ASC) ;


-- -----------------------------------------------------
-- Table `entity_data_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entity_data_type` ;

CREATE  TABLE IF NOT EXISTS `entity_data_type` (
  `entity_data_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`entity_data_type_id`) )
ENGINE = InnoDB;

CREATE UNIQUE INDEX `entitydatatype_code_uc` ON `entity_data_type` (`code` ASC) ;

CREATE UNIQUE INDEX `entitydatatype_name_uc` ON `entity_data_type` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `entity_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entity_type` ;

CREATE  TABLE IF NOT EXISTS `entity_type` (
  `entity_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `java_class` VARCHAR(200) NOT NULL COMMENT 'The java class which represents the entity within the core application.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`entity_type_id`) )
ENGINE = InnoDB;

CREATE UNIQUE INDEX `entitytype_code_uc` ON `entity_type` (`code` ASC) ;

CREATE UNIQUE INDEX `entitytype_name_uc` ON `entity_type` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `entity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entity` ;

CREATE  TABLE IF NOT EXISTS `entity` (
  `entity_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `entity_type_id` INT UNSIGNED NOT NULL ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`entity_id`) ,
  CONSTRAINT `entity_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `entity_entitytype_fk`
    FOREIGN KEY (`entity_type_id` )
    REFERENCES `entity_type` (`entity_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Used to define the scope specific attributes for a specific ' /* comment truncated */;

CREATE UNIQUE INDEX `entity_entitytypeid_scopeid_uc` ON `entity` (`entity_type_id` ASC, `scope_id` ASC) ;

CREATE INDEX `entity_scope_fki` ON `entity` (`scope_id` ASC) ;

CREATE INDEX `entity_entitytype_fki` ON `entity` (`entity_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `option_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `option_list` ;

CREATE  TABLE IF NOT EXISTS `option_list` (
  `option_list_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `shared` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'true: Can be used by multiple entitites.\nfalse: Only allowed to be used by a single entity' ,
  `sql_text` VARCHAR(10000) NULL DEFAULT NULL COMMENT 'This can be used if the list of values is not static and needs to be derived from live data.  The SQL should return exactly two columns: name, value.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`option_list_id`) ,
  CONSTRAINT `optionlist_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `optionlist_code_scopeid_uc` ON `option_list` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `optionlist_name_scopeid_uc` ON `option_list` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `optionlist_scope_fki` ON `option_list` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `entity_field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entity_field` ;

CREATE  TABLE IF NOT EXISTS `entity_field` (
  `entity_field_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `entity_id` INT UNSIGNED NOT NULL ,
  `entity_data_type_id` INT UNSIGNED NOT NULL ,
  `option_list_id` INT UNSIGNED NULL DEFAULT NULL ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.  ' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `description` VARCHAR(1000) NULL DEFAULT NULL COMMENT 'A longer description of the field which is displayed to the user as context sensitive help.' ,
  `environment_specific` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'ONLY USED FOR THE SCOPE ENTITY TYPE - indicates that this field is environment specific and should not be exported/imported from one environment to another.' ,
  `disabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Similar to a logical delete: used to \"remove\" a field without physically deleting it.  This is needed because the extension key/value pair rows have a FK to this table.  A disabled field will not be displayed on any views and will not be validated.' ,
  `required` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'The field must be provided by the user of the web site and included in any batch loads.' ,
  `regex` VARCHAR(2000) NULL DEFAULT NULL COMMENT 'Regular expression that is used to validate the fields data.  For example \"^[a-z]*$\" would be used to enforce that only lower case alpha characters are provided.' ,
  `regex_display` VARCHAR(2000) NULL DEFAULT NULL COMMENT 'A text description of the regular expression which can be displayed to the user.  Example: Alpha-numeric, numeric, etc.' ,
  `min_length` INT NULL DEFAULT NULL COMMENT 'Minimum number of characters allowed for this field.' ,
  `max_length` INT NULL DEFAULT NULL COMMENT 'Maximum number of characters allowed for this field.  The extension records allow for 30,000 characters.' ,
  `display_order` INT NULL DEFAULT NULL COMMENT 'Can be used to control the order the fields are displayed when no view is defined.  This is not normally used as a view is normally defined to control the fields displayed.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`entity_field_id`) ,
  CONSTRAINT `entityfield_entitydatatype_fk`
    FOREIGN KEY (`entity_data_type_id` )
    REFERENCES `entity_data_type` (`entity_data_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `entityfield_entity_fk`
    FOREIGN KEY (`entity_id` )
    REFERENCES `entity` (`entity_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `entityfield_optionlist_fk`
    FOREIGN KEY (`option_list_id` )
    REFERENCES `option_list` (`option_list_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `entityfield_name_entityid_uc` ON `entity_field` (`name` ASC, `entity_id` ASC) ;

CREATE UNIQUE INDEX `entityfield_code_entityid_uc` ON `entity_field` (`code` ASC, `entity_id` ASC) ;

CREATE INDEX `entityfield_entity_fki` ON `entity_field` (`entity_id` ASC) ;

CREATE INDEX `entityfield_optionlist_fki` ON `entity_field` (`option_list_id` ASC) ;

CREATE INDEX `entityfield_entitydatatype_fki` ON `entity_field` (`entity_data_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `org_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_ext` ;

CREATE  TABLE IF NOT EXISTS `org_ext` (
  `org_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`org_ext_id`) ,
  CONSTRAINT `orgext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `orgext_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `orgext_entityfield_fki` ON `org_ext` (`entity_field_id` ASC) ;

CREATE INDEX `orgext_value_entityfieldid_i` ON `org_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE INDEX `orgext_org_fki` ON `org_ext` (`org_id` ASC) ;

CREATE UNIQUE INDEX `orgext_orgid_entityfieldid_uc` ON `org_ext` (`org_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `scope_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scope_ext` ;

CREATE  TABLE IF NOT EXISTS `scope_ext` (
  `scope_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`scope_ext_id`) ,
  CONSTRAINT `scopeext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `scopeext_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `scopeext_entityfield_fki` ON `scope_ext` (`entity_field_id` ASC) ;

CREATE INDEX `scopeext_value_entityfieldid_i` ON `scope_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE INDEX `scopeext_scope_fki` ON `scope_ext` (`scope_id` ASC) ;

CREATE UNIQUE INDEX `scopeext_scopeid_entityfieldid_uc` ON `scope_ext` (`scope_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student` ;

CREATE  TABLE IF NOT EXISTS `student` (
  `student_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(40) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `last_name` VARCHAR(40) NOT NULL COMMENT 'Last name' ,
  `first_name` VARCHAR(30) NOT NULL COMMENT 'First name' ,
  `temporary` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'When the customer has an there own system to manage students, the temporary flag indicates that the student was added in our system to facilitate testing.' ,
  `inactive` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Logical delete of a student.' ,
  `local_code` VARCHAR(40) NULL DEFAULT NULL COMMENT 'The customer provided identifier for this student.  This field might be used by the customer to \"help\" identify an student, however it is not guaranteed to be unique.' ,
  `date_of_birth` DATE NULL DEFAULT NULL COMMENT 'Birth date' ,
  `middle_name` VARCHAR(30) NULL DEFAULT NULL COMMENT 'Students middle name or middle initial.' ,
  `gender` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Gender' ,
  `grade` VARCHAR(2) NULL DEFAULT NULL COMMENT 'Grade' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_id`) ,
  CONSTRAINT `student_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `student_code_scopeid_uc` ON `student` (`code` ASC, `scope_id` ASC) ;

CREATE INDEX `student_scope_fki` ON `student` (`scope_id` ASC) ;

CREATE INDEX `student_lastname_firstname_i` ON `student` (`last_name` ASC, `first_name` ASC) ;


-- -----------------------------------------------------
-- Table `student_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_ext` ;

CREATE  TABLE IF NOT EXISTS `student_ext` (
  `student_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_ext_id`) ,
  CONSTRAINT `studentext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentext_student_fk`
    FOREIGN KEY (`student_id` )
    REFERENCES `student` (`student_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `studentext_entityfield_fki` ON `student_ext` (`entity_field_id` ASC) ;

CREATE INDEX `studentext_value_entityfieldid_i` ON `student_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE INDEX `studentext_student_fki` ON `student_ext` (`student_id` ASC) ;

CREATE UNIQUE INDEX `studentext_studentid_entityfieldid_uc` ON `student_ext` (`student_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_reg`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_reg` ;

CREATE  TABLE IF NOT EXISTS `student_reg` (
  `student_reg_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_id` INT UNSIGNED NOT NULL ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `grade` VARCHAR(2) NULL COMMENT 'Grade' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_reg_id`) ,
  CONSTRAINT `studentreg_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentreg_student_fk`
    FOREIGN KEY (`student_id` )
    REFERENCES `student` (`student_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `studentreg_studentid_scopeid_uc` ON `student_reg` (`student_id` ASC, `scope_id` ASC) ;

CREATE INDEX `studentreg_scope_fki` ON `student_reg` (`scope_id` ASC) ;

CREATE INDEX `studentreg_student_fki` ON `student_reg` (`student_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_reg_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_reg_ext` ;

CREATE  TABLE IF NOT EXISTS `student_reg_ext` (
  `student_reg_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_reg_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_reg_ext_id`) ,
  CONSTRAINT `studentregext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentregext_studentreg_fk`
    FOREIGN KEY (`student_reg_id` )
    REFERENCES `student_reg` (`student_reg_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `studentregext_entityfield_fki` ON `student_reg_ext` (`entity_field_id` ASC) ;

CREATE INDEX `studentregext_studentreg_fki` ON `student_reg_ext` (`student_reg_id` ASC) ;

CREATE INDEX `studentregext_value_entityfieldid_i` ON `student_reg_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE UNIQUE INDEX `studentregext_studentregid_entityfieldid_uc` ON `student_reg_ext` (`student_reg_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `org_part`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_part` ;

CREATE  TABLE IF NOT EXISTS `org_part` (
  `org_part_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_id` INT UNSIGNED NOT NULL ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`org_part_id`) ,
  CONSTRAINT `orgpart_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `orgpart_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `orgpart_scope_fki` ON `org_part` (`scope_id` ASC) ;

CREATE INDEX `orgpart_org_fki` ON `org_part` (`org_id` ASC) ;

CREATE UNIQUE INDEX `orgpart_orgid_scopeid_uc` ON `org_part` (`org_id` ASC, `scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `org_part_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_part_ext` ;

CREATE  TABLE IF NOT EXISTS `org_part_ext` (
  `org_part_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_part_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`org_part_ext_id`) ,
  CONSTRAINT `orgpartext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `orgpartext_orgpart_fk`
    FOREIGN KEY (`org_part_id` )
    REFERENCES `org_part` (`org_part_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `orgpartext_entityfield_fki` ON `org_part_ext` (`entity_field_id` ASC) ;

CREATE INDEX `orgpartext_orgpart_fki` ON `org_part_ext` (`org_part_id` ASC) ;

CREATE INDEX `orgpartext_value_entityfieldid_i` ON `org_part_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE UNIQUE INDEX `orgpartext_orgpartid_entityfieldid_uc` ON `org_part_ext` (`org_part_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `group_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `group_type` ;

CREATE  TABLE IF NOT EXISTS `group_type` (
  `group_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `exclusive_student` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'If this flag is true, students can only be assigned to this group if they do not already belong to a group.' ,
  `description` VARCHAR(1000) NULL DEFAULT NULL COMMENT 'Detail description of the group type which can be used for context-sensitive help.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`group_type_id`) ,
  CONSTRAINT `grouptype_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `grouptype_code_scopeid_uc` ON `group_type` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `grouptype_name_scopeid_uc` ON `group_type` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `grouptype_scope_fki` ON `group_type` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_group` ;

CREATE  TABLE IF NOT EXISTS `student_group` (
  `student_group_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `group_type_id` INT UNSIGNED NOT NULL ,
  `org_part_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name (within an organization) which is displayed to the user.  This descriptive name can change and should not be be used as a key.\n' ,
  `description` VARCHAR(1000) NULL DEFAULT NULL COMMENT 'Detailed description of the group which can be displayed to the user as context sensitive help.' ,
  `grade` VARCHAR(50) NULL DEFAULT NULL COMMENT 'The grade level of the students in this group.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_group_id`) ,
  CONSTRAINT `studentgroup_grouptype_fk`
    FOREIGN KEY (`group_type_id` )
    REFERENCES `group_type` (`group_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentgroup_orgpart_fk`
    FOREIGN KEY (`org_part_id` )
    REFERENCES `org_part` (`org_part_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `studentgroup_name_orgpartid_uc` ON `student_group` (`name` ASC, `org_part_id` ASC) ;

CREATE INDEX `studentgroup_orgpart_fki` ON `student_group` (`org_part_id` ASC) ;

CREATE INDEX `studentgroup_grouptype_fki` ON `student_group` (`group_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_group_assign`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_group_assign` ;

CREATE  TABLE IF NOT EXISTS `student_group_assign` (
  `student_group_assign_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_id` INT UNSIGNED NOT NULL ,
  `student_group_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_group_assign_id`) ,
  CONSTRAINT `studentgroupassign_studentgroup_fk`
    FOREIGN KEY (`student_group_id` )
    REFERENCES `student_group` (`student_group_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentgroupassign_student_fk`
    FOREIGN KEY (`student_id` )
    REFERENCES `student` (`student_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `studentgroupassign_studentid_studentgroupid_uc` ON `student_group_assign` (`student_id` ASC, `student_group_id` ASC) ;

CREATE INDEX `studentgroupassign_studentgroup_fki` ON `student_group_assign` (`student_group_id` ASC) ;

CREATE INDEX `studentgroupassign_student_fki` ON `student_group_assign` (`student_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_enroll`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_enroll` ;

CREATE  TABLE IF NOT EXISTS `student_enroll` (
  `student_enroll_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_id` INT UNSIGNED NOT NULL ,
  `org_part_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_enroll_id`) ,
  CONSTRAINT `studentenroll_orgpart_fk`
    FOREIGN KEY (`org_part_id` )
    REFERENCES `org_part` (`org_part_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentenroll_student_fk`
    FOREIGN KEY (`student_id` )
    REFERENCES `student` (`student_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `studentenroll_studentid_orgpartid_uc` ON `student_enroll` (`student_id` ASC, `org_part_id` ASC) ;

CREATE INDEX `studentenroll_orgpart_fki` ON `student_enroll` (`org_part_id` ASC) ;

CREATE INDEX `studentenroll_student_fki` ON `student_enroll` (`student_id` ASC) ;


-- -----------------------------------------------------
-- Table `option_list_value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `option_list_value` ;

CREATE  TABLE IF NOT EXISTS `option_list_value` (
  `option_list_value_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `option_list_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name (within an option list) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `value` VARCHAR(200) NOT NULL COMMENT 'Unique value (within an option list) which will be stored to the database.' ,
  `display_order` INT NULL DEFAULT NULL COMMENT 'Controls the order in which the values for an option list are displayed to the user.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`option_list_value_id`) ,
  CONSTRAINT `optionlistvalue_optionlist_fk`
    FOREIGN KEY (`option_list_id` )
    REFERENCES `option_list` (`option_list_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `optionlistvalue_name_optionlistid_uc` ON `option_list_value` (`name` ASC, `option_list_id` ASC) ;

CREATE UNIQUE INDEX `optionlistvalue_value_optionlistid_uc` ON `option_list_value` (`value` ASC, `option_list_id` ASC) ;

CREATE INDEX `optionlistvalue_optionlist_fki` ON `option_list_value` (`option_list_id` ASC) ;


-- -----------------------------------------------------
-- Table `query_sql`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `query_sql` ;

CREATE  TABLE IF NOT EXISTS `query_sql` (
  `query_sql_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NULL ,
  `entity_type_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `sql_text` LONGTEXT NOT NULL COMMENT 'SQL query (select statement) text' ,
  `description` VARCHAR(1000) NOT NULL COMMENT 'Detail description of the query that can be displayed to the user.' ,
  `keywords` VARCHAR(1000) NULL DEFAULT NULL COMMENT 'Comma separated list of key words that can be used by a developer to search for a query to use.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`query_sql_id`) ,
  CONSTRAINT `querysql_entitytype_fk`
    FOREIGN KEY (`entity_type_id` )
    REFERENCES `entity_type` (`entity_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `querysql_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `querysql_code_scopeid_uc` ON `query_sql` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `querysql_name_scopeid_uc` ON `query_sql` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `querysql_entitytype_fki` ON `query_sql` (`entity_type_id` ASC) ;

CREATE INDEX `querysql_scope_fki` ON `query_sql` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `view_def_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view_def_type` ;

CREATE  TABLE IF NOT EXISTS `view_def_type` (
  `view_def_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `entity_type_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier that can be used code to lookup a specific row.' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique (within scope) name that is displayed to the user.' ,
  `category` ENUM('form','datagrid','group') NOT NULL DEFAULT 'form' COMMENT 'What category is this view type (see enum for possible values)' ,
  `defaultView` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is this the default view for the specified \"entity_type_id\"?  There should be only one default view per entity type.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`view_def_type_id`) ,
  CONSTRAINT `viewdeftype_entitytype_fk`
    FOREIGN KEY (`entity_type_id` )
    REFERENCES `entity_type` (`entity_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `viewdeftype_name_uc` ON `view_def_type` (`name` ASC) ;

CREATE UNIQUE INDEX `viewdeftype_code_uc` ON `view_def_type` (`code` ASC) ;

CREATE INDEX `viewdeftype_entitytype_fki` ON `view_def_type` (`entity_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `view_def`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view_def` ;

CREATE  TABLE IF NOT EXISTS `view_def` (
  `view_def_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `view_def_type_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(200) NULL COMMENT 'Optional name that can be provided to override the default (hard coded) view name displayed on a web page.' ,
  `collapsible` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Can the area on the screen displaying this view definition be collapsed.' ,
  `collapsed_by_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Should the area on the screen displaying this view definition be collapsed by default.' ,
  `column1_width` VARCHAR(10) NULL COMMENT 'The width of the first column.' ,
  `column1_label_width` VARCHAR(10) NULL COMMENT 'The width of the labels in the first column.' ,
  `column2_width` VARCHAR(10) NULL COMMENT 'The width of the second column.' ,
  `column2_label_width` VARCHAR(10) NULL COMMENT 'The width of the labels in the second column.' ,
  `column3_width` VARCHAR(10) NULL COMMENT 'The width of the third column.' ,
  `column3_label_width` VARCHAR(10) NULL COMMENT 'The width of the labels in the third column.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`view_def_id`) ,
  CONSTRAINT `viewdef_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `viewdef_viewdeftype_fk`
    FOREIGN KEY (`view_def_type_id` )
    REFERENCES `view_def_type` (`view_def_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `viewdef_scope_fki` ON `view_def` (`scope_id` ASC) ;

CREATE INDEX `viewdef_viewdeftype_fki` ON `view_def` (`view_def_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `entity_rule`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entity_rule` ;

CREATE  TABLE IF NOT EXISTS `entity_rule` (
  `entity_rule_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `entity_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NULL COMMENT 'Used to associate the error message to a particular field.  If this field is null then it is a global error.' ,
  `name` VARCHAR(200) BINARY NOT NULL COMMENT 'Unique name (within an entity) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `rule` VARCHAR(10000) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `disabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Deactivates the rule so that it is no longer enforced.  Allows the user to disable a rule without deleting it.' ,
  `type` ENUM('validation','view') NULL COMMENT 'Type of rule (see enum for possible values)' ,
  `error_message` VARCHAR(1000) NULL COMMENT 'Primary error message for this rule.  This error will be displayed to the user on the web site, and optionally displayed on batch loads if the \"batch_error_message\" is not provided.' ,
  `batch_error_message` VARCHAR(1000) NULL COMMENT 'Alternate batch specific error message for this rule.  If this field is not provided, the mandatory \"error_message\" field will be used instead.' ,
  `description` VARCHAR(1000) NULL COMMENT 'Detailed description the business rule being enforced.  This text will be displayed to the user as context sensitive help.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`entity_rule_id`) ,
  CONSTRAINT `entityrule_entity_fk`
    FOREIGN KEY (`entity_id` )
    REFERENCES `entity` (`entity_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `entityrule_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `entityrule_entityid_name_uc` ON `entity_rule` (`entity_id` ASC, `name` ASC) ;

CREATE INDEX `entityrule_entity_fki` ON `entity_rule` (`entity_id` ASC) ;

CREATE INDEX `entityrule_entityfield_fki` ON `entity_rule` (`entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `view_def_field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view_def_field` ;

CREATE  TABLE IF NOT EXISTS `view_def_field` (
  `view_def_field_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `view_def_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `read_only` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'If \"true\" the field will be displayed as static text and the user will be unable to edit the field.' ,
  `display_entity_rule_id` INT UNSIGNED NULL ,
  `edit_entity_rule_id` INT UNSIGNED NULL ,
  `override_name` VARCHAR(200) NULL COMMENT 'Can be used to display a different field label than the name defined on the entity.' ,
  `input_type` ENUM('dropdown','radio') NULL COMMENT 'For fields with option lists, determines which control should be displayed. (see enum for possible values)' ,
  `label_position` ENUM('hidden','after') NULL COMMENT 'How should the label for this field be positioned (see enum for possible values)' ,
  `display_width` VARCHAR(10) NULL COMMENT 'What should the CSS width for this input. (ex: 60px, 20em)' ,
  `label_style` VARCHAR(300) NULL ,
  `input_style` VARCHAR(300) NULL ,
  `column_number` INT NULL COMMENT 'Which column should this field be displayed.' ,
  `display_order` INT NULL COMMENT 'Controls the order of the fields displayed on the page.  Fields with lower numbers are displayed first.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`view_def_field_id`) ,
  CONSTRAINT `viewdeffield_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `viewdeffield_viewdef_fk`
    FOREIGN KEY (`view_def_id` )
    REFERENCES `view_def` (`view_def_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `viewdeffield_displayentityrule_fk`
    FOREIGN KEY (`display_entity_rule_id` )
    REFERENCES `entity_rule` (`entity_rule_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `viewdeffield_editentityrule_fk`
    FOREIGN KEY (`edit_entity_rule_id` )
    REFERENCES `entity_rule` (`entity_rule_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `viewdeffield_entityfield_fki` ON `view_def_field` (`entity_field_id` ASC) ;

CREATE INDEX `viewdeffield_viewdef_fki` ON `view_def_field` (`view_def_id` ASC) ;

CREATE INDEX `viewdeffield_displayentityrule_fki` ON `view_def_field` (`display_entity_rule_id` ASC) ;

CREATE INDEX `viewdeffield_editentityrule_fki` ON `view_def_field` (`edit_entity_rule_id` ASC) ;


-- -----------------------------------------------------
-- Table `test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `test` ;

CREATE  TABLE IF NOT EXISTS `test` (
  `test_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.' ,
  `allow_paper` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is this a test that can be delivered on paper?' ,
  `allow_online` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is this a test that can be delivered online?' ,
  `allow_alternate` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is this a test that is an alternate assessment?' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`test_id`) ,
  CONSTRAINT `test_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `test_code_scopeid_uc` ON `test` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `test_name_scopeid_uc` ON `test` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `test_scope_fki` ON `test` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_test` ;

CREATE  TABLE IF NOT EXISTS `student_test` (
  `student_test_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_enroll_id` INT UNSIGNED NOT NULL ,
  `test_id` INT UNSIGNED NOT NULL ,
  `student_group_id` INT UNSIGNED NULL DEFAULT NULL ,
  `type` ENUM('paper','online','alternate') NOT NULL COMMENT 'paper - printed document<br>\nonline - computer based test<br>\nalternative - instructor evaluation<br>' ,
  `status` ENUM('assign','testing','attempt','invalid_attempt') NOT NULL COMMENT 'assign - not yet started<br>\ntesting - testing in progress<br>\nattempt - test complete and valid<br>\ninvalid_attempt - test complete with problems' ,
  `preid_code` VARCHAR(100) NULL DEFAULT NULL COMMENT 'Unique barcode number which is printed on test booklets and used for test identification after scanning.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_test_id`) ,
  CONSTRAINT `studenttest_studentgroup_fk`
    FOREIGN KEY (`student_group_id` )
    REFERENCES `student_group` (`student_group_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studenttest_test_fk`
    FOREIGN KEY (`test_id` )
    REFERENCES `test` (`test_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studenttest_studentenroll_fk`
    FOREIGN KEY (`student_enroll_id` )
    REFERENCES `student_enroll` (`student_enroll_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `studenttest_studentgroup_fki` ON `student_test` (`student_group_id` ASC) ;

CREATE INDEX `studenttest_test_fki` ON `student_test` (`test_id` ASC) ;

CREATE INDEX `studenttest_studentenroll_fki` ON `student_test` (`student_enroll_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_test_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_test_ext` ;

CREATE  TABLE IF NOT EXISTS `student_test_ext` (
  `student_test_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_test_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_test_ext_id`) ,
  CONSTRAINT `studenttestext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studenttestext_studenttest_fk`
    FOREIGN KEY (`student_test_id` )
    REFERENCES `student_test` (`student_test_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `studenttestext_entityfield_fki` ON `student_test_ext` (`entity_field_id` ASC) ;

CREATE INDEX `studenttestext_value_entityfieldid_i` ON `student_test_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE INDEX `studenttestext_studenttest_fki` ON `student_test_ext` (`student_test_id` ASC) ;

CREATE UNIQUE INDEX `studenttestext_studenttestid_entityfieldid_uc` ON `student_test_ext` (`student_test_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `scope_tree`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scope_tree` ;

CREATE  TABLE IF NOT EXISTS `scope_tree` (
  `scope_tree_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `ancestor_scope_id` INT UNSIGNED NOT NULL COMMENT 'Identifies the scope which is the ancestor of the relation.' ,
  `ancestor_path` VARCHAR(2000) NOT NULL COMMENT 'Provides a unique string representation of the ancestor scope similar to the path of a file.\nex \"/va\"' ,
  `ancestor_depth` SMALLINT NOT NULL COMMENT 'The depth of the the descendant scope within the scope hierarchy. Depth is zero based, so the root of the tree is zero.' ,
  `scope_id` INT UNSIGNED NOT NULL COMMENT 'Identifies the scope which is the descendant of the relation.' ,
  `path` VARCHAR(2000) NOT NULL COMMENT 'Provides a unique string representation of the descendant scope similar to the path of a file.  ex \"/va/nwspg11\"' ,
  `distance` SMALLINT NOT NULL COMMENT 'The number of levels in the tree between the ancestor and descendant scopes.  A level of \"0\" indicates that this is an association of the scope to itself.' ,
  `depth` SMALLINT NOT NULL COMMENT 'The depth of the the descendant scope within the scope hierarchy. Depth is zero based, so the root of the tree is zero.' ,
  PRIMARY KEY (`scope_tree_id`) ,
  CONSTRAINT `scopetree_ancestorscope_fk`
    FOREIGN KEY (`ancestor_scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `scopetree_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 111;

CREATE UNIQUE INDEX `scopetree_ancestorscopeid_scopeid_uc` ON `scope_tree` (`ancestor_scope_id` ASC, `scope_id` ASC) ;

CREATE INDEX `scopetree_ancestorscope_fki` ON `scope_tree` (`ancestor_scope_id` ASC) ;

CREATE INDEX `scopetree_scope_fki` ON `scope_tree` (`scope_id` ASC) ;

CREATE INDEX `scopetree_ancestorpath_i` ON `scope_tree` (`ancestor_path`(767) ASC) ;

CREATE INDEX `scopetree_path_i` ON `scope_tree` (`path`(767) ASC) ;


-- -----------------------------------------------------
-- Table `org_tree`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_tree` ;

CREATE  TABLE IF NOT EXISTS `org_tree` (
  `org_tree_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `ancestor_org_id` INT UNSIGNED NULL COMMENT 'Identifies the organization which is the ancestor of the relation.' ,
  `ancestor_path` VARCHAR(2000) NOT NULL COMMENT 'Provides a unique string representation of the ancestor organization similar to the path of a file.  ex \"/TN\"' ,
  `org_id` INT UNSIGNED NOT NULL COMMENT 'Identifies the organization which is the descendant of the relation.' ,
  `path` VARCHAR(2000) NOT NULL COMMENT 'Provides a unique string representation of the descendant organization similar to the path of a file.  ex \"/TN/district1/school1\"' ,
  `distance` SMALLINT NOT NULL COMMENT 'The number of levels in the tree between the ancestor and descendant organizations.  A level of \"0\" indicates that this is an association of the organization to itself.' ,
  `depth` SMALLINT NOT NULL COMMENT 'The depth of the the descendant organization within the organization hierarchy.  Depth is zero based, so the root of the tree is zero.' ,
  PRIMARY KEY (`org_tree_id`) ,
  CONSTRAINT `orgtree_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `orgtree_ancestororg_fk`
    FOREIGN KEY (`ancestor_org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 116;

CREATE INDEX `orgtree_org_fki` ON `org_tree` (`org_id` ASC) ;

CREATE INDEX `orgtree_ancestororg_fki` ON `org_tree` (`ancestor_org_id` ASC) ;

CREATE INDEX `orgtree_ancestorpath_i` ON `org_tree` (`ancestor_path`(767) ASC) ;

CREATE INDEX `orgtree_path_i` ON `org_tree` (`path`(767) ASC) ;


-- -----------------------------------------------------
-- Table `student_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_hist` ;

CREATE  TABLE IF NOT EXISTS `student_hist` (
  `student_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_id` INT UNSIGNED NULL DEFAULT NULL ,
  `scope_id` INT UNSIGNED NULL DEFAULT NULL ,
  `code` VARCHAR(40) NULL DEFAULT NULL ,
  `temporary` TINYINT(1) NULL DEFAULT NULL ,
  `inactive` TINYINT(1) NULL DEFAULT NULL ,
  `local_code` VARCHAR(40) NULL DEFAULT NULL ,
  `date_of_birth` DATE NULL DEFAULT NULL ,
  `first_name` VARCHAR(30) NULL DEFAULT NULL ,
  `last_name` VARCHAR(40) NULL DEFAULT NULL ,
  `middle_name` VARCHAR(30) NULL DEFAULT NULL ,
  `gender` VARCHAR(1) NULL DEFAULT NULL ,
  `grade` VARCHAR(2) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`student_hist_id`) );


-- -----------------------------------------------------
-- Table `student_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `student_ext_hist` (
  `student_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `generic_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `generic_hist` ;

CREATE  TABLE IF NOT EXISTS `generic_hist` (
  `generic_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  `table_name` VARCHAR(64) NOT NULL ,
  `column_name` VARCHAR(64) NOT NULL ,
  `primary_key` INT NOT NULL ,
  `old_value` VARCHAR(10000) NULL DEFAULT NULL ,
  `new_value` VARCHAR(10000) NULL DEFAULT NULL ,
  PRIMARY KEY (`generic_hist_id`) )
ENGINE = InnoDB;

CREATE INDEX `generichist_tablename_columnname_primarykey_i` ON `generic_hist` (`table_name` ASC, `column_name` ASC, `primary_key` ASC) ;

CREATE INDEX `generichist_primarykey_changedate_i` ON `generic_hist` (`primary_key` ASC, `change_date` ASC) ;


-- -----------------------------------------------------
-- Table `contact_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact_type` ;

CREATE  TABLE IF NOT EXISTS `contact_type` (
  `contact_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique (within scope) code that can be used by import, exports, etc.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique (within scope) name that is displayed to the user.' ,
  `display_order` TINYINT NULL COMMENT 'Used to force the order (ascending) in which the contact types are displayed on the website.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`contact_type_id`) ,
  CONSTRAINT `contacttype_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `contacttype_code_scopeid_uc` ON `contact_type` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `contacttype_name_scopeid_uc` ON `contact_type` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `contacttype_scope_fki` ON `contact_type` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `contact`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact` ;

CREATE  TABLE IF NOT EXISTS `contact` (
  `contact_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `contact_type_id` INT UNSIGNED NOT NULL ,
  `org_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Full name of the person (first, middle, last)' ,
  `title` VARCHAR(50) NULL COMMENT 'Title (Mr, Mrs, etc)' ,
  `email` VARCHAR(100) NULL COMMENT 'Email address' ,
  `address_line_1` VARCHAR(100) NULL COMMENT 'Address line #1' ,
  `address_line_2` VARCHAR(100) NULL COMMENT 'Address line #2' ,
  `city` VARCHAR(50) NULL COMMENT 'City' ,
  `state` VARCHAR(20) NULL COMMENT 'state/province/region' ,
  `country` VARCHAR(2) NULL COMMENT '2 digit country code' ,
  `zip` VARCHAR(10) NULL COMMENT 'zip or postal code' ,
  `phone` VARCHAR(20) NULL COMMENT 'Phone number ... 20 digits to accomodate international numbers' ,
  `phone_extension` VARCHAR(10) NULL COMMENT 'Phone extension' ,
  `fax` VARCHAR(20) NULL COMMENT 'Fax number ... 20 digits to accomodate international numbers' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`contact_id`) ,
  CONSTRAINT `contact_contacttype_fk`
    FOREIGN KEY (`contact_type_id` )
    REFERENCES `contact_type` (`contact_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `contact_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `contact_contacttypeid_orgid_uc` ON `contact` (`contact_type_id` ASC, `org_id` ASC) ;

CREATE INDEX `contact_contacttype_fki` ON `contact` (`contact_type_id` ASC) ;

CREATE INDEX `contact_org_fki` ON `contact` (`org_id` ASC) ;


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE  TABLE IF NOT EXISTS `user` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `username` VARCHAR(100) NOT NULL COMMENT 'The username (namespaced by the scope path) corresponds to the authenticated username used to access the application.' ,
  `last_name` VARCHAR(50) NULL COMMENT 'Last name' ,
  `first_name` VARCHAR(50) NULL COMMENT 'First name' ,
  `email` VARCHAR(100) NULL COMMENT 'Email addres' ,
  `active_begin_date` DATETIME NULL COMMENT 'If the current date is before the active_begin_date, the user is unauthorized to access the application.' ,
  `active_end_date` DATETIME NULL COMMENT 'If the current date is after the active_end_date, the user is unauthorized to access the application.' ,
  `disable_date` DATETIME NULL COMMENT 'If this date is non-null, the user is unauthorized to access the application.' ,
  `disable_reason` VARCHAR(1000) NULL COMMENT 'A descriptive field which allows the admin to indicate why the user was disabled.' ,
  `delete_date` DATETIME NULL COMMENT 'If this date is non-null, the user is unauthorized to access the application.' ,
  `reset_token_1` VARCHAR(100) NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)' ,
  `reset_token_2` VARCHAR(100) NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)' ,
  `reset_token_3` VARCHAR(100) NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)' ,
  `reset_token_4` VARCHAR(100) NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)' ,
  `reset_token_5` VARCHAR(100) NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)' ,
  `selected_scope_id` INT UNSIGNED NULL COMMENT 'Scope that should be the default selected global scope for this user at login.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`user_id`) ,
  CONSTRAINT `user_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_selectedscope_fk`
    FOREIGN KEY (`selected_scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `user_username_scopeid_uc` ON `user` (`username` ASC, `scope_id` ASC) ;

CREATE INDEX `user_scope_fki` ON `user` (`scope_id` ASC) ;

CREATE INDEX `user_selectedscope_fki` ON `user` (`selected_scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role` ;

CREATE  TABLE IF NOT EXISTS `role` (
  `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name which is displayed to the user. This descriptive name can change and should not be be used as a key.' ,
  `category` VARCHAR(100) NULL COMMENT 'Display only ... used to group related roles on edit pages' ,
  `short_name` VARCHAR(50) NULL COMMENT 'A short name that can be used on the site when space is limited.  If not provided name will be displayed.' ,
  `description` VARCHAR(1000) NULL COMMENT 'A longer description of the role which is displayed to the user as context sensitive help.' ,
  `display_order` SMALLINT NULL COMMENT 'Can be used to control the order the features are displayed on our web pages.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`role_id`) ,
  CONSTRAINT `role_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `role_code_scopeid_uc` ON `role` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `role_name_scopeid_uc` ON `role` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `role_scope_fki` ON `role` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `permission` ;

CREATE  TABLE IF NOT EXISTS `permission` (
  `permission_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'nique name which is displayed to the user. This descriptive name can change and should not be be used as a key.' ,
  `description` VARCHAR(1000) NULL COMMENT 'A longer description of the field which is displayed to the user as context sensitive help.' ,
  `display_order` SMALLINT NULL COMMENT 'Can be used to control the order the permissions are displayed on our web pages.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`permission_id`) ,
  CONSTRAINT `permission_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `permission_code_scopeid_uc` ON `permission` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `permission_name_scopeid_uc` ON `permission` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `permission_scope_fki` ON `permission` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `role_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role_permission` ;

CREATE  TABLE IF NOT EXISTS `role_permission` (
  `role_permission_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `role_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`role_permission_id`) ,
  CONSTRAINT `rolepermission_permission_fk`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`permission_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `rolepermission_role_fk`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`role_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `rolepermission_permissionid_roleid_uc` ON `role_permission` (`permission_id` ASC, `role_id` ASC) ;

CREATE INDEX `rolepermission_permission_fki` ON `role_permission` (`permission_id` ASC) ;

CREATE INDEX `rolepermission_role_fki` ON `role_permission` (`role_id` ASC) ;


-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_role` ;

CREATE  TABLE IF NOT EXISTS `user_role` (
  `user_role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `role_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`user_role_id`) ,
  CONSTRAINT `userrole_user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `userrole_role_fk`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`role_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `userrole_roleid_userid_uc` ON `user_role` (`role_id` ASC, `user_id` ASC) ;

CREATE INDEX `userrole_user_fki` ON `user_role` (`user_id` ASC) ;

CREATE INDEX `userrole_role_fki` ON `user_role` (`role_id` ASC) ;


-- -----------------------------------------------------
-- Table `user_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_ext` ;

CREATE  TABLE IF NOT EXISTS `user_ext` (
  `user_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`user_ext_id`) ,
  CONSTRAINT `userext_user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `userext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `userext_userid_entityfieldid_uc` ON `user_ext` (`user_id` ASC, `entity_field_id` ASC) ;

CREATE INDEX `userext_user_fki` ON `user_ext` (`user_id` ASC) ;

CREATE INDEX `userext_entityfield_fki` ON `user_ext` (`entity_field_id` ASC) ;

CREATE INDEX `userext_value_entityfieldid_i` ON `user_ext` (`value` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `cas_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cas_user` ;

CREATE  TABLE IF NOT EXISTS `cas_user` (
  `cas_user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_name` VARCHAR(100) NOT NULL COMMENT 'used to login' ,
  `password` VARCHAR(100) NOT NULL COMMENT 'MD5 hash' ,
  `failed_attempts` INT NULL COMMENT 'The number of failed login attempts for this user.' ,
  PRIMARY KEY (`cas_user_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 5
COMMENT = 'table to hold CAS user credentials';

CREATE UNIQUE INDEX `casuser_user_name_uc` ON `cas_user` (`user_name` ASC) ;


-- -----------------------------------------------------
-- Table `feature`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `feature` ;

CREATE  TABLE IF NOT EXISTS `feature` (
  `feature_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name which is displayed to the user. This descriptive name can change and should not be be used as a key.' ,
  `description` VARCHAR(1000) NULL COMMENT 'A longer description of the field which is displayed to the user as context sensitive help.' ,
  `display_order` SMALLINT(2) NULL COMMENT 'Can be used to control the order the features are displayed on our web pages.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`feature_id`) ,
  CONSTRAINT `feature_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `feature_code_scopeid_uc` ON `feature` (`code` ASC, `scope_id` ASC) ;

CREATE UNIQUE INDEX `feature_name_scopeid_uc` ON `feature` (`name` ASC, `scope_id` ASC) ;

CREATE INDEX `feature_scope_fki` ON `feature` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `feature_active`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `feature_active` ;

CREATE  TABLE IF NOT EXISTS `feature_active` (
  `feature_active_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `feature_id` INT UNSIGNED NOT NULL ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `disabled` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Forces a feature to be made inactive and disables all associated permissions.' ,
  `begin_date` DATETIME NULL COMMENT 'The feature is considered inactive if the current date is before the begin_date.' ,
  `end_date` DATETIME NULL COMMENT 'The feature is considered inactive if the current date is after the end_date.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`feature_active_id`) ,
  CONSTRAINT `featureactive_feature_fk`
    FOREIGN KEY (`feature_id` )
    REFERENCES `feature` (`feature_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `featureactive_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `featureactive_featureid_scopeid_uc` ON `feature_active` (`feature_id` ASC, `scope_id` ASC) ;

CREATE INDEX `featureactive_feature_fki` ON `feature_active` (`feature_id` ASC) ;

CREATE INDEX `featureactive_scope_fki` ON `feature_active` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `user_org`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_org` ;

CREATE  TABLE IF NOT EXISTS `user_org` (
  `user_org_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `org_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`user_org_id`) ,
  CONSTRAINT `userorg_user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `userorg_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `userorg_orgid_userid_uc` ON `user_org` (`org_id` ASC, `user_id` ASC) ;

CREATE INDEX `userorg_user_fki` ON `user_org` (`user_id` ASC) ;

CREATE INDEX `userorg_org_fki` ON `user_org` (`org_id` ASC) ;


-- -----------------------------------------------------
-- Table `feature_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `feature_permission` ;

CREATE  TABLE IF NOT EXISTS `feature_permission` (
  `feature_permission_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `feature_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`feature_permission_id`) ,
  CONSTRAINT `featurepermission_feature_fk`
    FOREIGN KEY (`feature_id` )
    REFERENCES `feature` (`feature_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `featurepermission_permission_fk`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`permission_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `featurepermission_permissionid_featureid_uc` ON `feature_permission` (`permission_id` ASC, `feature_id` ASC) ;

CREATE INDEX `featurepermission_feature_fki` ON `feature_permission` (`feature_id` ASC) ;

CREATE INDEX `featurepermission_permission_fki` ON `feature_permission` (`permission_id` ASC) ;


-- -----------------------------------------------------
-- Table `custom_text`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `custom_text` ;

CREATE  TABLE IF NOT EXISTS `custom_text` (
  `custom_text_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `scope_id` INT UNSIGNED NOT NULL ,
  `code` VARCHAR(100) NOT NULL COMMENT 'Unique (within scope) identifier that can be used code to lookup a specific row.  This code should match the keys to the message bundles.' ,
  `text` VARCHAR(5000) NOT NULL COMMENT 'The text that is displayed to the user.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`custom_text_id`) ,
  CONSTRAINT `customtext_scope_fk`
    FOREIGN KEY (`scope_id` )
    REFERENCES `scope` (`scope_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `customtext_code_scopeid_uc` ON `custom_text` (`code` ASC, `scope_id` ASC) ;

CREATE INDEX `customtext_scope_fki` ON `custom_text` (`scope_id` ASC) ;


-- -----------------------------------------------------
-- Table `view_def_text`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `view_def_text` ;

CREATE  TABLE IF NOT EXISTS `view_def_text` (
  `view_def_text_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `view_def_id` INT UNSIGNED NOT NULL ,
  `text` VARCHAR(5000) NOT NULL COMMENT 'The text (HTML is supported) to be displayed to the user.' ,
  `column_number` INT NULL COMMENT 'Which column should this text be displayed.' ,
  `display_order` INT NULL COMMENT 'The order in which this text should be displayed ... used in conjunction with view_def_field.display_order.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`view_def_text_id`) ,
  CONSTRAINT `viewdeftext_viewdef_fk`
    FOREIGN KEY (`view_def_id` )
    REFERENCES `view_def` (`view_def_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `viewdeftext_viewdef_fki` ON `view_def_text` (`view_def_id` ASC) ;


-- -----------------------------------------------------
-- Table `user_role_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_role_hist` ;

CREATE  TABLE IF NOT EXISTS `user_role_hist` (
  `user_role_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_role_id` INT UNSIGNED NULL DEFAULT NULL ,
  `user_id` INT UNSIGNED NULL DEFAULT NULL ,
  `role_id` INT UNSIGNED NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`user_role_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_org_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_org_hist` ;

CREATE  TABLE IF NOT EXISTS `user_org_hist` (
  `user_org_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `user_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`user_org_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_hist` ;

CREATE  TABLE IF NOT EXISTS `user_hist` (
  `user_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NULL DEFAULT NULL ,
  `scope_id` INT UNSIGNED NULL DEFAULT NULL ,
  `username` VARCHAR(100) NULL DEFAULT NULL ,
  `last_name` VARCHAR(50) NULL DEFAULT NULL ,
  `first_name` VARCHAR(50) NULL DEFAULT NULL ,
  `email` VARCHAR(100) NULL DEFAULT NULL ,
  `active_begin_date` DATETIME NULL DEFAULT NULL ,
  `active_end_date` DATETIME NULL DEFAULT NULL ,
  `disable_date` DATETIME NULL DEFAULT NULL ,
  `disable_reason` VARCHAR(1000) NULL DEFAULT NULL ,
  `delete_date` DATETIME NULL DEFAULT NULL ,
  `reset_token_1` VARCHAR(100) NULL DEFAULT NULL ,
  `reset_token_2` VARCHAR(100) NULL DEFAULT NULL ,
  `reset_token_3` VARCHAR(100) NULL DEFAULT NULL ,
  `reset_token_4` VARCHAR(100) NULL DEFAULT NULL ,
  `reset_token_5` VARCHAR(100) NULL DEFAULT NULL ,
  `selected_scope_id` INT UNSIGNED NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`user_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `user_ext_hist` (
  `user_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `user_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`user_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_test_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_test_hist` ;

CREATE  TABLE IF NOT EXISTS `student_test_hist` (
  `student_test_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_test_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_enroll_id` INT UNSIGNED NULL DEFAULT NULL ,
  `test_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_group_id` INT UNSIGNED NULL DEFAULT NULL ,
  `type` ENUM('paper','online','alternate') NULL DEFAULT NULL ,
  `status` ENUM('assign','testing','attempt','invalid_attempt') NULL DEFAULT NULL ,
  `preid_code` VARCHAR(100) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_test_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_test_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_test_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `student_test_ext_hist` (
  `student_test_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_test_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_test_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_test_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_reg_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_reg_hist` ;

CREATE  TABLE IF NOT EXISTS `student_reg_hist` (
  `student_reg_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_reg_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_id` INT UNSIGNED NULL DEFAULT NULL ,
  `scope_id` INT UNSIGNED NULL DEFAULT NULL ,
  `grade` VARCHAR(2) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_reg_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_reg_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_reg_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `student_reg_ext_hist` (
  `student_reg_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_reg_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_reg_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_reg_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `org_part_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_part_hist` ;

CREATE  TABLE IF NOT EXISTS `org_part_hist` (
  `org_part_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_part_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `scope_id` INT UNSIGNED NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`org_part_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `org_part_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_part_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `org_part_ext_hist` (
  `org_part_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_part_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_part_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`org_part_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `org_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `org_ext_hist` (
  `org_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`org_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `contact_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contact_hist` ;

CREATE  TABLE IF NOT EXISTS `contact_hist` (
  `contact_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `contact_id` INT UNSIGNED NULL DEFAULT NULL ,
  `contact_type_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `name` VARCHAR(100) NULL DEFAULT NULL ,
  `title` VARCHAR(50) NULL DEFAULT NULL ,
  `email` VARCHAR(100) NULL DEFAULT NULL ,
  `address_line_1` VARCHAR(100) NULL DEFAULT NULL ,
  `address_line_2` VARCHAR(100) NULL DEFAULT NULL ,
  `city` VARCHAR(50) NULL DEFAULT NULL ,
  `state` VARCHAR(20) NULL DEFAULT NULL ,
  `country` VARCHAR(2) NULL DEFAULT NULL ,
  `zip` VARCHAR(10) NULL DEFAULT NULL ,
  `phone` VARCHAR(20) NULL DEFAULT NULL ,
  `phone_extension` VARCHAR(10) NULL DEFAULT NULL ,
  `fax` VARCHAR(20) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`contact_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `org_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `org_hist` ;

CREATE  TABLE IF NOT EXISTS `org_hist` (
  `org_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_type_id` INT UNSIGNED NULL DEFAULT NULL ,
  `scope_id` INT UNSIGNED NULL DEFAULT NULL ,
  `parent_org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `code` VARCHAR(50) NULL DEFAULT NULL ,
  `name` VARCHAR(100) NULL DEFAULT NULL ,
  `local_code` VARCHAR(50) NULL DEFAULT NULL ,
  `address_line1` VARCHAR(100) NULL DEFAULT NULL ,
  `address_line2` VARCHAR(100) NULL DEFAULT NULL ,
  `city` VARCHAR(50) NULL DEFAULT NULL ,
  `state` VARCHAR(2) NULL DEFAULT NULL ,
  `zip` VARCHAR(10) NULL DEFAULT NULL ,
  `phone` VARCHAR(20) NULL DEFAULT NULL ,
  `phone_extension` VARCHAR(10) NULL DEFAULT NULL ,
  `fax` VARCHAR(20) NULL DEFAULT NULL ,
  `inactive` TINYINT(1) NULL DEFAULT '0' ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`org_hist_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 13;


-- -----------------------------------------------------
-- Table `student_group_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_group_hist` ;

CREATE  TABLE IF NOT EXISTS `student_group_hist` (
  `student_group_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_group_id` INT UNSIGNED NULL DEFAULT NULL ,
  `group_type_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_part_id` INT UNSIGNED NULL DEFAULT NULL ,
  `name` VARCHAR(100) NULL DEFAULT NULL ,
  `description` VARCHAR(1000) NULL DEFAULT NULL ,
  `grade` VARCHAR(50) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_group_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_group_assign_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_group_assign_hist` ;

CREATE  TABLE IF NOT EXISTS `student_group_assign_hist` (
  `student_group_assign_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_group_assign_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_group_id` INT UNSIGNED NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_group_assign_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_enroll_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_enroll_hist` ;

CREATE  TABLE IF NOT EXISTS `student_enroll_hist` (
  `student_enroll_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_enroll_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_part_id` INT UNSIGNED NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_enroll_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `device`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `device` ;

CREATE  TABLE IF NOT EXISTS `device` (
  `device_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(200) NOT NULL COMMENT 'Name of the device' ,
  `location` VARCHAR(200) NULL COMMENT 'Location (ex: lab1) of the device' ,
  `count` INT NULL DEFAULT 1 COMMENT 'The number of devices with this identical configuration.' ,
  `operating_system` VARCHAR(200) NULL COMMENT 'Operation system (possible values in option_list)' ,
  `processor` INT NULL COMMENT 'Type of processor (possible values in option_list)' ,
  `memory` INT NULL COMMENT 'Memory installed (possible values in option_list)' ,
  `storage` INT NULL COMMENT 'Storage (hard disk, etc) space (possible values in option_list)' ,
  `flash_version` INT NULL COMMENT 'Version of the flash player (possible values in option_list)' ,
  `java_version` INT NULL COMMENT 'Java version installed (possible values in option_list)' ,
  `browser` INT NULL COMMENT 'Primary browser used (possible values in option_list)' ,
  `screen_resolution` INT NULL COMMENT 'Size of the display (possible values in option_list)' ,
  `display_size` INT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`device_id`) ,
  CONSTRAINT `device_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `device_org_fki` ON `device` (`org_id` ASC) ;


-- -----------------------------------------------------
-- Table `device_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `device_hist` ;

CREATE  TABLE IF NOT EXISTS `device_hist` (
  `device_hist_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `device_id` INT UNSIGNED NULL DEFAULT NULL ,
  `org_id` INT UNSIGNED NULL DEFAULT NULL ,
  `location` VARCHAR(200) NULL ,
  `name` VARCHAR(200) NULL DEFAULT NULL ,
  `count` INT NULL DEFAULT NULL ,
  `operating_system` VARCHAR(200) NULL DEFAULT NULL ,
  `processor` INT NULL DEFAULT NULL ,
  `memory` INT NULL DEFAULT NULL ,
  `storage` INT NULL DEFAULT NULL ,
  `flash_version` INT NULL DEFAULT NULL ,
  `java_version` INT NULL DEFAULT NULL ,
  `browser` INT NULL DEFAULT NULL ,
  `screen_resolution` INT NULL DEFAULT NULL ,
  `display_size` INT NULL ,
  `change_type` CHAR(1) NULL DEFAULT NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`device_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `device_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `device_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `device_ext_hist` (
  `device_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `device_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `device_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL DEFAULT NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`device_ext_hist_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 8
AVG_ROW_LENGTH = 2340;


-- -----------------------------------------------------
-- Table `device_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `device_ext` ;

CREATE  TABLE IF NOT EXISTS `device_ext` (
  `device_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `device_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`device_ext_id`) ,
  CONSTRAINT `deviceext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `deviceext_device_fk`
    FOREIGN KEY (`device_id` )
    REFERENCES `device` (`device_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 39
AVG_ROW_LENGTH = 585;

CREATE INDEX `deviceext_entityfield_fki` ON `device_ext` (`entity_field_id` ASC) ;

CREATE INDEX `deviceext_device_fki` ON `device_ext` (`device_id` ASC) ;

CREATE INDEX `deviceext_value_entityfieldid_i` ON `device_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE UNIQUE INDEX `deviceext_deviceid_entityfieldid_uc` ON `device_ext` (`device_id` ASC, `entity_field_id` ASC) ;


-- -----------------------------------------------------
-- Table `role_delegation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role_delegation` ;

CREATE  TABLE IF NOT EXISTS `role_delegation` (
  `role_delegation_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `role_id` INT UNSIGNED NOT NULL ,
  `delegated_role_id` INT UNSIGNED NOT NULL ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`role_delegation_id`) ,
  CONSTRAINT `roledelegation_role_fk`
    FOREIGN KEY (`role_id` )
    REFERENCES `role` (`role_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `roledelegation_delegatedrole_fk`
    FOREIGN KEY (`delegated_role_id` )
    REFERENCES `role` (`role_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `roledelegation_role_fki` ON `role_delegation` (`role_id` ASC) ;

CREATE INDEX `roledelegation_delegatedrole_fki` ON `role_delegation` (`delegated_role_id` ASC) ;


-- -----------------------------------------------------
-- Table `student_group_ext_hist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_group_ext_hist` ;

CREATE  TABLE IF NOT EXISTS `student_group_ext_hist` (
  `student_group_ext_hist_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_group_ext_id` INT UNSIGNED NULL DEFAULT NULL ,
  `student_group_id` INT UNSIGNED NULL DEFAULT NULL ,
  `entity_field_id` INT UNSIGNED NULL DEFAULT NULL ,
  `value` VARCHAR(500) NULL DEFAULT NULL ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  `change_type` CHAR(1) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_group_ext_hist_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `student_group_ext`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `student_group_ext` ;

CREATE  TABLE IF NOT EXISTS `student_group_ext` (
  `student_group_ext_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `student_group_id` INT UNSIGNED NOT NULL ,
  `entity_field_id` INT UNSIGNED NOT NULL ,
  `value` VARCHAR(500) NOT NULL COMMENT 'Value for the \"key\" referenced by the entity_field_id.' ,
  `change_date` DATETIME NULL DEFAULT NULL ,
  `change_user` VARCHAR(100) NULL DEFAULT NULL ,
  PRIMARY KEY (`student_group_ext_id`) ,
  CONSTRAINT `studentgroupext_entityfield_fk`
    FOREIGN KEY (`entity_field_id` )
    REFERENCES `entity_field` (`entity_field_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `studentgroupext_studentgroup_fk`
    FOREIGN KEY (`student_group_id` )
    REFERENCES `student_group` (`student_group_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `studentgroupext_entityfield_fki` ON `student_group_ext` (`entity_field_id` ASC) ;

CREATE INDEX `studentgroupext_value_entityfieldid_i` ON `student_group_ext` (`value`(10) ASC, `entity_field_id` ASC) ;

CREATE INDEX `studentgroupext_studentgroup_fki` ON `student_group_ext` (`student_group_id` ASC) ;


-- -----------------------------------------------------
-- Table `file_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `file_type` ;

CREATE  TABLE IF NOT EXISTS `file_type` (
  `file_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(50) NOT NULL COMMENT 'Unique code that can be used by import, exports, etc.' ,
  `name` VARCHAR(100) NOT NULL COMMENT 'Unique name that is displayed to the user.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`file_type_id`) )
ENGINE = InnoDB;

CREATE UNIQUE INDEX `filetype_code_uc` ON `file_type` (`code` ASC) ;

CREATE UNIQUE INDEX `filetype_name_uc` ON `file_type` (`name` ASC) ;


-- -----------------------------------------------------
-- Table `file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `file` ;

CREATE  TABLE IF NOT EXISTS `file` (
  `file_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `org_id` INT UNSIGNED NOT NULL ,
  `file_type_id` INT UNSIGNED NOT NULL ,
  `user_id` INT UNSIGNED NOT NULL ,
  `status` ENUM('pending','processing','complete','errors') NOT NULL DEFAULT 'pending' COMMENT 'Indicates the state of the processing of the file.\n<br><u>pending</u>:Processing has not yet started.\n<br><u>processing</u>:Currently processing the file.\n<br><u>complete</u>:Processing completed succesfully.\n<br><u>errors</u>:Processing completed with errors.' ,
  `path` VARCHAR(200) NOT NULL COMMENT 'The file system path used to access the file.' ,
  `filename` VARCHAR(100) NOT NULL COMMENT 'This is the real (generated) filename used to reference the file within the file system.' ,
  `display_filename` VARCHAR(100) NOT NULL COMMENT 'The name of the file that will be displayed to the user (ex: original uploaded filename).' ,
  `error_data_filename` VARCHAR(100) NULL ,
  `error_message_filename` VARCHAR(100) NULL ,
  `status_message` VARCHAR(1000) NULL COMMENT 'A detailed description (ex: 7 of the 99 rows had errors) of the status of the file.' ,
  `mode` ENUM('overwrite','append','replace') NULL DEFAULT 'overwrite' COMMENT 'How should the data in this file be processed? \n<br><u>overwrite</u>: Perform matching... overwrite existing data and insert new data.\n<br><u>append</u>: Simply insert (no updates) the records in the file.\n<br><u>replace</u>: Delete all of the data for this org and then insert the records in this file.\n' ,
  `request_date` DATETIME NULL COMMENT 'The date the user requested processing of the file.' ,
  `description` VARCHAR(500) NULL COMMENT 'An option user provided description of the file.' ,
  `total_record_count` INT NULL COMMENT 'Count of the number of records contained in the file ... provided by the first step in the batch job.' ,
  `kilobytes` INT NULL COMMENT 'The size of the file in kilobytes.' ,
  `batch_job_execution_id` BIGINT NULL COMMENT 'The ID of the Spring Batch job execution table ... used to get detailed information about the processing of the file.' ,
  `change_date` DATETIME NULL ,
  `change_user` VARCHAR(100) NULL ,
  PRIMARY KEY (`file_id`) ,
  CONSTRAINT `file_org_fk`
    FOREIGN KEY (`org_id` )
    REFERENCES `org` (`org_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `file_filetype_fk`
    FOREIGN KEY (`file_type_id` )
    REFERENCES `file_type` (`file_type_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `file_user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `file_org_fki` ON `file` (`org_id` ASC) ;

CREATE INDEX `file_filetype_fki` ON `file` (`file_type_id` ASC) ;

CREATE INDEX `file_user_fki` ON `file` (`user_id` ASC) ;

CREATE UNIQUE INDEX `file_filename_uc` ON `file` (`filename` ASC) ;


-- -----------------------------------------------------
-- Table `file_error`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `file_error` ;

CREATE  TABLE IF NOT EXISTS `file_error` (
  `file_error_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `file_id` INT UNSIGNED NOT NULL ,
  `record_number` INT NULL COMMENT 'The record number (line number for CSV, entity number for XML, etc) that has the error.  A null value indicates that the error is global and not specific to any record.' ,
  `error_code` VARCHAR(100) NOT NULL COMMENT 'Unique code that indicates the specific error being reported.' ,
  `message` VARCHAR(1000) NOT NULL COMMENT 'The detailed discription of the error to be displayed to the user.' ,
  PRIMARY KEY (`file_error_id`) ,
  CONSTRAINT `fileerror_file_fk`
    FOREIGN KEY (`file_id` )
    REFERENCES `file` (`file_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fileerror_file_fki` ON `file_error` (`file_id` ASC) ;


-- -----------------------------------------------------
-- Table `temp_tree`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `temp_tree` ;

CREATE  TABLE IF NOT EXISTS `temp_tree` (
  `root_id` INT(10) UNSIGNED NOT NULL ,
  `descendant_id` INT(10) UNSIGNED NOT NULL ,
  `distance` INT(10) UNSIGNED NOT NULL ,
  `table_name` VARCHAR(64) NOT NULL ,
  PRIMARY KEY (`root_id`, `descendant_id`, `table_name`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

USE `core_batch` ;

-- -----------------------------------------------------
-- Table `batch_job_instance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_job_instance` ;

CREATE  TABLE IF NOT EXISTS `batch_job_instance` (
  `JOB_INSTANCE_ID` BIGINT(20) NOT NULL ,
  `VERSION` BIGINT(20) NULL DEFAULT NULL ,
  `JOB_NAME` VARCHAR(100) NOT NULL ,
  `JOB_KEY` VARCHAR(32) NOT NULL ,
  PRIMARY KEY (`JOB_INSTANCE_ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `JOB_INST_UN` ON `batch_job_instance` (`JOB_NAME` ASC, `JOB_KEY` ASC) ;


-- -----------------------------------------------------
-- Table `batch_job_execution`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_job_execution` ;

CREATE  TABLE IF NOT EXISTS `batch_job_execution` (
  `JOB_EXECUTION_ID` BIGINT(20) NOT NULL ,
  `VERSION` BIGINT(20) NULL DEFAULT NULL ,
  `JOB_INSTANCE_ID` BIGINT(20) NOT NULL ,
  `CREATE_TIME` DATETIME NOT NULL ,
  `START_TIME` DATETIME NULL DEFAULT NULL ,
  `END_TIME` DATETIME NULL DEFAULT NULL ,
  `STATUS` VARCHAR(10) NULL DEFAULT NULL ,
  `EXIT_CODE` VARCHAR(100) NULL DEFAULT NULL ,
  `EXIT_MESSAGE` VARCHAR(2500) NULL DEFAULT NULL ,
  `LAST_UPDATED` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`JOB_EXECUTION_ID`) ,
  CONSTRAINT `JOB_INST_EXEC_FC`
    FOREIGN KEY (`JOB_INSTANCE_ID` )
    REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `JOB_INST_EXEC_FK` ON `batch_job_execution` (`JOB_INSTANCE_ID` ASC) ;


-- -----------------------------------------------------
-- Table `batch_job_execution_context`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_job_execution_context` ;

CREATE  TABLE IF NOT EXISTS `batch_job_execution_context` (
  `JOB_EXECUTION_ID` BIGINT(20) NOT NULL ,
  `SHORT_CONTEXT` VARCHAR(2500) NOT NULL ,
  `SERIALIZED_CONTEXT` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`JOB_EXECUTION_ID`) ,
  CONSTRAINT `JOB_EXEC_CTX_FK`
    FOREIGN KEY (`JOB_EXECUTION_ID` )
    REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `batch_job_execution_seq`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_job_execution_seq` ;

CREATE  TABLE IF NOT EXISTS `batch_job_execution_seq` (
  `ID` BIGINT(20) NOT NULL )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `batch_job_params`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_job_params` ;

CREATE  TABLE IF NOT EXISTS `batch_job_params` (
  `JOB_INSTANCE_ID` BIGINT(20) NOT NULL ,
  `TYPE_CD` VARCHAR(6) NOT NULL ,
  `KEY_NAME` VARCHAR(100) NOT NULL ,
  `STRING_VAL` VARCHAR(250) NULL DEFAULT NULL ,
  `DATE_VAL` DATETIME NULL DEFAULT NULL ,
  `LONG_VAL` BIGINT(20) NULL DEFAULT NULL ,
  `DOUBLE_VAL` DOUBLE(11,11) NULL DEFAULT NULL ,
  CONSTRAINT `JOB_INST_PARAMS_FC`
    FOREIGN KEY (`JOB_INSTANCE_ID` )
    REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `JOB_INST_PARAMS_FK` ON `batch_job_params` (`JOB_INSTANCE_ID` ASC) ;


-- -----------------------------------------------------
-- Table `batch_job_seq`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_job_seq` ;

CREATE  TABLE IF NOT EXISTS `batch_job_seq` (
  `ID` BIGINT(20) NOT NULL )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `batch_step_execution`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_step_execution` ;

CREATE  TABLE IF NOT EXISTS `batch_step_execution` (
  `STEP_EXECUTION_ID` BIGINT(20) NOT NULL ,
  `VERSION` BIGINT(20) NOT NULL ,
  `STEP_NAME` VARCHAR(100) NOT NULL ,
  `JOB_EXECUTION_ID` BIGINT(20) NOT NULL ,
  `START_TIME` DATETIME NOT NULL ,
  `END_TIME` DATETIME NULL DEFAULT NULL ,
  `STATUS` VARCHAR(10) NULL DEFAULT NULL ,
  `COMMIT_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `READ_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `FILTER_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `WRITE_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `READ_SKIP_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `WRITE_SKIP_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `PROCESS_SKIP_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `ROLLBACK_COUNT` BIGINT(20) NULL DEFAULT NULL ,
  `EXIT_CODE` VARCHAR(100) NULL DEFAULT NULL ,
  `EXIT_MESSAGE` VARCHAR(2500) NULL DEFAULT NULL ,
  `LAST_UPDATED` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`STEP_EXECUTION_ID`) ,
  CONSTRAINT `JOB_EXEC_STEP_FC`
    FOREIGN KEY (`JOB_EXECUTION_ID` )
    REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `JOB_EXEC_STEP_FK` ON `batch_step_execution` (`JOB_EXECUTION_ID` ASC) ;


-- -----------------------------------------------------
-- Table `batch_step_execution_context`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_step_execution_context` ;

CREATE  TABLE IF NOT EXISTS `batch_step_execution_context` (
  `STEP_EXECUTION_ID` BIGINT(20) NOT NULL ,
  `SHORT_CONTEXT` VARCHAR(2500) NOT NULL ,
  `SERIALIZED_CONTEXT` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`STEP_EXECUTION_ID`) ,
  CONSTRAINT `STEP_EXEC_CTX_FK`
    FOREIGN KEY (`STEP_EXECUTION_ID` )
    REFERENCES `batch_step_execution` (`STEP_EXECUTION_ID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `batch_step_execution_seq`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `batch_step_execution_seq` ;

CREATE  TABLE IF NOT EXISTS `batch_step_execution_seq` (
  `ID` BIGINT(20) NOT NULL )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `qrtz_job_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_job_details` ;

CREATE  TABLE IF NOT EXISTS `qrtz_job_details` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `JOB_NAME` VARCHAR(200) NOT NULL ,
  `JOB_GROUP` VARCHAR(200) NOT NULL ,
  `DESCRIPTION` VARCHAR(250) NULL DEFAULT NULL ,
  `JOB_CLASS_NAME` VARCHAR(250) NOT NULL ,
  `IS_DURABLE` VARCHAR(1) NOT NULL ,
  `IS_NONCONCURRENT` VARCHAR(1) NOT NULL ,
  `IS_UPDATE_DATA` VARCHAR(1) NOT NULL ,
  `REQUESTS_RECOVERY` VARCHAR(1) NOT NULL ,
  `JOB_DATA` BLOB NULL DEFAULT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `IDX_QRTZ_J_REQ_RECOVERY` ON `qrtz_job_details` (`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC) ;

CREATE INDEX `IDX_QRTZ_J_GRP` ON `qrtz_job_details` (`SCHED_NAME` ASC, `JOB_GROUP` ASC) ;


-- -----------------------------------------------------
-- Table `qrtz_triggers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_triggers` ;

CREATE  TABLE IF NOT EXISTS `qrtz_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  `JOB_NAME` VARCHAR(200) NOT NULL ,
  `JOB_GROUP` VARCHAR(200) NOT NULL ,
  `DESCRIPTION` VARCHAR(250) NULL DEFAULT NULL ,
  `NEXT_FIRE_TIME` BIGINT(13) NULL DEFAULT NULL ,
  `PREV_FIRE_TIME` BIGINT(13) NULL DEFAULT NULL ,
  `PRIORITY` INT(11) NULL DEFAULT NULL ,
  `TRIGGER_STATE` VARCHAR(16) NOT NULL ,
  `TRIGGER_TYPE` VARCHAR(8) NOT NULL ,
  `START_TIME` BIGINT(13) NOT NULL ,
  `END_TIME` BIGINT(13) NULL DEFAULT NULL ,
  `CALENDAR_NAME` VARCHAR(200) NULL DEFAULT NULL ,
  `MISFIRE_INSTR` SMALLINT(2) NULL DEFAULT NULL ,
  `JOB_DATA` BLOB NULL DEFAULT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `JOB_NAME` , `JOB_GROUP` )
    REFERENCES `qrtz_job_details` (`SCHED_NAME` , `JOB_NAME` , `JOB_GROUP` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `SCHED_NAME` ON `qrtz_triggers` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_T_J` ON `qrtz_triggers` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_T_JG` ON `qrtz_triggers` (`SCHED_NAME` ASC, `JOB_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_T_C` ON `qrtz_triggers` (`SCHED_NAME` ASC, `CALENDAR_NAME` ASC) ;

CREATE INDEX `IDX_QRTZ_T_G` ON `qrtz_triggers` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_T_STATE` ON `qrtz_triggers` (`SCHED_NAME` ASC, `TRIGGER_STATE` ASC) ;

CREATE INDEX `IDX_QRTZ_T_N_STATE` ON `qrtz_triggers` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) ;

CREATE INDEX `IDX_QRTZ_T_N_G_STATE` ON `qrtz_triggers` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) ;

CREATE INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME` ON `qrtz_triggers` (`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC) ;

CREATE INDEX `IDX_QRTZ_T_NFT_ST` ON `qrtz_triggers` (`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC) ;

CREATE INDEX `IDX_QRTZ_T_NFT_MISFIRE` ON `qrtz_triggers` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC) ;

CREATE INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE` ON `qrtz_triggers` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC) ;

CREATE INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` ON `qrtz_triggers` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) ;


-- -----------------------------------------------------
-- Table `qrtz_blob_triggers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers` ;

CREATE  TABLE IF NOT EXISTS `qrtz_blob_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  `BLOB_DATA` BLOB NULL DEFAULT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` )
    REFERENCES `qrtz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `SCHED_NAME` ON `qrtz_blob_triggers` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) ;


-- -----------------------------------------------------
-- Table `qrtz_calendars`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_calendars` ;

CREATE  TABLE IF NOT EXISTS `qrtz_calendars` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `CALENDAR_NAME` VARCHAR(200) NOT NULL ,
  `CALENDAR` BLOB NOT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `qrtz_cron_triggers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers` ;

CREATE  TABLE IF NOT EXISTS `qrtz_cron_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  `CRON_EXPRESSION` VARCHAR(120) NOT NULL ,
  `TIME_ZONE_ID` VARCHAR(80) NULL DEFAULT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` )
    REFERENCES `qrtz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `SCHED_NAME` ON `qrtz_cron_triggers` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) ;


-- -----------------------------------------------------
-- Table `qrtz_fired_triggers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers` ;

CREATE  TABLE IF NOT EXISTS `qrtz_fired_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `ENTRY_ID` VARCHAR(95) NOT NULL ,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  `INSTANCE_NAME` VARCHAR(200) NOT NULL ,
  `FIRED_TIME` BIGINT(13) NOT NULL ,
  `PRIORITY` INT(11) NOT NULL ,
  `STATE` VARCHAR(16) NOT NULL ,
  `JOB_NAME` VARCHAR(200) NULL DEFAULT NULL ,
  `JOB_GROUP` VARCHAR(200) NULL DEFAULT NULL ,
  `IS_NONCONCURRENT` VARCHAR(1) NULL DEFAULT NULL ,
  `REQUESTS_RECOVERY` VARCHAR(1) NULL DEFAULT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `IDX_QRTZ_FT_TRIG_INST_NAME` ON `qrtz_fired_triggers` (`SCHED_NAME` ASC, `INSTANCE_NAME` ASC) ;

CREATE INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` ON `qrtz_fired_triggers` (`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC) ;

CREATE INDEX `IDX_QRTZ_FT_J_G` ON `qrtz_fired_triggers` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_FT_JG` ON `qrtz_fired_triggers` (`SCHED_NAME` ASC, `JOB_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_FT_T_G` ON `qrtz_fired_triggers` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) ;

CREATE INDEX `IDX_QRTZ_FT_TG` ON `qrtz_fired_triggers` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) ;


-- -----------------------------------------------------
-- Table `qrtz_locks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_locks` ;

CREATE  TABLE IF NOT EXISTS `qrtz_locks` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `LOCK_NAME` VARCHAR(40) NOT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `qrtz_paused_trigger_grps`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps` ;

CREATE  TABLE IF NOT EXISTS `qrtz_paused_trigger_grps` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `qrtz_scheduler_state`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state` ;

CREATE  TABLE IF NOT EXISTS `qrtz_scheduler_state` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `INSTANCE_NAME` VARCHAR(200) NOT NULL ,
  `LAST_CHECKIN_TIME` BIGINT(13) NOT NULL ,
  `CHECKIN_INTERVAL` BIGINT(13) NOT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `qrtz_simple_triggers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers` ;

CREATE  TABLE IF NOT EXISTS `qrtz_simple_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  `REPEAT_COUNT` BIGINT(7) NOT NULL ,
  `REPEAT_INTERVAL` BIGINT(12) NOT NULL ,
  `TIMES_TRIGGERED` BIGINT(10) NOT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` )
    REFERENCES `qrtz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `SCHED_NAME` ON `qrtz_simple_triggers` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) ;


-- -----------------------------------------------------
-- Table `qrtz_simprop_triggers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers` ;

CREATE  TABLE IF NOT EXISTS `qrtz_simprop_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL ,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL ,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL ,
  `STR_PROP_1` VARCHAR(512) NULL DEFAULT NULL ,
  `STR_PROP_2` VARCHAR(512) NULL DEFAULT NULL ,
  `STR_PROP_3` VARCHAR(512) NULL DEFAULT NULL ,
  `INT_PROP_1` INT(11) NULL DEFAULT NULL ,
  `INT_PROP_2` INT(11) NULL DEFAULT NULL ,
  `LONG_PROP_1` BIGINT(20) NULL DEFAULT NULL ,
  `LONG_PROP_2` BIGINT(20) NULL DEFAULT NULL ,
  `DEC_PROP_1` DECIMAL(13,4) NULL DEFAULT NULL ,
  `DEC_PROP_2` DECIMAL(13,4) NULL DEFAULT NULL ,
  `BOOL_PROP_1` VARCHAR(1) NULL DEFAULT NULL ,
  `BOOL_PROP_2` VARCHAR(1) NULL DEFAULT NULL ,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` )
    REFERENCES `qrtz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `batch_job_execution_seq`
-- -----------------------------------------------------
START TRANSACTION;
USE `core_batch`;
INSERT INTO `batch_job_execution_seq` (`ID`) VALUES (0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `batch_job_seq`
-- -----------------------------------------------------
START TRANSACTION;
USE `core_batch`;
INSERT INTO `batch_job_seq` (`ID`) VALUES (0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `batch_step_execution_seq`
-- -----------------------------------------------------
START TRANSACTION;
USE `core_batch`;
INSERT INTO `batch_step_execution_seq` (`ID`) VALUES (0);

COMMIT;