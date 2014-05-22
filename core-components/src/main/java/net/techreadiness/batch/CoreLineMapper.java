package net.techreadiness.batch;

import org.springframework.batch.item.file.LineMapper;

public class CoreLineMapper<T> implements LineMapper<T> {
	private LineMapper<T> delegate;

	@Override
	public T mapLine(String line, int lineNumber) throws Exception {
		T object = delegate.mapLine(line, lineNumber);
		if (object instanceof BaseData) {
			BaseData baseData = (BaseData) object;
			baseData.setLineNumber(lineNumber);
			baseData.setRawData(line);
		}
		return object;
	}

	public LineMapper<T> getDelegate() {
		return delegate;
	}

	public void setDelegate(LineMapper<T> delegate) {
		this.delegate = delegate;
	}

}
