package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.activity_update_vehicle.*

class UpdateVehicle : AppCompatActivity() {
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vehicle)
        this.setTitle("Actualizando Vehiculo");
        var extra = intent.extras
        id = extra!!.getString("idUpdateVehicle")!! //Debe de llamarse igual que como lo nombramos en el otro activity

        //Recuperamos la data de los extras
        val vehicle = Vehicle(this).search(id)
        licencePlateUpdate.setText(vehicle.plate)
        brandUpdate.setText(vehicle.brand)
        modelUpdate.setText(vehicle.model)
        yearUpdate.setText(vehicle.year)

        btn_updateVehicleDriver.setOnClickListener {
            val vehicleUpdate = Vehicle(this)
            vehicleUpdate.plate = licencePlateUpdate.text.toString()
            vehicleUpdate.brand = brandUpdate.text.toString()
            vehicleUpdate.model = modelUpdate.text.toString()
            vehicleUpdate.year = yearUpdate.text.toString()

            val result = vehicleUpdate.update(id)
            if (result){
                Toast.makeText(this,"SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                cleanUpdateVehicleText()
            }else{
                Toast.makeText(this,"NO SE ACTUALIZO", Toast.LENGTH_LONG).show()
            }
        }

        btn_backVehicleDriver.setOnClickListener {
            finish()
        }
    }

    private fun cleanUpdateVehicleText(){
        licencePlateUpdate.setText("")
        brandUpdate.setText("")
        modelUpdate.setText("")
        yearUpdate.setText("")
    }
}