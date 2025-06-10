plugins {
    id("buildlogic.java-common-conventions")
    application
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
