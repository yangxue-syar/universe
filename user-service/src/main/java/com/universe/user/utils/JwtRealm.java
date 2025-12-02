package com.universe.user.utils;

import com.universe.user.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;

    public JwtRealm() {
        setAuthenticationTokenClass(org.apache.shiro.authc.UsernamePasswordToken.class);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // we won't use Shiro's UsernamePasswordToken here; allowed for extension
        return token instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // Not used since we use JwtFilter for authentication
        throw new AuthenticationException("Use JwtFilter for authentication");
    }

    // Optionally implement authorization methods
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        // load roles and permissions via userService
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // implementation omitted for brevity — can call userService.getRoles/Permissions(...)
        return info;
    }
}
