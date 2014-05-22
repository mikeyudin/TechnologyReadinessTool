package net.techreadiness.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CoreFieldSetMapper<T> implements FieldSetMapper<T> {
	private Binder<T> binder;
	private Class<T> clazz;

	public CoreFieldSetMapper(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T mapFieldSet(FieldSet fieldSet) throws BindException {
		try {
			T bind = binder.bind(clazz.newInstance(), fieldSet);
			if (bind instanceof BaseData) {
				((BaseData) bind).setFieldSet(fieldSet);
			}
			return bind;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Binder<T> getBinder() {
		return binder;
	}

	public void setBinder(Binder<T> binder) {
		this.binder = binder;
	}

}
