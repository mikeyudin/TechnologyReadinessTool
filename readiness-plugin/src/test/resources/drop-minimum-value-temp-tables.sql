CREATE PROCEDURE readiness.drop_mimimum_value_temp_tables() 

BEGIN

DROP TEMPORARY TABLE IF EXISTS readiness.compliance_filters;

END  //
