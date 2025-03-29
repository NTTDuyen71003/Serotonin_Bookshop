package bookshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bookshop.models.Order;
import bookshop.repository.OrderRepository;


@Service
public class OrderDetailService {

	@Autowired
	OrderRepository repo;

	public List<Order> listAll() {
		return (List<Order>) repo.findAll();
	}

}
