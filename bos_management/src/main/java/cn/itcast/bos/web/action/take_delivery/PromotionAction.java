package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
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

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	private File titleImgFile;
	private String titleImgFileFileName;
	
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}


	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	
	@Autowired
	private PromotionService promotionService;
	
	@Action(value = "promotion_save", results = { @Result(name = "success", location = "/pages/take_delivery/promotion.html", type = "redirect") })
	public String save() {
		String uuid = UUID.randomUUID().toString();
		String suffix = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf('.'));
		String imgNameInServer = uuid + suffix;
		
		String absPath = ServletActionContext.getServletContext().getRealPath("/upload/titleImg/" + imgNameInServer);
		File file = new File(absPath);
		try {
			FileUtils.copyFile(titleImgFile, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setTitleImg(ServletActionContext.getServletContext().getContextPath() + "/upload/titleImg/" + imgNameInServer);
		
		promotionService.save(model);
		return SUCCESS;
	}
	
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionService.findPageData(pageable);
		pushPageDateToValueStack(pageData);
		return SUCCESS;
	}
}
