package mx.tecnm.tepic.ladm_u3_ejercicio6

import android.content.Context
import androidx.appcompat.app.AlertDialog

public fun mensaje(s:String,c: Context){
    AlertDialog.Builder(c)
        .setTitle("ATENCIÓN")
        .setMessage(s)
        .setPositiveButton("OK"){d,i->d.dismiss()}
        .show()
}