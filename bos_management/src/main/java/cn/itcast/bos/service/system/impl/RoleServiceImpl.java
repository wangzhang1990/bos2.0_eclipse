package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Role> findByUser(User user) {
		// TODO Auto-generated method stub
		if (user.getUsername().equals("admin")) {
			return roleRepository.findAll();
		}
		return roleRepository.findByUser(user.getId());
	}

	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public void save(Role model, String[] permissionIds, String menuIds) {
		// TODO Auto-generated method stub
		System.out.println("保存前的id为" + model.getId());
		roleRepository.save(model);
		System.out.println("保存后的id为" + model.getId());
		
		if (permissionIds != null) {
			for (String string : permissionIds) {
				Permission permisson = permissionRepository.findOne(Integer.parseInt(string));
				System.out.println(permisson.getName());
				model.getPermissions().add(permisson);
			}
		}
		
		if (StringUtils.isNotBlank(menuIds)) {
			String[] split = menuIds.split(",");
			for (String string : split) {
				Menu menu = menuRepository.findOne(Integer.parseInt(string));
				model.getMenus().add(menu);
			}
		}
		
		
	}

}
