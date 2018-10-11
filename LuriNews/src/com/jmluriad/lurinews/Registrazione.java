package com.jmluriad.lurinews;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registrazione extends Activity implements OnClickListener {

	private static String url_registrazione = "http://lurinews.altervista.org/registrazione.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

	TextView tvUtente, tvPassword, tvPasswordConferma, tvEmail;
	EditText utente, password, passwordConferma, email;
	Button registrati;

	String sutente, spassword, semail;
	
	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
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
		setContentView(R.layout.registrazione);

		initial();

		registrati.setOnClickListener(this);

	}

	void initial() {

		tvUtente = (TextView) findViewById(R.id.tvRUtente);
		tvPassword = (TextView) findViewById(R.id.tvRPassword);
		tvPasswordConferma = (TextView) findViewById(R.id.tvRPasswordConferma);
		tvEmail = (TextView) findViewById(R.id.tvREmail);

		utente = (EditText) findViewById(R.id.etRUtente);
		password = (EditText) findViewById(R.id.etRPassword);
		passwordConferma = (EditText) findViewById(R.id.etRPasswordConferma);
		email = (EditText) findViewById(R.id.etREmail);

		registrati = (Button) findViewById(R.id.bRegistrati);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(email.getWindowToken(), 0);

		if (isOnline()) {

			spassword = password.getText().toString();

			if (spassword.equals(passwordConferma.getText().toString())) {

				sutente = utente.getText().toString();
				semail = email.getText().toString();

				if (spassword == "" || sutente == "" || semail == "") {

					Toast.makeText(getApplicationContext(),
							"Compilare tutti i campi", Toast.LENGTH_LONG)
							.show();

				} else if (sutente.length() < 6) {

					tvUtente.setTextColor(Color.RED);
					Toast.makeText(getApplicationContext(),
							"Nome Utente di almeno 6 caratteri",
							Toast.LENGTH_LONG).show();
				} else if (spassword.length() < 6) {

					tvPassword.setTextColor(Color.RED);
					Toast.makeText(getApplicationContext(),
							"Password di almeno 6 caratteri", Toast.LENGTH_LONG)
							.show();

				} else if (!isEmailValid(semail)){
					
					tvEmail.setTextColor(Color.RED);
					Toast.makeText(getApplicationContext(),
							"Formato email errato!", Toast.LENGTH_LONG)
							.show();
				
				}else {

					new inserisciRegistrazione().execute();

				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Le password inserite non combaciano",
						Toast.LENGTH_LONG).show();

				tvPassword.setTextColor(Color.RED);
				tvPassword.setTextColor(Color.RED);
			}
		}

		else {
			Toast.makeText(getApplicationContext(), "Nessuna rete",
					Toast.LENGTH_LONG).show();
		}

	}

	class inserisciRegistrazione extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Registrazione.this);
			pDialog.setMessage("Procedendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

			tvPassword.setTextColor(Color.BLACK);
			tvPassword.setTextColor(Color.BLACK);
			tvEmail.setTextColor(Color.BLACK);
			tvUtente.setTextColor(Color.BLACK);
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("IDUtente", sutente));
			params.add(new BasicNameValuePair("password", spassword));
			params.add(new BasicNameValuePair("email", semail));

			JSONObject json = jsonParser.inserisci(url_registrazione,
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
				Toast.makeText(getApplicationContext(),
						"Registrazione effettuata con successo",
						Toast.LENGTH_LONG).show();
				finish();
			} else if (success == 2) {

				tvUtente.setTextColor(Color.RED);
				Toast.makeText(getApplicationContext(), "Utente già esistente",
						Toast.LENGTH_LONG).show();

			} else if (success == 3) {

				tvEmail.setTextColor(Color.RED);
				Toast.makeText(getApplicationContext(), "Email già esistente",
						Toast.LENGTH_LONG).show();
			}

		}
	}
}
