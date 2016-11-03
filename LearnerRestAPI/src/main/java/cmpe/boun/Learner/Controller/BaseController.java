package cmpe.boun.Learner.Controller;

import java.util.*;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import cmpe.boun.Learner.DAO.UserDao;
import cmpe.boun.Learner.Model.User;

@Controller
public class BaseController {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);
	

	ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
	
	UserDao userDao = (UserDao) context.getBean("UserDao");

	@RequestMapping(value = "/getUsers", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	public void getUsers(HttpServletResponse response)
			throws Exception {

		List<User> users = userDao.getUsers();

		String json = new Gson().toJson(users);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);

	}
		

	   

}

