package com.neverik.vman.view

import javafx.scene.control.TabPane
import tornadofx.*

class Body : View() {
    private val helpText: HelpText by inject()
    private val vmManager: VMManager by inject()

    override val root = tabpane {
        tab("Welcome!") {
            this += helpText.root
        }
        tab("Manage VMs") {
            this += vmManager.root
        }
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
    }
}