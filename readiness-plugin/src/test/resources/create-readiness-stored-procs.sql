CREATE PROCEDURE  summaryDeviceCounts (IN ancestorOrgId int,
                            IN orgTypeName varchar(100),
                            IN processorListCode varchar(100),
                            IN processorListValue varchar(100),
                            IN osListCode varchar(100),
                            IN osListValue varchar(100),
                            IN storageListCode varchar(100),
                            IN storageListValue varchar(100),
                            IN memoryListCode varchar(100),
                            IN memoryListValue varchar(100),
                            IN orderBy varchar(100),
                            IN orgNameFilter varchar(100),
                            IN orgCodeFilter varchar(100), 
                            IN scopeId int,
                            IN firstRow int,
                            IN lastRow int) READS SQL DATA
BEGIN

SET @orderByColumn := '';
SET @orgNameFilterCriteria := '';
SET @orgCodeFilterCriteria := '';
   
IF orderBy = 'org_code' THEN
   SET @orderByColumn := 'ORDER BY org_code ';
ELSE 
   SET @orderByColumn := 'ORDER BY org_name ';
END IF;

IF (orgNameFilter IS NULL) THEN
    SET @orgNameFilterCriteria := '1=1';         
ELSE
    SET @orgNameFilterCriteria := 'UPPER(org.name) like UPPER(?)';    
END IF;

IF (orgCodeFilter IS NULL) THEN
   SET @orgCodeFilterCriteria := '1=1';   
ELSE 
   SET @orgCodeFilterCriteria := 'UPPER(org.code) like UPPER(?)';
END IF;

DROP TEMPORARY TABLE IF EXISTS totalOrgs;
DROP TEMPORARY TABLE IF EXISTS processorOrgs;
DROP TEMPORARY TABLE IF EXISTS osOrgs;
DROP TEMPORARY TABLE IF EXISTS memoryOrgs;
DROP TEMPORARY TABLE IF EXISTS storageOrgs;

SET @tempTotalOrgsddl :=
'CREATE TEMPORARY TABLE totalOrgs ENGINE=MEMORY
select parentOrgs.org_name,
       parentOrgs.org_code,
       parentOrgs.scope_name,
       parentOrgs.org_id parent_org_id,   
       org_part.org_id,   
       org_part.org_part_id
from org_tree,
       org_part,
       scope,
       (select org.org_id, 
               org.name org_name,
               org.code org_code,
               scope.name scope_name,
               org_part.org_part_id
          from org_tree,
               org,
               org_part,
               scope,
               org_type
         where org_tree.org_id = org.org_id
           and org_tree.ancestor_org_id = ?
           and org_part.org_id = org.org_id
           and org_part.scope_id = scope.scope_id
           and org.org_type_id = org_type.org_type_id
           and org_type.code in (?)
           and scope.scope_id = ?
           and nameFilter
           and codeFilter                 
           ORDER BY orderbycriteria        
         LIMIT ?, ?
        ) parentOrgs
where parentOrgs.org_id = org_tree.ancestor_org_id
   and org_part.org_id = org_tree.org_id
   and org_part.scope_id = scope.scope_id
   and scope.scope_id = ?';

SET @tempTotalOrgsddl := replace(@tempTotalOrgsddl,'nameFilter',@orgNameFilterCriteria);
SET @tempTotalOrgsddl := replace(@tempTotalOrgsddl,'codeFilter',@orgCodeFilterCriteria);
SET @tempTotalOrgsddl := replace(@tempTotalOrgsddl,'orderbycriteria',@orderByColumn);

SET @orgId := ancestorOrgId;
SET @otName := orgTypeName;
SET @fRow := firstRow;
SET @lRow := lastRow;
SET @localNameFilter := orgNameFilter;
SET @localCodeFilter := orgCodeFilter;
SET @localScopeId := scopeId;

PREPARE dynquery FROM @tempTotalOrgsddl;

IF (orgNameFilter IS NOT NULL AND orgCodeFilter IS NOT NULL) THEN
    EXECUTE dynquery using @orgId,@otName,@localScopeId,@localNameFilter,@localCodeFilter,@fRow,@lRow,@localScopeId;
ELSEIF (orgNameFilter IS NOT NULL AND orgCodeFilter IS NULL) THEN
    EXECUTE dynquery using @orgId,@otName,@localScopeId,@localNameFilter,@fRow,@lRow,@localScopeId;
ELSEIF (orgNameFilter IS NULL AND orgCodeFilter IS NOT NULL) THEN
    EXECUTE dynquery using @orgId,@otName,@localScopeId,@localCodeFilter,@fRow,@lRow,@localScopeId;
ELSE
    EXECUTE dynquery using @orgId,@otName,@localScopeId,@fRow,@lRow,@localScopeId;
END IF;

CREATE TEMPORARY TABLE osOrgs ENGINE=MEMORY
  SELECT * FROM totalOrgs;

CREATE TEMPORARY TABLE processorOrgs ENGINE=MEMORY
  SELECT * FROM totalOrgs;

CREATE TEMPORARY TABLE memoryOrgs ENGINE=MEMORY
  SELECT * FROM totalOrgs;

CREATE TEMPORARY TABLE storageOrgs ENGINE=MEMORY
  select * from totalOrgs;

SET @totalNumDevicesByOrg :=
'select totalNumDevicesByOrg.parent_org_id,
       org_name, 
       org_code, 
       total_count,
       processor_count,
       os_count,
       storage_count,
       memory_count
from  (SELECT IFNULL(sum(device.count),0) total_count, totalOrgs.org_name, totalOrgs.org_code, totalOrgs.parent_org_id
         FROM totalOrgs
    LEFT JOIN device
        USING (org_id)        
     GROUP BY totalOrgs.parent_org_id) totalNumDevicesByOrg,

      (SELECT processorOrgs.parent_org_id, IFNULL(sum(device.count),0) processor_count
        FROM option_list
  INNER JOIN option_list_value
          ON (option_list.option_list_id = option_list_value.option_list_id AND option_list.code = ?  )
  INNER JOIN device
          ON (device.processor = option_list_value.option_list_value_id AND option_list_value.value >= ?)
  RIGHT JOIN processorOrgs 
       USING (org_id)
    GROUP BY processorOrgs.parent_org_id) processorCompliantDevicesByOrg,

     (SELECT osOrgs.parent_org_id, IFNULL(sum(device.count),0) os_count
        FROM option_list
  INNER JOIN option_list_value
          ON (option_list.option_list_id = option_list_value.option_list_id AND option_list.code = ?  )
  INNER JOIN device
          ON (device.operating_system = option_list_value.option_list_value_id AND option_list_value.value >= ?)
  RIGHT JOIN osOrgs 
       USING (org_id)
    GROUP BY osOrgs.parent_org_id) osCompliantDevicesByOrg,

     (SELECT storageOrgs.parent_org_id, IFNULL(sum(device.count),0) storage_count
        FROM option_list
  INNER JOIN option_list_value
          ON (option_list.option_list_id = option_list_value.option_list_id AND option_list.code = ? )
  INNER JOIN device
          ON (device.storage = option_list_value.option_list_value_id AND option_list_value.value >= ?)
  RIGHT JOIN storageOrgs 
       USING (org_id)
  GROUP BY storageOrgs.parent_org_id) storageCompliantDevicesByOrg,

     (SELECT memoryOrgs.parent_org_id, IFNULL(sum(device.count),0) memory_count
        FROM option_list
  INNER JOIN option_list_value
          ON (option_list.option_list_id = option_list_value.option_list_id AND option_list.code = ?  )
  INNER JOIN device
          ON (device.memory = option_list_value.option_list_value_id AND option_list_value.value >= ? )
  RIGHT JOIN memoryOrgs 
       USING (org_id)
    GROUP BY memoryOrgs.parent_org_id) memoryCompliantDevicesByOrg

where osCompliantDevicesByOrg.parent_org_id = totalNumDevicesByOrg.parent_org_id
  and processorCompliantDevicesByOrg.parent_org_id = totalNumDevicesByOrg.parent_org_id
  and storageCompliantDevicesByOrg.parent_org_id = totalNumDevicesByOrg.parent_org_id
  and memoryCompliantDevicesByOrg.parent_org_id = totalNumDevicesByOrg.parent_org_id
  orderbycriteria';

SET @totalNumDevicesByOrg := replace(@totalNumDevicesByOrg,'orderbycriteria',@orderByColumn);

PREPARE devicesDynQuery FROM @totalNumDevicesByOrg;

SET @localProcessorListCode := processorListCode;
SET @localProcessorListValue := processorListValue;
SET @localOsListCode := osListCode;
SET @localOsListValue := osListValue;
SET @localStorageListCode := storageListCode;
SET @localStorageListValue := storageListValue;
SET @localMemoryListCode := memoryListCode;
SET @localMemoryListValue := memoryListValue;

EXECUTE devicesDynQuery using @localProcessorListCode,@localProcessorListValue,
                              @localOsListCode,@localOsListValue,
                              @localStorageListCode,@localStorageListValue,
                              @localMemoryListCode,@localMemoryListValue;

END //
