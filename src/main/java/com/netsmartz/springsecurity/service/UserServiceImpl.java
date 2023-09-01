package com.netsmartz.springsecurity.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.netsmartz.springsecurity.utils.MyLogger;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import com.lowagie.text.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.netsmartz.springsecurity.model.UserDetails;
import com.netsmartz.springsecurity.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserDetails createUser(UserDetails user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }

    public List<UserDetails> getAllUser() {
        List<UserDetails> user = (List<UserDetails>) userRepo.findAll();
        return user;
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Page<UserDetails> getAll(PageRequest pageRequest) {
        Page<UserDetails> page = userRepo.findAll(pageRequest);
        return page;
    }

    @Override
    public Page<UserDetails> getPaginated(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return userRepo.findAll(pageRequest);
    }

    public void updateResetPasswordToken(String resetPasswordToken, String email)
            throws UsernameNotFoundException {
        UserDetails user = userRepo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(resetPasswordToken);
            userRepo.save(user);
        } else {
            throw new UsernameNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public UserDetails getResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(UserDetails user, String newPassword) {
        BCryptPasswordEncoder pEncoder = new BCryptPasswordEncoder();
        String encoded = pEncoder.encode(newPassword);
        user.setPassword(encoded);
        user.setResetPasswordToken(null);
        userRepo.save(user);
    }

    public ByteArrayInputStream createPdf(String email) {
        MyLogger.info("Create Pdf Started");

        String title = "Student Info";
        UserDetails userDetails = userRepo.findByEmail(email);
        String[] arr = {"ID : " + String.valueOf(userDetails.getId()), "Name : " + String.valueOf(userDetails.getEmail()), "Name : " + String.valueOf(userDetails.getFullname()), "Role : " + String.valueOf(userDetails.getRole()), "Address : " + String.valueOf(userDetails.getAddress())};

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);

            Font paraFont = FontFactory.getFont(FontFactory.HELVETICA, 15);
            for (String content : arr) {
                Paragraph paragraph = new Paragraph(content, paraFont);
//        paragraph.add(new Chunk("SOme thibk"));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);
            }


            document.close();
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(null);
    }
}
