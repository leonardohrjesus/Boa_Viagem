package android.leonardo.com.br.boa_viagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class ConfiguracoesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}