package cn.itcast.crm.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerServiceImplTest {
	
	@Autowired
	private CustomerService customerService;
	@Test
	public void testFindNoAssociationCustomers() {
		List<Customer> findNoAssociationCustomers = customerService.findNoAssociationCustomers();
		System.out.println(findNoAssociationCustomers);
	}

	@Test
	public void testFindHasAssociationCustomers() {
		List<Customer> findHasAssociationCustomers = customerService.findHasAssociationCustomers("xia001");
		System.out.println(findHasAssociationCustomers);
	}

	@Test
	public void testAssociationCustomersToFixedArea() {
		customerService.associationCustomersToFixedArea("1-2-3", "xia001");
	}

}
