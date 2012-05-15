package be.niob.apps.scs;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;

public class ContactDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_detail_fragment);
		
		ContactDetailFragment detail = (ContactDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.contact_detail);
		
		Intent launchingIntent = getIntent();
		long id = launchingIntent.getLongExtra(ContactsContract.Contacts._ID, -1);
		
		if (id > -1)
			detail.updateContact(id);
	}
	
}
