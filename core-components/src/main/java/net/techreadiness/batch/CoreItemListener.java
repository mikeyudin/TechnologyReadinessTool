package net.techreadiness.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.techreadiness.service.FileService;
import net.techreadiness.service.exception.ServiceException;
import net.techreadiness.service.exception.ValidationServiceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectLineLengthException;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.context.MessageSource;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CoreItemListener extends AbstractServiceContextProvider {

	@Inject
	FileService fileService;
	@Inject
	private MessageSource messageSource;
	@Inject
	private PlatformTransactionManager txManager;

	private static String ERROR_DATA_FILE_SUFFIX = "-error.csv";
	private static String ERROR_MESSAGE_FILE_SUFFIX = "-error-messages.txt";

	private String fileName;
	private String errorDataFileName = null;
	private String errorMessageFileName = null;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@OnReadError
	public void onReadError(Exception e) {
		TransactionStatus transaction = null;
		try {
			transaction = txManager.getTransaction(new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			FlatFileParseException ffpe = getThrowable(e, FlatFileParseException.class);
			if (ffpe == null) {
				// TODO Handle error
				log.error("#### Encountered skipable Exception on read", e);
			} else {
				Map<String, List<String>> errorMap = Maps.newHashMap();
				List<String> errorList = Lists.newArrayList();
				errorMap.put("readError", errorList);

				IncorrectTokenCountException itce = getThrowable(e, IncorrectTokenCountException.class);
				IncorrectLineLengthException ille = getThrowable(e, IncorrectLineLengthException.class);
				SpelEvaluationException see = getThrowable(e, SpelEvaluationException.class);

				if (itce != null) {
					String message = getMessage("file.incorrectTokenCount", Integer.valueOf(ffpe.getLineNumber()),
							itce.getExpectedCount(), itce.getActualCount());
					errorList.add(message);
				} else if (ille != null) {
					String message = getMessage("file.incorrectLineLength", Integer.valueOf(ffpe.getLineNumber()),
							ille.getExpectedLength(), ille.getActualLength());
					errorList.add(message);
				} else if (see != null) {
					String msg = getMessage("file.bindingError", see.getInserts());
					errorList.add(msg);
				} else {
					String message = getMessage("file.genericParseError", Integer.valueOf(ffpe.getLineNumber()),
							ffpe.getInput());
					errorList.add(message);
				}

				createFileErrorEntries(ffpe.getLineNumber(), errorMap);
				writeErrorMessagesToFile(ffpe.getLineNumber(), errorMap);
				writeErrorLineDataToFile(ffpe.getInput());
			}
		} finally {
			if (transaction != null) {
				txManager.commit(transaction);
			}
		}

	}

	private static <T extends Throwable> T getThrowable(Throwable t, Class<T> type) {
		int index = ExceptionUtils.indexOfThrowable(t, type);
		if (index < 0) {
			return null;
		}
		return (T) ExceptionUtils.getThrowableList(t).get(index);
	}

	protected String getMessage(String key, Object... params) {
		String message = messageSource.getMessage(key, params, Locale.getDefault());
		return message;
	}

	@OnSkipInProcess
	public void onSkipInProcess(BaseData item, Throwable t) {
		if (t instanceof ValidationServiceException) {
			ValidationServiceException vse = (ValidationServiceException) t;

			createFileErrorEntries(item.getLineNumber(), vse.getBatchErrorMap());

			writeErrorMessagesToFile(item.getLineNumber(), vse.getBatchErrorMap());

			writeErrorLineDataToFile(item.getRawData());

		} else {
			log.error("#### Encountered skipable Exception on read", t);
		}
	}

	@OnSkipInWrite
	public void onSkipInWrite(BaseData item, Throwable t) {
		Map<String, List<String>> errors = Maps.newHashMap();
		Integer lineNumber = Integer.valueOf(item.getLineNumber());
		if (t instanceof ValidationServiceException) {
			ValidationServiceException vse = (ValidationServiceException) t;
			errors = vse.getBatchErrorMap();
		} else if (t instanceof ServiceException && StringUtils.isNotBlank(t.getMessage())) {
			List<String> error = Lists.newArrayList(t.getMessage());
			errors.put("serviceError", error);
		} else {
			Integer numItems = Integer.valueOf(1);
			Integer firstLine = Integer.valueOf(item.getLineNumber());
			String msg = getMessage("file.writeFailed", new Object[] { numItems, firstLine });
			List<String> error = Lists.newArrayList(msg);
			errors.put("writeError", error);
		}

		createFileErrorEntries(lineNumber, errors);

		writeErrorMessagesToFile(lineNumber, errors);
		
		writeErrorLineDataToFile(item.getRawData());
	}

	private void writeErrorMessagesToFile(Integer lineNumber, Map<String, List<String>> errorMap) {
		if (errorMessageFileName == null) {
			// our original file looks something like: file:/tmp/files/ORG-Import-7-3453535345
			errorMessageFileName = fileName.replaceFirst("file:", "") + ERROR_MESSAGE_FILE_SUFFIX;
			File errorFile = new File(errorMessageFileName);

			try {
				errorFile.createNewFile();
			} catch (IOException e) {
				log.error("Unable to create message error file for file: " + fileName, e);
			}

			// update file with data error file name
			net.techreadiness.service.object.File file = fileService.getById(getServiceContext(), getFileId());

			file.setErrorMessageFilename(StringUtils.substringAfterLast(errorMessageFileName, File.separator));

			fileService.addOrUpdate(getServiceContext(), file);
		}

		if (errorMap != null) {
			try (BufferedWriter out = new BufferedWriter(new FileWriter(errorMessageFileName, true))){
				for (Entry<String, List<String>> entry : errorMap.entrySet()) {
					for (String s : entry.getValue()) {
						out.write(lineNumber + " : " + s + "\n");
						log.info(getFileName() + " : " + lineNumber + " : " + entry.getKey() + " - " + s);
					}
				}
			} catch (IOException e) {
				log.error("Unable to write to error message file for file: " + fileName, e);
			}
		}
	}

	private void writeErrorLineDataToFile(String rawData) {
		if (errorDataFileName == null) {
			// our original file looks something like: file:/tmp/files/ORG-Import-7-3453535345
			errorDataFileName = fileName.replaceFirst("file:", "") + ERROR_DATA_FILE_SUFFIX;
			File errorFile = new File(errorDataFileName);

			try {
				errorFile.createNewFile();
			} catch (IOException e) {
				log.error("Unable to create error file for file: " + fileName, e);
			}

			// update file with data error file name
			net.techreadiness.service.object.File file = fileService.getById(getServiceContext(), getFileId());

			file.setErrorDataFilename(StringUtils.substringAfterLast(errorDataFileName, File.separator));

			fileService.addOrUpdate(getServiceContext(), file);
		}

		try (BufferedWriter out = new BufferedWriter(new FileWriter(errorDataFileName, true))) {
			out.write(rawData + "\n");
			out.close();
		} catch (IOException e) {
			log.error("Unable to write to error file for file: " + fileName, e);
		}

	}

	private void createFileErrorEntries(Integer lineNumber, Map<String, List<String>> errors) {
		fileService.addErrors(getServiceContext(), getFileId(), lineNumber, errors);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
