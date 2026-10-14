import org.springframework.boot.gradle.plugin.SpringBootPlugin

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
