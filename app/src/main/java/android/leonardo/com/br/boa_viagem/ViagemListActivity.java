package android.leonardo.com.br.boa_viagem;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amministratore on 20/08/2017.
 */
public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener , DialogInterface.OnClickListener {

    private SimpleDateFormat dateFormat;
    private Double valorLimite;

    private AlertDialog dialogConfirmacao;
    private int viagemSelecionada;
    private AlertDialog alertDialog;
    private Intent intent;
    private SimpleAdapter adapter;
    int VIAGENS = 1;

    BoaViagemDAO db = new BoaViagemDAO(this);

    ArrayAdapter<String> adpter;
    ArrayList<String> arraylist;
    LinearLayout layoutListaViagem;

    // atributo de instancia
    private List<Map<String, Object>> viagens;
    //metodo lista viagens
    private List<Map<String, Object>> listarViagens() {

        List<Viagem> viagems = db.listartodasasviagens();

        viagens = new ArrayList<Map<String, Object>>();


        for (Viagem v   : viagems){
            Map<String, Object> item = new HashMap<String, Object>();

            String  id = "" + v.getId();
            int  tipoViagem = v.getTipoViagem();
            String destino = v.getDestino();
            String dataChegada = v.getDataChegada();
            String dataSaida = v.getDataSaida();
            double orcamento = v.getOrcamento();

            item.put("id", id);
            if (tipoViagem == Constantes.VIAGEM_LAZER) {
                item.put("imagem", R.drawable.lazer);
            } else {
                item.put("imagem", R.drawable.negocios);
            }
            item.put("destino", destino);

            String periodo = dataChegada + " a "
                    + dataSaida;
            item.put("data", periodo);
            double totalGasto = db.calcularTotalGasto(id);
            item.put("total", "Gasto total R$ " + totalGasto);
            double alerta = orcamento * valorLimite / 100;
            Double [] valores =
                    new Double[] { orcamento, alerta, totalGasto };
            item.put("barraProgresso", valores);
            viagens.add(item);

        }

        return viagens;
    }




    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SharedPreferences preferencias =
                PreferenceManager.getDefaultSharedPreferences(this);
        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);




        atualizaroucriarlista();

        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();    }



    //metodo selciona a viagem
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.viagemSelecionada = position;
        alertDialog.show();
    }



    //metodo cria alert dialog com as opcoes
    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.editar),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover) };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);
        return builder.create();
    }

    //metodo criar e mostrar confirmacao de execlusao
    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim),  this);
        builder.setNegativeButton(getString(R.string.nao),  this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {

        String id = (String) viagens.get(viagemSelecionada).get("id");
        switch (item) {
            case 0: // editar viagem
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID,id);
                startActivity(intent);

                break;
            case 2:
                intent = new Intent(this, GastoListActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID,id);
                startActivity(intent);
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE: // exclusão
                db.apagarViagem(id);

                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
        atualizaroucriarlista();

    }

    //classe responsalvel de controlar barra de progresso
    private class ViagemViewBinder implements SimpleAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Object data,  String textRepresentation) {

            if (view.getId() == R.id.barraProgresso) {
                Double valores[] = (Double[]) data;
                ProgressBar progressBar = (ProgressBar) view;
                progressBar.setMax(valores[0].intValue());
                progressBar.setSecondaryProgress(valores[1].intValue());
                progressBar.setProgress(valores[2].intValue());
                return true;
            }
            return false;
        }


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
// A activity está prestes a se tornar visível
    }
    private  void atualizaroucriarlista(){

        String[] de = { "imagem", "destino", "data",  "total", "barraProgresso" };
        int[] para = { R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso };
        //verificar se adapter ja foi criado

        adapter =   new SimpleAdapter(this, listarViagens(),   R.layout.lista_viagem, de, para);
        adapter.setViewBinder(new ViagemViewBinder());
        setListAdapter(adapter);

        adapter.notifyDataSetChanged();


        getListView().setOnItemClickListener(this);
    }




}
