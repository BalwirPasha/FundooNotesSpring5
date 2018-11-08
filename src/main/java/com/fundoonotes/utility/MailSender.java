package com.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.fundoonotes.model.MyMail;

import reactor.core.publisher.TopicProcessor;
import reactor.util.Logger;
import reactor.util.Loggers;

public class MailSender {
	
	@Autowired
	JavaMailSender javaMailSender;
	
	Logger logger = Loggers.getLogger(MailSender.class);
	
	public MailSender(TopicProcessor<MyMail> userRegistration) {
		userRegistration.subscribe(myMail -> {
			System.out.println("TP "+Thread.currentThread().getId());
			System.out.println(myMail);
			try {
				sendMail(myMail.getTo(), myMail.getSubject(), myMail.getText());				
			} catch (Exception e) {
				logger.error("Mail not sent");
			}
		});
	}
	
	public void sendMail(String to, String subject, String text) {
		System.out.println("SM "+Thread.currentThread().getId());
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		javaMailSender.send(simpleMailMessage);
	}

}
