package net.techreadiness.service.common;

public interface ScopedDataGridItemProvider<T> extends DataGridItemProvider<T> {
	void setScopeId(Long scopeId);
}
