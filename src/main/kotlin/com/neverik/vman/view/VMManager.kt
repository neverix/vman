package com.neverik.vman.view

import com.neverik.vman.controller.VMManagerController
import com.neverik.vman.model.VM
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.paint.Color
import tornadofx.*

class VMManager : View() {
    private val vmManagerController = VMManagerController
    var selectedVM: VM? = null
    private val disabler = SimpleBooleanProperty(true)
    private lateinit var clearer: () -> Unit

    override val root = vbox {
        label("VM Management") { addClass(Styles.heading.name) }

        borderpane {
            isFillWidth = true
            top {
                hbox(10) {
                    hbox(10) {
                        disableWhen(disabler)
                        disabler.set(true)

                        button("Launch").action {
                            vmManagerController.launch(selectedVM!!)
                        }

                        button("Edit").action {
                                VMEditor(
                                        selectedVM!!.copy(),
                                        "Edit VM",
                                        vmManagerController.vDiskModel, vmManagerController.sdModel) {
                                    vmManagerController.save(it)
                                }.open()
                            }
                        button("Remove") {
                            textFill = Color.RED

                            action {
                                vmManagerController.removeVM(selectedVM!!)
                                selectedVM = null
                                clearer()
                                disabler.set(true)
                            }
                        }
                    }
                    button("New"). action {
                            VMEditor(VM(
                                    "Cool VM",
                                    "",
                                    "",
                                    100.0),
                                    "Create VM",
                                    vmManagerController.vDiskModel,
                                    vmManagerController.sdModel) {
                                vmManagerController.save(it)
                            }.open()
                        }

                    button("Save") {
                        disableWhen(vmManagerController.isDirty.not())
                        action {
                            vmManagerController.commit()
                        }
                    }

                    style {
                        padding = box(10.px)
                    }
                }
            }
            left {
                isFillWidth = true
                datagrid(vmManagerController.vmValues) {
                    isFillWidth = true
                    cellWidth = 120.0
                    cellHeight = 120.0

                    cellCache {
                        label(it.name)
                    }

                    selectionModel.selectedItemProperty().onChange {
                        selectedVM = it
                        disabler.set(false)
                    }

                    clearer = selectionModel::clearSelection
                }
            }
        }

        id = Styles.vmManager.name
    }
}