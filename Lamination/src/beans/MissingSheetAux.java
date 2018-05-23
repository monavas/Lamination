package beans;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import entity.Missingsheet;
import entity.User;

public class MissingSheetAux {
	private Missingsheet missingSheet;
	private User user;
	public Missingsheet getMissingSheet() {
		return missingSheet;
	}
	public void setMissingSheet(Missingsheet missingSheet) {
		this.missingSheet = missingSheet;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
