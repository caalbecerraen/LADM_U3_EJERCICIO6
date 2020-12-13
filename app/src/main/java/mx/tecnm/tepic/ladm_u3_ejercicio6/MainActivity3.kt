package mx.tecnm.tepic.ladm_u3_ejercicio6

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main3.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity3 : AppCompatActivity() {
    var CO=BD(this,"basedatos1",null,1)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        ///////////////////////////////////////////////////////////BOTON INSERTAR
        btn3Ins.setOnClickListener {
            insertar()
        }
        //////////////////////////////////////////////////////////BOTON REGRESAR
        btn3reg.setOnClickListener {
            var ventana= Intent(this,MainActivity::class.java)
            startActivity(ventana)
            finish()
        }
    }
    ////////////////////////////////////////////////////////////INSERTAR
    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertar(){
        try {
            var trans=CO.writableDatabase
            var variables= ContentValues()
            variables.put("DESCRIPCION",txt3inst.text.toString())
            variables.put("FechaCaptura", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            variables.put("FechaEntrega", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            var respuesta =trans.insert("Actividad",null,variables)
            if(respuesta==-1L){
                mensaje("FALLO AL INSERTAR",this)
            }else{
                mensaje("INSERCION EXITOSA",this)
                var ventana= Intent(this,MainActivity::class.java)
                startActivity(ventana)
                finish()
            }
            trans.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!,this)
        }

    }
}