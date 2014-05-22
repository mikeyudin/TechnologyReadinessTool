DROP TRIGGER IF EXISTS org_after_insert; //

CREATE TRIGGER org_after_insert AFTER INSERT ON org FOR EACH ROW
BEGIN
  call rebuild_org_tree(NEW.org_id);
END; //

DROP TRIGGER IF EXISTS org_after_update; //

CREATE TRIGGER org_after_update AFTER UPDATE ON org FOR EACH ROW
BEGIN
  IF IFNULL(NEW.code,'') != IFNULL(OLD.code,'')
     OR IFNULL(NEW.parent_org_id,'') != IFNULL(OLD.parent_org_id,'') THEN
    CALL rebuild_org_tree(NEW.org_id);
  END IF;
END; //
