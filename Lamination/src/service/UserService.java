package service;

import java.util.List;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import entity.User;

public class UserService {
	private UserDAO userDAO = new UserDAOImpl();
	
	public void save(User user){
		userDAO.save(user);
	}
	
	public User getUser(int id){
		return userDAO.getUser(id);
	}
	
	public void uptade(User user){
		userDAO.update(user);
	}
	
	public void remove(User user){
		userDAO.remove(user);
	}
	
	public List<User> list(){
		return userDAO.list();
	}
	
	public User getUserUserName(String userName){
		return userDAO.getUserUserName(userName);
	}
	
	public User getUserLogin(String userName, String password){
		return userDAO.getUserLogin(userName, password);
	}
}
