package dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import dao.MissingsheetDAO;
import entity.Missingsheet;
import entity.User;
import util.HibernateUtil;

public class MissingsheetDAOImpl implements MissingsheetDAO{
	
	@Override
	public void save(Missingsheet sheet) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(sheet);
		t.commit();
	}

	@Override
	public Missingsheet getMissingsheet(int id) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Missingsheet m = (Missingsheet) session.load(Missingsheet.class, id);
		return m;
	}

	@Override
	public List<Missingsheet> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<Missingsheet> lista = session.createQuery("FROM Missingsheet").list();
		t.commit();
		return lista;
	}

	@Override
	public void update(Missingsheet sheet) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(sheet);
		t.commit();
	}

	@Override
	public void remove(Missingsheet sheet) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(sheet);
		t.commit();
	}

	@Override
	public List<Missingsheet> getMissingsheetsMine(User user) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(Missingsheet.class);
		cr.add(Restrictions.eq("userId", user.getId()));
		List<Missingsheet> lista = cr.list();
		t.commit();
		return lista;
	}

	@Override
	public List<Missingsheet> getMissingsheetsTheir(User user) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		Criteria cr = session.createCriteria(Missingsheet.class);
		cr.add(Restrictions.not(Restrictions.eq("userId", user.getId())));
		List<Missingsheet> lista = cr.list();
		t.commit();
		return lista;
	}
	
}
