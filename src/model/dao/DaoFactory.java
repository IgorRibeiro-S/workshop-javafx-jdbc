package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	public static SellerDAO creteSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());		
	}
	
	public static DepartmentDAO creteDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());		
	}
	
}

