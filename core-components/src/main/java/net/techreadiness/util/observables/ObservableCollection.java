package net.techreadiness.util.observables;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * A Collection wrapper which allows observation of added and removed elements via an ElementObserver
 * 
 */
public class ObservableCollection<T> extends AbstractCollection<T> implements ElementObserver<T> {
	private static final long serialVersionUID = 1L;
	private final Collection<T> delegate;
	private final ElementObserver<T> observer;

	public ObservableCollection(Collection<T> delegate, ElementObserver<T> observer) {
		this.delegate = delegate;
		this.observer = observer;
	}

	@Override
	public Iterator<T> iterator() {
		return new ObservableIterator<>(delegate.iterator(), this);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean add(T e) {
		observer.elementAdded(e);
		return delegate.add(e);
	}

	@Override
	public void elementAdded(T element) {
		observer.elementAdded(element);
	}

	@Override
	public void elementRemoved(T element) {
		observer.elementRemoved(element);
	}
}
