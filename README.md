# Biblioteca Digital Manga (MangaLibrary)

Aplicación móvil nativa para Android desarrollada en **Kotlin** que permite buscar, explorar, leer y gestionar mangas favoritos utilizando la API pública de **MangaDex**.

---

## 📱 Características Principales

- 🔍 **Búsqueda y Catálogo de Mangas**: Búsqueda por título y exploración del catálogo de MangaDex.
- 📖 **Lector Interno de Capítulos**: Visor interactivo (`ViewPager2`) para leer las páginas de los capítulos servidos a través de la API MangaDex At-Home.
- 🎯 **Motor de Recomendaciones Inteligente**: Algoritmo propio local que calcula la similitud entre mangas basado en géneros (+4 pts), etiquetas (+3 pts), autor (+4 pts) y estado/clasificación (+1 pt).
- ⭐ **Gestor de Favoritos**: Almacenamiento local persistente en `SharedPreferences` (JSON con Gson).
- 👤 **Perfil de Usuario y Edición**: Visualización y actualización de los datos del perfil guardados localmente en `SharedPreferences`.

---

## 🛠️ Tecnologías y Arquitectura

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel) + Repository Pattern
- **UI & Layouts**: XML Layouts, ViewBinding, Material Components (`BottomNavigationView`, `ChipGroup`, `TextInputLayout`, `ViewPager2`)
- **Red & API**: Retrofit 2 + Gson Converter + OkHttp Logging Interceptor
- **Concurrencia**: Corrutinas de Kotlin + StateFlow + Lifecycle Scope
- **Carga de Imágenes**: Coil
- **Persistencia Local**: SharedPreferences + Gson

---

## 📋 Requisitos Previos

- **Android Studio**: Ladybug / Jellyfish o superior
- **JDK**: Java 17 o 21
- **Android SDK**: `compileSdk = 35`, `minSdk = 24`, `targetSdk = 35`
- Dispositivo Android físico o emulador con API 24 (Android 7.0) o superior.

---

## 🚀 Compilación y Ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/USUARIO/BibliotecaDigital.git
   cd BibliotecaDigital
   ```

2. Abre el proyecto en **Android Studio**.

3. Compila la aplicación desde la terminal o usando Gradle:
   ```bash
   ./gradlew assembleDebug
   ```

4. Ejecuta la aplicación en un dispositivo o emulador conectado desde Android Studio (botón **Run 'app'**).

---

## 📁 Estructura del Proyecto

```text
app/src/main/
├── java/com/mangalibrary/
│   ├── Constantes.kt
│   ├── MainActivity.kt
│   ├── MangaLibraryApp.kt
│   ├── Fragmentos/
│   │   ├── FragmentCuenta.kt
│   │   ├── FragmentDetalleManga.kt
│   │   ├── FragmentInicio.kt
│   │   ├── FragmentMisFavoritos.kt
│   │   └── FragmentRecomendaciones.kt
│   ├── data/
│   │   ├── api/MangaDexApi.kt
│   │   ├── local/FavoritesManager.kt
│   │   ├── model/MangaModels.kt
│   │   └── repository/MangaDexRepository.kt
│   ├── domain/
│   │   └── RecommendationEngine.kt
│   └── ui/
│       ├── EditarPerfilActivity.kt
│       ├── LectorActivity.kt
│       ├── MangaViewModel.kt
│       └── adapters/
│           ├── ChapterAdapter.kt
│           ├── MangaAdapter.kt
│           ├── ReaderPageAdapter.kt
│           └── RecommendationAdapter.kt
└── res/
    ├── layout/
    ├── menu/
    └── values/
```
