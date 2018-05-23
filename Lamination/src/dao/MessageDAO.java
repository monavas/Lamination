package dao;

import java.util.List;

import entity.Message;

public interface MessageDAO {
public void save(Message message);
	
	public Message getMessage(int id);
	
	public List<Message> list();
	
	public void update(Message message);
	
	public void remove(Message message);
}
