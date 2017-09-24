package android.leonardo.com.br.boa_viagem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.util.Date;

import static android.leonardo.com.br.boa_viagem.Constantes.PREFERENCIAS;
import static android.leonardo.com.br.boa_viagem.R.id.dataChegada;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class ViagemActivity extends AppCompatActivity {

    //data de chegada
    private int ano, mes, dia;
    //data de saida
    private int ano1, mes1, dia1;

    // atributo de instancia
    private CalendarService calendarService;


    private Button btnSalvarviagem;
    BoaViagemDAO  db = new BoaViagemDAO(this);
    //id que serve pra saber qual viagem se trata
    private String id;


    private Button dataSaidaButton, dataChegadaButton;



    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;
    private MenuItem item_remover;

    private Calendar calendar,calendar1;


    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_viagem);
//data de chegada
        calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

//data de saida
        calendar1 = Calendar.getInstance();
        ano1 = calendar1.get(Calendar.YEAR);
        mes1 = calendar1.get(Calendar.MONTH);
        dia1 = calendar1.get(Calendar.DAY_OF_MONTH);


//atributos do layout Viagem
        dataChegadaButton= (Button) findViewById(R.id.dataChegada);
        dataChegadaButton.setText(dia + "/" + (mes+1) + "/" + ano);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        dataSaidaButton.setText(dia1 + "/" + (mes1+1) + "/" + ano1);
        // recuperando novas views
        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);


        // botao salvar
        btnSalvarviagem = (Button) findViewById(R.id.btnSalvarviagem);


        //verificar se è  update
        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if(id != null){

            prepararEdicao();
        }


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

                    Calendar c = Calendar.getInstance();
                    c.set(ano, mes, dia);
                    Date data = c.getTime();

                    Calendar c1 = Calendar.getInstance();
                    c1.set(ano1, mes1, dia1);
                    Date data1 = c1.getTime();


                    Viagem viagem = new Viagem();

                    viagem.setDestino(v_destino);
                    viagem.setDateChegada(data);
                    viagem.setDateSaida(data1);
                    new Task().execute(viagem);

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






        calendarService = criarCalendarService();


    }


    //metodo selecionar data
    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(dataChegada == id ){
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



    //menu lado superior direito ...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }

//desabitar quando for nova viagem
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


    private CalendarService criarCalendarService() {
        SharedPreferences preferencias =
                getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
        String nomeConta = preferencias.getString(Constantes.NOME_CONTA, null);
        String tokenAcesso = preferencias.getString(Constantes.TOKEN_ACESSO, null);
        return new CalendarService(nomeConta, tokenAcesso);

}
    private class Task extends AsyncTask<Viagem, Void, Void> {
        @Override
        protected Void doInBackground(Viagem... viagens) {
            Viagem viagem = viagens[0];
            calendarService.criarEvento(viagem);
            return null;
        }
    }

}

