package dao;

import java.util.List;

import entity.Repeatedsheet;
import entity.User;

public interface RepeatedsheetDAO {
	public void save(Repeatedsheet sheet);
	
	public Repeatedsheet getRepeatedsheet(int id);
	
	public List<Repeatedsheet> list();
	
	public void update(Repeatedsheet sheet);
	
	public void remove(Repeatedsheet sheet);
	
	public List<Repeatedsheet> getRepeatedsheetsMine(User user);
	
	public List<Repeatedsheet> getRepeatedsheetsTheir(User user);
}
