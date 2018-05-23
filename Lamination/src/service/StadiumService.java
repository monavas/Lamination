package service;

import java.util.List;

import dao.StadiumDAO;
import dao.impl.StadiumDAOImpl;
import entity.Stadium;;
public class StadiumService {
	private StadiumDAO stadiumDAO = new StadiumDAOImpl();
	
	public void save(Stadium stadium){
		stadiumDAO.save(stadium);
	}
	
	public Stadium getStadium(int id){
		return stadiumDAO.getStadium(id);
	}
	
	public List<Stadium> list(){
		return stadiumDAO.list();
	}
	
	public void update(Stadium stadium){
		stadiumDAO.update(stadium);
	}
	
	public void remove(Stadium stadium){
		stadiumDAO.remove(stadium);
	}
}
