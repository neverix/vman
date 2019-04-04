package com.neverik.vman.view

import com.neverik.vman.model.*
import javafx.beans.property.*
import javafx.stage.FileChooser
import tornadofx.*

class VDCreator(private val vDiskModel: VDiskModel, private val path: StringProperty): Fragment("Create virtual disk") {
    override val root = form {
        fieldset("Create virtual disk") {
            var diskSize: DoubleProperty = SimpleDoubleProperty(1.0)
            field("Disk size (GB)") {
                slider(0 until 51, 1.0) {
                    diskSize = valueProperty()
                    isShowTickMarks = true
                    isShowTickLabels = true
                }
            }

            val type = SimpleStringProperty(vDiskModel.diskTypes[0])
            field("Path") {
                textfield(path)
                button("Pick automatically...").action {
                    path.set(vDiskModel.getDiskPath())
                }
                button("Choose virtual disk path...").action {
                    val chosenFile = chooseFile("Virtual disk",
                            arrayOf(
                                    FileChooser.ExtensionFilter(
                                            "Virtual disk images",
                                            vDiskModel.diskExtensions),
                                    FileChooser.ExtensionFilter(
                                            "All files",
                                            "*"
                                    )),
                            FileChooserMode.Save)
                    if(chosenFile.isNotEmpty()) {
                        val virtualDiskFile = chosenFile[0]
                        path.set(virtualDiskFile.absolutePath)
                    }
                }
            }

            field("Type") {
                combobox<String>(type, vDiskModel.diskTypes.toList())
            }

            hbox(20) {
                button("Create") {
                    action {
                        val vDisk = VDisk(diskSize.get(), path.get(), type.get())
                        runAsync {
                            vDiskModel.create(vDisk)
                        } ui {
                            close()
                        }
                    }
                }

                button("Cancel").action {
                    close()
                }
            }
        }
    }
}