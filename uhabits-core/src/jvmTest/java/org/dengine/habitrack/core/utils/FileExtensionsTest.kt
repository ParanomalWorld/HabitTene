package org.dengine.habitrack.core.utils

import org.dengine.habitrack.core.BaseUnitTest
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class FileExtensionsTest : BaseUnitTest() {

    @Test
    fun testIsSQLite3File() {
        val file = File.createTempFile("asset", "")
        copyAssetToFile("loop.db", file)
        val isSqlite3File = file.isSQLite3File()
        assertTrue(isSqlite3File)
    }
}
