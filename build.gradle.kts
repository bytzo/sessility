plugins {
	id("fabric-loom").version("1.4.4")
}

group = "net.bytzo"
version = "0.5.1"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:1.20.4")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:0.15.0")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

loom {
	serverOnlyMinecraftJar()
}

tasks.named<Jar>("jar") {
	from("LICENSE")
}
