plugins {
	id("fabric-loom").version("1.7.4")
}

group = "net.bytzo"
version = "0.6.4"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:1.21.3")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:0.16.7")
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
