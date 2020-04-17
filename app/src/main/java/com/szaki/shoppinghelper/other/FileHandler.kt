package com.szaki.shoppinghelper.other

import android.content.Context
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.szaki.shoppinghelper.R
import java.io.*

class FileHandler(val context: Context) {

    private lateinit var fis: FileInputStream
    private lateinit var ois: ObjectInputStream
    private lateinit var fos: FileOutputStream
    private lateinit var oos: ObjectOutputStream
    val fbstorage = FirebaseStorage.getInstance().reference

    fun <E> readArrayList(name: String): ArrayList<E> {
        var items: ArrayList<E>
        try {
            fis = context.openFileInput(name)
            ois = ObjectInputStream(fis)
            items = ois.readObject() as ArrayList<E>
            ois.close()
            fis.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            items = ArrayList()
            writeArrayList(items, name)
        }
        return items
    }

    fun <E> writeArrayList(list: ArrayList<E>, name: String) {
        fos = context.openFileOutput(name, Context.MODE_PRIVATE)
        oos = ObjectOutputStream(fos)
        oos.writeObject(list)
        oos.close()
        fos.close()
    }

    fun uploadList(name: String) {
        val uploadTask = fbstorage.child(name).putStream(context.openFileInput(name))
        uploadTask.addOnFailureListener {
            it.printStackTrace()
            Toast.makeText(context, context.getString(R.string.uploaderror), Toast.LENGTH_LONG).show()
        }
    }

    fun downloadList(name: String, func: () -> Unit) {
        val dl = fbstorage.child(name).getFile(File(context.filesDir, name))
        dl.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.downloaderror), Toast.LENGTH_LONG).show()
        }
        dl.addOnCompleteListener {
            func()
        }
    }
}