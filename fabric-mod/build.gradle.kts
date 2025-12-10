plugins {
	id("net.fabricmc.fabric-loom-remap").version("1.14.5")
}

group = "net.bytzo"
version = "0.7.7"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:1.21.11")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:0.18.2")
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
