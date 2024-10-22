plugins {
	id("fabric-loom").version("1.7.4")
}

group = "net.bytzo"
version = "0.6.2"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:1.21.1")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:0.15.11")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
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
