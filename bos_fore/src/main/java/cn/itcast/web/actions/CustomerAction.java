package cn.itcast.web.actions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.crm.domain.Customer;
import cn.itcast.sms.utils.SmsUtils;
import cn.itcast.utils.MailUtils;

@Controller
@ParentPackage("struts-default")
@Namespace("/")
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
	
	private Customer customer = new Customer();
	@Override
	public Customer getModel() {
		// TODO Auto-generated method stub
		return customer;
	}
	
	@Action(value = "customer_sendSms")
	public String sendSms() {
		String code = RandomStringUtils.randomNumeric(4);
		System.out.println("验证码为： " + code + ", 即将发送给用户" + customer.getTelephone());
		ServletActionContext.getRequest().getSession().setAttribute("sms", code);
		//调用发短信的util，发送短信验证码。这里就用假数据替代。
		try {
			//String smsResult = SmsUtils.sendSmsByHTTP(customer.getTelephone(), "您好，验证码为【" + code + "】");
			String smsResult = "000/abcdefg";
			if (smsResult.startsWith("000")) {
				return NONE;
			} else {
				throw new RuntimeException("短信发送失败，信息码" + smsResult);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
		
	}
	
	private String checkCode;
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	@Autowired
	private RedisTemplate<String, String> redistemplate;
	
	@Action(value = "customer_register", results = { 
			@Result(name = "success", type = "redirect", location = "signup-success.html"), 
			@Result(name = "input", type = "redirect", location = "signup.html") })
	public String register() {
		System.out.println(customer);
		String smsCode = (String) ServletActionContext.getRequest().getSession().getAttribute("sms");
		if (smsCode == null | !smsCode.equals(checkCode)) {
			System.out.println("验证码错误，请重新注册");
			return INPUT;
		}
		
		WebClient.create("http://localhost:9998/crm_management/services/customerService/customer").type(MediaType.APPLICATION_JSON).post(customer);
		System.out.println("注册成功");
		
		//发送邮件
		String emailCode = RandomStringUtils.randomNumeric(32);
		redistemplate.opsForValue().set(customer.getTelephone(), emailCode, 24, TimeUnit.HOURS);
		String content = "<a href='" + MailUtils.activeUrl + "?telephone=" + customer.getTelephone() + "&emailCode=" + emailCode + "'>请点击此链接进行激活</a>";
		MailUtils.sendMail("激活邮件", content, customer.getEmail());
		return SUCCESS;
	}
	
	private String emailCode;
	
	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

	@Action(value = "customer_activeMail")
	public String activeMail() {
		System.out.println(emailCode);
		System.out.println(customer.getTelephone());
		String redisCode = redistemplate.opsForValue().get(customer.getTelephone());
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		//判断激活码是否有效
		if (redisCode == null || !redisCode.equals(emailCode)) {
			try {
				ServletActionContext.getResponse().getWriter().write("无效的激活码");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//判断是否已经绑定
			Customer customerByPhone = WebClient.create("http://localhost:9998/crm_management/"
					+ "services/customerService/customer/telephone/" + customer.getTelephone()).get(Customer.class);
			System.out.println(customerByPhone);
			if (customerByPhone.getType() == null || customerByPhone.getType() != 1) {
				customerByPhone.setType(1);
				WebClient.create("http://localhost:9998/crm_management/services/customerService/customer/emailType")
					.type(MediaType.APPLICATION_JSON).put(customerByPhone);
				try {
					ServletActionContext.getResponse().getWriter().write("成功激活");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					ServletActionContext.getResponse().getWriter().write("已经激活，无须重复激活");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return NONE;
	}
}
