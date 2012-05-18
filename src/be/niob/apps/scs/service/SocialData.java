package be.niob.apps.scs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SocialData implements Serializable {

	public enum Source {
		
		NONE,
		GOOGLE_PLUS,
		GRAVATAR,
		FACEBOOK,
		TWITTER
		
	}
	
	private Source source;
	private String account;
	private List<String> profilePictures;
	
	public SocialData() {
		this(Source.NONE, null);
	}
	
	public SocialData(Source source, String account) {
		this.source = source;
		this.setAccount(account);
		this.profilePictures = new ArrayList<String>();
	}
	
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public List<String> getProfilePictures() {
		return profilePictures;
	}
	public void setProfilePictures(List<String> profilePictures) {
		this.profilePictures = profilePictures;
	}
	
}
