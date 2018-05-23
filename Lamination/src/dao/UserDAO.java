package dao;

import entity.User;
import java.util.List;

public interface UserDAO {

	public void save(User usuario);

	public User getUser(int id);

	public List<User> list();

	public void remove(User usuario);

	public void update(User usuario);
	
	public User getUserUserName(String userName);
	
	public User getUserLogin(String userName, String password);

}
