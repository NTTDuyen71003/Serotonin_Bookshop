package bookshop.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bookshop.commom.CommomDataService;
import bookshop.models.Order;
import bookshop.models.OrderDetail;
import bookshop.models.User;
import bookshop.repository.OrderDetailRepository;
import bookshop.repository.OrderRepository;
import bookshop.repository.UserRepository;


@Controller
public class HistoryOrderController extends CommomController{

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	CommomDataService commomDataService;

	@GetMapping(value = "/order")
	public String profile(Model model, Principal principal, User user, Pageable pageable,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

		if (principal != null) {

			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Order> orderPage = findPaginated(PageRequest.of(currentPage - 1, pageSize), user);

		int totalPages = orderPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		commomDataService.commonData(model, user);
		model.addAttribute("orderByUser", orderPage);

		return "web/historyOrder";
	}

	public Page<Order> findPaginated(Pageable pageable, User user) {

		List<Order> orderPage = orderRepository.findOrderByUserId(user.getUserId());

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Order> list;

		if (orderPage.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, orderPage.size());
			list = orderPage.subList(startItem, toIndex);
		}

		Page<Order> orderPages = new PageImpl<Order>(list, PageRequest.of(currentPage, pageSize), orderPage.size());

		return orderPages;
	}
	
	@GetMapping("/order/detail/{order_id}")
	public ModelAndView detail(Model model, Principal principal, User user, @PathVariable("order_id") Long id) {

		if (principal != null) {

			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		
		List<OrderDetail> listO = orderDetailRepository.findByOrderId(id);

//		model.addAttribute("amount", orderRepository.findById(id).get().getAmount());
		model.addAttribute("orderDetail", listO);
//		model.addAttribute("orderId", id);
		// set active front-end
//		model.addAttribute("menuO", "menu");
		commomDataService.commonData(model, user);
		
		return new ModelAndView("web/historyOrderDetail");
	}
	
	@RequestMapping("/order/cancel/{order_id}")
	public ModelAndView cancel(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isEmpty()) {
			return new ModelAndView("redirect:/profile", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 3);
		orderRepository.save(oReal);

		return new ModelAndView("redirect:/historyOrder", model);
	}

}
