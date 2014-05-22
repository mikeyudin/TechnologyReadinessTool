package net.techreadiness.navigation.taskflow.filebatch;

import java.util.ArrayList;
import java.util.List;

import net.techreadiness.service.object.File;
import net.techreadiness.ui.task.TaskFlowData;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class FileBatchTaskFlowData extends TaskFlowData {
	private static final long serialVersionUID = 1L;

	private List<File> files;

	public List<File> getFiles() {
		return files;
	}

	public List<Long> getFileIds() {
		List<Long> ids = new ArrayList<>();
		if (files != null) {
			for (File file : files) {
				ids.add(file.getFileId());
			}
		}
		return ids;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
