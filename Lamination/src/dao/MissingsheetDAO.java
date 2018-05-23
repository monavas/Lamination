package dao;

import java.util.List;

import entity.Missingsheet;
import entity.User;

public interface MissingsheetDAO {
	public void save(Missingsheet sheet);
	
	public Missingsheet getMissingsheet(int id);
	
	public List<Missingsheet> list();
	
	public void update(Missingsheet sheet);
	
	public void remove(Missingsheet sheet);
	
	public List<Missingsheet> getMissingsheetsMine(User user);
	
	public List<Missingsheet> getMissingsheetsTheir(User user);
}
