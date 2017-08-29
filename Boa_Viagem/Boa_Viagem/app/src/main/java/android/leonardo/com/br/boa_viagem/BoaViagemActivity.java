package android.leonardo.com.br.boa_viagem;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;

import java.io.IOException;




public class BoaViagemActivity extends AppCompatActivity
{
    // novos atributos
    private SharedPreferences preferencias;
    private GoogleAccountManager accountManager;
    private Account conta;

    private CheckBox manterConectado;
    private EditText usuario;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accountManager = new GoogleAccountManager(this);

        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
        manterConectado = (CheckBox) findViewById(R.id.manterConectado);

        preferencias = getSharedPreferences(Constantes.PREFERENCIAS, MODE_PRIVATE);
        boolean conectado = preferencias
                .getBoolean(Constantes. MANTER_CONECTADO, false);


        if(conectado){

            iniciarDashboard();

        }
    }

    public void entrarOnClick(View v) {
        String usuarioInformado = usuario.getText().toString();
        String senhaInformada = senha.getText().toString();

        autenticar(usuarioInformado, senhaInformada);


    }

    private void iniciarDashboard(){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void autenticar(final String nomeConta, String senha) {

        conta = accountManager.getAccountByName(nomeConta);

        if(conta == null){
            Toast.makeText(this, R.string.conta_inexistente,
                    Toast.LENGTH_LONG).show();
            return;
        }


        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, nomeConta);
        bundle.putString(AccountManager.KEY_PASSWORD, senha);
        accountManager.getAccountManager().confirmCredentials(conta , bundle, this, new AutenticacaoCallback(), null);
    }

    private class AutenticacaoCallback     implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                Bundle bundle = future.getResult();
                if(bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
                    iniciarDashboard();
                }else {
                    Toast.makeText(getBaseContext(),
                            getString(R.string.erro_autenticacao),
                            Toast.LENGTH_LONG).show();
                }
            } catch (OperationCanceledException e) {
// usuário cancelou a operação
            } catch (AuthenticatorException e) {
// possível falha no autenticador
            } catch (IOException e) {
// possível falha de comunicação
            }


        }

    }





}

