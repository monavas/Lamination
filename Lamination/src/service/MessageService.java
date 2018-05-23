package service;
import java.util.List;

import dao.MessageDAO;
import dao.impl.MessageDAOImpl;
import entity.Message;
public class MessageService {
	private MessageDAO messageDAO = new MessageDAOImpl();
	
	public void save(Message message){
		messageDAO.save(message);
	}
	
	public Message getMissingsheet(int id){
		return messageDAO.getMessage(id);
	}
	
	public List<Message> list(){
		return messageDAO.list();
	}
	
	public void update(Message message){
		messageDAO.update(message);
	}
	
	public void remove(Message message){
		messageDAO.remove(message);
	}
}
