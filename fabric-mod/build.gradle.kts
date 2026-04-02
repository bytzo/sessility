plugins {
	id("net.fabricmc.fabric-loom").version("1.15.5")
}

group = "net.bytzo"
version = "0.7.8"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:26.1.1")
	implementation("net.fabricmc:fabric-loader:0.18.4")
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
