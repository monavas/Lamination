package service;

import java.util.List;

import dao.AuditDAO;
import dao.impl.AuditDAOImpl;
import entity.Audit;

public class AuditService {
	private AuditDAO auditDAO = new AuditDAOImpl();
	
	public void save(Audit audit){
		auditDAO.save(audit);
	}
	
	public List<Audit> list(){
		return auditDAO.list();
	}
}
