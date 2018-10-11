package com.jmluriad.lurinews;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPrefs {

	public static String FILENAME = "Login";
	public static String FILE_IDUTENTE = "IDUtente";
	public static String FILE_PASSWORD = "password";
	public static String FILE_SN = "sn";

	SharedPreferences sp;

	String utente = null, password = null;

	public LoginPrefs(Context c) {

		sp = c.getSharedPreferences(FILENAME, 0);
	}

	public boolean isLogged() {

		boolean sn = sp.getBoolean(FILE_SN, false);

		return sn;
	}

	public void logout() {

		SharedPreferences.Editor Ed = sp.edit();
		Ed.putString(FILE_IDUTENTE, "");
		Ed.putString(FILE_PASSWORD, "");
		Ed.putBoolean(FILE_SN, false);
		Ed.commit();
	}

	public void getUtentePassword() {

		if (isLogged()) {

			utente = sp.getString(FILE_IDUTENTE, null);
			password = sp.getString(FILE_PASSWORD, null);
		}

	}

	public LoginPrefs(Context c, String idutente, String psw) {

		sp = c.getSharedPreferences(FILENAME, 0);
		utente = idutente;
		password = psw;
	}

	public void salvaUtente() {

		SharedPreferences.Editor Ed = sp.edit();
		Ed.putString(FILE_IDUTENTE, utente);
		Ed.putString(FILE_PASSWORD, password);
		Ed.putBoolean(FILE_SN, true);
		Ed.commit();
	}

}
