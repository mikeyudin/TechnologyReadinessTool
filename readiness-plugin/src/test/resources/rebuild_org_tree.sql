-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS rebuild_org_tree; //

CREATE PROCEDURE rebuild_org_tree(in p_org_id BIGINT)
BEGIN
  DECLARE v_count int unsigned default(0);
  DECLARE v_done tinyint unsigned default(0);
  DECLARE v_id BIGINT;
  DECLARE v_ancestor_org_id BIGINT;
  DECLARE v_ancestor_path VARCHAR(700);
  DECLARE v_path VARCHAR(700);
  DECLARE v_depth TINYINT;
  DECLARE v_code VARCHAR(700);
  DECLARE v_org_id BIGINT;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

  drop temporary table if exists temp_org_hier;
  create temporary table temp_org_hier(
    id bigint NOT NULL AUTO_INCREMENT,
    ancestor_org_id bigint, 
    org_id bigint,
    PRIMARY KEY (id)
  )engine = memory;

  -- Remove all data related to p_org_id from relation table 
  IF p_org_id IS NULL THEN
    DELETE FROM org_tree;
 ELSE
    DELETE FROM org_tree
    WHERE org_id in 
    (
      select org_id from
      (select 
        org_id 
      FROM
        org_tree
      WHERE
        ancestor_org_id=p_org_id) as x
    );
  END IF;
  
  -- initialize the temporary table with the org passed in
  if p_org_id is not null then
    insert into temp_org_hier
      (ancestor_org_id, org_id)
      (select parent_org_id, org_id from org where org_id = p_org_id);
  else
    insert into temp_org_hier
      (ancestor_org_id, org_id)
      (select parent_org_id, org_id from org where parent_org_id is null);
  end if;
  
  select count(*), if(count(*)>0,FALSE,TRUE) into v_count, v_done from temp_org_hier;

  -- while the temporary table is not empty
  while not v_done DO
  BEGIN
    DECLARE cur1 CURSOR FOR SELECT id, ancestor_org_id, org_id FROM temp_org_hier;
   
    OPEN cur1;
    --    for each row (s1) in temporary table
    read_loop: LOOP
      FETCH cur1 INTO v_id, v_ancestor_org_id, v_org_id;
      IF v_done THEN
        LEAVE read_loop;
      END IF;
      
      SELECT s.code INTO v_code FROM org s WHERE s.org_id=v_org_id;
      
      SET v_ancestor_path=null;
      SELECT sr.path INTO v_ancestor_path
        FROM org_tree sr
        WHERE sr.org_id=v_ancestor_org_id
        LIMIT 1;
      SET v_path=CONCAT(IFNULL(v_ancestor_path,''),'/',v_code);
      SET v_ancestor_path=IFNULL(v_ancestor_path,'/');
      SET v_depth=LENGTH(v_path) - LENGTH(REPLACE(v_path,'/',''));
      
      -- add new self relation
      insert into org_tree
        (ancestor_org_id,ancestor_path,org_id,path, distance, depth)
        values (v_org_id, v_path, v_org_id, v_path, 0, v_depth);

      -- add new relations for the current row and it's ancestor
      insert into org_tree
        (ancestor_org_id,ancestor_path,org_id,path, distance, depth)
        values (v_ancestor_org_id, v_ancestor_path, v_org_id, v_path,1, v_depth);

      -- add new relations based on ancestors relations
      insert into org_tree
        (ancestor_org_id,ancestor_path,org_id,path, distance, depth)
        (select ancestor_org_id, ancestor_path, v_org_id, CONCAT(path,'/',v_code),
          distance+1 , LENGTH(path) - LENGTH(REPLACE(path,'/','') + 1)
          from org_tree where org_id = v_ancestor_org_id and distance>0);
      
      -- mark s1 as processed in temporary table
      delete from temp_org_hier where id=v_id;
      
      -- add the children to temporary table
      insert into temp_org_hier
        (ancestor_org_id, org_id)
        (select parent_org_id, org_id from org where parent_org_id = v_org_id);

    END LOOP;
    CLOSE cur1;

    select count(*), if(count(*)>0,FALSE,TRUE) into v_count, v_done from temp_org_hier;
    END;
  END WHILE;
    
  drop temporary table if exists temp_org_hier;

end; //
