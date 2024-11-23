import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.naver.maps.map.compose.buildlogic.configureAndroid
import com.naver.maps.map.compose.buildlogic.configureKotlin
import com.naver.maps.map.compose.buildlogic.implementation
import com.naver.maps.map.compose.buildlogic.project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            configureAndroid()
            configureKotlin()
        }
    }
}
