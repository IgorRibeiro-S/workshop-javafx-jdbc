package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDAO depdao = DaoFactory.creteDepartmentDao();

	public List<Department> findAll() {
		return depdao.findAll();
	}

	public void saveOrUpdate(Department dep) {
		if (dep.getId() == null) {
			depdao.insert(dep);
		} else {
			depdao.update(dep);
		}
	}
}
