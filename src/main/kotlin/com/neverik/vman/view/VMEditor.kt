package com.neverik.vman.view

import com.neverik.vman.model.SDModel
import com.neverik.vman.model.VDiskModel
import com.neverik.vman.model.VM
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.stage.FileChooser
import tornadofx.*

class VMEditor(private val vm: VM,
               private val formName: String,
               private val vDiskModel: VDiskModel,
               private val sdModel: SDModel,
               private val save: (VM) -> Unit) : Fragment(formName) {
    override val root = form {
        fieldset(formName) {
            var name: StringProperty = SimpleStringProperty(vm.name)
            field("Name") {
                name = textfield(vm.name).textProperty()
            }

            val virtualDisk: StringProperty = SimpleStringProperty(vm.virtualDisk)
            field ("Virtual disk") {
                textfield(virtualDisk)
                button("Create virtual disk...").action {
                    VDCreator(vDiskModel, virtualDisk).openWindow()
                }
                button("Choose virtual disk...").action {
                    val chosenFile = chooseFile("Virtual disk",
                            arrayOf(
                                    FileChooser.ExtensionFilter(
                                            "Virtual disk images",
                                            vDiskModel.diskExtensions),
                                    FileChooser.ExtensionFilter(
                                            "All files",
                                            "*"
                                    )))
                    if(chosenFile.isNotEmpty()) {
                        val virtualDiskFile = chosenFile[0]
                        virtualDisk.set(virtualDiskFile.absolutePath)
                    }
                }
            }

            val startupDisk = SimpleStringProperty(vm.startupDisk)
            field("Startup disk") {
                textfield(startupDisk)
                button("No startup disk").action {
                    startupDisk.set("")
                }
                button("Download/Choose downloaded startup disk...").action {
                    SDDownloader(startupDisk, sdModel).openWindow()
                }
                button("Start up from file...").action {
                    val chosenFile = chooseFile("Startup disk",
                            arrayOf(
                                FileChooser.ExtensionFilter("Disk Images", "*.img", "*.iso")))
                    if(chosenFile.isNotEmpty()) {
                        val startupDiskFile = chosenFile[0]
                        startupDisk.set(startupDiskFile.absolutePath)
                    }
                }
            }

            var ramSize: DoubleProperty = SimpleDoubleProperty(1.0)
            field("RAM size (GB)") {
                slider(0 until 5, 0.1) {
                    ramSize = valueProperty()
                    isShowTickMarks = true
                    isShowTickLabels = true
                }
            }

            hbox(20) {
                button("Save").action {
                    vm.virtualDisk = virtualDisk.get()
                    vm.name = name.get()
                    vm.startupDisk = startupDisk.get()
                    vm.ramSize = ramSize.get()
                    save(vm)
                    close()
                }
                button("Cancel").action {
                    close()
                }
            }
        }

        style {
            minWidth = 600.px
        }
    }

    fun open() {
        openWindow()
    }
}
