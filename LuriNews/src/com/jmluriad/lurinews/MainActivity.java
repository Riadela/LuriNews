package com.jmluriad.lurinews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnClickListener,
		OnScrollListener {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> notizieList;

	private static String url = "http://lurinews.altervista.org/leggiNotizieMie.php";
	private static String url_numeroNotizieMie = "http://lurinews.altervista.org/numeroNotizieMie.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_IDUTENTE = "IDUtente";
	private static final String TAG_IDNOTIZIA = "IDNotizia";
	private static final String TAG_TITOLO = "titoloPrimo";
	private static final String TAG_TESTOPRIMO = "testoPrimo";
	private static final String TAG_TESTOSECONDO = "testoSecondo";
	private static final String TAG_DATA = "dataPubblicato";
	private static final String TAG_NUMEROINTERESSANTE = "numeroInteressante";
	private static final String TAG_NUMEROYES = "numeroYes";
	private static final String TAG_NUMERONO = "numeroNo";
	private static final String TAG_NUMEROCOMMENTO = "numeroCommento";

	// notizie JSONArray
	JSONArray notizie = null;

	String numNotizie = "10";

	ListView lv;

	String idutente, idNotizia, titolo, testoPrimo, testoSecondo, data;
	String numInteressante, numYes, numNo, numCommento;

	Button bScriviNotizia, bImpostazioni;
	TextView tvIDUtente, tvNumeroNotizie;

	LoginPrefs loggato;
	Context c;

	String mioIDUtente;
	
	public static Activity fa;

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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fa = this;
		
		initial();
		
		LoginPrefs loggato = new LoginPrefs(getApplicationContext());
		loggato.getUtentePassword();
		mioIDUtente = loggato.utente;

		bScriviNotizia.setOnClickListener(this);
		bImpostazioni.setOnClickListener(this);

		c = getApplicationContext();

		loggato = new LoginPrefs(c);
		loggato.getUtentePassword();

		tvIDUtente.setText(loggato.utente);

		// Hashmap for ListView
		notizieList = new ArrayList<HashMap<String, String>>();

		if (isOnline()){
			
			new CaricaNotizie().execute();
			new NumeroNotizie().execute();
		}else {
			Toast.makeText(getApplicationContext(),
					"No connessione a Internet", Toast.LENGTH_LONG).show();
		}

		// Get listview
		lv = getListView();

		lv.setOnScrollListener(this);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				idutente = ((TextView) view.findViewById(R.id.tvLIIDUtente))
						.getText().toString();
				idNotizia = ((TextView) view.findViewById(R.id.tvLIIDNotizia))
						.getText().toString();
				titolo = ((TextView) view.findViewById(R.id.tvLITitoloPrimo))
						.getText().toString();
				testoPrimo = ((TextView) view.findViewById(R.id.tvLITestoPrimo))
						.getText().toString();
				testoSecondo = ((TextView) view
						.findViewById(R.id.tvLITestoSecondo)).getText()
						.toString();
				data = ((TextView) view.findViewById(R.id.tvLIData)).getText()
						.toString();

				Bundle cesto = new Bundle();

				cesto.putString(TAG_IDNOTIZIA, idNotizia);
				cesto.putString(TAG_IDUTENTE, idutente);
				cesto.putString(TAG_TITOLO, titolo);
				cesto.putString(TAG_TESTOPRIMO, testoPrimo);
				cesto.putString(TAG_TESTOSECONDO, testoSecondo);
				cesto.putString(TAG_DATA, data);

				Intent i = new Intent(MainActivity.this, Notizia.class);
				i.putExtras(cesto);

				startActivity(i);

			}
		});

	}

	private void initial() {
		// TODO Auto-generated method stub

		bScriviNotizia = (Button) findViewById(R.id.bScriviNotizia);
		tvIDUtente = (TextView) findViewById(R.id.tvNickname);
		tvNumeroNotizie = (TextView) findViewById(R.id.tvNumeroPost);
		bImpostazioni = (Button) findViewById(R.id.bImpostazioneProfilo);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bScriviNotizia:

			Intent scriviIntent = new Intent(
					"com.jmluriad.lurinews.ScriviNotizia");
			startActivity(scriviIntent);
			super.finish();

			break;

		case R.id.bImpostazioneProfilo:
			
			Intent impostazioni = new Intent(this, Impostazioni.class);
			startActivity(impostazioni);
			
			break;

		}
	}

	@Override
	public void finish() {

		// TODO Auto-generated method stub
		GestioneTabs parentActivity;
		parentActivity = (GestioneTabs) this.getParent();
		parentActivity.switchTab(1);
	}

	class NumeroNotizie extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIDUtente));

			JSONObject json = jParser.inserisci(url_numeroNotizieMie, params);

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

			int success = Integer.parseInt(v);
			if (success >= 0) {

				tvNumeroNotizie.setText(v);

			} else {
				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class CaricaNotizie extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Caricamento Notizie...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("numNotizie", numNotizie));
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIDUtente));

			// getting JSON string from URL
			JSONObject json = jParser.inserisci(url, params);

			int success = -1;

			try {
				// Checking for SUCCESS TAG
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					// Getting Array
					notizie = json.getJSONArray("notizie");

					
					for (int i = 0; i < notizie.length(); i++) {
						JSONObject c = notizie.getJSONObject(i);

						// Storing each json item in variable
						idutente = c.getString(TAG_IDUTENTE);
						idNotizia = c.getString(TAG_IDNOTIZIA);
						titolo = c.getString(TAG_TITOLO);
						testoPrimo = c.getString(TAG_TESTOPRIMO);
						testoSecondo = c.getString(TAG_TESTOSECONDO);
						data = c.getString(TAG_DATA);

						numInteressante = c.getString(TAG_NUMEROINTERESSANTE);
						numYes = c.getString(TAG_NUMEROYES);
						numNo = c.getString(TAG_NUMERONO);
						numCommento = c.getString(TAG_NUMEROCOMMENTO);

						testoPrimo += "...";
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_IDUTENTE, idutente);
						map.put(TAG_IDNOTIZIA, idNotizia);
						map.put(TAG_TITOLO, titolo);
						map.put(TAG_TESTOPRIMO, testoPrimo);
						map.put(TAG_TESTOSECONDO, testoSecondo);
						map.put(TAG_DATA, data);

						map.put(TAG_NUMEROINTERESSANTE, numInteressante);
						map.put(TAG_NUMEROYES, numYes);
						map.put(TAG_NUMERONO, numNo);
						map.put(TAG_NUMEROCOMMENTO, numCommento);

						// adding HashList to ArrayList
						notizieList.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			String ssuccess = Integer.toString(success);
			return ssuccess;
		}

		protected void onPostExecute(String v) {
			// dismiss the dialog after getting all notizie
			pDialog.dismiss();

			int success = Integer.parseInt(v);
			if (success == 1) {

				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */

						ListAdapter adapter = new SimpleAdapter(
								MainActivity.this, notizieList,
								R.layout.list_item, new String[] {
										TAG_IDUTENTE, TAG_IDNOTIZIA,
										TAG_TITOLO, TAG_TESTOPRIMO,
										TAG_TESTOSECONDO, TAG_DATA,
										TAG_NUMEROINTERESSANTE, TAG_NUMEROYES,
										TAG_NUMERONO, TAG_NUMEROCOMMENTO },
								new int[] { R.id.tvLIIDUtente,
										R.id.tvLIIDNotizia,
										R.id.tvLITitoloPrimo,
										R.id.tvLITestoPrimo,
										R.id.tvLITestoSecondo, R.id.tvLIData,
										R.id.tvLINumeroInteressante,
										R.id.tvLINumeroYes, R.id.tvLINumeroNo,
										R.id.tvLINumeroCommenti });
						// updating listview
						setListAdapter(adapter);
					}
				});
			
			} else {
				Toast.makeText(getApplicationContext(), "Nessuna Notizia",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	int ultimo = -1;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

		if (visibleItemCount > 5) {
			int temp = firstVisibleItem + visibleItemCount;
			if (temp == totalItemCount) {

				if (ultimo != temp) {					

					int value = Integer.parseInt(numNotizie) + 10;
					numNotizie = Integer.toString(value);
					new CaricaNotizie().execute();
				}
				ultimo = temp;
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
