package android.leonardo.com.br.boa_viagem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import static android.leonardo.com.br.boa_viagem.Constantes.VIAGEM_ID;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class GastoActivity extends AppCompatActivity {

    private int ano,mes,dia;
    private Button dataGasto;
    private Spinner categoria;
    private EditText valor,descricao,local;
    private ArrayAdapter<CharSequence> adapter;
    private String id;
    private Button btnsalvargasto;
    BoaViagemDAO  db = new BoaViagemDAO(this);


//criar tela de gasto
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);


        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        dataGasto = (Button) findViewById(R.id.data);
        dataGasto.setText(dia + "/" + (mes+1) + "/" + ano);

        valor = (EditText) findViewById(R.id.valor);
        descricao = (EditText) findViewById(R.id.descricao);
        local = (EditText) findViewById(R.id.local);
        btnsalvargasto = (Button) findViewById(R.id.btnsalvargasto);

        btnsalvargasto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String g_descricao = descricao.getText().toString();
                String g_valor = valor.getText().toString();
                String g_local = local.getText().toString();
                String g_categoria = categoria.getSelectedItem().toString();
                String g_data =  (String) dataGasto.getText();


                //verificar se existe alguma viagem selecionada
                id = getIntent().getStringExtra(VIAGEM_ID);
                if(id  ==   null){
                    irlistarviagem();
                }
                else

                if (g_descricao.isEmpty() || g_valor.isEmpty()|| g_local.isEmpty()|| g_data.isEmpty()) {
                    descricao.setError("Este campo è obrigatorio");
                    valor.setError("Este campo è obrigatorio");
                    local.setError("Este campo è obrigatorio");
                    dataGasto.setError("Este campo è obrigatorio");
                }else{

                    db.addgasto(new Gasto(g_data,g_categoria,g_descricao,Double.parseDouble(g_valor),g_local,Integer.parseInt(id)));
                    Toast.makeText(GastoActivity.this,"Gasto inserido com sucesso", Toast.LENGTH_LONG).show();
                    limparCampos();
                }
            }
        });
        adapter = ArrayAdapter.createFromResource(
                this, R.array.categoria_gasto,  android.R.layout.simple_spinner_item);
        categoria = (Spinner) findViewById(R.id.categoria);
        categoria.setAdapter(adapter);





    }


    public void irlistarviagem (){

        Intent irlistarviagem = new Intent(this,ViagemListActivity.class);
        startActivity(irlistarviagem);
        Toast.makeText(GastoActivity.this,"Por favor selecionar alguma Viagem!", Toast.LENGTH_LONG).show();
;

    }


    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(R.id.data == id){
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataGasto.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.remover_gasto:
                limparCampos();
                //banco de dados depois
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_remover;
        item_remover = menu.findItem(R.id.remover_gasto);

        id = getIntent().getStringExtra(VIAGEM_ID);
        if(id != null){
            item_remover.setEnabled(true);

        }else{
            item_remover.setEnabled(false);

        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void limparCampos(){
        descricao.setText("");
        valor.setText("");
        local.setText("");
        categoria.getSelectedItem().toString();
        dataGasto.setText(dia + "/" + (mes+1) + "/" + ano);
        categoria.setAdapter(adapter);

    }

    //activity destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
