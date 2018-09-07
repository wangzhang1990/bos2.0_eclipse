package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ZHANG
 * @create 2018-09-06 18:18
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value = "fixedArea_save", results = { @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect") })
    public String save() {
        //System.out.println(model);
        fixedAreaService.save(model);
        return SUCCESS;
    }
    @Action(value = "fixedArea_pageQuery", results = { @Result(name = "success", type = "json") })
    public String pageQuery() {
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate p1 = criteriaBuilder.equal(root.get("id").as(String.class), model.getId());
                    predicates.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate p2 = criteriaBuilder.equal(root.get("company").as(String.class), model.getCompany());
                    predicates.add(p2);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Pageable pageable = new PageRequest(page -1, rows);
        Page<FixedArea> pageData = fixedAreaService.findPageData(specification, pageable);

        pushPageDateToValueStack(pageData);
        return SUCCESS;
    }
    @Action(value = "fixedArea_findNoAssociationCustomers", results = { @Result(name = "success", type = "json")})
    public String findNoAssociationCustomers() {
    	Collection<? extends Customer> collection = 
    			WebClient.create("http://localhost:9998/crm_management/services/customerService/noassociationcustomers")
    			.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
    	ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
    }
    @Action(value = "fixedArea_findHasAssociationCustomers", results = { @Result(name = "success", type = "json")})
    public String findHasAssociationCustomers() {
    	Collection<? extends Customer> collection = 
    			WebClient.create("http://localhost:9998/crm_management/services/customerService/associationfixedareacustomers/" + model.getId())
    			.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
    	ActionContext.getContext().getValueStack().push(collection);
    	return SUCCESS;
    }
    
    private String[] customerIds;
    
    public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	@Action(value = "fixedArea_associationCustomerToFixedArea", results = { @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect") })
    public String associationCustomerToFixedArea() {
    	String joinCustomerIds = StringUtils.join(customerIds, "-");
    	if (StringUtils.isBlank(joinCustomerIds)) {
    		joinCustomerIds = "";
		}
    	WebClient.create("http://localhost:9998/crm_management/services/customerService/"
    			+ "associationcustomerstofixedarea/?customerIdStr=" + joinCustomerIds + "&fixedAreaId=" + model.getId()).put(null);
    	return SUCCESS;
    }
	
	private Integer takeTimeId;
	private Integer courierId;
	
	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	@Action(value = "fixedArea_associationCourierToFixedArea", results = { @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect") })
	public String associationCourierToFixedArea() {
		fixedAreaService.associationCourierToFixedArea(courierId, takeTimeId, model.getId());
		return SUCCESS;
	}
}
