package dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dao.RepeatedsheetDAO;
import entity.Missingsheet;
import entity.Repeatedsheet;
import entity.User;
import util.HibernateUtil;

public class RepeatedsheetDAOImpl implements RepeatedsheetDAO{

	@Override
	public void save(Repeatedsheet sheet) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(sheet);
		t.commit();
	}

	@Override
	public Repeatedsheet getRepeatedsheet(int id) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Repeatedsheet r = (Repeatedsheet) session.load(Repeatedsheet.class, id);
		return r;
	}

	@Override
	public List<Repeatedsheet> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<Repeatedsheet> lista = session.createQuery("FROM Repeatedsheet").list();
		t.commit();
		return lista;
	}

	@Override
	public void update(Repeatedsheet sheet) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(sheet);
		t.commit();
	}

	@Override
	public void remove(Repeatedsheet sheet) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(sheet);
		t.commit();
	}

	@Override
	public List<Repeatedsheet> getRepeatedsheetsMine(User user) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(Repeatedsheet.class);
		cr.add(Restrictions.eq("userId", user.getId()));
		List<Repeatedsheet> lista = cr.list();
		t.commit();
		return lista;
	}

	@Override
	public List<Repeatedsheet> getRepeatedsheetsTheir(User user) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		Criteria cr = session.createCriteria(Repeatedsheet.class);
		cr.add(Restrictions.not(Restrictions.eq("userId", user.getId())));
		List<Repeatedsheet> lista = cr.list();
		t.commit();
		return lista;
	}

}
