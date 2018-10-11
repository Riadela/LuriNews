package com.jmluriad.lurinews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Impostazioni extends Activity implements OnClickListener {

	Button bUpdateEmail, bUpdateIDUtente, bUpdatePassword, bLogout;

	LoginPrefs loggato;

	Intent i;
	
	public static Activity fa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.impostazioni);

		initial();
		
		fa = this;
		
		bUpdateEmail.setOnClickListener(this);
		bUpdateIDUtente.setOnClickListener(this);
		bUpdatePassword.setOnClickListener(this);
		bLogout.setOnClickListener(this);
	}

	private void initial() {

		bUpdateEmail = (Button) findViewById(R.id.bIUpdateEmail);
		bUpdateIDUtente = (Button) findViewById(R.id.bIUpdateIDUtente);
		bUpdatePassword = (Button) findViewById(R.id.bIUpdatePassword);
		bLogout = (Button) findViewById(R.id.bILogout);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bIUpdateIDUtente:
			
			i = new Intent(this, UpdateIDUtente.class);
			startActivity(i);			
			
			break;

		case R.id.bIUpdatePassword:
			
			i = new Intent(this, UpdatePassword.class);
			startActivity(i);		
			
			break;

		case R.id.bIUpdateEmail:
			
			i = new Intent(this, UpdateEmail.class);
			startActivity(i);

			break;

		case R.id.bILogout:

			loggato = new LoginPrefs(getApplicationContext());
			loggato.logout();

			i = new Intent(this, Login.class);
			startActivity(i);
			finish();

			break;
		}

	}

}
