package mx.tecnm.tepic.ladm_u3_ejercicio6
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.xml.transform.TransformerFactory

class BD(
    context: Context?,
    name:String?,
    factory:SQLiteDatabase.CursorFactory?,
    version:Int
):SQLiteOpenHelper(context,name,factory,version){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Actividad(Id_actividad INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, DESCRIPCION VARCHAR(2000),FechaCaptura DATE,FechaEntrega DATE)")
        db.execSQL("CREATE TABLE Evidencia(Id_Evidencias INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Id_actividad INTEGER REFERENCES Actividad(Id_actividad),Foto BLOB)")
    }
    //Lugar Hora Fecha Descripcion
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}



}