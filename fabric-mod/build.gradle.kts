plugins {
	id("net.fabricmc.fabric-loom").version("1.17.11")
}

group = "net.bytzo"
version = "0.7.11"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:26.2")
	implementation("net.fabricmc:fabric-loader:0.19.3")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

loom {
	serverOnlyMinecraftJar()

	runs.configureEach {
		ideConfigGenerated(true)
	}
}

tasks.named<Jar>("jar") {
	base.archivesName = "${rootProject.name}-${base.archivesName.get()}"
	from(rootDir.toPath().resolve("LICENSE.txt"))
}
