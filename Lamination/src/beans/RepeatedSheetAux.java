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

import entity.Repeatedsheet;
import entity.User;

public class RepeatedSheetAux {
	private Repeatedsheet repeatedSheet;
	private User user;
	public Repeatedsheet getRepeatedSheet() {
		return repeatedSheet;
	}
	public void setRepeatedSheet(Repeatedsheet repeatedSheet) {
		this.repeatedSheet = repeatedSheet;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
