subprojects {
  apply(plugin = "java")
  repositories { mavenCentral() }
  extensions.configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
  }
  dependencies {
    "testImplementation"(platform("org.junit:junit-bom:5.11.0"))
    "testImplementation"("org.junit.jupiter:junit-jupiter")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
  }
  tasks.withType<Test> { useJUnitPlatform() }
}
