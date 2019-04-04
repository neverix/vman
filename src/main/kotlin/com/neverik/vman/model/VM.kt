package com.neverik.vman.model
import java.util.*

data class VM(
        var name: String,
        var virtualDisk: String,
        var startupDisk: String,
        var ramSize: Double,
        val id: Long = UUID.randomUUID().mostSignificantBits)
