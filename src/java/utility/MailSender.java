/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author nikli
 */
public class MailSender {

    public static void send(String toEmail, String subject, String text) {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            Session session = (Session) envCtx.lookup("mail/Session");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@apdvoice.com"));
            InternetAddress to[] = new InternetAddress[1];
            to[0] = new InternetAddress(toEmail);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(text, "text/plain");
            Transport.send(message);
        } catch (NamingException | MessagingException ex) {
            System.out.println("Mail couldn't send: "+ex.getMessage());
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

}
