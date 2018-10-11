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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Vota extends ListActivity implements OnScrollListener {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> notizieList;

	private static String url = "http://lurinews.altervista.org/votaNotizie.php";

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
		setContentView(R.layout.vota);

		// Hashmap for ListView
		notizieList = new ArrayList<HashMap<String, String>>();

		if (isOnline())
			// Loading products in Background Thread
			new CaricaNotizie().execute();
		else {
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

				Intent i = new Intent(Vota.this, Notizia.class);
				i.putExtras(cesto);

				startActivity(i);

			}
		});

	}

	class CaricaNotizie extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Vota.this);
			pDialog.setMessage("Caricamento Notizie...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("numNotizie", numNotizie));

			// getting JSON string from URL
			JSONObject json = jParser.inserisci(url, params);

			int success = -1;

			try {
				// Checking for SUCCESS TAG
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					// Getting Array of Products
					notizie = json.getJSONArray("notizie");

					// looping through All Products
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
				} /*
				 * else { // no products found // Launch Add New product
				 * Activity Toast.makeText(getApplicationContext(),
				 * "Nessuna Notizia", Toast.LENGTH_LONG).show(); }
				 */
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

						ListAdapter adapter = new SimpleAdapter(Vota.this,
								notizieList, R.layout.list_item, new String[] {
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

		if (visibleItemCount > 3) {
			int temp = firstVisibleItem + visibleItemCount;
			if (temp == totalItemCount) {

				if (ultimo != temp) {
					// Toast.makeText(getApplicationContext(),
					// "Nessuna Notizia",
					// Toast.LENGTH_LONG).show();

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

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		GestioneTabs parentActivity;
		parentActivity = (GestioneTabs) this.getParent();
		parentActivity.switchTab(1);
	}

}
