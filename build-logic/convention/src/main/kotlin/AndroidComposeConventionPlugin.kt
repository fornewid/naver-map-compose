import org.gradle.api.Plugin
import org.gradle.api.Project
import com.naver.maps.map.compose.buildlogic.configureCompose

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureCompose()
        }
    }
}
