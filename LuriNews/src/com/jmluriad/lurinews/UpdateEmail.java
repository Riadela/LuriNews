package com.jmluriad.lurinews;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jmluriad.lurinews.UpdatePassword.CambiaPassword;

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

public class UpdateEmail extends Activity implements OnClickListener {

	private static String url_update = "http://lurinews.altervista.org/updateEmail.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

	Button bCambia;
	EditText etEmail1, etEmail2;
	TextView tvEmail1, tvEmail2;

	String email1, email2;
	String mioIDUtente;

	LoginPrefs loggato;

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

		setContentView(R.layout.pdtemail);

		initial();

		loggato = new LoginPrefs(getApplicationContext());
		loggato.getUtentePassword();
		mioIDUtente = loggato.utente;

		bCambia.setOnClickListener(this);
	}

	private void initial() {

		bCambia = (Button) findViewById(R.id.bUECambia);

		etEmail1 = (EditText) findViewById(R.id.etUEEmailNuova1);
		etEmail2 = (EditText) findViewById(R.id.etUEEmailNuova2);

		tvEmail1 = (TextView) findViewById(R.id.tvUEEmailNuova1);
		tvEmail2 = (TextView) findViewById(R.id.tvUEEmailNuova2);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bUECambia:

			if (isOnline()) {

				email1 = etEmail1.getText().toString();
				
				if (isEmailValid(email1)) {

					if (email1.equals(etEmail2.getText().toString())) {

						if (email1 == "") {

							Toast.makeText(getApplicationContext(),
									"Compilare tutti i campi",
									Toast.LENGTH_LONG).show();

						} else {

							new CambiaEmail().execute();

						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Le email inserite non combaciano",
								Toast.LENGTH_LONG).show();

						tvEmail1.setTextColor(Color.RED);
						tvEmail2.setTextColor(Color.RED);
					}
				}
				else
					Toast.makeText(getApplicationContext(),
							"Formato email errato",
							Toast.LENGTH_LONG).show();
			}

			break;
		}

	}

	class CambiaEmail extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(UpdateEmail.this);
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
			params.add(new BasicNameValuePair("email", email1));
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

				finish();
				Impostazioni.fa.finish();
				
				Toast.makeText(getApplicationContext(),
						"Email cambiata con successo!",
						Toast.LENGTH_LONG).show();

			} else if (success == 2) {

				Toast.makeText(getApplicationContext(), "Errore 2!!",
						Toast.LENGTH_LONG).show();
			}

		}

	}
}
