package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Seller;

public class SellerService {

	private SellerDAO depdao = DaoFactory.creteSellerDao();

	public List<Seller> findAll() {
		return depdao.findAll();
	}

	public void saveOrUpdate(Seller dep) {
		if (dep.getId() == null) {
			depdao.insert(dep);
		} else {
			depdao.update(dep);
		}
	}
	
	public void remove(Seller dep) {
		depdao.deleteById(dep.getId());
	}
}
