package com.neverik.vman.model

import java.io.File
import java.util.*

class LinuxVDiskModel: VDiskModel() {
    init {
        File(getLocalPathLinux("disks")).mkdirs()
    }

    override fun cleanup(usedDiskPaths: List<String>) {
        for (f in File(getLocalPathLinux("disks")).list()) {
            if(!usedDiskPaths.contains(f)) {
                File(f).delete()
            }
        }
    }

    override val diskTypes =
            arrayOf("qcow2",
                    "raw",
                    "dmg",
                    "vdi",
                    "vhdx",
                    "vmdk",
                    "vvfat"
            )

    override fun getDiskPath() =
            getLocalPathLinux("disks", UUID.randomUUID().toString())

    override fun create(disk: VDisk) {
            Runtime.getRuntime().exec(arrayOf("qemu-img", "create", "-f", disk.type, disk.path, "${(disk.size * 1024).toInt()}M")).waitFor()
    }
}