package service;

import java.util.List;

import dao.MissingsheetDAO;
import dao.impl.MissingsheetDAOImpl;
import entity.Missingsheet;
import entity.User;

public class MissingsheetService {
	private MissingsheetDAO missingsheetDAO = new MissingsheetDAOImpl();
	
	public void save(Missingsheet sheet){
		missingsheetDAO.save(sheet);
	}
	
	public Missingsheet getMissingsheet(int id){
		return missingsheetDAO.getMissingsheet(id);
	}
	
	public List<Missingsheet> list(){
		return missingsheetDAO.list();
	}
	
	public void update(Missingsheet sheet){
		missingsheetDAO.update(sheet);
	}
	
	public void remove(Missingsheet sheet){
		missingsheetDAO.remove(sheet);
	}
	
	public List<Missingsheet> getMissingsheetsMine(User user){
		return missingsheetDAO.getMissingsheetsMine(user);
	}
	
	public List<Missingsheet> getMissingsheetTheir(User user){
		return missingsheetDAO.getMissingsheetsTheir(user);
	}
}
