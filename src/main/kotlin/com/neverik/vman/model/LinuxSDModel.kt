package com.neverik.vman.model


class LinuxSDModel: SDModel() {
    override val basePath = getLocalPathLinux("startups")

    init {
        initialize()
    }

    override fun getFileForDisk(name: String, version: String) =
            getLocalPathLinux("startups", name, version)
}