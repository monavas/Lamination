package service;

import java.util.List;

import dao.ParameterDAO;
import dao.impl.ParameterDAOImpl;
import entity.Parameter;

public class ParameterService {
	private ParameterDAO parameterDAO = new ParameterDAOImpl();
	
	public void save(Parameter parameter){
		parameterDAO.save(parameter);
	}
	
	public Parameter getParameter(int id){
		return parameterDAO.getParameter(id);
	}
	
	public List<Parameter> list(){
		return parameterDAO.list();
	}
	
	public void update(Parameter parameter){
		parameterDAO.update(parameter);
	}
	
	public void remove(Parameter parameter){
		parameterDAO.remove(parameter);
	}
	
	public Parameter getParameter(String code){
		return parameterDAO.getParameter(code);
	}
}
