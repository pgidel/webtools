package com.bierocratie.security;

import com.bierocratie.db.security.AccountDAO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 13/04/14
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class DBSecurityRealm extends JdbcRealm {

    // FIXME @Inject & rendre la db paramétrable
    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (accountDAO == null) {
            accountDAO = new AccountDAO();
        }

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        String username = upToken.getUsername();
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        byte[] password = null;
        try {
            password = accountDAO.getPasswordForUser(username);
            if (password == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }
        } catch (SQLException e) {
            throw new AuthenticationException("An error occured while authenticating user [" + username + "]", e);
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (accountDAO == null) {
            accountDAO = new AccountDAO();
        }

        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);

        Set<String> roleNames = new HashSet<>();
        try {
            roleNames.add(accountDAO.getRoleNamesForUser(username));
        } catch (SQLException e) {
            throw new AuthorizationException("An error occured while authorizing user [" + username + "]", e);
        }

        return new SimpleAuthorizationInfo(roleNames);
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

}
