package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	//查询所有未关联客户列表
	@Path("/noassociationcustomers")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssociationCustomers();
	
	//已经关联到指定定区历客户列表
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findHasAssociationCustomers(@PathParam("fixedareaid") String fixedAreaId);
	
	//将客户关联到定区上，将所有客户，拼成字符串
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void associationCustomersToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr, 
			@QueryParam("fixedAreaId") String fixedAreaId);
	
	@Path("/customer")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void register(Customer customer);
	
	@Path("/customer/telephone/{phoneNum}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public Customer findByPhone(@PathParam("phoneNum") String phoneNum);
	
	@Path("/customer/emailType")
	@PUT
	@Produces({ "application/xml", "application/json" })
	public void updateCustomer(Customer customer);
	
	@Path("/customer/login")
	@GET
	@Produces({ "application/xml", "application/json" })
	public Customer login(@QueryParam("telephone") String telephone, 
			@QueryParam("password") String password);
	
	@Path("/customer/findFixedAreaIdByAddress")
	@GET
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
