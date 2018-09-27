package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
	
	@Autowired
	private OrderService orderService;
	
	@Action(value = "order_findByOrderNum", results = { @Result(name = "success", type = "json") })
	public String findByOrderNum() {
		Order order = orderService.findByOrderNum(model.getOrderNum());
		HashMap<Object, Object> hashMap = new HashMap<>();
		
		if (order != null) {
			hashMap.put("success", true);
			hashMap.put("orderData", order);
		} else {
			hashMap.put("success", false);
			hashMap.put("orderData", null);
		}
		
		ActionContext.getContext().getValueStack().push(hashMap);
		return SUCCESS;
	}
}
