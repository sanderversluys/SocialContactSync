package be.niob.apps.scs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactDetailFragment extends Fragment {
 	
	private static final String TAG = Util.TAG + ":ContactDetailFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View  view = inflater.inflate(R.layout.contact_detail, container);
		return view;
	}
	
	public void updateContact(long id) {
		Log.i(TAG, "Update contact detail: " + id);
	} 
	
}
