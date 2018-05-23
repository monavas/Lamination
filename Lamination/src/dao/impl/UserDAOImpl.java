package dao.impl;

import entity.User;
import dao.UserDAO;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import beans.UserBean;
import util.HibernateUtil;

public class UserDAOImpl implements UserDAO {

	public void save(User User) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(User);
		t.commit();
	}

	public User getUser(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		User u = (User) session.load(User.class, id);
		return u;
	}

	public void update(User User) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(User);
		t.commit();
	}

	public void remove(User User) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(User);
		t.commit();
	}

	public List<User> list() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<User> lista = session.createQuery("FROM User").list();
		t.commit();
		return lista;
	}

	@Override
	public User getUserUserName(String userName) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("userName", userName));
		List<User> lista = cr.list();
		User u = null;
		if(lista.size()==1){
			u = lista.get(0);
		}
		t.commit();
		return u;
	}
	

	@Override
	public User getUserLogin(String userName, String password) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("userName", userName));
		cr.add(Restrictions.eq("password", password));
		List<User> lista = cr.list();
		User u = null;
		if(lista.size()==1){
			u = lista.get(0);
		}
		t.commit();
		return u;
	}

}
