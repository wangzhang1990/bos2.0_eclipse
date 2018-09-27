package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.constant.Constants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private WorkBillRepository workBillRepository;
	
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jsmTemplate;
	@Override
	public void save(Order order) {
		// TODO Auto-generated method stub
		//orderRepository.save(order);
		System.out.println(order);
		order.setOrderNum(UUID.randomUUID().toString());
		order.setOrderTime(new Date());
		order.setStatus("1");
		Area sendArea = order.getSendArea();
		Area area = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
		order.setSendArea(area);
		Area recArea = order.getRecArea();
		Area recAreaOid = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
		order.setRecArea(recAreaOid);
		
		
		
		String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/findFixedAreaIdByAddress?address=" + order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
		
		if (fixedAreaId != null) {
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			Courier courier = fixedArea.getCouriers().iterator().next();
			if (courier != null) {
				
				saveOrder(order, courier);
				generateWorkBill(order);
				System.out.println("直接匹配快递员成功------------------------------");
				System.out.println(order);
				return;
			}
		}
		
		
		Set<SubArea> subareas = area.getSubareas();
		for (SubArea subArea : subareas) {
			if (order.getSendAddress().contains(subArea.getKeyWords())) {
				Courier courier = subArea.getFixedArea().getCouriers().iterator().next();
				saveOrder(order, courier);
				generateWorkBill(order);
				System.out.println("关键字匹配快递员成功------------------------------");
				return;
			}
		}
		for (SubArea subArea : subareas) {
			if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				Courier courier = subArea.getFixedArea().getCouriers().iterator().next();
				saveOrder(order, courier);
				generateWorkBill(order);
				System.out.println("辅助关键字匹配快递员成功------------------------------");
				return;
			}
		}
		
		//进入人工分单
		order.setOrderType("2");
		orderRepository.save(order);
	}
	
	private void generateWorkBill(Order order) {
		// TODO Auto-generated method stub
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		workBill.setSmsNumber(RandomStringUtils.randomNumeric(4));
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		
		workBillRepository.save(workBill);
		
		//发短信
		jsmTemplate.send("bos_orderSms", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("msg", "这是给快递员的信息，这里就不多写了...");
				return mapMessage;
			}
		});
	}

	private void saveOrder(Order order, Courier courier) {
		order.setCourier(courier);
		order.setOrderType("1");
		orderRepository.save(order);
	}

	@Override
	public Order findByOrderNum(String string) {
		// TODO Auto-generated method stub
		return orderRepository.findByOrderNum(string);
	}



}
