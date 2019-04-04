package com.neverik.vman.view

import javafx.scene.Parent
import tornadofx.*

class Footer: View() {
    override val root: Parent = label ("Copyright Stepan Shabalin, 2019") {
        id = Styles.footer.name
    }
}