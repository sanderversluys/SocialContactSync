package be.niob.apps.scs;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ContactDetailFragment extends Fragment 
	implements LoaderManager.LoaderCallbacks<Cursor> {
 	
	private static final String TAG = Util.TAG + ":ContactDetailFragment";
	
	private ImageView mContactPhoto;
	private ImageView mGravatar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View  view = inflater.inflate(R.layout.contact_detail, container);
		mContactPhoto = (ImageView) view.findViewById(R.id.contact_photo);
		mGravatar = (ImageView) view.findViewById(R.id.gravatar);
		return view;
	}
	
	public void updateContact(long id) {
		Log.i(TAG, "Update contact detail: " + id);
		
		ContentResolver cr = getActivity().getContentResolver();
		
		
		
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
	
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
		Contacts._ID, 
		Contacts.DISPLAY_NAME, 
		Contacts.CONTACT_STATUS,
		Contacts.CONTACT_PRESENCE, 
		Contacts.PHOTO_ID, 
		Contacts.LOOKUP_KEY, };

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		Uri baseUri = Contacts.CONTENT_URI;
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor arg1) {
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	
}
