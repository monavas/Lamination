package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import entity.Audit;
import entity.Missingsheet;
import entity.Repeatedsheet;
import service.AuditService;
import service.MissingsheetService;
import service.RepeatedsheetService;
import service.UserService;

@ManagedBean
public class SheetBean {
	private List<Missingsheet> myMissingSheets;
	private List<Repeatedsheet> myRepeatedSheets;
	private List<MissingSheetAux> missingSheets;
	private List<RepeatedSheetAux> repeatedSheets;
	private List<Missingsheet> allMissingSheets;
	private List<Repeatedsheet> allRepeatedSheets;
	private MissingsheetService msService = new MissingsheetService();
	private RepeatedsheetService rsService = new RepeatedsheetService();
	private AuditService auditService = new AuditService();
	private MissingSheetAux selectedM;
	
	private RepeatedSheetAux selectedR;
	
	
	public String agregarLamina(Missingsheet selM){
		
		if(selM.getUserId()==0){
			selM.setUserId(UserBean.darUsuarioEnSesion().getId());
			msService.save(selM);
		}else{
			msService.remove(selM);
		}
		
		
		return "";
	}
	
	public String agregarLaminaR(Repeatedsheet selR) {
		
		if(selR.getUserId()==0){
			selR.setUserId(UserBean.darUsuarioEnSesion().getId());
			rsService.save(selR);
		}else{
			rsService.remove(selR);
		}
		
		return "";
	}
	
	public List<Missingsheet> getAllMissingSheets() {
		getMyMissingSheets();
		allMissingSheets = new ArrayList<Missingsheet>();
		for(int z = 0;z<=639;z++){
			Missingsheet s = new Missingsheet();
			s.setCountSheets(1);
			s.setUserId(0);
			s.setNumberSheets(z);
			allMissingSheets.add(s);
		}
		for(Missingsheet s : myMissingSheets){
			allMissingSheets.set(s.getNumberSheets(), s);
		}
		return allMissingSheets;
	}
	public void setAllMissingSheets(List<Missingsheet> allMissingSheets) {
		this.allMissingSheets = allMissingSheets;
	}
	public List<MissingSheetAux> getMissingSheets() {
		UserService u = new UserService();
		getMyRepeatedSheets();
		missingSheets = new ArrayList<MissingSheetAux>();
		List<Missingsheet> missingSheetsA = msService.getMissingsheetTheir(UserBean.darUsuarioEnSesion());
		for(int z = 0;z<missingSheetsA.size();z++){
			MissingSheetAux a = new MissingSheetAux();
			Missingsheet ms = missingSheetsA.get(z);
			a.setMissingSheet(ms);
				
				boolean seguir = true;
				for(int i = 0;i<myRepeatedSheets.size();i++){
					if(myRepeatedSheets.get(i).getNumberSheets()==ms.getNumberSheets()){
						seguir = false;
					}
				}
				if(!seguir){
					a.setUser(u.getUser(ms.getUserId()));
					missingSheets.add(a);
				}
			
		}
		
		
		return missingSheets;
	}
	public void setMissingSheets(List<MissingSheetAux> missingSheets) {
		this.missingSheets = missingSheets;
	}
	public List<RepeatedSheetAux> getRepeatedSheets() {
		UserService u = new UserService();
		getMyMissingSheets();
		repeatedSheets = new ArrayList<RepeatedSheetAux>();
		List<Repeatedsheet> repeatedSheetsA = rsService.getRepeatedsheetsTheir(UserBean.darUsuarioEnSesion());
		for(int z = 0;z<repeatedSheetsA.size();z++){
			RepeatedSheetAux a = new RepeatedSheetAux();
			Repeatedsheet rs = repeatedSheetsA.get(z);
			a.setRepeatedSheet(rs);
				
				boolean seguir = true;
				for(int i = 0;i<myMissingSheets.size();i++){
					if(myMissingSheets.get(i).getNumberSheets()==rs.getNumberSheets()){
						seguir = false;
					}
				}
				if(!seguir){
					a.setUser(u.getUser(rs.getUserId()));
					repeatedSheets.add(a);
				}
				
			
		}
		
		return repeatedSheets;
	}
	public void setRepeatedSheets(List<RepeatedSheetAux> repeatedSheets) {
		this.repeatedSheets = repeatedSheets;
	}
	public List<Missingsheet> getMyMissingSheets() {
		myMissingSheets = msService.getMissingsheetsMine(UserBean.darUsuarioEnSesion());
		return myMissingSheets;
	}
	public void setMyMissingSheets(List<Missingsheet> myMissingSheets) {
		this.myMissingSheets = myMissingSheets;
	}
	public List<Repeatedsheet> getMyRepeatedSheets() {
		myRepeatedSheets = rsService.getRepeatedsheetsMine(UserBean.darUsuarioEnSesion());
		return myRepeatedSheets;
	}
	public void setMyRepeatedSheets(List<Repeatedsheet> myRepeatedSheets) {
		this.myRepeatedSheets = myRepeatedSheets;
	}
	public MissingSheetAux getSelectedM() {
		return selectedM;
	}
	public void setSelectedM(MissingSheetAux selected) {
		this.selectedM = selected;
	}
	public void enviarMensajeMissing(){
		Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.user", "laminationmundial@gmail.com");
        props.put("mail.smtp.password", "LAMINATIONmundial2018");
 
        Session session = Session.getInstance(props,null);
 
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("laminationmundial@gmail.com"));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress (selectedM.getUser().getEmailAddress()));
            message.setSubject("Intercambio");
            message.setText("Lamination.\n"
            		+ "El usuario "+UserBean.darUsuarioEnSesion().getUserName()+" tiene la lámina número "+selectedM.getMissingSheet().getNumberSheets()+", la cuál tu necesitas.\n"
            		+ "Si deseas contactarte con este usuario, su correo es "+UserBean.darUsuarioEnSesion().getEmailAddress()+" y su teléfono es: "+UserBean.darUsuarioEnSesion().getPhoneNumber()+"\n"
            		+ "Para mas información puedes comunicarte con nosotros: laminationmundial@gmail.com");
            
            Transport t = session.getTransport("smtp");
            t.connect("smtp.gmail.com","laminationmundial@gmail.com","LAMINATIONmundial2018");
            t.sendMessage(message,message.getAllRecipients());
            t.close();
            
            Audit audit = new Audit();
    		audit.setCreateDate(new Date());
    		audit.setOperation("Contactó");
    		audit.setUserId(UserBean.darUsuarioEnSesion().getId());
    		audit.setTableId(selectedM.getUser().getId());
    		audit.setTableName("Missingsheets");
    		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
			}
			audit.setIp(ipAddress);
    		auditService.save(audit);
            
 
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
	
	public RepeatedSheetAux getSelectedR() {
		return selectedR;
	}
	public void setSelectedR(RepeatedSheetAux selectedR) {
		this.selectedR = selectedR;
	}
	public void enviarMensajeRepeated(){
		Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.user", "laminationmundial@gmail.com");
        props.put("mail.smtp.password", "LAMINATIONmundial2018");
 
        Session session = Session.getInstance(props,null);
 
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("laminationmundial@gmail.com"));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(selectedR.getUser().getEmailAddress()));
            message.setSubject("Intercambio");
            message.setText("Lamination.\n"
            		+ "El usuario "+UserBean.darUsuarioEnSesion().getUserName()+" desea la lámina número "+selectedR.getRepeatedSheet().getNumberSheets()+", la cuál tu tienes repetida.\n"
            		+ "Si deseas contactarte con este usuario, su correo es "+UserBean.darUsuarioEnSesion().getEmailAddress()+" y su teléfono es: "+UserBean.darUsuarioEnSesion().getPhoneNumber()+"\n"
            		+ "Para mas información puedes comunicarte con nosotros: laminationmundial@gmail.com");
            
            Transport t = session.getTransport("smtp");
            t.connect("smtp.gmail.com","laminationmundial@gmail.com","LAMINATIONmundial2018");
            t.sendMessage(message,message.getAllRecipients());
            t.close();
            
            Audit audit = new Audit();
    		audit.setCreateDate(new Date());
    		audit.setOperation("Contactó");
    		audit.setUserId(UserBean.darUsuarioEnSesion().getId());
    		audit.setTableId(selectedR.getUser().getId());
    		audit.setTableName("Repeatedsheets");
    		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
			}
			audit.setIp(ipAddress);
    		auditService.save(audit);
            
 
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}

	public List<Repeatedsheet> getAllRepeatedSheets() {
		getMyRepeatedSheets();
		allRepeatedSheets = new ArrayList<Repeatedsheet>();
		for(int z = 0;z<=669;z++) {
			Repeatedsheet rs = new Repeatedsheet();
			rs.setCountSheets(1);
			rs.setNumberSheets(z);
			rs.setUserId(0);
			allRepeatedSheets.add(rs);
		}
		for (Repeatedsheet rs : myRepeatedSheets) {
			allRepeatedSheets.set(rs.getNumberSheets(), rs);
		}
		return allRepeatedSheets;
	}

	public void setAllRepeatedSheets(List<Repeatedsheet> allRepeatedSheets) {
		this.allRepeatedSheets = allRepeatedSheets;
	}
	
}
