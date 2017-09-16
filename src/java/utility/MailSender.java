/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
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
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author nikli
 */
public class MailSender {
    
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    public static void send(String toEmail, String subject, String text) {
        final String sendgridApiKey = System.getenv("SENDGRID_API_KEY");
        //final String sendgridSender = System.getenv("SENDGRID_SENDER");
        Email from = new Email("APDVoice <no-reply@apdvoice.com>");
        Email to = new Email(toEmail);
        Content content = new Content("text/html", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error("Mail couldn't send: "+toEmail, ex);
        }
    }

}
