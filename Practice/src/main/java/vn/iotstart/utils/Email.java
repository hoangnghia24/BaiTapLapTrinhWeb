package vn.iotstart.utils;

import java.util.Properties;
import java.util.Random;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import vn.iotstart.model.User;

public class Email {
	public String getRandom() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}
	
	public boolean sendEmail(User user) {
		boolean test = false;
		String toEmail = user.getEmail();
		String fromEmail = "dqhn2403@gmail.com";
		String password = "dangquanghoangnghia";
		try {
			Properties pr = configEmail(new Properties());
			
			Session session = Session.getInstance(pr, new Authenticator(){
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail,password);
				}
			});
			
			//set email message details
			Message mess = new MimeMessage(session);
			mess.setHeader("Content-Type", "text/plain; charset=UTF-8");
			//set from email address
			mess.setFrom(new InternetAddress(fromEmail));
			//set to email address or destination email address
			mess.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
 
			//set email subject
			mess.setSubject("Confirm Code");
			
			mess.setText("Your code is:" + user.getCode());
			
			Transport.send(mess);
			
			test = true;
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		return test;
	}
	public Properties configEmail(Properties pr) {
	    // your host email smtp server details
	    pr.setProperty("mail.smtp.host", "smtp.gmail.com");
	    pr.setProperty("mail.smtp.port", "587");
	    pr.setProperty("mail.smtp.auth", "true");
	    pr.setProperty("mail.smtp.starttls.enable", "true");
	    pr.put("mail.smtp.socketfactory.port", "587");
	    pr.put("mail.smtp.socketfactory.class", "javax.net.ssl.SSLSocketFactory");

	    return pr;
	}
}
