package com.neverik.vman.view

import tornadofx.*

class HelpText : View() {
    override val root = vbox {
        id = Styles.body.name
        label("Welcome to VMan!\n") { addClass(Styles.heading) }
        label("""
            Things you can do:
            - download images;
            - configure virtual environtments;
            - run other OSes.
        """.trimIndent())
    }
}