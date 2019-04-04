import com.neverik.vman.view.*
import tornadofx.*

class MainView : View("VMan") {
    private val header: Header by inject()
    private val body: Body by inject()
    private val footer: Footer by inject()

    override val root = borderpane {
        top = header.root
        center = body.root
        bottom = footer.root
    }
}