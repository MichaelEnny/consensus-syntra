rootProject.name = "ConsensusSyntra"

include("consensussyntra")
include("consensussyntra-hocon")
include("consensussyntra-metrics")
include("consensussyntra-store-sqlite")
include("consensussyntra-tutorial")
include("consensussyntra-yaml")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage") //
    repositories {
        mavenCentral()
    }
}
