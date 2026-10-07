import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.20"
    id("com.gradleup.shadow") version "9.6.1"
    `maven-publish`
}

group = "me.awabi2048"
version = "26.1007.1"

repositories {
    mavenLocal()
    maven { url = uri("../.m2-paper26-kotlin2320") }
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.inventivetalent.org/repository/public/")
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

// pom.xml の provided スコープ相当（compileOnly へ配置し、テストのコンパイル・実行双方で見えるよう testImplementation にも追加）
val providedDeps = listOf(
    "io.papermc.paper:paper-api:26.1.2.build.72-stable",
    "org.jetbrains.kotlin:kotlin-stdlib:2.3.20",
)

dependencies {
    providedDeps.forEach {
        compileOnly(it)
        testImplementation(it)
    }
    // gson・guava は Paper サーバーが提供するため、MineSkin client の推移依存から外して同梱しない
    implementation("org.mineskin:java-client:3.2.6") {
        exclude(group = "com.google.code.gson")
        exclude(group = "com.google.guava")
    }
    implementation("org.mineskin:java-client-java11:3.2.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.3.20")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
}

tasks.test {
    useJUnitPlatform()
}

// pom.xml の resources filtering 相当（${project.version} を展開）
tasks.processResources {
    val versionString = project.version.toString()
    inputs.property("projectVersion", versionString)
    filesMatching("plugin.yml") {
        expand(mapOf("project" to mapOf("version" to versionString)))
    }
}

tasks.shadowJar {
    archiveBaseName.set("CustomHeadPainter")
    archiveClassifier.set("")
    // 他プラグインが別バージョンの MineSkin client を同梱していても衝突しないよう退避する
    relocate("org.mineskin", "me.awabi2048.customheadpainter.libs.mineskin")
    // pom.xml の shade フィルタ相当（依存JAR含め署名とマニフェストを除外）
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/MANIFEST.MF")
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            from(components["shadow"])
        }
    }
    repositories {
        maven {
            name = "workspace"
            url = uri("../.m2-paper26-kotlin2320")
        }
    }
}
