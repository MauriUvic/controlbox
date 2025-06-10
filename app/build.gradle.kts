plugins {
    id("buildlogic.java-application-conventions")
}

dependencies {
    implementation(project(":jdbc"))
    implementation(project(":model"))
    implementation(project(":repositories"))
    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("com.github.freva:ascii-table:1.8.0")
}

application {
    mainClass.set("cat.uvic.teknos.dam.controlbox.main.App")

}
