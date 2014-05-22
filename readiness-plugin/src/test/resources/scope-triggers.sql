
	CREATE TRIGGER scope_after_insert AFTER INSERT ON scope FOR EACH ROW
	BEGIN
	  call rebuild_scope_tree(NEW.scope_id);
	END; //
