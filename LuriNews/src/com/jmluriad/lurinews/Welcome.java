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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Welcome extends Activity {

	private static String url_login = "http://lurinews.altervista.org/login.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

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

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		c = getApplicationContext();

		setContentView(R.layout.benvenuto);
		if(isOnline())
			new Caricamento().execute();
		else{
			Toast.makeText(getApplicationContext(),
					"No connessione a Internet", Toast.LENGTH_LONG)
					.show();
		}
	}

	class Caricamento extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			loggato = new LoginPrefs(c);
			if (loggato.isLogged()) {

				loggato.getUtentePassword();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("IDUtente", loggato.utente));
				params.add(new BasicNameValuePair("password", loggato.password));

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

			} else {

				Intent i = new Intent(Welcome.this, Login.class);
				startActivity(i);

			}

			return null;
		}

		@Override
		protected void onPostExecute(String v) {
			// TODO Auto-generated method stub
			super.onPostExecute(v);

			if (v != null) {
				
				int success = Integer.parseInt(v);
				if (success == 1) {
					
					Intent i = new Intent("com.jmluriad.lurinews.GestioneTabs");
					startActivity(i);
					finish();
					
				} else if (success == 2) {
					
					
					Intent i = new Intent(Welcome.this, Login.class);
					startActivity(i);
					Toast.makeText(getApplicationContext(),
							"Utente e/o password errati", Toast.LENGTH_LONG)
							.show();
				}

			}

			finish();
		}

	}
}
