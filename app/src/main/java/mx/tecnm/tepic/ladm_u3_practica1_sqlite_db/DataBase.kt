package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
//Se ejecuta cuando se instala la app en el cel y corre por primera vez
        //En Kotlin se puede hacer un CRUD de 2 maneras distintas, mediante un QUERY o mediante metodos
        db.execSQL("CREATE TABLE CONDUCTOR( ID_CONDUCTOR INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR (100), DOMICILIO VARCHAR(100), NOLICENCIA VARCHAR(100), VENCE DATE)")
        db.execSQL("CREATE TABLE VEHICULO(ID_VEHICULO INTEGER PRIMARY KEY AUTOINCREMENT, PLACA VARCHAR(100), MARCA VARCHAR(100), MODELO VARCHAR(100), ANIO VARCHAR(5), ID_CONDUCTOR INTEGER REFERENCES CONDUCTOR(ID_CONDUCTOR) )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}