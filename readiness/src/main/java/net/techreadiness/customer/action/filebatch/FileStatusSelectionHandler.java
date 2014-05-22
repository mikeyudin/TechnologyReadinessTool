package net.techreadiness.customer.action.filebatch;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.techreadiness.service.FileService.FileStatus;
import net.techreadiness.ui.action.filters.AbstractConversationFilterSelectionHandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class FileStatusSelectionHandler extends AbstractConversationFilterSelectionHandler<FileStatus> {
	private static final String FILE_STATUS = "fileStatuses";
	private static final String FILE_LIST = "fileDataGrid";

	@Override
	public List<FileStatus> getList(Map<String, Object> parameters) {
		List<FileStatus> fileStatuses = Lists.newArrayList(FileStatus.values());
		// Filter out the already selected types
		fileStatuses.removeAll(getSelectedStatuses(fileStatuses));

		String term = ((String[]) parameters.get("term"))[0];

		if (StringUtils.isNotBlank(term)) {
			// Filter out the types that do not match the search term
			Iterator<FileStatus> i = fileStatuses.iterator();
			while (i.hasNext()) {
				FileStatus status = i.next();
				if (!StringUtils.containsIgnoreCase(status.name(), term)) {
					i.remove();
				}
			}
		}

		return fileStatuses;
	}

	@Override
	public List<FileStatus> getSelection() {
		List<FileStatus> fileStatuses = Lists.newArrayList(FileStatus.values());
		return getSelectedStatuses(fileStatuses);
	}

	private List<FileStatus> getSelectedStatuses(Collection<FileStatus> types) {
		List<FileStatus> newList = Lists.newLinkedList(types);
		Iterator<FileStatus> i = newList.iterator();
		Collection<String> selection = getDataGridState(FILE_LIST).getFilters().get(FILE_STATUS);
		while (i.hasNext()) {
			FileStatus status = i.next();
			if (!selection.contains(status.name().toLowerCase())) {
				i.remove();
			}
		}
		return newList;
	}

	@Override
	public void add(Long id) {
		FileStatus status = FileStatus.values()[id.intValue()];
		getDataGridState(FILE_LIST).getFilters().get(FILE_STATUS).add(status.name().toLowerCase());
	}

	@Override
	public void remove(Long id) {
		FileStatus status = FileStatus.values()[id.intValue()];
		getDataGridState(FILE_LIST).getFilters().get(FILE_STATUS).remove(status.name().toLowerCase());
	}

	@Override
	public void clear() {
		getDataGridState(FILE_LIST).getFilters().get(FILE_STATUS).clear();
	}

}
