package net.techreadiness.service;

import java.util.List;
import java.util.Map;

import net.techreadiness.service.object.Device;
import net.techreadiness.service.object.Org;

public interface DeviceService extends BaseServiceWithValidation {

	/**
	 * This method returns a Device object with the specified deviceId.
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param deviceId
	 *            The specified deviceId
	 * @return The {@code Device} object with the specified deviceId
	 */
	Device getById(ServiceContext context, Long deviceId);

	/**
	 * This method returns a list of Device objects belonging to the specified orgPartId.
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param orgId
	 *            The specified orgId
	 * @return A {@link List} of Devices found for the given orgPartId
	 */
	List<Device> findByOrgId(ServiceContext context, Long orgId);

	/**
	 * Used to create an entry in the device table. This method should only be used when you need to return the newly created
	 * entry. You should use persist if you don't need to return the newly created object.
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param device
	 *            The {@link Device} to to created in the DB.
	 * @return The newly created {@code Device}, with deviceId populated.
	 */
	Device create(ServiceContext context, Device device, Long orgId);

	/**
	 * Used for adding a new entry to the device table. This method does not return the newly created object. If you need to
	 * return the newly created Device object, use create();
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param device
	 *            The {@link Device} to be updated in the DB.
	 */
	void persist(ServiceContext context, Device device, Long orgId);

	/**
	 * Used for adding a new entry to the device table. This method does not return the newly created object. If you need to
	 * return the newly created Device object, use create();
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param device
	 *            The {@link Device} to be updated in the DB.
	 * @param orgCode
	 *            The orgCode to the {@link Org} for the {@link Device}.
	 */
	void persist(ServiceContext context, Device device, String orgCode);

	/**
	 * Used to update an entry in the device table.
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param device
	 *            The {@link Device} to be updated in the DB.
	 */
	Device update(ServiceContext context, Device device, Long orgId);

	/**
	 * Used for deleting an entry from the device table.
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param device
	 *            The {@link Device} to be deleted from the DB.
	 */
	void delete(ServiceContext context, Device device);

	/**
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param map
	 *            The {@link Map} representing a Device object that is to be updated.
	 */
	Device update(ServiceContext context, Map<String, String> map, Long orgId, Long scopeId);

	/**
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param deviceId
	 *            The {@link Long} representing the deviceId that is being deleted.
	 */
	void delete(ServiceContext context, Long deviceId);

	/**
	 *
	 * @param context
	 *            The {@link ServiceContext} to be used for this service call.
	 * @param orgCode
	 *            The specified orgCode
	 */
	void deleteAllByOrgCode(ServiceContext context, String orgCode);
}
