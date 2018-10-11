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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScriviNotiziaAnteprima extends Activity implements OnClickListener {

	private static String url_inserisciNotizia = "http://lurinews.altervista.org/nuovaNotizia.php";
	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();

	TextView tvTitolo, tvTesto;
	Button pubblicaNotizia;

	String titolo, testoSecondo, testoPrimo;
	String utente;
	
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
		setContentView(R.layout.scrivinotiziaanteprima);
		
		c = getApplicationContext();

		Bundle cestoPreso = getIntent().getExtras();

		titolo = cestoPreso.getString("infoTitolo");
		testoPrimo = cestoPreso.getString("infoArticolo");

		tvTesto = (TextView) findViewById(R.id.tvNotiziaAnteprima);
		tvTitolo = (TextView) findViewById(R.id.tvTitoloAnteprima);
		pubblicaNotizia = (Button) findViewById(R.id.bPubblicaNotizia);

		tvTesto.setText(testoPrimo);
		tvTitolo.setText(titolo);

		pubblicaNotizia.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (isOnline())
			// TODO Auto-generated method stub
			new InserisciNotizia().execute();
		else {
			Toast.makeText(getApplicationContext(),
					"No connessione a Internet", Toast.LENGTH_LONG).show();
		}
	}

	class InserisciNotizia extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ScriviNotiziaAnteprima.this);
			pDialog.setMessage("Procedendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			loggato = new LoginPrefs(c);
			loggato.getUtentePassword();
			utente = loggato.utente;
			
			dividiTesto();
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("titoloPrimo", titolo));
			params.add(new BasicNameValuePair("testoPrimo", testoPrimo));
			params.add(new BasicNameValuePair("testoSecondo", testoSecondo));
			params.add(new BasicNameValuePair("IDUtente", utente));

			JSONObject json = jsonParser.inserisci(url_inserisciNotizia,
			/* "POST", */params);

			int success = 0;
			try {
				success = json.getInt(TAG_SUCCESS);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (success == 1) {
				// successfully
				// chiudo le due activity
				ScriviNotizia.fa.finish();
				// finish();
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
						"Pubblicato con successo", Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(ScriviNotiziaAnteprima.this, GestioneTabs.class);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}
	private void dividiTesto() {
		// TODO Auto-generated method stub
		
		if(testoPrimo.length() > 80){
			testoSecondo = testoPrimo.substring(60);
			testoPrimo = testoPrimo.substring(0, 60);
		}else{
			testoSecondo = "";
		}
	}
}
