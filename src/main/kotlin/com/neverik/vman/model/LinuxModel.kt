package com.neverik.vman.model

import tornadofx.*

class LinuxModel: ConfigModel() {
    override fun launch(vm: VM) {
        runAsync {
            var command = arrayOf(
                    "qemu-system-x86_64",
                    "-enable-kvm",
                    "-name",
                    vm.name,
                    "-hda",
                    vm.virtualDisk,
                    "-m",
                    "${(vm.ramSize * 1024).toInt()}M"
            )

            if(vm.startupDisk != "") {
                command += arrayOf(
                        "-boot",
                        "d",
                        "-cdrom",
                        vm.startupDisk
                )
            }

            Runtime.getRuntime().exec(command)
        }
    }

    override val vDiskModel: VDiskModel = LinuxVDiskModel()
}