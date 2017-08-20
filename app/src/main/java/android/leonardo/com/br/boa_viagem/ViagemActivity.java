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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class ViagemActivity extends AppCompatActivity {
    private int ano, mes, dia;
    private int ano1, mes1, dia1;

    private Button btnSalvarviagem;
    BoaViagemDAO  db = new BoaViagemDAO(this);
    private String id;

    private Button dataChegadaButton;
    private Button dataSaidaButton;

    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;
    private MenuItem item_remover;



    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar calendar1 = Calendar.getInstance();
        ano1 = calendar1.get(Calendar.YEAR);
        mes1 = calendar1.get(Calendar.MONTH);
        dia1 = calendar1.get(Calendar.DAY_OF_MONTH);


        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataChegadaButton.setText(dia + "/" + (mes+1) + "/" + ano);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        dataSaidaButton.setText(dia1 + "/" + (mes1+1) + "/" + ano1);
        // recuperando novas views
        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        btnSalvarviagem = (Button) findViewById(R.id.btnSalvarviagem);

        btnSalvarviagem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String v_destino = destino.getText().toString();
                String v_data_chegada = dataChegadaButton.getText().toString();
                String v_data_saida = dataSaidaButton.getText().toString();
                String v_orcamento = orcamento.getText().toString();
                String v_quantidade_pessoas = quantidadePessoas.getText().toString();
                Integer v_tipoviagem_aux = radioGroup.getCheckedRadioButtonId();
                Integer v_tipoviagem ;

                if(v_tipoviagem_aux == R.id.lazer) {
                    v_tipoviagem = Constantes.VIAGEM_LAZER;
                } else {
                    v_tipoviagem = Constantes.VIAGEM_NEGOCIOS;

                }


                if (v_destino.isEmpty() || v_orcamento.isEmpty()|| v_quantidade_pessoas.isEmpty()) {
                    destino.setError("Este campo è obrigatorio");
                    orcamento.setError("Este campo è obrigatorio");
                    quantidadePessoas.setError("Este campo è obrigatorio");
                }else  if(id == null){
                    db.addViagem(new Viagem(v_destino,v_tipoviagem,v_data_chegada,v_data_saida,Double.parseDouble(v_orcamento),
                            Integer.parseInt(v_quantidade_pessoas)));
                    Toast.makeText(ViagemActivity.this,"Viagem inserido com sucesso", Toast.LENGTH_LONG).show();


                } else {
                    id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
                    db.atualizarViagem(new Viagem(Integer.parseInt(id),v_destino,v_tipoviagem,v_data_chegada,v_data_saida,Double.parseDouble(v_orcamento),
                            Integer.parseInt(v_quantidade_pessoas)));

                    Toast.makeText(ViagemActivity.this,"Viagem Atualizado  com sucesso", Toast.LENGTH_LONG).show();

                }
                limparcamposviagem();
            }
        });


        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if(id != null){

            prepararEdicao();
        }




    }


    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(R.id.dataChegada == id ){
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }

        else if( R.id.dataSaida == id){
            return new DatePickerDialog(this, listener1, ano1, mes1, dia1);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataChegadaButton.setText(dia + "/" + (mes+1) + "/" + ano);

        }

    };
    private DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,    int year, int monthOfYear, int dayOfMonth) {


            ano1 = year;
            mes1 = monthOfYear;
            dia1 = dayOfMonth;
            dataSaidaButton.setText(dia1 + "/" + (mes1+1) + "/" + ano1);

        }

    };

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        item_remover = menu.findItem(R.id.remover_viagem);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if(id != null){
            item_remover.setEnabled(true);

        }else{
            item_remover.setEnabled(false);

        }

        return super.onPrepareOptionsMenu(menu);
    }


    //selecionar opcao do menu
    public boolean onOptionsItemSelected(MenuItem item) {
        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);


        switch (item.getItemId()) {
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            case R.id.remover_viagem:
                //remover viagem do banco de dados
                removerViagem(id);
                String mensagem = "Excluido com sucesso!";
                makeText(this, mensagem, LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    //fechar conexao com banco
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void prepararEdicao() {
        Viagem viagem= db.prepararEdicao(id);
        String v_destino = viagem.getDestino();
        int v_tipo_de_viagem = viagem.getTipoViagem();




        String v_dataChegada = viagem.getDataChegada();
        String v_dataSaida = viagem.getDataSaida();
        int v_quantidadesPessoas = viagem.getQuantidadePessoas();
        Double v_orcamento = viagem.getOrcamento();


        if(v_tipo_de_viagem == Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }

        destino.setText(v_destino);
        dataChegadaButton.setText(v_dataChegada);
        dataSaidaButton.setText(v_dataSaida);
        quantidadePessoas.setText(String.valueOf(v_quantidadesPessoas));
        orcamento.setText(v_orcamento.toString());

    }

    /*private void prepararEdicao() {
      SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.rawQuery("SELECT tipo_viagem, destino, data_chegada, " +
                        "data_saida, quantidade_pessoas, orcamento " +
                        "FROM tb_viagem WHERE _id = ?", new String[]{ id });
        cursor.moveToFirst();

        if(curso.getint(0) == Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }
        destino.setText(cursor.getString(1));
         String dataChegada = cursor.getString(2);
        String dataSaida = cursor.getString(3);
        dataChegadaButton.setText(dataChegada);
        dataSaidaButton.setText(dataSaida);
        quantidadePessoas.setText(cursor.getString(4));
        orcamento.setText(cursor.getString(5));
        cursor.close();

    }*/

    private void removerViagem(String id) {

        String v_destino = destino.getText().toString();
        String v_orcamento = orcamento.getText().toString();
        String v_quantidade_pessoas = quantidadePessoas.getText().toString();

        if (v_destino.isEmpty() || v_orcamento.isEmpty()|| v_quantidade_pessoas.isEmpty()) {
            destino.setError("Este campo è obrigatorio");
            orcamento.setError("Este campo è obrigatorio");
            quantidadePessoas.setError("Este campo è obrigatorio");
        }else{

            db.apagarViagem(id);
            limparcamposviagem();

        }

    }
    //LIMPAR CAMPOS
    public void  limparcamposviagem(){

        destino.setText("");
        dataChegadaButton.setText(R.string.selecione);
        dataSaidaButton.setText(R.string.selecione);
        orcamento.setText("");
        quantidadePessoas.setText("");

    }




}

