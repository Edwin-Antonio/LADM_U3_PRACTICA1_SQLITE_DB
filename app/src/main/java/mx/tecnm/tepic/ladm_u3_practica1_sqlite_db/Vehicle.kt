package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.lang.Exception

class Vehicle(vehicle: Context) {
    var act = vehicle
    var plate = ""
    var brand = ""
    var model = ""
    var year = ""
    var id_driver = ""

    fun request(id: String) : ArrayList<String>{ // id: String
        //Cursor = objeto que tiene el resulado de un select
        val drivTable = DataBase(act,"API11",null,1).readableDatabase
        val queryResult = java.util.ArrayList<String>()
        //SELECT * FROM CONDUCTOR                                               //ID_CONDUCTOR=? arrayOf(id)
        val cursor = drivTable.query("VEHICULO", arrayOf("*"), "ID_CONDUCTOR=?", arrayOf(id),null,null,null)
        if (cursor.moveToFirst()){ //Se mueve a la primer posición del select y si hay un dato returna un true
            var datas = ""
            do {
                datas = cursor.getString(1)+" - "+cursor.getString(2)+"\n"+
                        cursor.getString(3)+" - "+cursor.getString(4)
                queryResult.add(datas)
            }while (cursor.moveToNext())
        }else{
            queryResult.add("NO HAY DATOS EN LA SOLICITUD")
        }
        drivTable.close()
        return queryResult
    }

    fun insert() : Boolean{
        val vehicleTable = DataBase(act,"API11",null,1).writableDatabase
        var data = ContentValues()
        data.put("PLACA",plate)
        data.put("MARCA",brand)
        data.put("MODELO",model)
        data.put("ANIO",year)
        data.put("ID_CONDUCTOR",id_driver.toInt())
        val result = vehicleTable.insert("VEHICULO",null,data)
        //Insert ID > 0 numero de renglon insertado = SI SE PUDO
        // si regresa -1 LONG (Entero largo) = NO SE PUDO
        vehicleTable.close()
        if (result == -1L) return false
        return true
    }

    fun getIds(id: String) : ArrayList<Int>{
        //Cursor = objeto que tiene el resulado de un select
        val vehicleTable = DataBase(act,"API11",null,1).readableDatabase
        val queryResult = java.util.ArrayList<Int>()
        //SELECT * FROM VHICULO
        val cursor = vehicleTable.query("VEHICULO", arrayOf("*"), "ID_CONDUCTOR=?", arrayOf(id),null,null,null)
        if (cursor.moveToFirst()){ //Se mueve a la primer posición del select y si hay un dato returna un true
            do {
                queryResult.add(cursor.getInt(0))
            }while (cursor.moveToNext())
        }
        vehicleTable.close()
        return queryResult
    }

    fun search(id: String) : Vehicle{
        val vehicleTable = DataBase(act,"API11",null,1).readableDatabase
        val cursor = vehicleTable.query("VEHICULO", arrayOf("*"),"ID_VEHICULO=?", arrayOf(id),null,null,null)
        val vehicle = Vehicle(MainActivity())
        if(cursor.moveToFirst()){
            vehicle.plate = cursor.getString(1)
            vehicle.brand = cursor.getString(2)
            vehicle.model = cursor.getString(3)
            vehicle.year = cursor.getString(4)
            vehicle.id_driver = cursor.getString(5)
        }
        return vehicle
    }

    fun update(id: String) : Boolean{
        val vehicleTable = DataBase(act,"API11",null,1).writableDatabase
        val data = ContentValues()
        data.put("PLACA",plate)
        data.put("MARCA",brand)
        data.put("MODELO",model)
        data.put("ANIO",year)
        val result = vehicleTable.update("VEHICULO",data,"ID_VEHICULO=?", arrayOf(id))
        //Update regresa en forma entera la cantidad de espacios que se actualizaron ID > 0 numero de renglon actualizado = SI SE PUDO
        // si regresa 0 entonces no actualizo = NO SE PUDO
        vehicleTable.close()
        if (result == 0) return false
        return true
    }

    fun delete(id: Int ) : Boolean{
        val drivTable = DataBase(act,"API11",null,1).writableDatabase
        val result = drivTable.delete("VEHICULO","ID_VEHICULO=?", arrayOf(id.toString()))
        if(result == 0){
            drivTable.close()
            return false
        }
        drivTable.close()
        return true
    }

    fun export() : Boolean{
        val folder = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/SQLite")
        if(!folder.exists()){
            folder.mkdir()
        }
        //Nombre del archivo
        val fileName = "vehiculos.csv" //csv es para archivo de tipo excel
        val fileNameAndPath = "$folder/$fileName"

        try {
            //Creando el archivo
            val documents = FileWriter(fileNameAndPath)

            // Abriendo la base de datos
            val vehicleTable = DataBase(act,"API11",null,1).readableDatabase
            val cursor = vehicleTable.query("VEHICULO", arrayOf("*"),null,null,null,null,null)
            if(cursor.moveToFirst()){
                do {
                    documents.append("ID: ${cursor.getString(0)} ")
                    documents.append(",")
                    documents.append("Placas: ${cursor.getString(1)} ")
                    documents.append(",")
                    documents.append("Marca: ${cursor.getString(2)} ")
                    documents.append(",")
                    documents.append("Modelo: ${cursor.getString(3)} ")
                    documents.append(",")
                    documents.append("Anio:  ${cursor.getString(4)} ")
                    documents.append(",")
                    documents.append("Id_Conductor: ${cursor.getString(5)} ")
                }while (cursor.moveToNext())
            }
            vehicleTable.close()
            documents.close()
            //Toast.makeText(context,"NO SE PUDO EXPORTAR EL ARCHIVO",Toast.LENGTH_LONG).show()
            return true
        }catch (e: Exception){
            //Toast.makeText(context,"NO SE PUDO EXPORTAR EL ARCHIVO",Toast.LENGTH_LONG).show()
            return false
        }
    }
}