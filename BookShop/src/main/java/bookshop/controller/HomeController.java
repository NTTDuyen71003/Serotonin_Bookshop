package bookshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import bookshop.commom.CommomDataService;
import bookshop.models.Product;
import bookshop.models.User;
import bookshop.repository.ProductRepository;


@Controller
public class HomeController extends CommomController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CommomDataService commomDataService;
	


	@GetMapping(value = "/")
	public String home(Model model, User user) {

		commomDataService.commonData(model, user);
		bestSaleProduct20(model, user);
		return "web/home";
	}
	
	// list product ở trang chủ limit 10 sản phẩm mới nhất
	@ModelAttribute("listProduct10")
	public List<Product> listproduct10(Model model) {
		List<Product> productList = productRepository.listProductNew20();
		model.addAttribute("productList", productList);
		return productList;
	}
	
	// Top 20 best sale.
	public void bestSaleProduct20(Model model, User customer) {
		List<Object[]> productList = productRepository.bestSaleProduct20();
		if (productList != null) {
			ArrayList<Integer> listIdProductArrayList = new ArrayList<>();
			for (int i = 0; i < productList.size(); i++) {
				String id = String.valueOf(productList.get(i)[0]);
				listIdProductArrayList.add(Integer.valueOf(id));
			}
			List<Product> listProducts = productRepository.findByInventoryIds(listIdProductArrayList);

			List<Product> listProductNew = new ArrayList<>();

			for (Product product : listProducts) {

				Product productEntity = new Product();

				BeanUtils.copyProperties(product, productEntity);

				listProductNew.add(productEntity);

			}
			model.addAttribute("bestSaleProduct20", listProductNew);
		}
	}
}
