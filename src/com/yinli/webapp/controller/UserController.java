package com.yinli.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yinli.pojo.User;
import com.yinli.webapp.serviceDao.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService us;
	
	
	@RequestMapping("query.html")
	public String query(HttpServletRequest rq,HttpServletResponse rp,Model model){
		List<User> listU=us.getUsers();
		model.addAttribute("user", listU);
		return "hello";
	}
	
	@RequestMapping("delete.html")
	public String delete(HttpServletRequest rq,HttpServletResponse rp,Model model){
		us.deleteUser(Integer.valueOf(rq.getParameter("id")));
		return "redirect:/query.html";
	}
	
	@RequestMapping("add.html")
	public String add(HttpServletRequest rq,HttpServletResponse rp,Model model){
		rq.setAttribute("user", new User());
		return "helloAdd";
	}
	
	@RequestMapping("update.html")
	public String update(HttpServletRequest rq,HttpServletResponse rp,Model model){
		User usr=us.getUser(Integer.valueOf(rq.getParameter("id")));
		rq.setAttribute("user", usr);
		return "helloAdd";
	}
	
	@RequestMapping("save.html")
	public String save(HttpServletRequest rq,HttpServletResponse rp,User user){
		us.saveUser(user);
		return "redirect:/query.html";
	}
	
}
