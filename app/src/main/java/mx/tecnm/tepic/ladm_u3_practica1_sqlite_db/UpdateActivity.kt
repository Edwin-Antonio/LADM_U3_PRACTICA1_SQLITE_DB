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
    }

    private fun listResultUpdate(){
        // El tipo de dato final de result es de tipo ArrayList<String>
        val result = Vehicle(this).request(id)
        listsVehicle.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,result)
        //idVehicle.clear()
        //idVehicle = Driver(this).getIds()
        //eventAction(listsVehicle)
    }
    private fun cleanUpdateText(){
        updateName.setText("")
        updateAddress.setText("")
        updateNumber.setText("")
        updateDate.setText("")
    }

    private fun updateVehicle(id: Int){
        val caller2 = Intent(this,VehicleActivity::class.java)
        caller2.putExtra("idUpdateDriver",id)
        startActivity(caller2)
    }

}