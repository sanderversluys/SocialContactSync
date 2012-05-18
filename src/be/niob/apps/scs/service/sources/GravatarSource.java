package be.niob.apps.scs.service.sources;

import android.content.Context;
import be.niob.apps.scs.Util;
import be.niob.apps.scs.service.SocialData;
import be.niob.apps.scs.service.SocialData.Source;

public class GravatarSource implements SocialSource {

	
	@Override
	public SocialData getDataForAccount(Context context, String account) {
		
		SocialData data = new SocialData(Source.GRAVATAR, account);
		
		data.getProfilePictures().add(Util.Gravatar.url(account));
		
		return data;
	}

	
}
