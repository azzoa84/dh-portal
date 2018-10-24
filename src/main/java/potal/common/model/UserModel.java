package potal.common.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserModel extends User {
	private static final long serialVersionUID = 1L;

	public UserModel(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
		super(userName, password, true, true, true, true, authorities);
		this.userId = userName;
	}

	private String userId;
    private String autoLoginKey;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getAutoLoginKey()
	{
		return autoLoginKey;
	}

	public void setAutoLoginKey(String autoLoginKey)
	{
		this.autoLoginKey = autoLoginKey;
	}
}
