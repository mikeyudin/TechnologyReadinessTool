

CREATE PROCEDURE rebuild_scope_tree(in p_scope_id BIGINT)
BEGIN
  DECLARE v_count int unsigned default(0);
  DECLARE v_done tinyint unsigned default(0);
  DECLARE v_id BIGINT;
  DECLARE v_ancestor_scope_id BIGINT;
  DECLARE v_ancestor_path VARCHAR(700);
  DECLARE v_ancestor_depth TINYINT;
  DECLARE v_path VARCHAR(700);
  DECLARE v_depth TINYINT;
  DECLARE v_code VARCHAR(700);
  DECLARE v_scope_id BIGINT;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

  drop temporary table if exists temp_scope_hier;
  create temporary table temp_scope_hier(
    id bigint NOT NULL AUTO_INCREMENT,
    ancestor_scope_id bigint, 
    scope_id bigint,
    PRIMARY KEY (id)
  )engine = memory;

  -- Remove all data related to p_scope_id from relation table 
  IF p_scope_id IS NULL THEN
    DELETE FROM scope_tree;
 ELSE
    DELETE FROM scope_tree
    WHERE scope_id in 
    (
      select scope_id from
      (select 
        scope_id 
      FROM
        scope_tree
      WHERE
        ancestor_scope_id=p_scope_id) as x
    );
  END IF;
  
  -- initialize the temporary table with the scope passed in
  if p_scope_id is not null then
    insert into temp_scope_hier
      (ancestor_scope_id, scope_id)
      (select parent_scope_id, scope_id from scope where scope_id = p_scope_id);
  else
    insert into temp_scope_hier
      (ancestor_scope_id, scope_id)
      (select parent_scope_id, scope_id from scope where parent_scope_id is null);
  end if;
  
  select count(*), if(count(*)>0,FALSE,TRUE) into v_count, v_done from temp_scope_hier;

  -- while the temporary table is not empty
  while not v_done DO
  BEGIN
    DECLARE cur1 CURSOR FOR SELECT id, ancestor_scope_id, scope_id FROM temp_scope_hier;

    OPEN cur1;
    read_loop: LOOP
      FETCH cur1 INTO v_id, v_ancestor_scope_id, v_scope_id;
      IF v_done THEN
        LEAVE read_loop;
      END IF;
      
      SELECT s.code INTO v_code FROM scope s WHERE s.scope_id=v_scope_id;
      SET v_ancestor_path=null;
      SELECT sr.path INTO v_ancestor_path
        FROM scope_tree sr
        WHERE sr.scope_id=v_ancestor_scope_id
        LIMIT 1;
      
      -- The scope with code 'root' is treated specially to treat it like the
      -- root of the path ... similar to the '/' of a filesystem path.
      IF v_code='root' THEN
        SET v_path='/';
        SET v_ancestor_path='/';
        SET v_depth=0;
	SET v_ancestor_depth = 0;
      ELSE
        SET v_path=CONCAT(IF(v_ancestor_path='/','',v_ancestor_path),'/',v_code);
        SET v_depth=LENGTH(v_path) - LENGTH(REPLACE(v_path,'/',''));
        SET v_ancestor_path=IFNULL(v_ancestor_path,'/');
        IF v_ancestor_path='/' THEN
	   SET v_ancestor_depth = 0;
        ELSE
	   SET v_ancestor_depth = LENGTH(v_ancestor_path) - LENGTH(REPLACE(v_ancestor_path,'/',''));
        END IF;
      END IF;
      
      
      -- add new self relation
      insert into scope_tree
        (ancestor_scope_id,ancestor_path, ancestor_depth, scope_id,path, distance,depth)
        values (v_scope_id, v_path, v_depth,  v_scope_id, v_path, 0,v_depth);

      -- 'root' is treated as '/' ... so skip the ancestor relation inserts.
      IF v_code != 'root' THEN
        -- add new relations for the current row and it's ancestor
        insert into scope_tree
          (ancestor_scope_id,ancestor_path, ancestor_depth,scope_id,path, distance, depth)
          values (v_ancestor_scope_id, v_ancestor_path, v_ancestor_depth, v_scope_id, v_path,1,v_depth);
  
        -- add new relations based on ancestors relations
        insert into scope_tree
          (ancestor_scope_id,ancestor_path, ancestor_depth,scope_id,path, distance,depth)
          (select ancestor_scope_id, ancestor_path, 
            case when ancestor_path='/' then 0 else LENGTH(ancestor_path) - LENGTH(REPLACE(ancestor_path,'/',''))end anc_depth,
            v_scope_id, CONCAT(path,'/',v_code), distance+1,v_depth
            from scope_tree where scope_id = v_ancestor_scope_id and distance>0);
      END IF;

      -- mark s1 as processed in temporary table
      delete from temp_scope_hier where id=v_id;
      
      -- add the children to temporary table
      insert into temp_scope_hier
        (ancestor_scope_id, scope_id)
        (select parent_scope_id, scope_id from scope where parent_scope_id = v_scope_id);

    END LOOP;
    CLOSE cur1;

    select count(*), if(count(*)>0,FALSE,TRUE) into v_count, v_done from temp_scope_hier;
    END;
  END WHILE;
    
  drop temporary table if exists temp_scope_hier;

END; //
