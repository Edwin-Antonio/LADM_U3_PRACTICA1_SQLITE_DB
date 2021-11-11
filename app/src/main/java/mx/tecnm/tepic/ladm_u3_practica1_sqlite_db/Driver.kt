package mx.tecnm.tepic.ladm_u3_practica1_sqlite_db

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileWriter
import java.lang.Exception

class Driver(driver: Context) {
    var act = driver
    var name = ""
    var address = ""
    var licNumber = ""
    var dueDate = ""

    fun insert() : Boolean{
        val drivTable = DataBase(act,"API10",null,1).writableDatabase
        var data = ContentValues()
        data.put("nombre",name)
        data.put("domicilio",address)
        data.put("nolicencia",licNumber)
        data.put("vence",dueDate)
        val result = drivTable.insert("CONDUCTOR",null,data)
        //Insert ID > 0 numero de renglon insertado = SI SE PUDO
        // si regresa -1 LONG (Entero largo) = NO SE PUDO
        drivTable.close()
        if (result == -1L) return false
        return true
    }

    fun request() : ArrayList<String>{
        //Cursor = objeto que tiene el resulado de un select
        val drivTable = DataBase(act,"API10",null,1).readableDatabase
        val queryResult = java.util.ArrayList<String>()
        //SELECT * FROM CONDUCTOR
        val cursor = drivTable.query("CONDUCTOR", arrayOf("*"), null,null,null,null,null)
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

    fun getIds() : ArrayList<Int>{
        //Cursor = objeto que tiene el resulado de un select
        val drivTable = DataBase(act,"API10",null,1).readableDatabase
        val queryResult = java.util.ArrayList<Int>()
        //SELECT * FROM CONDUCTOR
        val cursor = drivTable.query("CONDUCTOR", arrayOf("*"), null,null,null,null,null)
        if (cursor.moveToFirst()){ //Se mueve a la primer posición del select y si hay un dato returna un true
            do {
                queryResult.add(cursor.getInt(0))
            }while (cursor.moveToNext())
        }
        drivTable.close()
        return queryResult
    }

    fun delete(id: Int ) : Boolean{
        val drivTable = DataBase(act,"API10",null,1).writableDatabase
        val result = drivTable.delete("CONDUCTOR","ID_CONDUCTOR=?", arrayOf(id.toString()))
        if(result == 0){
            return false
        }
        return true
    }

    // Necesarios para cuando queramos hacer un UPDATE

    fun search(id: String) : Driver{
        val drivTable = DataBase(act,"API10",null,1).readableDatabase
        val cursor = drivTable.query("CONDUCTOR", arrayOf("*"),"ID_CONDUCTOR=?", arrayOf(id),null,null,null)
        val driver = Driver(MainActivity())
        if(cursor.moveToFirst()){
            driver.name = cursor.getString(1)
            driver.address = cursor.getString(2)
            driver.licNumber = cursor.getString(3)
            driver.dueDate = cursor.getString(4)
        }
        return driver
    }

    fun update(id: String) : Boolean{
        val drivTable = DataBase(act,"API10",null,1).writableDatabase
        val data = ContentValues()
        data.put("nombre",name)
        data.put("domicilio",address)
        data.put("nolicencia",licNumber)
        data.put("vence",dueDate)
        val result = drivTable.update("CONDUCTOR",data,"ID_CONDUCTOR=?", arrayOf(id))
        //Update regresa en forma entera la cantidad de espacios que se actualizaron ID > 0 numero de renglon actualizado = SI SE PUDO
        // si regresa 0 entonces no actualizo = NO SE PUDO
        drivTable.close()
        if (result == 0) return false
        return true
        return true
    }

    fun export() : Boolean{
        val folder = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/SQLite")
        if(!folder.exists()){
            folder.mkdir()
        }
        //Nombre del archivo
        val fileName = "conductores.csv" //csv es para archivo de tipo excel
        val fileNameAndPath = "$folder/$fileName"

        try {
            //Creando el archivo
            val documents = FileWriter(fileNameAndPath)

            // Abriendo la base de datos
            val drivTable = DataBase(act,"API10",null,1).readableDatabase
            val cursor = drivTable.query("CONDUCTOR", arrayOf("*"),null,null,null,null,null)
            if(cursor.moveToFirst()){
                do {
                    documents.append("${cursor.getString(0)}")
                    documents.append(",")
                    documents.append("${cursor.getString(1)}")
                    documents.append(",")
                    documents.append("${cursor.getString(2)}")
                    documents.append(",")
                    documents.append("${cursor.getString(3)}")
                    documents.append(",")
                    documents.append("${cursor.getString(4)}")
                    documents.append(",")
                }while (cursor.moveToNext())
            }
            drivTable.close()
            documents.close()
            //Toast.makeText(context,"NO SE PUDO EXPORTAR EL ARCHIVO",Toast.LENGTH_LONG).show()
            return true
        }catch (e: Exception){
            //Toast.makeText(context,"NO SE PUDO EXPORTAR EL ARCHIVO",Toast.LENGTH_LONG).show()
            return false
        }
    }
}