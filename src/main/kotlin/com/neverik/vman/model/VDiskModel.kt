package com.neverik.vman.model

abstract class VDiskModel {
    abstract fun create(disk: VDisk)
    abstract val diskTypes: Array<String>
    abstract fun getDiskPath(): String
    abstract fun cleanup(usedDiskPaths: List<String>)
    val diskExtensions get() = diskTypes.map {"*.$it"}
}