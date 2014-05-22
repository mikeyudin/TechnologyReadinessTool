package net.techreadiness.util.observables;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/**
 * 
 * A Multimap wrapper which allows observation of added and removed elements via an ElementObserver
 * 
 */
public class ObservableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final ElementObserver<Entry<K, V>> observer;
	private final Multimap<K, V> delegate;

	public ObservableMultimap(Multimap<K, V> delegate, ElementObserver<Entry<K, V>> observer) {
		this.delegate = delegate;
		this.observer = observer;
	}

	@Override
	protected Multimap<K, V> delegate() {
		return delegate;
	}

	class MultiMapObserver implements ElementObserver<Entry<K, Collection<V>>> {
		private static final long serialVersionUID = 1L;

		@Override
		public void elementAdded(Entry<K, Collection<V>> element) {
			for (V value : element.getValue()) {
				observer.elementAdded(Maps.immutableEntry(element.getKey(), value));
			}
		}

		@Override
		public void elementRemoved(Entry<K, Collection<V>> element) {
			for (V value : element.getValue()) {
				observer.elementRemoved(Maps.immutableEntry(element.getKey(), value));
			}
		}

	}

	class ValueObserver implements ElementObserver<V> {
		private static final long serialVersionUID = 1L;

		@Override
		public void elementAdded(V element) {
			observer.elementAdded(Maps.immutableEntry((K) null, element));
		}

		@Override
		public void elementRemoved(V element) {
			observer.elementRemoved(Maps.immutableEntry((K) null, element));
		}

	}

	class KeyObserver implements ElementObserver<K> {
		private static final long serialVersionUID = 1L;

		@Override
		public void elementAdded(K element) {
			observer.elementAdded(Maps.immutableEntry(element, (V) null));
		}

		@Override
		public void elementRemoved(K element) {
			observer.elementRemoved(Maps.immutableEntry(element, (V) null));
		}

	}

	@Override
	public Map<K, Collection<V>> asMap() {
		// TODO: modifications to this map are not supported
		return ImmutableMap.copyOf(super.asMap());
	}

	@Override
	public void clear() {
		for (Entry<K, V> entry : super.entries()) {
			observer.elementRemoved(entry);
		}
		super.clear();
	}

	@Override
	public Collection<Entry<K, V>> entries() {
		return new ObservableCollection<>(super.entries(), observer);
	}

	@Override
	public Collection<V> get(K key) {
		return new ObservableCollection<>(delegate.get(key), new ValueObserver());
	}

	@Override
	public Multiset<K> keys() {
		// TODO: modifications to this structure are not supported
		return ImmutableMultiset.<K> builder().addAll(super.keys()).build();
	}

	@Override
	public Set<K> keySet() {
		return new ObservableSet<>(super.keySet(), new KeyObserver());
	}

	@Override
	public boolean put(K key, V value) {
		observer.elementAdded(Maps.immutableEntry(key, value));
		return super.put(key, value);
	}

	@Override
	public boolean putAll(K key, Iterable<? extends V> values) {
		for (V value : values) {
			observer.elementAdded(Maps.immutableEntry(key, value));
		}
		return super.putAll(key, values);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
		for (Entry<?, ?> entry : multimap.entries()) {
			observer.elementAdded((Entry<K, V>) entry);
		}
		return super.putAll(multimap);
	}

	@Override
	public boolean remove(Object key, Object value) {
		observer.elementRemoved((Entry<K, V>) Maps.immutableEntry(key, value));
		return super.remove(key, value);
	}

	@Override
	public Collection<V> removeAll(Object key) {

		Collection<V> removedValues = super.removeAll(key);
		for (V value : removedValues) {
			observer.elementRemoved((Entry<K, V>) Maps.immutableEntry(key, value));
		}
		return removedValues;
	}

	@Override
	public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
		Collection<V> replacedValues = super.replaceValues(key, values);
		for (V value : replacedValues) {
			observer.elementRemoved(Maps.immutableEntry(key, value));
		}
		for (V value : values) {
			observer.elementAdded(Maps.immutableEntry(key, value));
		}
		return replacedValues;
	}

}
