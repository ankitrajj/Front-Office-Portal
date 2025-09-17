package in.ankit.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
    
	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendEmail(String toEmail, String subject, String body ) {
		boolean isSent = false;
		MimeMessage mimeMsg = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
		try {
			helper.setTo(toEmail);
			helper.setSubject(subject);
		    helper.setText(body,true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		 mailSender.send(mimeMsg);
       
		isSent = true;
		return isSent;
		
	}
}
