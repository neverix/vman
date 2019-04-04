package com.neverik.vman.controller

import com.neverik.vman.model.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import tornadofx.*
import javafx.collections.MapChangeListener
import javafx.collections.ObservableList

@Suppress("RedundantLambdaArrow")
object VMManagerController: Controller() {
    private val model = LinuxModel()
    val vDiskModel = LinuxVDiskModel()
    val sdModel = LinuxSDModel()

    private val vms = FXCollections.observableHashMap<Long, VM>()
    val vmValues: ObservableList<VM> = FXCollections.observableArrayList<VM>()
    val isDirty = SimpleBooleanProperty(false)

    init {
        vms.addListener { _: MapChangeListener.Change<out Long, out VM> ->
            isDirty.set(true)
            vmValues.setAll(vms.values)
        }

        vms.putAll(model.state)
        isDirty.set(false)
        cleanup()
    }

    fun removeVM(vm: VM) {
        vms.remove(vm.id)
    }

    fun save(vm: VM) {
        vms[vm.id] = vm
    }

    fun commit() {
        model.commit(vms.toMap())
        isDirty.set(false)
    }

    private fun cleanup() {
        vDiskModel.cleanup(vms.values.map { it.virtualDisk })
        sdModel.cleanup(vms.values.map { it.startupDisk }.filter { it != "" })
    }

    fun launch(vm: VM) {
        commit()
        cleanup()
        assert(vms.values.contains(vm))
        model.launch(vm)
    }
}