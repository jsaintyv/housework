package org.housework.server.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.housework.server.models.PendingRegistration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class MailService {
	
	static final Log LOG = LogFactory.getLog(MailService.class);
	
	private MailService() {
	}

	private static final MailService INSTANCE = new MailService();

	public static MailService instance() {
		return INSTANCE;
	}

	public void sendRegistrationConfirmationMail(PendingRegistration pending) {
		//TODO Send email
		UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/confirmRegisteringhouse/" + pending.getId() + "/" + pending.getWorker().getId() + "/" + pending.getSecret()).build();
		LOG.info(uri.toUriString());
	}
}
