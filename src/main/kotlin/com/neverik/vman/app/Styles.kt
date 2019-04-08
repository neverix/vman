import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val header by cssid()
        val footer by cssid()
        val body by cssid()
        val heading by cssclass()
        val vmManager by cssid()
        val languagePicker by cssid()
    }

    init {
        header {
            padding = box(10.px)
            fontSize = 30.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#edeff3")
            and(languagePicker) {
                fontSize = 15.px
                padding = box(0.px)
            }
        }
        footer {
            padding = box(5.px)
            fontSize = 20.px
            minWidth = 100.percent
            and(label) {
                alignment = Pos.BASELINE_RIGHT
            }
            backgroundColor += c("#edeff3")
        }
        body {
            padding = box(20.px)
            fontSize = 15.px
            backgroundColor += c("#edeff3")
        }
        vmManager {
            padding = box(15.px)
            fontSize = 15.px
            backgroundColor += c("#edeff3")
        }
        heading {
            fontSize = 25.px
            fontWeight = FontWeight.BOLD
            padding = box(7.px)
            backgroundColor += c("#edeff3")
        }
    }
}
