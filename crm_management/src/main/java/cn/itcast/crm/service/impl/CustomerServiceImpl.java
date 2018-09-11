package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<Customer> findNoAssociationCustomers() {
		// TODO Auto-generated method stub
		customerRepository.findByFixedAreaIdIsNull();
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findHasAssociationCustomers(String fixedAreaId) {
		// TODO Auto-generated method stub
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		customerRepository.clearFixedAreaId(fixedAreaId);
		// TODO Auto-generated method stub
		System.out.println(customerIdStr);
		if (StringUtils.isBlank(customerIdStr)) {
			
			return;
		}
		String[] split = customerIdStr.split("-");
		for (String string : split) {
			customerRepository.updateFixedAreaId(Integer.parseInt(string), fixedAreaId);
		}
	}

	@Override
	public void register(Customer customer) {
		// TODO Auto-generated method stub
		customerRepository.save(customer);
	}

	@Override
	public Customer findByPhone(String phoneNum) {
		// TODO Auto-generated method stub
		Customer customerByTelephone = customerRepository.findByTelephone(phoneNum);
		return customerByTelephone;
	}

	@Override
	public void updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		customerRepository.save(customer);
	}

}
