package bookshop.commom;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import bookshop.models.CartItem;
import bookshop.models.Order;
import bookshop.models.User;
import bookshop.repository.ProductRepository;
import bookshop.service.ShoppingCartService;


@Service
public class CommomDataService {

	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	public JavaMailSender emailSender;
	
	@Autowired
	TemplateEngine templateEngine;

	public void commonData(Model model, User user) {
		listCategoryByProductName(model);

		Integer totalCartItems = shoppingCartService.getCount();


		model.addAttribute("totalCartItems", totalCartItems);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);

	}
	
	// count product by category
	public void listCategoryByProductName(Model model) {

		List<Object[]> countProductByCategory = productRepository.listCategoryByProductName();
		model.addAttribute("countProductByCategory", countProductByCategory);
	}
	
	//sendEmail by order success
	public void sendSimpleEmail(String email, String subject, String contentEmail, Collection<CartItem> cartItems,
			double totalPrice, Order orderFinal) throws MessagingException {
		Locale locale = LocaleContextHolder.getLocale();

		// Prepare the evaluation context
		Context ctx = new Context(locale);
		ctx.setVariable("cartItems", cartItems);
		ctx.setVariable("totalPrice", totalPrice);
		ctx.setVariable("orderFinal", orderFinal);
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setTo(email);
		// Create the HTML body
		String htmlContent = "";
		htmlContent = templateEngine.process("mail/email_en.html", ctx);
		mimeMessageHelper.setText(htmlContent, true);

		// Send Message!
		emailSender.send(mimeMessage);
	}
}
