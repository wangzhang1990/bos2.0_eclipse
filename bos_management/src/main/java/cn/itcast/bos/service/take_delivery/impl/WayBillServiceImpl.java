package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	
	
	@Override
	public void save(WayBill model) {
		// TODO Auto-generated method stub
		wayBillRepository.save(model);
		wayBillIndexRepository.save(model);
	}
	/*@Override
	public Page<WayBill> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return wayBillRepository.findAll(pageable);
	}*/
	@Override
	public WayBill findByOrderNum(String wayBillNum) {
		// TODO Auto-generated method stub
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
	@Override
	public Page<WayBill> findPageData(WayBill model, Pageable pageable) {
		// TODO Auto-generated method stub
		/*return wayBillRepository.findAll(pageable);*/
		//判断是有条件还是无条件查询
		if (StringUtils.isBlank(model.getRecAddress()) && StringUtils.isBlank(model.getSendAddress())) {
			//直接在数据库查询
			return wayBillRepository.findAll(pageable);
		} else {
			//有条件查询不在数据库差，而是在索引里查
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			
			if (StringUtils.isNotBlank(model.getSendAddress())) {
				WildcardQueryBuilder query = new WildcardQueryBuilder("sendAddress", "*" + model.getSendAddress() + "*");
				boolQueryBuilder.must(query);
			}
			
			if (StringUtils.isNotBlank(model.getRecAddress())) {
				//条件1
				WildcardQueryBuilder query = new WildcardQueryBuilder("sendAddress", "*" + model.getRecAddress() + "*");
				
				//条件2
				QueryStringQueryBuilder queryStringQueryBuilder
					= new QueryStringQueryBuilder(model.getRecAddress()).field("recAddress").defaultOperator(Operator.AND);
				
				BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
				boolQueryBuilder2.should(query);
				boolQueryBuilder2.should(queryStringQueryBuilder);
				
				boolQueryBuilder.must(boolQueryBuilder2);
			}
			
			NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
			nativeSearchQuery.setPageable(pageable);
			
			return wayBillIndexRepository.search(nativeSearchQuery);
		}
	}

}
