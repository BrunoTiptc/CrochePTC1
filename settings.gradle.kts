// Configuração de plugins (como Kotlin, Android Gradle Plugin, etc.)
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// Resolução de dependências centralizada para todos os módulos
dependencyResolutionManagement {
    // Impede repositórios declarados nos build.gradle.kts dos módulos
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Repositórios usados pelo projeto inteiro
    repositories {
        google()
        mavenCentral()
    }
}

// Nome do projeto e módulos incluídos
rootProject.name = "CrochePTC"
include(":app")
