package com.neverik.vman.model

interface Model {
    val state: Map<Long, VM>
    fun commit(vms: Map<Long, VM>)
    fun launch(vm: VM)

    val vDiskModel: VDiskModel
}