package android.leonardo.com.br.boa_viagem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class BoaViagemDAO  extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO = 1;
    private static  final String BANCO_BOAVIAGEM= "bd_boaviagem";
    //TABELA CLIENTE
    private static  final String TABELA_VIAGEM = "tb_viagem";
    private static  final String V_COLUNA_ID     = "_id";
    private static  final String V_COLUNA_DESTINO = "destino";
    private static  final String V_COLUNA_TIPOVIAGEM = "tipoviagem";
    private static  final String V_COLUNA_DATACHEGADA = "datachegada";
    private static  final String V_COLUNA_DATASAIDA = "datasaida";
    private static  final String V_COLUNA_ORCAMENTO = "orcamento";
    private static  final String V_COLUNA_QUANTIDADEPESSOAS = "quantidadespessoas";
    //TABELA GASTO
    private static  final String TABELA_GASTO = "tb_gasto";
    private static  final String G_COLUNA_ID = "_id";
    private static  final String G_COLUNA_DATA = "data";
    private static  final String G_COLUNA_CATEGORIA = "categoria";
    private static  final String G_COLUNA_DESCRICAO = "descricao";
    private static  final String G_COLUNA_LOCAL  = "local";
    private static  final String G_COLUNA_VALOR = "valor";
    private static  final String G_COLUNAVIAGEMID = "viagemid";

    public BoaViagemDAO(Context context ){
        super(context, BANCO_BOAVIAGEM, null, VERSAO_BANCO);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY_COLUNA_VIAGEM = "CREATE TABLE " + TABELA_VIAGEM + " ( " + V_COLUNA_ID + " INTEGER PRIMARY KEY," +
                V_COLUNA_DESTINO + "  TEXT ," + V_COLUNA_TIPOVIAGEM + " INTEGER ," + V_COLUNA_DATACHEGADA + " DATE ,"+
                V_COLUNA_DATASAIDA + " DATE," +  V_COLUNA_ORCAMENTO +  " DOUBLE," +  V_COLUNA_QUANTIDADEPESSOAS + " INTEGER)";
        db.execSQL(QUERY_COLUNA_VIAGEM);

        String QUERY_COLUNA_GASTO = " CREATE TABLE " + TABELA_GASTO + " ( " + G_COLUNA_ID + " INTEGER PRIMARY KEY," +
                G_COLUNA_DATA + " DATE ," + G_COLUNA_CATEGORIA + " TEXT ," + G_COLUNA_DESCRICAO + " TEXT ," + G_COLUNA_LOCAL +
                " TEXT ," + G_COLUNA_VALOR+ " DOUBLE, "+ G_COLUNAVIAGEMID  + " INTEGER ,"+ " FOREIGN KEY ( "+ G_COLUNAVIAGEMID + " ) " + " REFERENCES " + TABELA_VIAGEM +
                " ( " +V_COLUNA_ID +" ) ) ; ";
        db.execSQL(QUERY_COLUNA_GASTO);





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /////////////////////// crud da tabela viagem /////////////////////////////////////

    //insert na tabela viagen
    void  addViagem(Viagem viagem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(V_COLUNA_DESTINO , viagem.getDestino());
        values.put(V_COLUNA_DATACHEGADA , viagem.getDataChegada());
        values.put(V_COLUNA_DATASAIDA , viagem.getDataSaida());
        values.put(V_COLUNA_ORCAMENTO ,viagem.getOrcamento());
        values.put(V_COLUNA_QUANTIDADEPESSOAS , viagem.getQuantidadePessoas());
        values.put(V_COLUNA_TIPOVIAGEM , viagem.getTipoViagem());

        db.insert(TABELA_VIAGEM,null,values);

        db.close();
    }


    //update na tabela viagem
    void atualizarViagem(Viagem viagem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(V_COLUNA_DESTINO , viagem.getDestino());
        values.put(V_COLUNA_DATACHEGADA , viagem.getDataChegada());
        values.put(V_COLUNA_DATASAIDA , viagem.getDataSaida());
        values.put(V_COLUNA_ORCAMENTO ,viagem.getOrcamento());
        values.put(V_COLUNA_QUANTIDADEPESSOAS , viagem.getQuantidadePessoas());
        values.put(V_COLUNA_TIPOVIAGEM , viagem.getTipoViagem());

        String id = String.valueOf(viagem.getId());

        db.update(TABELA_VIAGEM, values, V_COLUNA_ID+ "= ?", new String[] {id});

        db.close();


    }


    //delete na tabela viagem
    void apagarViagem(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABELA_VIAGEM,V_COLUNA_ID + "=?", new String[]{String.valueOf(id)});
        db.delete(TABELA_GASTO,G_COLUNAVIAGEMID + "=?",new String[]{String.valueOf(id)} );

        db.close();

    }



    public List<Viagem> listartodasasviagens(){
        List<Viagem> listaviagens  = new ArrayList<Viagem>();
        String query = "SELECT  * FROM "+ TABELA_VIAGEM;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
                Viagem viagem= new Viagem();
                viagem.setId(Integer.parseInt(cursor.getString(0)));
                viagem.setDestino(cursor.getString(1));
                viagem.setTipoViagem(Integer.parseInt(cursor.getString(2)));
                viagem.setDataChegada(cursor.getString(3));
                viagem.setDataSaida(cursor.getString(4));
                viagem.setOrcamento(Double.parseDouble(cursor.getString(5)));


                listaviagens.add(viagem);

            }while (cursor.moveToNext());

        }
        cursor.close();
        return  listaviagens;

    }


    public double calcularTotalGasto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + G_COLUNA_VALOR +") FROM " +  TABELA_GASTO +" WHERE viagemid = ?",
                new String[]{ id });
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    public Viagem prepararEdicao(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

     /*  Cursor cursor = db.rawQuery( " SELECT "+ V_COLUNA_TIPOVIAGEM + ", "+ V_COLUNA_DESTINO + " , " + V_COLUNA_DATACHEGADA + " , "
                                    + V_COLUNA_DATASAIDA+ " , " + V_COLUNA_QUANTIDADEPESSOAS +" , "+ V_COLUNA_ORCAMENTO+ " FROM "+
                                    TABELA_VIAGEM + " WHERE "+ V_COLUNA_ID + " = ? ",  new String[]{ id });*/


        Cursor cursor = db.rawQuery("SELECT  tipoviagem, destino, datachegada, datasaida, quantidadespessoas, orcamento FROM tb_viagem" +
                " WHERE  _id = ?", new String[] { id } );
        /*String query =  " SELECT "+ V_COLUNA_TIPOVIAGEM + ", "+ V_COLUNA_DESTINO + " , " + V_COLUNA_DATACHEGADA + ","
                + V_COLUNA_DATASAIDA+ " , " + V_COLUNA_QUANTIDADEPESSOAS +" , "+ V_COLUNA_ORCAMENTO+ " FROM "+
                TABELA_VIAGEM + "WHERE "+ V_COLUNA_ID + " id = ? ", new String[]{String.valueOf(id)};*/



        cursor.moveToFirst();


        Viagem viagem= new Viagem();

        viagem.setTipoViagem(Integer.parseInt(cursor.getString(0)));
        viagem.setDestino(cursor.getString(1));
        viagem.setDataChegada(cursor.getString(2));
        viagem.setDataSaida(cursor.getString(3));
        viagem.setQuantidadePessoas(Integer.parseInt(cursor.getString(4)));
        viagem.setOrcamento(Double.parseDouble(cursor.getString(5)));




        cursor.close();

        return viagem;

    }

    /////////////////////// crud da tabela gasto /////////////////////////////////////

    void addgasto(Gasto gasto){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(G_COLUNA_DATA , gasto.getData());
        values.put(G_COLUNA_CATEGORIA , gasto.getCategoria());
        values.put(G_COLUNA_DESCRICAO , gasto.getDescricao());
        values.put(G_COLUNA_VALOR , gasto.getValor());
        values.put(G_COLUNA_LOCAL , gasto.getLocal());
        values.put(G_COLUNAVIAGEMID, gasto.getViagemId());


        db.insert(TABELA_GASTO,null,values);

        db.close();
    }

    public List<Gasto> listartodososgastos(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Gasto> listagasto  = new ArrayList<Gasto>();

        Cursor cursor = db.rawQuery ("SELECT  _id , data , descricao , valor , categoria , local FROM tb_gasto WHERE viagemid = ?", new String[]{id });

        /*String query = "SELECT " + G_COLUNA_ID+ ", "+G_COLUNA_DATA +", "+ G_COLUNA_DESCRICAO+", "+ G_COLUNA_VALOR +", "+ G_COLUNA_CATEGORIA+", " + G_COLUNA_LOCAL+ " FROM "+
                TABELA_VIAGEM + " WHERE "+ G_COLUNA_ID+" = ?";*/






        if (cursor.moveToFirst()){
            do {
                Gasto gasto= new Gasto();
                gasto.setId(Integer.parseInt(cursor.getString(0)));
                gasto.setData(cursor.getString(1));
                gasto.setDescricao(cursor.getString(2));
                gasto.setValor(Double.parseDouble(cursor.getString(3)));
                gasto.setCategoria(cursor.getString(4));
                gasto.setLocal(cursor.getString(5));

                listagasto.add(gasto);

            }while (cursor.moveToNext());

        }
        cursor.close();
        return  listagasto;

    }

    public void removerGasto(String data,String categoria,String local,String descricao, Double valor) {
        String valoconv = valor.toString();

        SQLiteDatabase db = this.getWritableDatabase();
        String where [] = new String[]{ data,categoria,local,descricao,valoconv };
     /*   db.execSQL("DELETE FROM "+ TABELA_GASTO+ " WHERE "+ G_COLUNA_DATA+ " = " + data + " AND "+ G_COLUNA_CATEGORIA+ " = " +categoria+" AND "
                 + G_COLUNA_LOCAL +" = "+ local + " AND "+ G_COLUNA_DESCRICAO +" = "+descricao+ " AND "  +G_COLUNA_VALOR+" = "+valor);*/
        db.delete("tb_gasto", " data = ? AND  categoria = ? AND local = ? AND descricao = ? AND valor = ?  ", where);
        db.close();


    }






}