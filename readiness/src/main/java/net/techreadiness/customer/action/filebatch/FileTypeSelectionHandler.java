package net.techreadiness.customer.action.filebatch;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.techreadiness.service.FileService;
import net.techreadiness.service.object.FileType;
import net.techreadiness.ui.action.filters.AbstractConversationFilterSelectionHandler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@org.springframework.context.annotation.Scope("prototype")
public class FileTypeSelectionHandler extends AbstractConversationFilterSelectionHandler<FileType> {
	private static final String FILE_TYPES = "fileTypes";
	private static final String FILE_LIST = "fileDataGrid";
	@Inject
	private FileService fileService;

	@Override
	public List<FileType> getList(Map<String, Object> parameters) {
		List<FileType> fileTypes = fileService.findFileTypes(getServiceContext());
		// Filter out the already selected types
		fileTypes.removeAll(getSelectedTypes(fileTypes));

		String term = ((String[]) parameters.get("term"))[0];

		if (StringUtils.isNotBlank(term)) {
			// Filter out the types that do not match the search term
			Iterator<FileType> i = fileTypes.iterator();
			while (i.hasNext()) {
				FileType type = i.next();
				if (!StringUtils.containsIgnoreCase(type.getName(), term)) {
					i.remove();
				}
			}
		}

		return fileTypes;
	}

	@Override
	public List<FileType> getSelection() {
		List<FileType> allTypes = fileService.findFileTypes(getServiceContext());
		return getSelectedTypes(allTypes);
	}

	private List<FileType> getSelectedTypes(Collection<FileType> types) {
		List<FileType> newList = Lists.newLinkedList(types);
		Iterator<FileType> i = newList.iterator();
		Collection<String> selection = getDataGridState(FILE_LIST).getFilters().get(FILE_TYPES);
		while (i.hasNext()) {
			FileType type = i.next();
			if (!selection.contains(type.getFileTypeId().toString())) {
				i.remove();
			}
		}
		return newList;
	}

	@Override
	public void add(Long id) {
		getDataGridState(FILE_LIST).getFilters().get(FILE_TYPES).add(id.toString());
	}

	@Override
	public void remove(Long id) {
		getDataGridState(FILE_LIST).getFilters().get(FILE_TYPES).remove(id.toString());
	}

	@Override
	public void clear() {
		getDataGridState(FILE_LIST).getFilters().get(FILE_TYPES).clear();
	}

}
