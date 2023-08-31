package com.netsmartz.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.netsmartz.springsecurity.model.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(Email em) throws MessagingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
      
		
		System.out.println("CALLED");
		
		helper.setFrom("sushantr3999@gmail.com");
		helper.setPriority(10);
		helper.setTo(em.getTo());
		helper.setSubject(em.getSubject());
		helper.setText(em.getMessage());
        System.out.println(em);
		mailSender.send(message);
	}
}
