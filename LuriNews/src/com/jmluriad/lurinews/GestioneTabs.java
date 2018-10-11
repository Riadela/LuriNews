package com.jmluriad.lurinews;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class GestioneTabs extends TabActivity {

	LoginPrefs loggato;
	Context c;
	TabHost th;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		c = getApplicationContext();

		Intent i;

		th = getTabHost();
		th.setup();
		TabSpec specs;

		i = new Intent(this, Notizie.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		specs = th.newTabSpec("tag1");
		specs.setContent(i);
		specs.setIndicator("Notizie");
		th.addTab(specs);
		
		
		i = new Intent(this, Vota.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		specs = th.newTabSpec("tag2");
		specs.setContent(i);
		specs.setIndicator("Vota");
		th.addTab(specs);

		i = new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		specs = th.newTabSpec("tag3");
		specs.setContent(i);
		specs.setIndicator("Profilo");
		th.addTab(specs);

	}

	public void switchTab(int tab) {
		th.setCurrentTab(tab);
	}


}
