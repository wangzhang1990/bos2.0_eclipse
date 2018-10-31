package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	public User fingByUsername(String username);

	public List<User> findAll();

	public void save(User model, String[] roleIds);
}
