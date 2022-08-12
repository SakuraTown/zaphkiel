val taboolib_version: String by project

plugins {
    id("io.izzel.taboolib") version "1.42"
}

taboolib {
    install("common", "common-5", "module-nms", "module-nms-util", "module-configuration", "module-chat")
    install("module-kether", "module-ui")
    install("platform-bukkit")
    install("module-database")
    install("expansion-player-database")
    options("skip-minimize", "keep-kotlin-module", "skip-plugin-file", "skip-taboolib-relocate")
    classifier = null
    version = taboolib_version
}
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}
dependencies {
    api(project(":project:common"))
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11802:11802-minimize:mapped")
    compileOnly("ink.ptms.core:v11802:11802-minimize:universal")
    compileOnly("public:AttributePlus:3.2.6")
    compileOnly("public:HeadDatabase:1.3.0")
    compileOnly("public:Tiphareth:1.0.0")
    compileOnly("com.github.Maxlego08:zAuctionHouseV3-API:3.0.8.2")
    taboo("ink.ptms:um:1.0.0-beta9")
}