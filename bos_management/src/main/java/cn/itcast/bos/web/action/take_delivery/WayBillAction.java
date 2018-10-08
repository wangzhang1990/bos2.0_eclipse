package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);
	
	@Autowired
	private WayBillService wayBillService;
	
	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String save() {
		
		System.out.println("------id------------" + model.getOrder().getId());
		if (model.getOrder() != null && model.getOrder().getId() == null) {
			model.setOrder(null);
		}
		wayBillService.save(model);
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("success", true);
		hashMap.put("msg", "运单保存成功");
		LOGGER.info("这里是log日志...保存运单成功，运单号" + model.getWayBillNum());
		ActionContext.getContext().getValueStack().push(hashMap);
		return SUCCESS;
	}
	
	
	@Action(value = "waybill_query", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<WayBill> pageData = wayBillService.findPageData(pageable);
		
		pushPageDateToValueStack(pageData);
		return SUCCESS;
	}
	
	@Action(value = "wayBill_findByWayBillNum", results = { @Result(name = "success", type = "json") })
	public String findByWayBillNum() {
		WayBill wayBill = wayBillService.findByOrderNum(model.getWayBillNum());
		HashMap<Object, Object> hashMap = new HashMap<>();
		
		if (wayBill != null) {
			hashMap.put("success", true);
			hashMap.put("wayBillData", wayBill);
		} else {
			hashMap.put("success", false);
			hashMap.put("wayBillData", null);
		}
		
		ActionContext.getContext().getValueStack().push(hashMap);
		return SUCCESS;
	}
}
