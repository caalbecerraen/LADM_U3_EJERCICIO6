package mx.tecnm.tepic.ladm_u3_ejercicio6

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var CO=BD(this,"basedatos1",null,1)
    var listaID=ArrayList<String>()
    var datos=ArrayList<String>()
    var estado=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cargaract()
        /////////////////////////////////////
        btn1Agregar.setOnClickListener {
            var ventana= Intent(this,MainActivity3::class.java)
            startActivity(ventana)
            finish()
        }
        /////////////////////////////////////
        btn1Buscar.setOnClickListener {
            consultar()
        }
        List1Act.setOnItemClickListener {adapterView,view, i, l ->mostrarAlertEliminarActualizar(i) }
    }
    private fun cargaract(){
        datos.clear()
        listaID.clear()
        var listaFecha=ArrayList<Int>()
        try{
            listaID.clear()
            var trans=CO.readableDatabase
            var eventos=ArrayList<String>()
            var prueba=trans.query("Evidencia", arrayOf("Id_actividad"),null,null,null,null,null)
            if (prueba.moveToFirst()){
                do{
                    listaFecha.add(prueba.getInt(0))
                }while (prueba.moveToNext())

            }else{
                mensaje("No hay evidencia entregada",this)
            }
            var respuesta=trans.query("Actividad", arrayOf("*"),null,null,null,null,null)
            if (respuesta.moveToFirst()){
                do{
                    var concatenacion=""
                    if(listaFecha.contains(respuesta.getInt(0))) {
                        concatenacion =
                            "Descripción: ${respuesta.getString(1)}\nFecha de Captura: ${
                                respuesta.getString(2)
                            }\nFecha de Entrega :${respuesta.getString(3)}"
                        estado.add("Fecha de Entrega :${respuesta.getString(3)}")
                    }else{
                        concatenacion =
                            "Descripción: ${respuesta.getString(1)}\nFecha de Captura: ${
                                respuesta.getString(2)
                            }\nFecha de Entrega :No ha sido entregado"
                        estado.add("Fecha de Entrega :No ha sido entregado")
                    }
                    eventos.add(concatenacion)
                    datos.add(concatenacion)
                    listaID.add(respuesta.getInt(0).toString())
                }while (respuesta.moveToNext())

            }else{
                eventos.add("NO Tienes Actividades")
            }
            List1Act.adapter=
                ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventos)
            this.registerForContextMenu(List1Act)
            trans.close()
        }catch (e: SQLiteException){mensaje("ERROR: "+e.message!!,this)}
    }
    private fun mostrarAlertEliminarActualizar(Posicion:Int){
        var idLista=listaID.get(Posicion)
        var ent=estado.get(Posicion)
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage("¿Que desea hacer con \n${datos.get(Posicion)}?")
            .setPositiveButton("ELIMINAR"){d,i->eliminar(idLista)}
            .setNeutralButton("CANCELAR"){d,i->}
            .setNegativeButton("ENTREGAR"){d,i->llamarVentanaAcualizar(idLista,ent)}
            .show()
    }
    private fun eliminar(idEliminar:String){
        try {
            var trans=CO.writableDatabase
            var listaFecha=ArrayList<Int>()
            /////////////////////
            var DelAct=trans.delete("Actividad","Id_actividad=?",
                arrayOf(idEliminar))
            var DelEv=trans.delete("Evidencia","Id_actividad=?",
                arrayOf(idEliminar))
            if (DelAct==0||DelEv==0){
                mensaje("ERROR! No se pudo elimminar",this)

            }else{
                mensaje("Se logro eliminar con éxito La actividad${idEliminar}",this)
            }
            trans.close()
            cargaract()////
        }catch (e:SQLiteException){
            mensaje(e.message!!,this)
        }
    }
    private fun llamarVentanaAcualizar(idLista:String,ent:String){
        var ventana= Intent(this,MainActivity2::class.java)
        ventana.putExtra("id",idLista)
        ventana.putExtra("ent",ent)
        mensaje(idLista,this)
        startActivity(ventana)
        finish()
    }
    private fun consultar(){
        var operacion = spinner.selectedItemPosition.toString().toInt()
        var parametro=""
        var campo=""
        when (operacion) {
            0 -> {
                //En el combo es descripcion
                parametro=txt1Descripcion.text.toString()
                campo="DESCRIPCION"
            }
            1 -> {
                //En el combo es Hora
                parametro=txt1Fecha1.text.toString()
                campo="FechaCaptura"
            }
            2 -> {
                //En el combo Fecha
                parametro=txt1Fecha2.text.toString()
                campo="FechaEntrega"
            }
            3 -> {
                // En el combo es lugar
                /*parametro=txt3Lugar.text.toString()
                campo="LUGAR"*/
            }
        }
        try{
            datos.clear()
            listaID.clear()
            var listaFecha=ArrayList<Int>()
            var trans=CO.readableDatabase
            var eventos=ArrayList<String>()
            var prueba=trans.query("Evidencia", arrayOf("Id_actividad"),null,null,null,null,null)
            if (prueba.moveToFirst()){
                do{
                    listaFecha.add(prueba.getInt(0))
                }while (prueba.moveToNext())

            }else{
                mensaje("No hay evidencia entregada",this)
            }
            var respuesta=trans.query("Actividad", arrayOf("*"),"${campo}=?", arrayOf(parametro),null,null,null)
            if (respuesta.moveToFirst()){
                do{
                    var concatenacion=""
                    if(listaFecha.contains(respuesta.getInt(0))) {
                        concatenacion =
                            "Descripción: ${respuesta.getString(1)}\nFecha de Captura: ${
                                respuesta.getString(2)
                            }\nFecha de Entrega :${respuesta.getString(3)}"
                        estado.add("Fecha de Entrega :${respuesta.getString(3)}")
                    }else{
                        concatenacion =
                            "Descripción: ${respuesta.getString(1)}\nFecha de Captura: ${
                                respuesta.getString(2)
                            }\nFecha de Entrega :No ha sido entregado"
                        estado.add("Fecha de Entrega :No ha sido entregado")
                    }
                    eventos.add(concatenacion)
                    datos.add(concatenacion)
                    listaID.add(respuesta.getInt(0).toString())
                }while (respuesta.moveToNext())

            }else{
                eventos.add("NO TIENES EVENTO")
            }
            List1Act.adapter=
                ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventos)
            this.registerForContextMenu(List1Act)
            trans.close()
        }catch (e: SQLiteException){mensaje("ERROR: "+e.message!!,this)}
    }


}