package cn.itcast.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> findPageData(Pageable pageable);
	
	@Path("/promotion")
	@GET
	@Produces({ "application/xml", "application/json" })
	PageBean<Promotion> findPageData(@QueryParam("page") int page,@QueryParam("rows") int rows);
	
	@Path("/promotionById")
	@GET
	@Produces({ "application/xml", "application/json" })
	Promotion findById(@QueryParam("id") Integer id);

	void updateStatus(Date date);
	
}
