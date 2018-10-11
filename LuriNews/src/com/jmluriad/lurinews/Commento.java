package com.jmluriad.lurinews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Commento extends ListActivity implements OnClickListener,
		OnScrollListener {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> commentiList;

	private static String url_leggi = "http://lurinews.altervista.org/leggiCommenti.php";
	private static String url_invia = "http://lurinews.altervista.org/inviaCommento.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_IDNOTIZIA = "IDNotizia";
	private static final String TAG_IDUTENTE = "IDUtente";	
	private static final String TAG_TESTO = "testo";
	private static final String TAG_DATA = "dataCommento";

	// notizie JSONArray
	JSONArray commenti = null;

	String numCommenti = "10";

	ListView lv;

	String idNotizia;
	String idUtente, testo, data;

	EditText etInviaCommento;
	Button bInviaCommento;
	String mioIdUtente;

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
		setContentView(R.layout.commento);

		LoginPrefs loggato = new LoginPrefs(getApplicationContext());
		loggato.getUtentePassword();
		mioIdUtente = loggato.utente;

		bInviaCommento = (Button) findViewById(R.id.bCInviaCommento);
		etInviaCommento = (EditText) findViewById(R.id.etCInviaCommento);

		Bundle cestoPreso = getIntent().getExtras();
		idNotizia = cestoPreso.getString(TAG_IDNOTIZIA);

		
		commentiList = new ArrayList<HashMap<String, String>>();

		if (isOnline())
			
			new CaricaCommenti().execute();
		else {
			Toast.makeText(getApplicationContext(),
					"No connessione a Internet", Toast.LENGTH_LONG).show();
		}

		// Get listview
		lv = getListView();

		lv.setOnScrollListener(this);

		bInviaCommento.setOnClickListener(this);

	}

	class InviaCommento extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Commento.this);
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
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIdUtente));
			params.add(new BasicNameValuePair(TAG_TESTO, testo));
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));
			JSONObject json = jParser.inserisci(url_invia, params);

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
				Toast.makeText(getApplicationContext(),
						"Commentato con successo", Toast.LENGTH_LONG).show();

			} else {

				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class CaricaCommenti extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Commento.this);
			pDialog.setMessage("Caricamento commenti...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));
			params.add(new BasicNameValuePair("numCommenti", numCommenti));

			// getting JSON string from URL
			JSONObject json = jParser.inserisci(url_leggi, params);

			int success = -1;

			try {
				// Checking for SUCCESS TAG
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					// Getting Array 
					commenti = json.getJSONArray("commenti");

					// looping through All Notizie
					for (int i = 0; i < commenti.length(); i++) {
						JSONObject c = commenti.getJSONObject(i);

						// Storing each json item in variable
						idUtente = c.getString(TAG_IDUTENTE);
						testo = c.getString(TAG_TESTO);	
						data = c.getString(TAG_DATA);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_IDUTENTE, idUtente);
						map.put(TAG_TESTO, testo);
						map.put(TAG_DATA, data);

						// adding HashList to ArrayList
						commentiList.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			String ssuccess = Integer.toString(success);
			return ssuccess;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String v) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();

			int success = Integer.parseInt(v);
			if (success == 1) {

				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */

						ListAdapter adapter = new SimpleAdapter(Commento.this,
								commentiList, R.layout.list_comment,
								new String[] { TAG_IDUTENTE, TAG_TESTO, TAG_DATA },
								new int[] { R.id.tvLCIDUtente, R.id.tvLCTesto, R.id.tvLCData });
						// updating listview
						setListAdapter(adapter);
					}
				});

			}
		}
	}

	int ultimo = -1;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

		if (visibleItemCount > 8) {
			int temp = firstVisibleItem + visibleItemCount;
			if (temp == totalItemCount) {

				if (ultimo != temp) {

					int value = Integer.parseInt(numCommenti) + 10;
					numCommenti = Integer.toString(value);
					new CaricaCommenti().execute();
				}
				ultimo = temp;
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bCInviaCommento:
			testo = etInviaCommento.getText().toString();
			if (isOnline())
				// Loading Notizie in Background Thread
				new InviaCommento().execute();
			else {
				Toast.makeText(getApplicationContext(),
						"No connessione a Internet", Toast.LENGTH_LONG).show();
			}
			break;
		}

	}

}
