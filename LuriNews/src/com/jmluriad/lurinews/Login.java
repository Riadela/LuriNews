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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	private static String url_login = "http://lurinews.altervista.org/login.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

	EditText utente, password;
	TextView tvUtente, tvPassword;
	TextView registrazione;
	Button login;

	String sutente, spassword;
	
	LoginPrefs loggato;
	Context c;

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
		setContentView(R.layout.login);

		utente = (EditText) findViewById(R.id.etUtente);
		password = (EditText) findViewById(R.id.etPassword);
		registrazione = (TextView) findViewById(R.id.tvRegistrazione);
		login = (Button) findViewById(R.id.bLogin);
		tvUtente = (TextView) findViewById(R.id.tvUtente);
		tvPassword = (TextView) findViewById(R.id.tvPassword);

		login.setOnClickListener(this);
		registrazione.setOnClickListener(this);
		
		c = getApplicationContext();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bLogin: 
			
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(password.getWindowToken(), 0);
			
			if (isOnline()) {
				
			sutente = utente.getText().toString();
			spassword = password.getText().toString();
			
			if(sutente == "" || spassword == ""){
				
				Toast.makeText(getApplicationContext(),
						"Compilare tutti i campi", Toast.LENGTH_LONG).show();
				
			}else{
				new inserisciLogin().execute();}		}
			else{
				
				Toast.makeText(getApplicationContext(),
						"Nessuna rete", Toast.LENGTH_LONG).show();
			}
			
		break;
		
		case R.id.tvRegistrazione:
			Intent i = new Intent(this, Registrazione.class);
			startActivity(i);
			
			break;
		}
	}

	class inserisciLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
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
			params.add(new BasicNameValuePair("IDUtente", sutente));
			params.add(new BasicNameValuePair("password", spassword));

			JSONObject json = jsonParser.inserisci(url_login,
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
				
				loggato = new LoginPrefs(c, sutente, spassword);
				loggato.salvaUtente();				
				finish();
				
			} else if (success == 2) {
				password.setText("");
				tvUtente.setTextColor(Color.RED);
				tvPassword.setTextColor(Color.RED);
				Toast.makeText(getApplicationContext(),
						"Utente e/o password errati", Toast.LENGTH_LONG).show();
			}

		}
	}
}
