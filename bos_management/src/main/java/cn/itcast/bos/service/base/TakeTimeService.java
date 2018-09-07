package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.TakeTime;


public interface TakeTimeService {
	
	List<TakeTime> findAll();

}
