package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	@Autowired
	private WayBillRepository wayBillRepository;
	@Override
	public void save(WayBill model) {
		// TODO Auto-generated method stub
		wayBillRepository.save(model);
	}
	@Override
	public Page<WayBill> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return wayBillRepository.findAll(pageable);
	}
	@Override
	public WayBill findByOrderNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}

}
