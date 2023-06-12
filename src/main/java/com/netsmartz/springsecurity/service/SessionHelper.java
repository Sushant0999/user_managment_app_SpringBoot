package com.netsmartz.springsecurity.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void removeMessage() {
		try {
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getSession();
			session.removeAttribute("msg");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void removeMessageAuthor() {
		try {
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getSession();
			session.removeAttribute("author");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void removeMessageQuote() {
		try {
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getSession();
			session.removeAttribute("quote");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
