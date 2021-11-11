package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.activity_update.listsVehicle

class UpdateActivity : AppCompatActivity() {
    var id = ""
    var idVehicle = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        this.setTitle("Inspeccionando Trabajador");
        var extra = intent.extras
        id = extra!!.getString("idUpdate")!! //Debe de llamarse igual que como lo nombramos en el otro activity

        //Recuperamos la data de los extras
        val driver = Driver(this).search(id)
        updateName.setText(driver.name)
        updateAddress.setText(driver.address)
        updateNumber.setText(driver.licNumber)
        updateDate.setText(driver.dueDate)

        listResultUpdate()
        btn_update.setOnClickListener {
            val driverUpdate = Driver(this)
            driverUpdate.name = updateName.text.toString()
            driverUpdate.address = updateAddress.text.toString()
            driverUpdate.licNumber = updateNumber.text.toString()
            driverUpdate.dueDate = updateDate.text.toString()

            val result = driverUpdate.update(id)
            if (result){
                Toast.makeText(this,"SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                cleanUpdateText()
            }else{
                Toast.makeText(this,"NO SE ACTUALIZO", Toast.LENGTH_LONG).show()
            }
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_vehicleInsert.setOnClickListener {
            val caller2 = Intent(this,VehicleActivity::class.java)
            caller2.putExtra("idUpdateDriver",id)
            startActivity(caller2)
            AlertDialog.Builder(this)
                .setMessage("¿Deseas actualizar la lista?")
                .setPositiveButton("SI"){d, i-> listResultUpdate()}
                .setNegativeButton("NO"){d,i-> d.cancel()}
                .show()
        }

        btn_exportVehicles.setOnClickListener {
            if(Vehicle(this).export()){
                Toast.makeText(this,"¡¡ARCHIVO EXPORTADO CON EXITO!!",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"NO SE PUDO EXPORTAR EL ARCHIVO",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun listResultUpdate(){
        // El tipo de dato final de result es de tipo ArrayList<String>
        val result = Vehicle(this).request(id)
        listsVehicle.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,result)
        idVehicle.clear()
        idVehicle = Vehicle(this).getIds(id)
        eventAction(listsVehicle)
    }
    private fun cleanUpdateText(){
        updateName.setText("")
        updateAddress.setText("")
        updateNumber.setText("")
        updateDate.setText("")
    }

    private fun updateVehicle(id: Int){
        val caller3 = Intent(this,UpdateVehicle::class.java)
        caller3.putExtra("idUpdateVehicle",id.toString())
        startActivity(caller3)
        AlertDialog.Builder(this)
            .setMessage("¿Deseas actualizar la lista?")
            .setPositiveButton("SI"){d, i-> listResultUpdate()}
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }


    private fun eventAction(vehicleList : ListView){
        vehicleList.setOnItemClickListener { parent, view, position, id ->
            val selectedId = idVehicle[position]
            AlertDialog.Builder(this)
                .setTitle("ATENCIÓN")
                .setMessage("¿Que se desea hacer con el vehiculo seleccionado?"+"\n\n"+ "${vehicleList.getItemAtPosition(position)}")
                .setPositiveButton("EDITAR"){d, i-> updateVehicle(selectedId)}
                .setNegativeButton("ELIMINAR"){d,i-> deleteVehicle(selectedId)}
                .setNeutralButton("CANCELAR"){d,i-> d.cancel()}
                .show()
        }
    }

    private fun deleteVehicle(id: Int){
        AlertDialog.Builder(this)
            .setTitle("!ADVERTENCIA¡")
            .setMessage("¿Seguro de que deseas eliminar este usuario?")
            .setPositiveButton("SI"){d, i->
                val result = Vehicle(this).delete(id)
                if(result){
                    Toast.makeText(this,"SE ELIMINO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                    listResultUpdate()
                }else{
                    Toast.makeText(this,"NO SE LOGRO BORRAR", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("NO"){d,i-> d.cancel()}
            .show()
    }

}