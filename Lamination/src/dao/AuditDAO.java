package dao;

import java.util.List;

import entity.Audit;

public interface AuditDAO {
	public void save(Audit audit);
	
	public List<Audit> list();
}
