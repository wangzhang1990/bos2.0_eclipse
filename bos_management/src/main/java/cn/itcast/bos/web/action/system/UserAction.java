package cn.itcast.bos.web.action.system;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@Controller
@ParentPackage("json-default")
@Scope("prototype")
public class UserAction extends BaseAction<User> {
	
	
	
	@Action(value = "user_login", results = { 
			@Result(name = "success", location = "/index.html", type = "redirect"), 
			@Result(name = "login", location= "/login.html", type = "redirect") })
	public String login() {
		System.out.println(model.getUsername() + model.getPassword());
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			subject.login(token);
			return SUCCESS;
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LOGIN;
		}
	}
}
