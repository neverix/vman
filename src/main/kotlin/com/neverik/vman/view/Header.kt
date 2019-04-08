package com.neverik.vman.view

import tornadofx.*

class Header: View() {
    override val root = borderpane {
        left = label("VMan")
        right = hbox {
            button("EN")
            button("RU")
            id = Styles.languagePicker.name
        }
        id = Styles.header.name
    }
}