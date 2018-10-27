package com.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.fundoonotes.model.User;

import reactor.core.publisher.TopicProcessor;
import reactor.util.Logger;
import reactor.util.Loggers;

public class MailSender {
	
	@Autowired
	JavaMailSender javaMailSender;
	
	Logger logger = Loggers.getLogger(MailSender.class);
	
	public MailSender(TopicProcessor<User> userRegistration) {
		userRegistration.subscribe(user1 -> {
			try {
				sendMail(user1.getEmail(), "link to activate", "click on the link below");				
			} catch (Exception e) {
				logger.error("Mail not sent");
			}
		});
	}
	
	public void sendMail(String to, String subject, String text) {
		System.out.println(Thread.currentThread());
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		javaMailSender.send(simpleMailMessage);
	}

}
