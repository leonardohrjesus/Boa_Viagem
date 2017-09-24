package android.leonardo.com.br.boa_viagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class ConfiguracoesActivity extends PreferenceActivity {

    //preferencia onde tem duas opcoes
    //1 modo viagem assim fica conectado direto
    //2 limitar gasto de viagens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }

    //activity destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}