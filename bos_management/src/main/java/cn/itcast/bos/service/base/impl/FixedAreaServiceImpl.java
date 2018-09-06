package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author ZHANG
 * @create 2018-09-06 18:30
 */
@Service
public class FixedAreaServiceImpl implements FixedAreaService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
        int A = 5;
    }

    @Override
    public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
        Page<FixedArea> pageData = fixedAreaRepository.findAll(specification, pageable);
        return pageData;
    }
}
