package mx.tecnm.tepic.ladm_u3_ejercicio6

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toIcon
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*
import java.io.ByteArrayInputStream
import java.net.URI
import java.sql.Blob
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity2 : AppCompatActivity() {
    var CO=BD(this,"basedatos1",null,1)
    private val SELECT_ACTIVITY = 50
    var n=0
    private var imageUri: Uri?=null
    var datos=ArrayList<Bitmap>()
    var bd = BD(this, "basedatos1", null, 1)
    var id = ""
    var ent = ""
    var Desc=""
    var Fecha=""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        rellenarcampos()
        imageView.setOnClickListener {
            ImageController.selectPhotoFromGallery(this,SELECT_ACTIVITY)
        }
        ///////////////////////////////////////////////////////////btn Regresar/////////////////////////////////////
        btn2Reg.setOnClickListener {
            var ventana = Intent(this, MainActivity::class.java)
            startActivity(ventana)
            finish()
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        var extra = intent.extras
        id = extra!!.getString("id")!!

        btn2Ingr.setOnClickListener {
            insertar()
        }
        btn2sig.setOnClickListener{
            n++
            if(datos.size<=n){n=0}
            imageView.setImageBitmap(datos.get(n))
    }
    }

    private fun rellenarcampos() {
        var extra = intent.extras
        id = extra!!.getString("id")!!
        ent = extra!!.getString("ent")!!
        try {
            var base = bd.readableDatabase
            var respuesta = base.query(
                "Actividad", arrayOf("*"), "Id_actividad=?",
                arrayOf(id), null, null, null, null
            )
            if (respuesta.moveToFirst()) {
                vt2Actid.setText("Actividad: " + respuesta.getString(0))
                vt2Descripcion.setText("Descripcion: " + respuesta.getString(1))
                vt2Fech1.setText("Fecha Captura: " + respuesta.getString(2))
                vt2Fech2.setText(ent)
                Fecha=respuesta.getString(2)
                Desc=respuesta.getString(1)
            } else {
                mensaje("ERROR! no se encontro ID", this)
            }
            base.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!, this)
        }
        try {
            var base = bd.readableDatabase
            var respuesta = base.query(
                "Evidencia", arrayOf("*"), "Id_actividad=?",
                arrayOf(id), null, null, null, null)
            if (respuesta.moveToFirst()) {
                do {
                    var photo=ByteArrayInputStream(respuesta.getBlob(2))
                    var pf=BitmapFactory.decodeStream(photo)
                    imageView.setImageBitmap(pf)
                    datos.add(pf)
                }while (respuesta.moveToNext())
            } else {
                mensaje("ERROR! no se encontro ID", this)
            }
            base.close()
        } catch (e: SQLiteException) {
            mensaje(e.message!!, this)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode==SELECT_ACTIVITY && resultCode==Activity.RESULT_OK->{
                imageUri=data!!.data
                imageView.setImageURI(imageUri)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertar(){
        if(imageUri==null){
            mensaje("No ha seleccionado evidencia",this)
            return}
        try {
            var trans=CO.writableDatabase
            var variables= ContentValues()
            variables.put("DESCRIPCION",Desc)
            variables.put("FechaCaptura",Fecha.format(DateTimeFormatter.ofPattern("dd/mm/yy")))
            variables.put("FechaEntrega",LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))

            var respuesta=trans.update("Actividad",variables,"Id_actividad=?", arrayOf(id))
            if(respuesta>0){
                mensaje("Se actualizo correctamente",this)
            }else{

            }
            trans.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!,this)
        }
        try {
            var trans=CO.writableDatabase
            var varimg=ContentValues()
            varimg.put("Id_actividad",id.toInt())
            val bytes=this.contentResolver.openInputStream(imageUri!!)?.readBytes()!!
            varimg.put("Foto",bytes)
            var img=trans.insert("Evidencia",null,varimg)
            if(img==-1L){
                mensaje("Fallo al insertar",this)
            }else{
                mensaje("Insercion exitosa",this)
            }
            trans.close()
        }catch (e: SQLiteException){
            mensaje(e.message!!,this)
        }
        var ventana= Intent(this,MainActivity::class.java)
        startActivity(ventana)
        finish()
    }
}