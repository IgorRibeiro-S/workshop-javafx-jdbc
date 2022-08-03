package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentService {
	
	
	private DepartmentDAO depdao = DaoFactory.creteDepartmentDao();
	
	public List<Department> findAll(){
		return depdao.findAll();
	}
}
