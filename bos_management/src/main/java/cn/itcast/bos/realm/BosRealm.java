package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;

@Service
public class BosRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		// TODO Auto-generated method stub
		System.out.println("this is doGetAuthorizationInfo 授权");
		//SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			authorizationInfo.addRole(role.getKeyword());
		}
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("this is doGetAuthorizationInfo 登陆");
		
		UsernamePasswordToken token = (UsernamePasswordToken) at;
		User user = userService.fingByUsername(token.getUsername());
		
		if (user == null) {
			return null;
		} else {
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
	}

}
