package dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.MessageDAO;
import entity.Message;
import util.HibernateUtil;

public class MessageDAOImpl implements MessageDAO{

	@Override
	public void save(Message message) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(message);
		t.commit();
	}

	@Override
	public Message getMessage(int id) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Message u = (Message) session.load(Message.class, id);
		return u;
	}

	@Override
	public List<Message> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<Message> lista = session.createQuery("FROM Message").list();
		t.commit();
		return lista;
	}

	@Override
	public void update(Message message) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.update(message);
		t.commit();
	}

	@Override
	public void remove(Message message) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.delete(message);
		t.commit();
	}

}
