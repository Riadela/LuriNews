package com.jmluriad.lurinews;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.jmluriad.lurinews.Registrazione.inserisciRegistrazione;

public class UpdateIDUtente extends Activity implements OnClickListener {
	
	private static String url_update = "http://lurinews.altervista.org/updateIDUtente.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

	Button bCambia;
	EditText etIDUtenteVecchio, etIDUtenteNuovo;
	TextView tvIDUtenteNuovo;

	String mioIDUtente, nuovoIDUtente;
	
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
		setContentView(R.layout.pdtidutente);

		initial();

		LoginPrefs loggato = new LoginPrefs(getApplicationContext());
		loggato.getUtentePassword();
		mioIDUtente = loggato.utente;
		etIDUtenteVecchio.setText(mioIDUtente);

		bCambia.setOnClickListener(this);

	}

	private void initial() {

		bCambia = (Button) findViewById(R.id.bUUCambia);

		etIDUtenteVecchio = (EditText) findViewById(R.id.etUUIDUtenteVecchio);
		etIDUtenteNuovo = (EditText) findViewById(R.id.etUUIDUtenteNuovo);

		tvIDUtenteNuovo = (TextView) findViewById(R.id.tvUUIDUtenteNuovo);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.bUUCambia:

			if (isOnline()) {
				
				nuovoIDUtente = etIDUtenteNuovo.getText().toString();
				
				if (nuovoIDUtente == "") {

					Toast.makeText(getApplicationContext(),
							"Compilare il campo", Toast.LENGTH_LONG).show();

				} else if (nuovoIDUtente.length() < 6) {

					tvIDUtenteNuovo.setTextColor(Color.RED);
					Toast.makeText(getApplicationContext(),
							"Username di almeno 6 caratteri", Toast.LENGTH_LONG)
							.show();
				} else {

					new CambiaIDUtente().execute();

				}
			}

			break;
		}

	}
	
	class CambiaIDUtente extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(UpdateIDUtente.this);
			pDialog.setMessage("Procedendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("IDUtente", nuovoIDUtente));			
			params.add(new BasicNameValuePair("IDUtenteVecchio", mioIDUtente));
			
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
				
				Intent cambia = new Intent(UpdateIDUtente.this, Login.class);
				startActivity(cambia);
				
				Toast.makeText(getApplicationContext(),
						"Username cambiato con successo!",
						Toast.LENGTH_LONG).show();
				
			} else if (success == 3) {
				
				tvIDUtenteNuovo.setTextColor(Color.RED);
				
				Toast.makeText(getApplicationContext(),
						"Username già esistente!", Toast.LENGTH_LONG).show();
			}

		}
	}
}
