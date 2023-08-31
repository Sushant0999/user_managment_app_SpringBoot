package com.netsmartz.springsecurity.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    public ByteArrayInputStream createPdf() {
//		logger.info("Create Pdf Started");

        String title = "Welcome";
        List<UserDetails> list = getAllUser();
        String content = String.valueOf(list);


        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Document document = new Document();

            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();

            PdfWriter.getInstance(document, byteArrayOutputStream);

            float pos = height / 2;
            PdfPTable table = null;
            PdfPCell cell = null;

            document.open();

            float[] columnDefinitionSize = {33.33F, 33.33F, 33.33F};

            table = new PdfPTable(columnDefinitionSize);
            table.getDefaultCell().setBorder(0);
            table.setHorizontalAlignment(0);
            table.setTotalWidth(width - 72);
            table.setLockedWidth(true);

            table = new PdfPTable(columnDefinitionSize);
            table.getDefaultCell().setBorder(0);
            table.setHorizontalAlignment(0);
            table.setTotalWidth(width - 72);
            table.setLockedWidth(true);

            cell = new PdfPCell(new Phrase("Table added with document.add()"));
            cell.setColspan(columnDefinitionSize.length);
            table.addCell(cell);
            cell.addElement(new Phrase("Hello1"));
            cell.addElement(new Phrase("Hello2"));
            cell.addElement(new Phrase("Hello3"));
            cell.addElement(new Phrase("Hello4"));
            cell.addElement(new Phrase("Hello5"));

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);

            System.out.println(table);

            //TESTCODE

//            float margin = 50;
//            float yStart = document   getMediaBox().getHeight() - margin;
//            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
//            float yPosition = yStart;
//            int rows = 1;
//            int cols = 5;
//            float rowHeight = 20;
//            float tableHeight = rowHeight * rows;
//            float tableYBottom = yStart - tableHeight;


            //TESTCODE


            Font paraFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph paragraph = new Paragraph(content, paraFont);
//        paragraph.add(new Chunk("SOme thibk"));
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);

            document.add(table);


            document.close();
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(null);
    }
}
