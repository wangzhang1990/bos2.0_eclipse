package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author ZHANG
 * @create 2018-09-06 18:27
 */
public interface FixedAreaService {
    void save(FixedArea model);

    Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);
}
