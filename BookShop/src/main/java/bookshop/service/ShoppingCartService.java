package bookshop.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import bookshop.models.CartItem;
import bookshop.models.Product;


@Service
public interface ShoppingCartService {

	int getCount();

	double getAmount();

	void clear();

	Collection<CartItem> getCartItems();

	void remove(CartItem item);

	void add(CartItem item);

	void remove(Product product);

}
