package dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.StadiumDAO;
import entity.Stadium;
import util.HibernateUtil;

public class StadiumDAOImpl implements StadiumDAO{

	@Override
	public void save(Stadium stadium) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(stadium);
		t.commit();
	}

	@Override
	public Stadium getStadium(int id) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Stadium s = (Stadium) session.load(Stadium.class, id);
		return s;
	}

	@Override
	public List<Stadium> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<Stadium> lista = session.createQuery("FROM Stadium").list();
		t.commit();
		return lista;
	}

	@Override
	public void update(Stadium stadium) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(stadium);
		t.commit();
	}

	@Override
	public void remove(Stadium stadium) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(stadium);
		t.commit();
	}

}
