plugins {
  java
  jacoco
  id("com.diffplug.spotless")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

spotless {
  java {
    googleJavaFormat()
    removeUnusedImports()
    trimTrailingWhitespace()
    endWithNewline()
  }
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.11.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
  useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  reports { xml.required = true; html.required = true }
}
