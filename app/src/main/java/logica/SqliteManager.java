package logica;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by RSMAPP on 16/12/2015.
 */
public class SqliteManager {

    private SqliteDBHelper helper;
    private SQLiteDatabase db;
    //tablas
public static final String tablaMensajes="Mensajes";
public static final String tablaConversacion="Conversacion";

    //atributos tabla mensaje

    public static final String id_m="_id";
    public static final String Adjunto="adjunto";
    public static final String Conversacion="conversacion";
    public static final String Texto="texto";
    public static final String Ubicacion="ubicacion";
    public static final String Autor="autor";


//atributos tabla conversacion

    public static final String id_c="_id";
    public static final String Usuario1="usuario1";
    public static final String Usuario2="usuario2";

//atributos parse
public static final String ParseId="parseid";
    public static final String Creado="creado";
    public static final String modificado="modificado";

//query to create conversation store Table
    public static final String Crear_Tabla_conversaciones="create table "+tablaConversacion+" ("+
            id_c+" integer primary key autoincrement,"+Usuario1+" text not null,"+Usuario2+" text not null,"+
            ParseId+" text not null,"+Creado+" text not null);";

    //query to create mensajes store Table
    public static final String Crear_Tabla_mensajes="create table "+tablaMensajes+" ("+
            id_m+" integer primary key autoincrement,"+Autor+" text not null,"+Adjunto+" text,"+Conversacion+" text not null,"+Texto+" text,"+
            ParseId+" text not null,"+Ubicacion+" text,"+Creado+" text not null);";

//obvious is the constructor =)
    public SqliteManager(Context context) {
      helper=new SqliteDBHelper(context);
        db=helper.getWritableDatabase();
    }

    // metodo para crear contendor
    private ContentValues generarContentValues(String autor,String adjunto,String conversa,String msj,String prsId,String ubica,String creacion  ) {

        ContentValues valores = new ContentValues();
       valores.put(Autor,autor);
       valores.put(Adjunto ,adjunto);
       valores.put(Conversacion,conversa);
       valores.put(Texto,msj);
       valores.put(ParseId,prsId);
       valores.put(Ubicacion,ubica);
       valores.put(Creado,creacion);
        return valores;
    }

    //metodo agregar
    public void GuardarMensajes(String tabla,String autor,String adjunto,String conversa,String msj,String prsId,String ubica,String creacion )
    {
          db.insert(tabla, null, generarContentValues(autor, adjunto, conversa, msj, prsId, ubica, creacion));
    }
    //metodo agregar
    public void GuardarConversaciones()
    {

    }
    public void eliminar(String tabla)
    { //delete all registers of a specific table
        db.delete(tabla,null,null);

    }



}
