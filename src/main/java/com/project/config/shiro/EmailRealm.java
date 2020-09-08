package com.project.config.shiro;

import com.project.constants.ResultConstants;
import com.project.entity.MyException;
import com.project.entity.User;
import com.project.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailRealm extends AuthorizingRealm {
    @Autowired
    private LoginService loginService;
    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        EmailToken token = null;
        // 如果是PhoneToken，则强转，获取phone；否则不处理。
        if(authenticationToken instanceof EmailToken){
            token = (EmailToken) authenticationToken;
        }else{
            return null;
        }
        String email = (String) token.getPrincipal();
        User user = loginService.getUserByCase(User.builder().email(email).build());
        if (user == null) {
            throw new MyException(ResultConstants.BAD_REQUEST,"验证码错误");
        }
        return new SimpleAuthenticationInfo(user, email, this.getName());
    }
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof EmailToken;
    }
}