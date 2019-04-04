package com.neverik.vman.view

import tornadofx.*

class Header: View() {
    override val root = label("VMan") {
        id = Styles.header.name
    }
}