package net.techreadiness.ui.tags.datagrid;

import java.util.Map.Entry;

import net.techreadiness.util.observables.ElementObserver;
import net.techreadiness.util.observables.ObservableMultimap;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class FilterMap extends ForwardingMultimap<String, String> implements ElementObserver<Entry<String, String>> {
	private static final long serialVersionUID = 1L;
	private final Multimap<String, String> delegate;
	private boolean modified;

	public FilterMap() {
		HashMultimap<String, String> delegate = HashMultimap.create();
		this.delegate = new ObservableMultimap<>(delegate, this);
	}

	@Override
	protected Multimap<String, String> delegate() {
		return delegate;
	}

	@Override
	public void elementAdded(Entry<String, String> element) {
		modified = true;
	}

	@Override
	public void elementRemoved(Entry<String, String> element) {
		modified = true;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
}