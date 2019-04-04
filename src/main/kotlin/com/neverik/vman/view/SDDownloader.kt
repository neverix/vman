package com.neverik.vman.view

import com.neverik.vman.model.SDModel
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.StringProperty
import tornadofx.*


class SDDownloader(startupDisk: StringProperty, sdModel: SDModel): Fragment("Download/Choose startup disk") {
    override val root = vbox {
        label("Disks").addClass(Styles.heading)
            datagrid(sdModel.disks.keys.toList()) {
                cellCache {
                    label(it)
                }

                selectionModel.selectedItemProperty().onChangeOnce {
                    if(it != null ) {
                        val prop = SimpleDoubleProperty(0.0)
                        val canceller: ObjectProperty<() -> Unit> = SimpleObjectProperty {}
                        this@vbox.vbox {
                            label("Download in progress...")
                            progressbar(prop).fitToParentWidth()
                            button("Cancel") {
                                action {
                                    canceller.get()()
                                }
                                fitToParentWidth()
                            }
                        }
                        runAsync {
                            startupDisk.set(sdModel.download(it, prop, canceller))
                        } ui {
                            close()
                        }
                    }
                }
            }
        }
}