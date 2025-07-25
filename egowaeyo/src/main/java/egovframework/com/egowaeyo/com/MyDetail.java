package egovframework.com.egowaeyo.com;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyDetail implements UserDetails{

	
	private String USERNAME;
	private String REALNAME;
	private String PASSWORD;
	private String EMAIL;
	private String AUTHORITY;
	//필요한 거 생기면 추가 하고 userDetailService 에서 
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		auth.add(new SimpleGrantedAuthority(AUTHORITY));
		return auth;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	public String getRealName() {
		
		return null;
	}

	public void setUserName(String userName) {
		this.USERNAME = userName;
	}

	public String getEmail() {
		return EMAIL;
	}

	public void setEmail(String email) {
		this.EMAIL = email;
	}

	public void setRealName(String realName) {
		this.REALNAME = realName;
	}
}
