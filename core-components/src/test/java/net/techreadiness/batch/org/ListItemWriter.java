package net.techreadiness.batch.org;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.google.common.collect.Lists;

public class ListItemWriter<T> implements ItemWriter<T> {
	private List<T> list = Lists.newArrayList();

	@Override
	public void write(List<? extends T> items) throws Exception {
		list.addAll(items);
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
