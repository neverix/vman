package com.neverik.vman.model

import java.nio.file.Paths

fun getLocalPathLinux(vararg suffixes: String): String {
    return Paths.get(System.getProperty("user.home"), ".vman", *suffixes).toAbsolutePath().toString()
}