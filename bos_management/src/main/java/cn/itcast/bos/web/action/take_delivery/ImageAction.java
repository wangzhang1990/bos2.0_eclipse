package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {
	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	
	
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}



	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}



	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}



	@Action(value = "img_upload", results = { @Result(name = "success", type = "json") })
	public String upload() {
		String uuid = UUID.randomUUID().toString();
		String suffix = imgFileFileName.substring(imgFileFileName.lastIndexOf('.'));
		String imgNameInServer = uuid + suffix;
		
		String absPath = ServletActionContext.getServletContext().getRealPath("/upload/attached/" + imgNameInServer);
		File fileInServer = new File(absPath);
		try {
			FileUtils.copyFile(imgFile, fileInServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String urlPath = ServletActionContext.getServletContext().getContextPath() + "/upload/attached/" + imgNameInServer;
		System.out.println(absPath);
		System.out.println(urlPath);
		
		HashMap<Object, Object> hashMap = new HashMap<>();
		hashMap.put("error", 0);
		hashMap.put("url", urlPath);
		
		ServletActionContext.getContext().getValueStack().push(hashMap);
		return SUCCESS;
	}
	
	@Action(value = "img_manager", results = { @Result(name = "success", type = "json") })
	public String manager() {
		String rootPath = ServletActionContext.getServletContext().getRealPath("/upload/attached/");
		String rootUrl = ServletActionContext.getServletContext().getContextPath() + "/upload/attached/";
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		
		File currentPathFile = new File(rootPath);
		List<Map<String, Object>> fileList = new ArrayList<>();
		
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		
		HashMap<String, Object> result = new HashMap<>();
		
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
