package cn.itcast.web.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
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

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.web.actions.common.BaseAction;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {
	
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		PageBean<Promotion> pageBean = WebClient.create("http://localhost:9999/bos_management/services/promotionService"
							+ "/promotion?page=" + page + "&rows=" + rows).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	
	@Action(value = "promotion_showDetail")
	public String showDetail() throws IOException, TemplateException {
		System.out.println(model.getId() + "----------------------");
		String htmlPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
		File html = new File(htmlPath + "/" + model.getId() + ".html");
		if (!html.exists()) {
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/template/freemarker_templates"));
			Template template = configuration.getTemplate("promotion_detail.ftl");
			
			Promotion promotion = WebClient.create("http://localhost:9999/bos_management/services"
					+ "/promotionService/promotionById?id=" + model.getId()).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("promotion", promotion);
			
			System.out.println();
			template.process(hashMap, new OutputStreamWriter(new FileOutputStream(html), "utf-8"));
		}
		
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		FileUtils.copyFile(html, ServletActionContext.getResponse().getOutputStream());
		return "none";
	}
}
