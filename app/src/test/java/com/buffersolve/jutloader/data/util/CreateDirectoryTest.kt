//package com.buffersolve.jutloader.data.util
//
//import android.os.Environment
//import io.mockk.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.io.File
//
//class CreateDirectoryTest {
//
//    private lateinit var createDirectory: CreateDirectory
//
//    @BeforeEach
//    fun setUp() {
//        createDirectory = CreateDirectory()
//    }
//
//    @Test
//    fun testCreateDirectory() {
//
//        mockkStatic(Environment::class)
//        val mockDirectory: File = mockk(relaxed = true)
////        val mockEnvironment: Environment = mockkStatic()
////        var mockDirectory: File = mockk()
////        mockDirectory.path = "dir"
////        val directory = File("dir")
//
//        every { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) } returns mockDirectory
//        every { mockDirectory.path } returns "dir"
////        every { mockDirectory.exists() } returns false
////        every { mockDirectory.mkdirs() } returns true
//        val expected = File("dir\\JutLoader")
//
//        val result = createDirectory.createDirectory()
//
//        assertEquals(expected, result)
//        verify { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) }
////        verify { mockDirectory.path }
////        verify { mockDirectory.exists() }
////        verify { mockDirectory.mkdirs() }
//    }
//
//    @Test
//    fun testCreateDirectoryDirectoryExists() {
//
//        mockkStatic(Environment::class)
//        val mockDirectory: File = mockk()
////        val directory = mockk<File>()
//        every { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) } returns mockDirectory
//        every { mockDirectory.path } returns "dir"
////        every { mockDirectory.absolutePath } returns "dir"
//        every { mockDirectory.exists() } returns true
//        val expected = File("dir\\JutLoader")
//
//        val result = createDirectory.createDirectory()
//
//        assertEquals(expected, result)
//        verify { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) }
//        verify { mockDirectory.exists() }
//        verify(exactly = 0) { mockDirectory.mkdirs() }
//    }
//}