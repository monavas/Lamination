package service;

import java.util.List;

import dao.RepeatedsheetDAO;
import dao.impl.RepeatedsheetDAOImpl;
import entity.Repeatedsheet;
import entity.User;

public class RepeatedsheetService {
	private RepeatedsheetDAO repeatedsheetDAO = new RepeatedsheetDAOImpl();
	
	public void save(Repeatedsheet sheet){
		repeatedsheetDAO.save(sheet);
	}
	
	public Repeatedsheet getRepeatedsheet(int id){
		return repeatedsheetDAO.getRepeatedsheet(id);
	}
	
	public List<Repeatedsheet> list(){
		return repeatedsheetDAO.list();
	}
	
	public void update(Repeatedsheet sheet){
		repeatedsheetDAO.update(sheet);
	}
	
	public void remove(Repeatedsheet sheet){
		repeatedsheetDAO.remove(sheet);
	}
	
	public List<Repeatedsheet> getRepeatedsheetsMine(User user){
		return repeatedsheetDAO.getRepeatedsheetsMine(user);
	}
	
	public List<Repeatedsheet> getRepeatedsheetsTheir(User user){
		return repeatedsheetDAO.getRepeatedsheetsTheir(user);
	}
}
