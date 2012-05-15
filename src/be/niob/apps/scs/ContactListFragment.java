package be.niob.apps.scs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.imageloader.ImageLoader;

public class ContactListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String TAG = Util.TAG + ":ContactListFragment";
	
	// This listener is used as callback for the activity using this fragment
	OnContactSelectedListener onContactSelectedListener;
	
	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	// If non-null, this is the current filter the user has provided.
	String mCurFilter;
	
	public interface OnContactSelectedListener {
	    public void onContactSelected(long position);
	}
	@Override
	public void onAttach(Activity activity) {
		try {
			onContactSelectedListener = (OnContactSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
	                + " must implement OnContactSelectedListener");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText(getString(R.string.no_contacts));

		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new ContactCursorAdapter(getActivity(),
				R.layout.contact_list_item, null, new String[] {
						Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS },
				new int[] { android.R.id.text1, android.R.id.text2 }, 0);
		setListAdapter(mAdapter);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	public boolean onQueryTextChange(String newText) {
		// Called when the action bar search text has changed. Update
		// the search filter, and restart the loader to do a new query
		// with this filter.
		mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
		getLoaderManager().restartLoader(0, null, this);
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Call OnContactSelectedListener
		Log.i(TAG, "Contact clicked: " + id);
		onContactSelectedListener.onContactSelected(id);
	}

	// These are the Contacts rows that we will retrieve.
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			Contacts._ID, 
			Contacts.DISPLAY_NAME, 
			Contacts.CONTACT_STATUS,
			Contacts.CONTACT_PRESENCE, 
			Contacts.PHOTO_ID, 
			Contacts.LOOKUP_KEY, };

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		Uri baseUri;
		if (mCurFilter != null) {
			baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
					Uri.encode(mCurFilter));
		} else {
			baseUri = Contacts.CONTENT_URI;
		}

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ Contacts.DISPLAY_NAME + " != '' ))";
		return new CursorLoader(getActivity(), baseUri,
				CONTACTS_SUMMARY_PROJECTION, select, null,
				Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}
	
	public class ContactCursorAdapter extends SimpleCursorAdapter {

		private HashMap<ImageView, PhotoLoadTask> mPhotoMap;
		
		ImageLoader mImageLoader;
		ContentResolver mContentResolver;
		
		int nameIndex = -1;
		int idIndex = -1;
		int photoIndex = -1;
		
		public ContactCursorAdapter(Context context, int layout, Cursor cursor,
				String[] from, int[] to, int flags) {
			super(context, layout, cursor, from, to, flags);
			mImageLoader = ImageLoader.get(context);
			mContentResolver = context.getContentResolver();
			mPhotoMap = new HashMap<ImageView, ContactListFragment.ContactCursorAdapter.PhotoLoadTask>();
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			if (nameIndex < 0) {
				nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
				photoIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID);
			}
			
			ImageView photoView = (ImageView) view.findViewById(R.id.contact_photo);
			TextView nameView = (TextView) view.findViewById(R.id.contact_name);
			
			String name = cursor.getString(nameIndex);
			nameView.setText(name);
			
			long id = cursor.getLong(idIndex);
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);

			loadPhoto(photoView, uri);
			
		}
		
		private void loadPhoto(ImageView imageView, Uri uri) {
			imageView.setImageResource(R.drawable.person);
			if (mPhotoMap.containsKey(imageView))
				mPhotoMap.get(imageView).cancel(false);
			try {
				mPhotoMap.put(imageView, (PhotoLoadTask) new PhotoLoadTask(imageView).execute(uri));	
			} catch(RejectedExecutionException ex) {
				Log.e(TAG, ex.getMessage());
			}
			
		}
		
		private class PhotoLoadTask extends AsyncTask<Uri, Void, Bitmap> {
			
			ImageView imageView;
			
			public PhotoLoadTask(ImageView imageView) {
				this.imageView = imageView;
			}

			@Override
			protected Bitmap doInBackground(Uri... params) {
				InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(mContentResolver, params[0]);
				return BitmapFactory.decodeStream(input);
			}
			
			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null)
					imageView.setImageBitmap(result);
			}
			
		}
	
	}
	
	

}