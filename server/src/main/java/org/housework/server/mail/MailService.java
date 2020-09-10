package org.housework.server.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.housework.server.ConfigurationService;
import org.housework.server.models.PendingRegistration;
import org.housework.server.models.ServerConfiguration;
import org.housework.server.models.User;
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
	
	public MimeMessage buildMimeMessage() throws AddressException, MessagingException {
		ServerConfiguration config = ConfigurationService.getInstance().getConfiguration();
		if(config.smtpServer == null) {
			return null;
		}
	    
	    Properties props = new Properties();
	    props.put("mail.smtp.host", config.smtpServer);
        props.put("mail.smtp.port", config.smtpPort);
        props.put("mail.smtp.auth", "true");        
	    props.put("mail.smtp.starttls.enable", config.smtpTls ?  "true" : "false");
	    Session session = Session.getInstance(props);	    
	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(config.senderEmail));
	    
	    return msg;
	}
		

	public void sendRegistrationConfirmationMail(PendingRegistration pending)  {
		
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(retrievePublicUrl() + "/confirmRegisteringhouse/" + pending.getId() + "/" + pending.getWorker().getId() + "/" + pending.getSecret()).build();
		LOG.info(uri.toUriString());					
		try {
			MimeMessage msg = buildMimeMessage();
			if(msg == null) {
				LOG.info("Copiez l'URL ci-dessus dans votre navigateur pour valider l'enregistrement");
				return;
			}
			
			InternetAddress[] targets = new InternetAddress[1];
			targets[0] = new InternetAddress(pending.getWorker().getLogin());
			msg.setRecipients(Message.RecipientType.TO, targets);
			msg.setSubject("Invitation dans HouseWork");
			Multipart multipart = new MimeMultipart("related");
			
			MimeBodyPart htmlPart = new MimeBodyPart();        	    	          
			htmlPart.setText(
					"<html>"
					+ "<body>" 
					+ "<p>Vous êtes invité par " + pending.getTarget().getOwner().getLogin() + " dans la maison " + pending.getTarget().getName() + "</p>"        
				    + "<a href='" + uri.toUriString() + "'>Acceptez en cliquant ici</a>"
					+ "</body>"
					+ "</html>"
					, "utf-8", "html");
			multipart.addBodyPart(htmlPart);        
			msg.setContent(multipart);
			
			LOG.info("Mail sended");

			
			sendMail(msg);
			  // set the message content here
		    		
		} catch (AddressException e) {
			LOG.error(e);			
		} catch (MessagingException e) {
			LOG.error(e);
		}	        
	}

	private void sendMail(MimeMessage msg) throws MessagingException {
		ServerConfiguration config = ConfigurationService.getInstance().getConfiguration();
		Transport.send(msg, config.smtpLogin, config.smtpPassword);
	}

	private String retrievePublicUrl() {
		return ConfigurationService.getInstance().getConfiguration().publicUrl;
	}

	public void sendRegistrationUserConfirmation(User user) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl( retrievePublicUrl() + "/confirmRegister").queryParam("login", user.getLogin()).queryParam("registeringCode", user.getRegisteringCode()).build();
		LOG.info(uri.toUriString());
		System.out.println(uri.toUriString());
		try {
			MimeMessage msg = buildMimeMessage();
			if(msg == null) {
				LOG.info("Copiez l'URL ci-dessus dans votre navigateur pour valider la création de compte");
				System.out.println("Copiez l'URL ci-dessus dans votre navigateur pour valider la création de compte");
				return;
			}
			
			InternetAddress[] targets = new InternetAddress[1];
			targets[0] = new InternetAddress(user.getLogin());
			msg.setRecipients(Message.RecipientType.TO, targets);
			msg.setSubject("Mail d'inscription à HouseWork");
			Multipart multipart = new MimeMultipart("related");
			
			MimeBodyPart htmlPart = new MimeBodyPart();        	    	          
			htmlPart.setText(
					"<html>"
					+ "<body>" 
					+ "<p>Vous avez ouvert une inscription chez HouseWork. Merci de confirmer celle-ci en suivant le lien ci-dessous</p>"        
				    + "<a href='" + uri.toUriString() + "'>Confirmez en cliquant ici</a>"
					+ "</body>"
					+ "</html>"
					, "utf-8", "html");
			multipart.addBodyPart(htmlPart);        
			msg.setContent(multipart);
			
			sendMail(msg);
			
			LOG.info("Mail sended");
		} catch (AddressException e) {
			LOG.error(e);			
		} catch (MessagingException e) {
			LOG.error(e);
		}	        
		
	}
}
