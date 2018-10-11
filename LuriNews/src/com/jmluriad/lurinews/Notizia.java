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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

public class Notizia extends Activity implements OnClickListener {

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_IDUTENTE = "IDUtente";
	private static final String TAG_IDNOTIZIA = "IDNotizia";
	private static final String TAG_TITOLO = "titoloPrimo";
	private static final String TAG_TESTOPRIMO = "testoPrimo";
	private static final String TAG_TESTOSECONDO = "testoSecondo";
	private static final String TAG_DATA = "dataPubblicato";
	private static final String TAG_TESTO = "testo";
	private static final String TAG_DATACOMMENTO = "dataCommento";

	private static String url_interessante = "http://lurinews.altervista.org/interessante.php";
	private static String url_nonInteressante = "http://lurinews.altervista.org/nonInteressante.php";
	private static String url_SNInteressante = "http://lurinews.altervista.org/SNInteressante.php";
	private static String url_numeroInteressante = "http://lurinews.altervista.org/numeroInteressante.php";
	private static String url_numeroYesNo = "http://lurinews.altervista.org/numeroYesNo.php";
	private static String url_Yes = "http://lurinews.altervista.org/yes.php";
	private static String url_No = "http://lurinews.altervista.org/no.php";
	private static String url_SNYesNo = "http://lurinews.altervista.org/SNYesNo.php";
	private static String url_noYesNo = "http://lurinews.altervista.org/noYesNo.php";
	private static String url_numeroCommento = "http://lurinews.altervista.org/numeroCommento.php";
	private static String url_leggiCommentiPrimi = "http://lurinews.altervista.org/leggiCommentiPrimi.php";
	private static String url_eliminaNotizia = "http://lurinews.altervista.org/eliminaNotizia.php";
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> commentiList;
	JSONArray commenti;
	JSONObject jsonCommento;

	TextView tvIDNotizia, tvTitolo, tvTestoPrimo, tvData, tvIDUtente,
			tvNumeroInteressante, tvNumeroYes, tvNumeroNo;
	Button bInteressante, bYes, bNo, bCommento, bElimina;
	String idutente = "", idNotizia = "", titolo = "", testoPrimo = "",
			testoSecondo = "", data = "";

	String mioIdUtente;

	boolean SNInteressante; // false non ha messo interessante
	String SNYesNo = null; // "Yes" oppure "No"

	// ******** Commenti

	TextView tvNumeroCommento, tvIDUtente1, tvCommento1, tvData1, tvIDUtente2,
			tvCommento2, tvData2, tvIDUtente3, tvCommento3, tvData3;
	RelativeLayout ll1, ll2, ll3, rlElimina;
	LinearLayout ll0;

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
		setContentView(R.layout.notizia);

		initial();

		LoginPrefs loggato = new LoginPrefs(getApplicationContext());
		loggato.getUtentePassword();
		mioIdUtente = loggato.utente;

		Bundle cestoPreso = getIntent().getExtras();
		idutente = cestoPreso.getString(TAG_IDUTENTE);
		idNotizia = cestoPreso.getString(TAG_IDNOTIZIA);
		titolo = cestoPreso.getString(TAG_TITOLO);
		testoPrimo = cestoPreso.getString(TAG_TESTOPRIMO);
		testoSecondo = cestoPreso.getString(TAG_TESTOSECONDO);
		data = cestoPreso.getString(TAG_DATA);

		if (mioIdUtente.equalsIgnoreCase(idutente)) {
			rlElimina.setVisibility(View.VISIBLE);
		}

		if (testoPrimo.length() > 60)
			testoPrimo = testoPrimo.substring(0, 60) + testoSecondo;

		tvIDUtente.setText(idutente);
		tvIDNotizia.setText(idNotizia);
		tvTitolo.setText(titolo);
		tvTestoPrimo.setText(testoPrimo);
		tvData.setText(data);

		bInteressante.setOnClickListener(this);
		bYes.setOnClickListener(this);
		bNo.setOnClickListener(this);
		bCommento.setOnClickListener(this);
		tvNumeroCommento.setOnClickListener(this);
		bElimina.setOnClickListener(this);

		if (isOnline()) {
			new SNInteressante().execute();
			new NumeroInteressante().execute();
			new NumeroYesNo().execute();
			new SNYesNo().execute();
			new NumeroCommento().execute();

		}
	}

	private void initial() {
		// TODO Auto-generated method stub

		tvIDUtente = (TextView) findViewById(R.id.tvNIDUtente);
		tvIDNotizia = (TextView) findViewById(R.id.tvNIdNotizia);
		tvTitolo = (TextView) findViewById(R.id.tvNTitolo);
		tvTestoPrimo = (TextView) findViewById(R.id.tvNTesto);
		tvData = (TextView) findViewById(R.id.tvNData);
		tvNumeroInteressante = (TextView) findViewById(R.id.tvNNumeroInteressante);
		tvNumeroYes = (TextView) findViewById(R.id.tvNNumeroYes);
		tvNumeroNo = (TextView) findViewById(R.id.tvNNumeroNo);

		bInteressante = (Button) findViewById(R.id.bNInteressante);
		bYes = (Button) findViewById(R.id.bNYes);
		bNo = (Button) findViewById(R.id.bNNo);
		bCommento = (Button) findViewById(R.id.bNCommento);
		bElimina = (Button) findViewById(R.id.bNElimina);

		tvNumeroCommento = (TextView) findViewById(R.id.tvNNumeroCommento);
		tvIDUtente1 = (TextView) findViewById(R.id.tvNIDUtente1);
		tvCommento1 = (TextView) findViewById(R.id.tvNCommento1);
		tvIDUtente2 = (TextView) findViewById(R.id.tvNIDUtente2);
		tvCommento2 = (TextView) findViewById(R.id.tvNCommento2);
		tvIDUtente3 = (TextView) findViewById(R.id.tvNIDUtente3);
		tvCommento3 = (TextView) findViewById(R.id.tvNCommento3);
		tvData1 = (TextView) findViewById(R.id.tvNData1);
		tvData2 = (TextView) findViewById(R.id.tvNData2);
		tvData3 = (TextView) findViewById(R.id.tvNData3);

		ll0 = (LinearLayout) findViewById(R.id.Nll0);
		ll1 = (RelativeLayout) findViewById(R.id.Nll1);
		ll2 = (RelativeLayout) findViewById(R.id.Nll2);
		ll3 = (RelativeLayout) findViewById(R.id.Nll3);
		rlElimina = (RelativeLayout) findViewById(R.id.rlNElimina);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bNInteressante:

			if (isOnline())
				// TODO Auto-generated method stub
				new Interessante().execute();
			else {
				Toast.makeText(getApplicationContext(),
						"No connessione a Internet", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.bNCommento:

			Intent i = new Intent(Notizia.this, Commento.class);
			Bundle cesto = new Bundle();
			cesto.putString(TAG_IDNOTIZIA, idNotizia);
			i.putExtras(cesto);
			startActivity(i);

			break;
		case R.id.tvNNumeroCommento:

			Intent iNC = new Intent(Notizia.this, Commento.class);
			Bundle cestoNC = new Bundle();
			cestoNC.putString(TAG_IDNOTIZIA, idNotizia);
			iNC.putExtras(cestoNC);
			startActivity(iNC);

			break;
		case R.id.bNYes:

			if (SNYesNo == null)
				new Yes().execute();
			else if (SNYesNo == "Yes") {
				new NoYesNo().execute();
			} else if (SNYesNo == "No") {
				new NoYesNo().execute();
				new Yes().execute();
			}

			break;

		case R.id.bNNo:

			if (SNYesNo == null)
				new No().execute();
			else if (SNYesNo == "No") {
				new NoYesNo().execute();
			} else if (SNYesNo == "Yes") {
				new NoYesNo().execute();
				new No().execute();
			}

			break;
		case R.id.bNElimina:

			new AlertDialog.Builder(this)
					.setTitle("Elimina notizia")
					.setMessage("Vuoi davvero eliminare questa notizia ?")
					.setPositiveButton("Si",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									
									new EliminaNotizia().execute();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();

			break;

		}

	}

	class EliminaNotizia extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Notizia.this);
			pDialog.setMessage("Procedendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_eliminaNotizia, params);

			int success = -1;

			try {
				// Checking for SUCCESS TAG
				success = json.getInt(TAG_SUCCESS);
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
				
				Toast.makeText(getApplicationContext(),
						"Eliminata con successo", Toast.LENGTH_LONG).show();
				Intent i = new Intent(Notizia.this, GestioneTabs.class);
				startActivity(i);
				Toast.makeText(getApplicationContext(),
						"Eliminata con successo", Toast.LENGTH_LONG).show();
				finish();
			}

		}
	}

	class CaricaCommenti extends AsyncTask<String, String, String> {

		
		protected String doInBackground(String... args) {
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			jsonCommento = jsonParser.inserisci(url_leggiCommentiPrimi, params);

			int success = -1;

			try {
				// Checking for SUCCESS TAG
				success = jsonCommento.getInt(TAG_SUCCESS);
				commenti = jsonCommento.getJSONArray("commenti");
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
			// dismiss the dialog
			pDialog.dismiss();

			int success = Integer.parseInt(v);
			if (success == 1) {

				String idutenteCommento, testoCommento, dataCommento;

				int i = 0;

				try {

					for (int n = 0; n < commenti.length(); n++) {
						JSONObject cc = commenti.getJSONObject(n);

						// Storing each json item in variable
						idutenteCommento = cc.getString(TAG_IDUTENTE);
						testoCommento = cc.getString(TAG_TESTO);
						dataCommento = cc.getString(TAG_DATACOMMENTO);

						if (n == 0) {
							tvIDUtente1.setText(idutenteCommento);
							tvCommento1.setText(testoCommento);
							tvData1.setText(dataCommento);
						}
						if (n == 1) {

							tvIDUtente2.setText(idutenteCommento);
							tvCommento2.setText(testoCommento);
							tvData2.setText(dataCommento);

						}
						if (n == 2) {

							tvIDUtente3.setText(idutenteCommento);
							tvCommento3.setText(testoCommento);
							tvData3.setText(dataCommento);

						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	class NumeroCommento extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_numeroCommento, params);

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

			if (success != -1) {

				new CaricaCommenti().execute();

				if (success > 3) {

					ll0.setVisibility(View.VISIBLE);
					tvNumeroCommento.setText(v);
					ll1.setVisibility(View.VISIBLE);
					ll2.setVisibility(View.VISIBLE);
					ll3.setVisibility(View.VISIBLE);

				} else if (success == 3) {

					ll1.setVisibility(View.VISIBLE);
					ll2.setVisibility(View.VISIBLE);
					ll3.setVisibility(View.VISIBLE);

				} else if (success == 2) {

					ll1.setVisibility(View.VISIBLE);
					ll2.setVisibility(View.VISIBLE);

				} else if (success == 1) {

					ll1.setVisibility(View.VISIBLE);

				}
			}

		}
	}

	class NoYesNo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIdUtente));
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_noYesNo, params);

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
			if (success == 1) {

				SNYesNo = null;
				// bYes.setBackgroundResource(R.drawable.interessante);
				bYes.setPressed(false);
				bNo.setPressed(false);
				new NumeroYesNo().execute();

			} else if (success != 0) {

				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class Yes extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIdUtente));
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json;

			json = jsonParser.inserisci(url_Yes, params);

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
			if (success == 1) {

				// bYes.setBackgroundResource(R.drawable.noninteressante);
				bYes.setPressed(true);
				SNYesNo = "Yes";
				Toast.makeText(getApplicationContext(), "Yessato con successo",
						Toast.LENGTH_LONG).show();
				new NumeroYesNo().execute();

			} else {
				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi " + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class No extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIdUtente));
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json;

			json = jsonParser.inserisci(url_No, params);

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
			if (success == 1) {

				// bYes.setBackgroundResource(R.drawable.noninteressante);

				bNo.setPressed(true);
				SNYesNo = "No";
				Toast.makeText(getApplicationContext(), "Noato con successo",
						Toast.LENGTH_LONG).show();
				new NumeroYesNo().execute();

			} else {

				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi " + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	int yes = -1, no = -1;

	class NumeroYesNo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_numeroYesNo, params);

			int success = 0;
			try {

				success = json.getInt(TAG_SUCCESS);
				yes = json.getInt("numeroYes");
				no = json.getInt("numeroNo");

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
			if (success != -1) {

				tvNumeroYes.setText(Integer.toString(yes));
				tvNumeroNo.setText(Integer.toString(no));

			} else
				Toast.makeText(getApplicationContext(),
						"Errore numero yes e no" + success, Toast.LENGTH_LONG)
						.show();
		}
	}

	class SNYesNo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIdUtente));
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_SNYesNo, params);

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
			if (success == 1) {

				SNYesNo = "Yes";
				// bYes.setBackgroundResource(R.drawable.interessante);
				bYes.setPressed(true);

			} else if (success == 2) {

				SNYesNo = "No";
				// bNo.setBackgroundResource(R.drawable.noninteressante);
				bNo.setPressed(true);

			} else if (success != 0) {

				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class NumeroInteressante extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_numeroInteressante,
					params);

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

				tvNumeroInteressante.setText(v);

			} else {
				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class SNInteressante extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Notizia.this);
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
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json = jsonParser.inserisci(url_SNInteressante, params);

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
			if (success == 0) {
				// Non ha messo interessante

				SNInteressante = false;
				bInteressante.setBackgroundResource(R.drawable.interessante);

			} else if (success == 1) {
				// Ha già messo interessante

				SNInteressante = true;
				bInteressante.setBackgroundResource(R.drawable.noninteressante);

			} else {

				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi" + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class Interessante extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDUTENTE, mioIdUtente));
			params.add(new BasicNameValuePair(TAG_IDNOTIZIA, idNotizia));

			JSONObject json;
			if (SNInteressante == true)
				json = jsonParser.inserisci(url_nonInteressante,
				/* "POST", */params);
			else
				json = jsonParser.inserisci(url_interessante, params);

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
			if (success == 1) {

				new NumeroInteressante().execute();

				if (SNInteressante == true) {
					SNInteressante = false;
					bInteressante
							.setBackgroundResource(R.drawable.interessante);
					Toast.makeText(getApplicationContext(),
							"Non interessato con successo", Toast.LENGTH_LONG)
							.show();
				}

				else {
					SNInteressante = true;
					bInteressante
							.setBackgroundResource(R.drawable.noninteressante);
					Toast.makeText(getApplicationContext(),
							"Interessato con successo", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Errore! Riprovare più tardi " + success,
						Toast.LENGTH_LONG).show();
			}

		}

	}

}
