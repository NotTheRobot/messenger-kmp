import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import org.company.app.App
import org.company.app.di.KoinInit
import org.koin.core.Koin
import org.koin.core.context.startKoin

lateinit var koin: Koin
fun main() {

    koin = KoinInit().initKoin()
    koin.loadModules(
        listOf(),
    )
    return application {
        Window(
            title = "FirstMultiplatfporm",
            state = rememberWindowState(width = 1440.dp, height = 900.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(360, 600)
            App()
        }
    }
}
