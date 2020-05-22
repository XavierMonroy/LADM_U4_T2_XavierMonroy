package mx.edu.ittepic.ladm_u4_t2_xaviermonroy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val PHONE_CALL_REQUEST = 1
    var hiloControl : HiloControl ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_CALL_LOG),1)
        }

        obtenerLlamadas()

        hiloControl = HiloControl(this)
        hiloControl?.start()

        btnLlamar.setOnClickListener {
            llamar()
        }
    }

    private fun llamar(){
        val numero = txtTel.text.toString().trim()
        val intent = Intent(Intent.ACTION_CALL)

        intent.data = Uri.parse("tel:$numero")

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            startActivity(intent)
        } else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), PHONE_CALL_REQUEST)
        }
    }

    @SuppressLint("MissingPermission")
    fun obtenerLlamadas() {
        var cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,"date DESC")
        var arreglo = ArrayList<String>()

        if(cursor!!.moveToFirst()){
            var numeroTel = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            var tipo = cursor.getColumnIndex(CallLog.Calls.TYPE)

            do{
                var data = "Teléfono: ${cursor.getString(numeroTel)} \nTipo: ${cursor.getString(tipo)}"
                arreglo.add(data)
            }while(cursor.moveToNext())

            lista_llamadas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
        }

        else{
            var noHay = ArrayList<String>()
            var data = "NO HAY LLAMADAS RECIBIDAS"

            noHay.add(data)
            lista_llamadas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noHay)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PHONE_CALL_REQUEST){
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                llamar()
            }else{
                Toast.makeText(this,"Se debe otorgar permiso para poder llamar", Toast.LENGTH_LONG).show()
            }
        }
    }
}
