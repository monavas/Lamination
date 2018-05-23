package dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dao.ParameterDAO;
import entity.Parameter;
import entity.User;
import util.HibernateUtil;

public class ParameterDAOImpl implements ParameterDAO{

	@Override
	public void save(Parameter parameter) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(parameter);
		t.commit();
	}

	@Override
	public Parameter getParameter(int id) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Parameter p = (Parameter) session.load(Parameter.class, id);
		return p;
	}

	@Override
	public List<Parameter> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<Parameter> lista = session.createQuery("FROM Parameter").list();
		t.commit();
		return lista;
	}

	@Override
	public void update(Parameter parameter) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(parameter);
		t.commit();
	}

	@Override
	public void remove(Parameter parameter) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(parameter);
		t.commit();
	}

	@Override
	public Parameter getParameter(String code) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(Parameter.class);
		cr.add(Restrictions.eq("parameterCode", code));
		List<Parameter> lista = cr.list();
		Parameter u = null;
		if(lista.size()==1){
			u = lista.get(0);
		}
		t.commit();
		return u;
	}

}
