plugins {
    id("org.jetbrains.intellij") version "0.4.16" apply false
}

group = "net.sf.intelliplugin"
version = "1.2"


subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.intellij")

    repositories {
        mavenCentral()
    }

}
