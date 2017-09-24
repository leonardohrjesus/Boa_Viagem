package android.leonardo.com.br.boa_viagem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class DashboardActivity  extends AppCompatActivity {

    private String opcao;
    private TextView textView;


    //criando Tela
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

    }

    //botao opcoes
    public void selecionarOpcao(View view) {
        switch (view.getId()) {
            case R.id.nova_viagem:
                startActivity(new Intent(this, ViagemActivity.class));
                textView = (TextView) view;
                opcao = "Opção: " + textView.getText().toString();
                Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
                break;
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                textView = (TextView) view;
                opcao = "Opção: " + textView.getText().toString();
                Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
                break;
            case R.id.minhas_viagens:
                startActivity(new Intent(this, ViagemListActivity.class));
                textView = (TextView) view;
                opcao = "Opção: " + textView.getText().toString();
                Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
                break;
            case R.id.configuracoes:
                startActivity(new Intent(this, ConfiguracoesActivity.class));
                textView = (TextView) view;
                opcao = "Opção: " + textView.getText().toString();
                Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
                break;

        }
    }

    //opcao criar menu em cima sair
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }
    //opcao menu em cima sair
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sair:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //activity destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
