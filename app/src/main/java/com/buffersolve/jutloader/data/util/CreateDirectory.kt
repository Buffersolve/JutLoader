package com.buffersolve.jutloader.data.util

import android.os.Environment
import java.io.File

class CreateDirectory {

    fun createDirectory(): File {

        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "JutLoader"
        )
        if (!directory.exists()) directory.mkdirs()

        return directory
    }

}