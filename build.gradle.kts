plugins {
	id("fabric-loom").version("1.6.12")
}

group = "net.bytzo"
version = "0.6"

repositories {
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:1.21")
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
}

tasks.named<Jar>("jar") {
	from("LICENSE")
}
