package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.activity_vehicle.*

class VehicleActivity : AppCompatActivity() {
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle)

        var extra = intent.extras
        id = extra!!.getString("idUpdateDriver")!! //Debe de llamarse igual que como lo nombramos en el otro activity

        licencePlate.setText(id)
        btn_updateVehicle.setOnClickListener {
            val vehicleUpdate = Vehicle(this)
            vehicleUpdate.plate = licencePlate.text.toString()
            vehicleUpdate.brand = brand.text.toString()
            vehicleUpdate.model = model.text.toString()
            vehicleUpdate.year = year.text.toString()
            vehicleUpdate.id_driver = id

            val result = vehicleUpdate.insert()
            if (result){
                Toast.makeText(this,"SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                cleanVehicleText()
            }else{
                Toast.makeText(this,"NO SE ACTUALIZO", Toast.LENGTH_LONG).show()
            }
        }
        btn_backDriver.setOnClickListener {
            finish()
        }
    }

    private fun cleanVehicleText(){
        updateName.setText("")
        updateAddress.setText("")
        updateNumber.setText("")
        updateDate.setText("")
    }
}