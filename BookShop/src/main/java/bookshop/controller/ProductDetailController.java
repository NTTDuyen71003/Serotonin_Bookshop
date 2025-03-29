package bookshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bookshop.commom.CommomDataService;
import bookshop.models.Product;
import bookshop.models.User;
import bookshop.repository.ProductRepository;


@Controller
public class ProductDetailController extends CommomController{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CommomDataService commomDataService;

	@GetMapping(value = "productDetail")
	public String productDetail(@RequestParam("id") Long id, Model model, User user) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return "redirect:/error"; // or some error page
		}
		model.addAttribute("product", product);

		commomDataService.commonData(model, user);
		listProductByCategory10(model, product.getCategory().getCategoryId(), product.getProductId());

		return "web/productDetail";
	}

	// Gợi ý top 10 sản phẩm cùng loại trừ sản phẩm hiện tại
	public void listProductByCategory10(Model model, Long categoryId, Long productId) {
		List<Product> products = productRepository.listProductByCategory10(categoryId, productId);
		model.addAttribute("productByCategory", products);
	}
}
