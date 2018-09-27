package cn.itcast.web.actions;

import javax.jws.WebService;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
import cn.itcast.web.actions.common.BaseAction;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
	
	private String sendAreaInfo;
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}


	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value = "order_add", results = { @Result(name = "success", type = "redirect", location = "/index.html") })
	public String add() {
		System.out.println(model);
		System.out.println(sendAreaInfo);
		System.out.println(recAreaInfo);
		
		String[] sendAreaInfoArr = sendAreaInfo.split("/");
		String[] recAreaInfoArr = recAreaInfo.split("/");
		
		Area sendArea = new Area();
		sendArea.setProvince(sendAreaInfoArr[0]);
		sendArea.setCity(sendAreaInfoArr[1]);
		sendArea.setDistrict(sendAreaInfoArr[2]);
		Area recArea = new Area();
		recArea.setProvince(recAreaInfoArr[0]);
		recArea.setCity(recAreaInfoArr[1]);
		recArea.setDistrict(recAreaInfoArr[2]);
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		model.setCustomer_id(customer.getId());
		
		//System.out.println(model);
		
		WebClient.create("http://localhost:9999/bos_management/services/orderService/order/save").type(MediaType.APPLICATION_JSON).post(model);
		return SUCCESS;
	}
}
