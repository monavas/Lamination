package service;

import java.util.List;

import dao.NewDAO;
import dao.impl.NewDAOImpl;
import entity.New;

public class NewService {
	private NewDAO newDAO = new NewDAOImpl();
	
	public void save(New pNew){
		newDAO.save(pNew);
	}
	
	public New getNew(int id){
		return newDAO.getNew(id);
	}
	
	public List<New> list(){
		return newDAO.list();
	}
	
	public void update(New pNew){
		newDAO.update(pNew);
	}
	
	public void remove(New pNew){
		newDAO.remove(pNew);
	}
}
