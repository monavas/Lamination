package dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.NewDAO;
import entity.New;
import util.HibernateUtil;

public class NewDAOImpl implements NewDAO{

	@Override
	public void save(New pNew) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(pNew);
		t.commit();
	}

	@Override
	public New getNew(int id) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		New n = (New) session.load(New.class, id);
		return n;
	}

	@Override
	public List<New> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<New> lista = session.createQuery("FROM New").list();
		t.commit();
		return lista;
	}

	@Override
	public void update(New pNew) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(pNew);
		t.commit();
	}

	@Override
	public void remove(New pNew) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(pNew);
		t.commit();
	}

	
}
