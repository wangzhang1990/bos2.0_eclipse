package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
	@Autowired
	private PromotionRepository promotionRepository;
	@Override
	public void save(Promotion model) {
		// TODO Auto-generated method stub
		promotionRepository.save(model);
	}
	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return promotionRepository.findAll(pageable);
	}
	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		// TODO Auto-generated method stub
		PageRequest pageRequest = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageRequest);
		
		PageBean<Promotion> pageBean = new PageBean<>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}
	@Override
	public Promotion findById(Integer id) {
		// TODO Auto-generated method stub
		return promotionRepository.findOne(id);
	}
	@Override
	public void updateStatus(Date date) {
		// TODO Auto-generated method stub
		promotionRepository.updateStatus(date);
	}

}
