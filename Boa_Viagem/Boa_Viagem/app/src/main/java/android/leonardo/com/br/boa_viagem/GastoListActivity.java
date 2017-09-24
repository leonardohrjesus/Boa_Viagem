package android.leonardo.com.br.boa_viagem;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amministratore on 20/08/2017.
 */
public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener , DialogInterface.OnClickListener {

    private String dataAnterior = "";
    private String id;
    BoaViagemDAO db = new BoaViagemDAO(this);

    private AlertDialog dialogConfirmacao;

    private String data;
    private String descricao ;
    private double valor ;
    private String categoria ;
    private String local ;
    private SimpleAdapter adapter;



    private List<Map<String, Object>> gastos;

    //lista de gastos
    private List<Map<String, Object>> listarGastos() {



        //pega o id de outra activity
        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        List<Gasto> gasto = db.listartodososgastos(id);

        gastos = new ArrayList<Map<String, Object>>();



        for (Gasto g   : gasto){
            Map<String, Object> item = new HashMap<String, Object>();
            String id =  ""+ g.getId();
            data = g.getData();
            descricao = g.getDescricao();
            valor = g.getValor();
            categoria = g.getCategoria();
            local = g.getLocal();
            item.put("id", id);
            item.put("data", data);
            item.put("descricao",descricao);
            item.put("valor",valor);
            item.put("categoria",R.color.categoria_hospedagem);
            item.put("local",local);
            gastos.add(item);
        }
        return gastos;
    }


    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        atualizaoucriarlista();


        // registramos aqui o novo menu de contexto
        registerForContextMenu(getListView());



        this.dialogConfirmacao = criaDialogConfirmacao();
    }




    //classe responsalver por classificar  por data e cor
    private class GastoViewBinder implements SimpleAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if(view.getId() == R.id.data )  {
                if(!dataAnterior.equals(data)){
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if(view.getId() == R.id.categoria){
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descricao");
        String mensagem = "Gasto selecionada: " + descricao;
        dialogConfirmacao.show();
        Toast.makeText(this, mensagem,Toast.LENGTH_SHORT).show();

    }

    //exibir menu gasto(nao funcioa por causa da heranca listView)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
    }
    //pegar objeto da listagem(nao funciona por causa da heranca lisview)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remover_gasto) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            // remover do banco de dados
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_gasto);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao),  this);
        return builder.create();

    }


    @Override
    public void onClick(DialogInterface dialog, int item){

        switch (item) {
            case 0:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                db.removerGasto(data,categoria,local,descricao,valor);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
        atualizaoucriarlista();



    }


    private void atualizaoucriarlista(){

        String[] de = { "data", "categoria" ,"descricao", "valor" };
        int[] para = { R.id.data,  R.id.categoria ,R.id.descricao, R.id.valor};
        adapter = new SimpleAdapter(this, listarGastos(),  R.layout.lista_gasto, de, para);
        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener( this);

    }

    //activity destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }









}



