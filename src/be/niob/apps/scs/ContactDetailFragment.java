package be.niob.apps.scs;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ContactDetailFragment extends Fragment {
 	
	private static final String TAG = Util.TAG + ":ContactDetailFragment";
	
	private ImageView mContactPhoto;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View  view = inflater.inflate(R.layout.contact_detail, container);
		mContactPhoto = (ImageView) view.findViewById(R.id.contact_photo);
		return view;
	}
	
	public void updateContact(long id) {
		Log.i(TAG, "Update contact detail: " + id);
		showPhoto(id);
	}
	
	private void showPhoto(long id) {
		ContentResolver cr = getActivity().getContentResolver();

		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, id);
		
		InputStream input = null;
		if (Build.VERSION.SDK_INT >= 14)
			input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri, true);
		else
			input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
		
		if (input != null) 
			mContactPhoto.setImageBitmap(BitmapFactory.decodeStream(input));
	}
	
}
