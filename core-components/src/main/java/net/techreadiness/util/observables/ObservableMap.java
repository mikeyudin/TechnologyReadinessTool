package net.techreadiness.util.observables;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * A Map wrapper which allows observation of added and removed elements via an ElementObserver
 * 
 */
public class ObservableMap<K, V> extends AbstractMap<K, V> implements ElementObserver<Entry<K, V>> {
	private static final long serialVersionUID = 1L;
	private final Map<K, V> delegate;
	private final ElementObserver<Entry<K, V>> observer;

	public ObservableMap(Map<K, V> delegate, ElementObserver<java.util.Map.Entry<K, V>> observer) {
		this.delegate = delegate;
		this.observer = observer;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new ObservableSet<>(delegate.entrySet(), this);
	}

	@Override
	public V put(K key, V value) {
		return delegate.put(key, value);
	}

	@Override
	public void elementAdded(Entry<K, V> element) {
		observer.elementAdded(element);

	}

	@Override
	public void elementRemoved(Entry<K, V> element) {
		observer.elementRemoved(element);
	}
}
