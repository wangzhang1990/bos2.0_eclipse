package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public User fingByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public void save(User model, String[] roleIds) {
		// TODO Auto-generated method stub
		userRepository.save(model);
		
		if (roleIds != null) {
			for (int i = 0; i < roleIds.length; i++) {
				Role role = roleRepository.findOne(Integer.parseInt(roleIds[i]));
				model.getRoles().add(role);
			}
		}
	}

}
