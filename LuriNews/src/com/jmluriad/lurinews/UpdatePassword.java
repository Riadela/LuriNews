package com.jmluriad.lurinews;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jmluriad.lurinews.Registrazione.inserisciRegistrazione;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePassword extends Activity implements OnClickListener {

	private static String url_update = "http://lurinews.altervista.org/updatePassword.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

	Button bCambia;
	EditText etPasswordVecchia, etPasswordNuova1, etPasswordNuova2;
	TextView tvPasswordNuova1, tvPasswordNuova2, tvPasswordVecchia;

	String miaPassword, nuovaPassword1, nuovaPassword2;
	String mioIDUtente;

	LoginPrefs loggato;

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdtpassword);

		initial();

		loggato = new LoginPrefs(getApplicationContext());
		loggato.getUtentePassword();
		mioIDUtente = loggato.utente;

		bCambia.setOnClickListener(this);

	}

	private void initial() {

		bCambia = (Button) findViewById(R.id.bUPCambiaPassword);

		etPasswordVecchia = (EditText) findViewById(R.id.etUPPasswordVecchia);
		etPasswordNuova1 = (EditText) findViewById(R.id.etUPPasswordNuova1);
		etPasswordNuova2 = (EditText) findViewById(R.id.etUPPasswordNuova2);

		tvPasswordNuova1 = (TextView) findViewById(R.id.tvUPPasswordNuova1);
		tvPasswordNuova2 = (TextView) findViewById(R.id.tvUPPasswordNuova2);
		tvPasswordVecchia = (TextView) findViewById(R.id.tvUPPasswordVecchia);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bUPCambiaPassword:

			if (isOnline()) {

				nuovaPassword1 = etPasswordNuova1.getText().toString();

				if (nuovaPassword1
						.equals(etPasswordNuova2.getText().toString())) {

					if (nuovaPassword1 == "") {

						Toast.makeText(getApplicationContext(),
								"Compilare tutti i campi", Toast.LENGTH_LONG)
								.show();

					} else if (nuovaPassword1.length() < 6) {

						tvPasswordNuova1.setTextColor(Color.RED);
						tvPasswordNuova2.setTextColor(Color.RED);
						Toast.makeText(getApplicationContext(),
								"Password di almeno 6 caratteri",
								Toast.LENGTH_LONG).show();

					} else {

						new CambiaPassword().execute();

					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Le password inserite non combaciano",
							Toast.LENGTH_LONG).show();

					tvPasswordNuova1.setTextColor(Color.RED);
					tvPasswordNuova2.setTextColor(Color.RED);
				}
			}
			break;

		}
	}

	class CambiaPassword extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(UpdatePassword.this);
			pDialog.setMessage("Procedendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			miaPassword = etPasswordVecchia.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Password", nuovaPassword1));
			params.add(new BasicNameValuePair("PasswordVecchia", miaPassword));
			params.add(new BasicNameValuePair("IDUtente", mioIDUtente));

			JSONObject json = jsonParser.inserisci(url_update,
			/* "POST", */params);

			int success = 0;
			try {
				success = json.getInt(TAG_SUCCESS);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String ssuccess = Integer.toString(success);
			return ssuccess;
		}

		@Override
		protected void onPostExecute(String v) {
			// TODO Auto-generated method stub
			super.onPostExecute(v);

			pDialog.dismiss();

			int success = Integer.parseInt(v);
			if (success == 1) {

				Intent i = new Intent("com.jmluriad.lurinews.GestioneTabs");
				startActivity(i);

				loggato = new LoginPrefs(getApplicationContext());
				loggato.logout();

				Impostazioni.fa.finish();
				MainActivity.fa.finish();
				finish();
				
				Intent cambia = new Intent(UpdatePassword.this, Login.class);
				startActivity(cambia);
				
				Toast.makeText(getApplicationContext(),
						"Password cambiata con successo!",
						Toast.LENGTH_LONG).show();

			} else if (success == 2) {

				tvPasswordVecchia.setTextColor(Color.RED);

				Toast.makeText(getApplicationContext(), "Password errata !!",
						Toast.LENGTH_LONG).show();
			}

		}

	}
}
