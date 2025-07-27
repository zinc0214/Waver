dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
rootProject.name = "Waver"
include(":app")
include(":domain")
include(":data")
include(":common")
include(":datastore")
include(":feature:ui-common")
include(":feature:ui-my")
include(":feature:ui-feed")
include(":feature:ui-more")
include(":feature:ui-write")
include(":feature:ui-other")
include(":feature:ui-search")
include(":feature:ui-detail")
include(":feature:ui-report")
