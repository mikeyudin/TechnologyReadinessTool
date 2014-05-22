package net.techreadiness.batch.device;

import java.util.List;

import net.techreadiness.service.ServiceContext;
import net.techreadiness.service.common.ValidationError;
import net.techreadiness.service.exception.ValidationServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import com.google.common.collect.Lists;

public class DeviceEraser extends AbstractDeviceStep implements ItemWriter<DeviceData>, StepExecutionListener {
	private final Logger log = LoggerFactory.getLogger(DeviceEraser.class);

	@Override
	public void write(List<? extends DeviceData> items) throws Exception {
		ServiceContext serviceContext = getServiceContext();
		log.debug("Attempting to delete {} devices.", items.size());
		for (final DeviceData deviceData : items) {
			List<ValidationError> errors = Lists.newArrayList();
			try {
				deviceService.deleteAllByOrgCode(serviceContext, deviceData.getCombinedCode());
			} catch (ValidationServiceException vse) {
				errors.addAll(vse.getFaultInfo().getAttributeErrors());
			} catch (Exception e) {
				addGeneralError(errors, e);
			}
			throwIfErrors(errors);
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Nothing to do before the step
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext()
				.putInt("device.org.delete.count", stepExecution.getWriteCount());
		return null;
	}
}