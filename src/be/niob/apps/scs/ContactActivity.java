package be.niob.apps.scs;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;

public class ContactActivity extends FragmentActivity implements ContactListFragment.OnContactSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);
	}

	@Override
	public void onContactSelected(long id) {
		
		ContactDetailFragment detail = (ContactDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.contact_detail);
		
		if (detail == null || !detail.isInLayout()) {
			Intent showContact = new Intent(getApplicationContext(),
		            ContactDetailActivity.class);
			showContact.putExtra(ContactsContract.Contacts._ID, id);
		    startActivity(showContact);	
		} else {
			detail.updateContact(id);
		}
		
	}
	
}
