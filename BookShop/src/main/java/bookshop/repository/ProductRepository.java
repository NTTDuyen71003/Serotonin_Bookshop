package bookshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bookshop.models.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	// List product by category
	@Query(value = "SELECT * FROM products WHERE category_id = ?", nativeQuery = true)
	public List<Product> listProductByCategory(Long categoryId);

	// Top 10 products by category excluding the current product
	@Query(value = "SELECT * FROM products WHERE category_id = ?1 AND product_id <> ?2 ORDER BY product_id DESC LIMIT 10;", nativeQuery = true)
	List<Product> listProductByCategory10(Long categoryId, Long productId);

	// List product new
	@Query(value = "SELECT * FROM products ORDER BY entered_date DESC limit 20;", nativeQuery = true)
	public List<Product> listProductNew20();
	
	// Search Product
	@Query(value = "SELECT * FROM products WHERE product_name LIKE %?1%" , nativeQuery = true)
	public List<Product> searchProduct(String productName);
	
	// count quantity by product
	@Query(value = "SELECT c.category_id,c.category_name,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM products p\r\n"
			+ "JOIN categories c ON p.category_id = c.category_id\r\n"
			+ "GROUP BY c.category_name;" , nativeQuery = true)
	List<Object[]> listCategoryByProductName();
	
	// Top 20 product best sale
	@Query(value = "SELECT p.product_id,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM order_details p\r\n"
			+ "JOIN products c ON p.product_id = c.product_id\r\n"
			+ "GROUP BY p.product_id\r\n"
			+ "ORDER by SoLuong DESC limit 20;", nativeQuery = true)
	public List<Object[]> bestSaleProduct20();
	
	@Query(value = "select * from products o where product_id in :ids", nativeQuery = true)
	List<Product> findByInventoryIds(@Param("ids") List<Integer> listProductId);

}
