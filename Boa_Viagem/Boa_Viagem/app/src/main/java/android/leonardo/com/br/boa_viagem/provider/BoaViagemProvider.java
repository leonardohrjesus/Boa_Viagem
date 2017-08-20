package android.leonardo.com.br.boa_viagem.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import android.leonardo.com.br.boa_viagem.BoaViagemDAO;


import static android.leonardo.com.br.boa_viagem.provider.BoaViagemContract.AUTHORITY;
import static android.leonardo.com.br.boa_viagem.provider.BoaViagemContract.GASTO_PATH;
import static android.leonardo.com.br.boa_viagem.provider.BoaViagemContract.VIAGEM_PATH;


/**
 * Created by Amministratore on 18/08/2017.
 */

public class BoaViagemProvider extends ContentProvider {

    private BoaViagemDAO boaViagemDAO;


    private static final int VIAGENS = 1;
    private static final int VIAGEM_ID = 2;
    private static final int GASTOS = 3;
    private static final int GASTO_ID = 4;
    private static final int GASTOS_VIAGEM_ID = 5;
    private static final UriMatcher uriMatcher =  new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(AUTHORITY, VIAGEM_PATH+"/", VIAGENS);
        uriMatcher.addURI(AUTHORITY, VIAGEM_PATH + "/#", VIAGEM_ID);
        uriMatcher.addURI(AUTHORITY, GASTO_PATH+"/", GASTOS);
        uriMatcher.addURI(AUTHORITY, GASTO_PATH + "/#", GASTO_ID);
        uriMatcher.addURI(AUTHORITY, GASTO_PATH + "/"+ VIAGEM_PATH + "/#", GASTOS_VIAGEM_ID);
    }




    //CRIAR
    @Override
    public boolean onCreate() {
        boaViagemDAO = new BoaViagemDAO(getContext());
        return true;
    }

    //SELECIONAR UMA TABELA
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = boaViagemDAO.getReadableDatabase();


        Log.i("Cursor_valor:","passou");


        switch (uriMatcher.match(uri)) {

            case VIAGENS:
                return db.query(VIAGEM_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            case VIAGEM_ID:
                selection = BoaViagemContract.Viagem._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.query(VIAGEM_PATH, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Uri desconhecida");
            case GASTOS:
                return db.query(GASTO_PATH, projection,
                        selection, selectionArgs, null, null, sortOrder);
            case GASTO_ID:
                selection = BoaViagemContract.Gasto._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.query(GASTO_PATH, projection,
                        selection, selectionArgs, null, null, sortOrder);
            case GASTOS_VIAGEM_ID:
                selection = BoaViagemContract.Gasto.VIAGEM_ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.query(GASTO_PATH, projection,
                        selection, selectionArgs, null, null, sortOrder);
        }





    }

    //
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = boaViagemDAO.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case VIAGENS:
                id = db.insert(VIAGEM_PATH, null, values);
                return Uri.withAppendedPath(BoaViagemContract.Viagem.CONTENT_URI,
                        String.valueOf(id));
            case GASTOS:
                id = db.insert(GASTO_PATH, null, values);
                return Uri.withAppendedPath(BoaViagemContract.Gasto.CONTENT_URI,
                        String.valueOf(id));
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }


    }

    //
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = boaViagemDAO.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case VIAGEM_ID:
                selection = BoaViagemContract.Viagem._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.delete(VIAGEM_PATH,
                        selection, selectionArgs);
            case GASTO_ID:
                selection = BoaViagemContract.Gasto._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.delete(GASTO_PATH,
                        selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }
    //ATUALIZAR
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = boaViagemDAO.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case VIAGEM_ID:
                selection = BoaViagemContract.Viagem._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.update(VIAGEM_PATH, values,
                        selection, selectionArgs);
            case GASTO_ID:
                selection = BoaViagemContract.Gasto._ID + " = ?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return db.update(GASTO_PATH, values,
                        selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }
    }
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case VIAGENS:
                return BoaViagemContract.Viagem.CONTENT_TYPE;
            case VIAGEM_ID:
                return BoaViagemContract.Viagem.CONTENT_ITEM_TYPE;
            case GASTOS:
            case GASTOS_VIAGEM_ID:
                return BoaViagemContract.Gasto.CONTENT_TYPE;
            case GASTO_ID:
                return BoaViagemContract.Gasto.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri desconhecida");
        }

    }


}

