package net.techreadiness.util;

import java.util.Map;

import net.techreadiness.service.exception.ServiceException;

public interface EmailService {
	/**
	 * Sends a text email.
	 *
	 * @param to
	 *            The email address to send to
	 * @param subject
	 *            The subject of the email
	 * @param textBody
	 *            The text body of the email
	 * @throws ServiceException
	 *             If there is an issue creating or sending the email.
	 */
	void sendTextEmail(String to, String subject, String textBody) throws ServiceException;

	/**
	 * Sends a text email.
	 *
	 * @param to
	 *            The email address to send to
	 * @param titleKey
	 *            The resource key to lookup up the email title
	 * @param textKey
	 *            The resource key to lookup up the email text
	 * @param values
	 *            The key/value pairs to substitute into the email text pulled from the resource bundle
	 * @throws ServiceException
	 *             If there is an issue creating or sending the email.
	 */
	void sendSubstitutedTextEmail(String to, String titleKey, String textKey, Map<String, String> values)
			throws ServiceException;

	/**
	 * Sends an HTML email.
	 *
	 * @param to
	 *            The email address to send to
	 * @param subject
	 *            The subject of the email
	 * @param htmlBody
	 *            The HTML body of the email
	 * @throws ServiceException
	 *             If there is an issue creating or sending the email.
	 */
	void sendHtmlEmail(String to, String subject, String htmlBody) throws ServiceException;

	/**
	 * Sends an HTML and text email.
	 *
	 * @param to
	 *            The email address to send to
	 * @param subject
	 *            The subject of the email
	 * @param htmlBody
	 *            The HTML body of the email
	 * @param textBody
	 *            The text body of the email
	 * @throws ServiceException
	 *             If there is an issue creating or sending the email.
	 */
	void sendHtmlAndTextEmail(String to, String subject, String htmlBody, String textBody) throws ServiceException;
}
