package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	List<Customer> findByFixedAreaIdIsNull();

	List<Customer> findByFixedAreaId(String fixedAreaId);
	
	@Query("update Customer set fixedAreaId = ?2 where id = ?1")
	@Modifying
	void updateFixedAreaId(int parseInt, String fixedAreaId);
	
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	@Modifying
	void clearFixedAreaId(String fixedAreaId);

}
