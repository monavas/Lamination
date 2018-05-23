package dao;

import java.util.List;

import entity.Stadium;
public interface StadiumDAO {
	public void save(Stadium stadium);
	
	public Stadium getStadium(int id);
	
	public List<Stadium> list();
	
	public void update(Stadium stadium);
	
	public void remove(Stadium stadium);
}
