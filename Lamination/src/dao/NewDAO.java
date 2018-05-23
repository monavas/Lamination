package dao;

import java.util.List;

import entity.New;

public interface NewDAO {
	public void save(New pNew);
	
	public New getNew(int id);
	
	public List<New> list();
	
	public void update(New pNew);
	
	public void remove(New pNew);
}
