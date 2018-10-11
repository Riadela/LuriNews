package com.jmluriad.lurinews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScriviNotizia extends Activity implements OnClickListener {
	EditText scriviTitolo, scriviNotizia;
	Button pubblica;
	String sScriviNotizia, sScriviTitolo;

	public static Activity fa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrivinotizia);

		initial();

		pubblica.setOnClickListener(this);

		fa = this;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		sScriviNotizia = scriviNotizia.getText().toString();
		sScriviTitolo = scriviTitolo.getText().toString();

		if (sScriviNotizia == "" || sScriviTitolo == "") {

			Toast.makeText(getApplicationContext(), "Compilare tutti i campi",
					Toast.LENGTH_LONG).show();
			
		} else if (sScriviNotizia.length() < 61) {
			
			Toast.makeText(getApplicationContext(), "Minimo caratteri 60",
					Toast.LENGTH_LONG).show();

		} else {
			Bundle cesto = new Bundle();
			cesto.putString("infoArticolo", sScriviNotizia);
			cesto.putString("infoTitolo", sScriviTitolo);

			Intent i = new Intent(ScriviNotizia.this,
					ScriviNotiziaAnteprima.class);
			i.putExtras(cesto);

			startActivity(i);
		}
	}

	private void initial() {
		// TODO Auto-generated method stub

		scriviNotizia = (EditText) findViewById(R.id.etNotizia);

		scriviTitolo = (EditText) findViewById(R.id.etTitoloNotizia);
		pubblica = (Button) findViewById(R.id.bAnteprimaNotizia);
	}

}
