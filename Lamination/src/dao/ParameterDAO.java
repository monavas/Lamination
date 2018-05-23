package dao;

import java.util.List;

import entity.Parameter;

public interface ParameterDAO {
	public void save(Parameter parameter);
	
	public Parameter getParameter(int id);
	
	public List<Parameter> list();
	
	public void update(Parameter parameter);
	
	public void remove(Parameter parameter);
	
	public Parameter getParameter(String code);
}
