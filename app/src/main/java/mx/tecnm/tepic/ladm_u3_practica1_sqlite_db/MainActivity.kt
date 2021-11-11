package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var idDriver = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setTitle("Formulario de Trabajadores");
        listResult()
        btn_insert.setOnClickListener {
            val driver = Driver(this)
            driver.name = name.text.toString()
            driver.address = address.text.toString()
            driver.licNumber = number.text.toString()
            driver.dueDate = date.text.toString()

            val result = driver.insert()
            if (result){
                Toast.makeText(this,"SE CAPTURARON LOS DATOS", Toast.LENGTH_LONG).show()
                cleanText()
                listResult()
            }else{
                Toast.makeText(this,"NO SE CAPTURARON LOS DATOS", Toast.LENGTH_LONG).show()
            }
        }

        btn_export.setOnClickListener {
            if(Driver(this).export()){
                Toast.makeText(this,"¡¡ARCHIVO EXPORTADO CON EXITO!!",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"NO SE PUDO EXPORTAR EL ARCHIVO",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun eventAction(driverList : ListView){
        driverList.setOnItemClickListener { parent, view, position, id ->
            val selectedId = idDriver[position]
            AlertDialog.Builder(this)
                .setTitle("ATENCIÓN")
                .setMessage("¿Que se desea hacer con el conductor seleccionado?"+"\n\n"+ "${driverList.getItemAtPosition(position)}")
                .setPositiveButton("INSPECCIONAR"){d, i-> updateDriver(selectedId)}
                .setNegativeButton("ELIMINAR"){d,i-> deleteDriver(selectedId)}
                .setNeutralButton("CANCELAR"){d,i-> d.cancel()}
                .show()
        }
    }

    private fun deleteDriver(id: Int){
        AlertDialog.Builder(this)
            .setTitle("!ADVERTENCIA¡")
            .setMessage("¿Seguro de que deseas eliminar este usuario?")
            .setPositiveButton("SI"){d, i->
                val result = Driver(this).delete(id)
                if(result){
                    Toast.makeText(this,"SE ELIMINO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                    listResult()
                }else{
                    Toast.makeText(this,"NO SE LOGRO BORRAR", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }

    private fun updateDriver(id: Int){
        val caller = Intent(this,UpdateActivity::class.java)
        caller.putExtra("idUpdate",id.toString())
        startActivity(caller)
        AlertDialog.Builder(this)
                .setMessage("¿Deseas actualizar la lista?")
                .setPositiveButton("SI"){d, i-> listResult()}
                .setNegativeButton("NO"){d,i-> d.cancel()}
                .show()
    }

    private fun listResult(){
        // El tipo de dato final de result es de tipo ArrayList<String>
        val result = Driver(this).request()
        lists.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,result)
        idDriver.clear()
        idDriver = Driver(this).getIds()
        eventAction(lists)
    }

    fun cleanText(){
        name.setText("")
        address.setText("")
        number.setText("")
        date.setText("")
    }
}