rootProject.name = "excel-utils"

module("excel-utils-reader")
module("excel-utils-writer")
module("example")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

fun module(name: String) {
    include(name)
}