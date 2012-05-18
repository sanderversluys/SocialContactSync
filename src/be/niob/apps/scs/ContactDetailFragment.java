package be.niob.apps.scs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.android.imageloader.ImageLoader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ContactDetailFragment extends Fragment 
	implements LoaderManager.LoaderCallbacks<Cursor> {
 	
	private static final String TAG = Util.TAG + ":ContactDetailFragment";

	private long mContactId = -1;
	
	private ImageLoader mImageLoader;
	
	
	
	private Cursor emailCursor;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View  view = inflater.inflate(R.layout.contact_detail, container);
		mImageLoader = ImageLoader.get(getActivity());
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getLoaderManager().initLoader(0, null, this);
	}
	
	public void updateContact(long id) {
		Log.i(TAG, "Update contact detail: " + id);
		
		mContactId = id;
		
		//showPhoto(id);
		
		if (emailCursor != null)
			emailCursor.close();
		
		getLoaderManager().restartLoader((int)id, null, this);
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
		
//		if (input != null) 
//			mContactPhoto.setImageBitmap(BitmapFactory.decodeStream(input));
	}
	
//	private void showGravatar() {
//		
//		mGravatars.removeAllViews();
//		
//		if (emailCursor != null) {
//			
//			while (emailCursor.moveToNext()) {
//
//				String email = emailCursor
//						.getString(emailCursor
//								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//				String emailType = emailCursor
//						.getString(emailCursor
//								.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
//				
//				LayoutParams params = new LinearLayout.LayoutParams(
//						LinearLayout.LayoutParams.WRAP_CONTENT, 
//						LinearLayout.LayoutParams.WRAP_CONTENT);
//				
//				ImageView imageView = new ImageView(getActivity());
//				imageView.setLayoutParams(params);
//				imageView.setImageResource(R.drawable.person);
//				mGravatars.addView(imageView);
//				
//				mImageLoader.bind(imageView, Util.Gravatar.url(email), null);		
//				
//				break;
//			}
//
//		} else {
//			Log.d(TAG, "Cursor for email is not loaded");
//		}
//	}
	
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
		Contacts._ID, 
		Contacts.DISPLAY_NAME, 
		Contacts.CONTACT_STATUS,
		Contacts.CONTACT_PRESENCE, 
		Contacts.PHOTO_ID, 
		Contacts.LOOKUP_KEY, };

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg) {
		Log.d(TAG, "Loader create");
		Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"=?";
		return new CursorLoader(getActivity(), uri, null, selection, new String[] {""+id}, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "Loader finished");
		emailCursor = cursor;
		//showGravatar();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "Loader reset");
	}
	
}
