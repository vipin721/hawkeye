package com.apigee.hawk.mailImpl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apigee.hawk.mail.PostMan;
import com.apigee.hawk.properties.MonitorPropertyVariables;

public class PostManImpl implements PostMan {
	
	private static String host ;
	private static String from;
	private static String pass;
	static Properties props;
	Session session;
	MimeMessage message;
	Logger logger;
	public PostManImpl (){
		host = MonitorPropertyVariables.HOST;
		from = MonitorPropertyVariables.FROM;
		pass = new String (MonitorPropertyVariables.getPASSWORD());
		props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.user", from);
	    props.put("mail.smtp.password", pass);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    this.session = Session.getDefaultInstance(props,null);
	    message = new MimeMessage(this.session);
	    logger = LoggerFactory.getLogger(PostManImpl.class);
	}
	
	public void sendMail(String emailSubject, String emailAddress, String emailPayload)
	{
		/**
		 * System.out.println(PostManImpl.pass+ " host : "+PostManImpl.from);
		 * System.out.println("Sending mail: "+"\n"+emailSubject +"\n"+emailAddress+"\n"+emailPayload);
		*/
		String [] emails = emailAddress.split(",");
		try {
			this.message.setFrom(new InternetAddress(PostManImpl.from));
			InternetAddress [] toAddress = new InternetAddress[emails.length];
			
			for (int i = 0 ; i< emails.length ; i++ )
			{
				toAddress [i] = new InternetAddress(emails[i]);
				this.message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}
			this.message.setSubject(emailSubject);
			this.message.setText(emailPayload);
			
			
			Transport transport = this.session.getTransport("smtp");
			transport.connect(PostManImpl.host, PostManImpl.from, PostManImpl.pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			logger.info(emailAddress +","+emailSubject+","+emailPayload); 
			
			
		} catch (AddressException e) {
			logger.error(e.getLocalizedMessage());
		} catch (MessagingException e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	public  static void main (String [] args){
		PostManImpl p = new PostManImpl();
		p.sendMail("hello", "abhishek.8in@gmail.com,abhishek_mca04@yahoo.com", "Hello from postmaster");
	}
	

}
