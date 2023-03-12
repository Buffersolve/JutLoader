package com.buffersolve.jutloader.data.util

import android.content.Context
import android.os.Environment
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class CreateDirectoryTest {

    @Test
    fun testCreateDirectory() {
//            val context = ApplicationProvider.getApplicationContext<Context>()
        val directory = CreateDirectory().createDirectory()
        assertTrue(directory.exists())
        assertEquals(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/JutLoader",
            directory.absolutePath
        )
        directory.delete()
    }

    @Test
    fun createDirectoryCreatesTheExpectedDirectory() {
        val createDirectory = CreateDirectory()
        val directory = createDirectory.createDirectory()

        assertEquals(
            File(
                "/storage/emulated/0/Download/JutLoader"
            ),
            directory
        )
        assertEquals(
            true,
            directory.exists()
        )
        assertEquals(
            true,
            directory.isDirectory
        )
    }

    @Test
    fun createDirectoryReturnsAnExistingDirectory() {
        val createDirectory = CreateDirectory()
        val expectedDirectory = File(
            "/storage/emulated/0/Download/JutLoader"
        )
        expectedDirectory.mkdirs()

        val directory = createDirectory.createDirectory()

        assertEquals(
            expectedDirectory,
            directory
        )
    }

}
