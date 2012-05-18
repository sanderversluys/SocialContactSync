package be.niob.apps.scs.service;

import java.util.List;

import android.content.Context;
import be.niob.apps.scs.Util;
import be.niob.apps.scs.service.sources.SocialSource;

public class SocialService {
	
	public static final String NAME = "be.niob.apps.scs";
	
	public static final String TAG = Util.TAG + ":SocialManager";

	public List<SocialSource> socialServices;
	
	public List<SocialData> loadDataForAllSources(Context context, String uid) {
		
		return null;
		
	}
	
}
