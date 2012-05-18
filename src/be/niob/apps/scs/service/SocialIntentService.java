package be.niob.apps.scs.service;

import java.io.Serializable;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import be.niob.apps.scs.Util;

public class SocialIntentService extends IntentService {
	
	public static final String TAG = Util.TAG + ":SocialIntentService";
	
	public static final String ACTION_LOAD_SOCIAL_DATA = "actionLoadSocialData";
	
	public static final String RESULT_SOCIAL_DATA_LOADED = "actionSocialDataLoaded";
	
	public static final String EXTRA_ID = "extraId";
	public static final String EXTRA_DATA = "extraData";

	public SocialIntentService(String name) {
		super("SocialIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		if (intent == null && intent.getAction() == null)
			return;
		
		String action = intent.getAction();
		
		if (action.equals(ACTION_LOAD_SOCIAL_DATA)) {
		
			long id = intent.getLongExtra(EXTRA_ID, -1);
			
			if (id > -1) {
				
				SocialService service = (SocialService) getSystemService(SocialService.NAME);
				
				List<SocialData> data = service.loadDataForAllSources(this, null);
				
				Intent result = new Intent();
				result.setAction(RESULT_SOCIAL_DATA_LOADED);
				result.putExtra(EXTRA_ID, id);
				result.putExtra(EXTRA_DATA, (Serializable) data);
				
				sendBroadcast(result);
			
			}
			
		}

		
	}
	
	

}
