import org.springframework.boot.gradle.plugin.SpringBootPlugin

version = "0.1.0"

springBoot {
  buildInfo()
}

plugins {
  id("bifrost.java-conventions")
  alias(libs.plugins.spring.boot)
}

dependencies {
  implementation(platform(SpringBootPlugin.BOM_COORDINATES))
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation(libs.springdoc.webmvc.ui)

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}
