package be.niob.apps.scs.service.sources;

import be.niob.apps.scs.service.SocialData;
import android.content.Context;

public interface SocialSource {

	public SocialData getDataForAccount(Context context, String account);
	
}