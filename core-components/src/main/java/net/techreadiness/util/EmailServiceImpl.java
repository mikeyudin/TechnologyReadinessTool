package net.techreadiness.util;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.techreadiness.service.exception.ServiceException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Value("${emailServiceHostName}")
	private String hostName;
	@Value("${emailServiceReplyName}")
	private String replyName;
	@Value("${emailServiceReplyAddress}")
	private String replyAddress;

	@Value("${emailServiceSMTPuser}")
	private String authenticationUserName;
	@Value("${emailServiceSMTPpass}")
	private String authenticationPassword;

	@Value("${emailServiceSMTPport}")
	private String smtpPort;
	@Value("${emailServiceSSLport}")
	private String sslPort;

	@Value("${emailServiceUseTLS}")
	private String useTls;
	@Value("${emailServiceUseSSL}")
	private String useSsl;

	@Inject
	private MessageSource bundleSource;

	private static final Logger log = Logger.getLogger(EmailServiceImpl.class);

	@Override
	public void sendHtmlAndTextEmail(String to, String subject, String htmlBody, String textBody) throws ServiceException {
		HtmlEmail email = new HtmlEmail();

		try {
			setupEmail(email);
			validateAddress(to);
			email.addTo(to);
			email.setSubject(subject);
			email.setHtmlMsg(htmlBody);
			email.setTextMsg(textBody);
			email.send();
		} catch (EmailException e) {
			log.error("ZZZ.EmailException. To: " + to + " Subject: " + subject, e);
			throw new ServiceException("Unable to send email.", e);
		}
	}

	@Override
	public void sendHtmlEmail(String to, String subject, String htmlBody) throws ServiceException {
		HtmlEmail email = new HtmlEmail();

		try {
			setupEmail(email);
			validateAddress(to);
			email.addTo(to);
			email.setSubject(subject);
			email.setHtmlMsg(htmlBody);
			email.send();
		} catch (EmailException e) {
			log.error("ZZZ.EmailException. To: " + to + " Subject: " + subject, e);
			throw new ServiceException("Unable to send email.", e);
		}
	}

	@Override
	public void sendTextEmail(String to, String subject, String textBody) throws ServiceException {
		SimpleEmail email = new SimpleEmail();

		try {
			setupEmail(email);
			validateAddress(to);
			email.addTo(to);
			email.setSubject(subject);
			email.setMsg(textBody);
			email.send();
		} catch (EmailException e) {
			log.error("ZZZ.EmailException. To: " + to + " Subject: " + subject, e);
			throw new ServiceException("Unable to send email.", e);
		}
	}

	@Override
	public void sendSubstitutedTextEmail(String to, String titleKey, String textKey, Map<String, String> values)
			throws ServiceException {
		String title = getSubstitutedText(titleKey, values);
		String s = getSubstitutedText(textKey, values);

		sendTextEmail(to, title, s);
	}

	private String getSubstitutedText(String key, Map<String, String> values) {
		StringTemplate st = new StringTemplate(bundleSource.getMessage(key, null, Locale.ENGLISH),
				DefaultTemplateLexer.class);

		st.setAttributes(values);

		return st.toString();
	}

	private void setupEmail(Email email) throws EmailException {
		email.setHostName(getHostName());
		email.setSslSmtpPort(getSmtpPort());
		email.setFrom(getReplyAddress(), getReplyName());

		if (StringUtils.isNotBlank(useSsl)) {
			email.setSSL(Boolean.valueOf(getUseSsl()));
			email.setSslSmtpPort(getSslPort());
		}

		if (StringUtils.isNotBlank(useTls)) {
			email.setTLS(Boolean.valueOf(getUseTls()));
		}

		// use authentication if configured
		if (StringUtils.isNotBlank(getAuthenticationUserName())) {
			email.setAuthenticator(new DefaultAuthenticator(getAuthenticationUserName(), getAuthenticationPassword()));
		}
	}

	private static void validateAddress(String potentialEmail) throws ServiceException {
		try {
			// validate potential email
			if (potentialEmail != null) {
				@SuppressWarnings("unused")
				InternetAddress address = new InternetAddress(potentialEmail, true);
			}
		} catch (AddressException e) {
			ServiceException se = new ServiceException(e.getMessage());
			se.initCause(e);
			throw se;
		}
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public String getReplyAddress() {
		return replyAddress;
	}

	public void setReplyAddress(String replyAddress) {
		this.replyAddress = replyAddress;
	}

	public String getAuthenticationUserName() {
		return authenticationUserName;
	}

	public void setAuthenticationUserName(String authenticationUserName) {
		this.authenticationUserName = authenticationUserName;
	}

	public String getAuthenticationPassword() {
		return authenticationPassword;
	}

	public void setAuthenticationPassword(String authenticationPassword) {
		this.authenticationPassword = authenticationPassword;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSslPort() {
		return sslPort;
	}

	public void setSslPort(String sslPort) {
		this.sslPort = sslPort;
	}

	public String getUseTls() {
		return useTls;
	}

	public void setUseTls(String useTls) {
		this.useTls = useTls;
	}

	public String getUseSsl() {
		return useSsl;
	}

	public void setUseSsl(String useSsl) {
		this.useSsl = useSsl;
	}
}
