package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class TakeTimeAaction extends ActionSupport implements ModelDriven<TakeTime> {
	
	private TakeTime takeTime = new TakeTime();
	@Override
	public TakeTime getModel() {
		// TODO Auto-generated method stub
		return takeTime;
	}
	
	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value = "takeTime_findAll", results = { @Result(name = "success", type = "json")})
	public String findAll() {
		List<TakeTime> findAll = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(findAll);
		return SUCCESS;
	}
}
