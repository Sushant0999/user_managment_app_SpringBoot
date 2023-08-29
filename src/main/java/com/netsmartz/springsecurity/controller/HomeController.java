package com.netsmartz.springsecurity.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsmartz.springsecurity.model.Email;
import com.netsmartz.springsecurity.model.Quote;
import com.netsmartz.springsecurity.model.UserDetails;
import com.netsmartz.springsecurity.service.EmailService;
import com.netsmartz.springsecurity.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.Quota;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Quote quote;

    @GetMapping("/")
    public String index(HttpSession session) throws IOException {
        System.out.println("CALLED");
        JsonNode root = null;
        try {
            URL url = new URL("https://api.api-ninjas.com/v1/quotes?category=life");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", "2XwR3PzOHyNmeClX2i6ho8YRrolj17eAEd1WzitW");
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(responseStream);
            if (root != null) {
                quote.setAuthor(root.get(0).get("author").toString());
                quote.setQuote(root.get(0).get("quote").toString());
                quote.setCategory(root.get(0).get("category").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = quote.toString();
        System.out.println(msg);
        session.setAttribute("author", quote.getAuthor());
        session.setAttribute("quote", quote.getQuote());
        session.setAttribute("category", quote.getCategory());
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/createUser")
//	 use @RequestBody to insert data via postman
    public String createUser(@ModelAttribute UserDetails user, HttpSession session) {
        boolean f = userService.checkEmail(user.getEmail());
        boolean checkNull = UserDetails.isNonEmptyAndNonBlank(user.getEmail()) &&
                UserDetails.isNonEmptyAndNonBlank(user.getFullname()) &&
                UserDetails.isNonEmptyAndNonBlank(user.getPassword()) &&
                UserDetails.isNonEmptyAndNonBlank(user.getAddress()) &&
                UserDetails.isNonEmptyAndNonBlank(user.getQualification());
        if (f) {
            session.setAttribute("msg", "EMAIL ALREADY EXIST");
        } else {
            if (checkNull) {
                userService.createUser(user);
                session.setAttribute("msg", "REGISTERED SUCCESSFULLY");
            }
            if (!checkNull) {
                session.setAttribute("msg", "EMPTY VALUE NOT ACCEPTED");
            } else
                session.setAttribute("msg", "REGISTERED UNSUCCESSFULLY SOMETHING GOES WRONG");
        }
        return "redirect:/register";
    }

    @GetMapping("/forget")
    public String forgetPassword() {
        return "forget";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request, Email em, HttpSession httpSession) {
        String email = request.getParameter("email");
        System.out.println("THIS IS EMAIL : " + email);
        boolean checkMail = userService.checkEmail(email);
        if (!checkMail) {
            String error = "USER NOT FOUND";
            httpSession.setAttribute("error", error);
            return "redirect:/forget";
        }
        String zeros = "000000";
        Random rnd = new Random();
        String s = Integer.toString(rnd.nextInt(0X1000000), 16);
        s = zeros.substring(s.length()) + s;
        s = s.toUpperCase();

        try {
            em.setTo(email);
            em.setSubject("VERIFICATION CODE");
            em.setMessage("VERIFICATION CODE TO RESET PASSWORD IS : " + s);
            emailService.sendEmail(em);
            httpSession.setAttribute("msg", "Email with Verification Code Send Successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        userService.updateResetPasswordToken(s, email);
        System.out.println(s + " " + email);
        return "updatePassword";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(HttpServletRequest request, HttpSession session) {
        String newPassword = request.getParameter("newPassword");
        String token = request.getParameter("token");
        UserDetails user = userService.getResetPasswordToken(token);
        if (user == null) {
            System.out.println("INVALID CODE");
            session.setAttribute("msg", "INVALID CODE");
            return "updatePassword";
        }
        userService.updatePassword(user, newPassword);
        return "login";

    }

}
