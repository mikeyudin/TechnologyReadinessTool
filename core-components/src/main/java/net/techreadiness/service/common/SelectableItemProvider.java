package net.techreadiness.service.common;

public interface SelectableItemProvider<T> extends DataGridItemProvider<T> {

	T getObjectForKey(String rowKey);
}
