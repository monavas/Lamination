package dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.AuditDAO;
import entity.Audit;
import entity.User;
import util.HibernateUtil;
import util.HibernateUtil;

public class AuditDAOImpl implements AuditDAO{

	@Override
	public void save(Audit audit) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(audit);
		t.commit();
	}

	@Override
	public List<Audit> list() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List<Audit> lista = session.createQuery("FROM Audit").list();
		t.commit();
		return lista;
	}
	
}
