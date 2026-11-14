# Biblioteca Digital Manga (MangaLibrary)

Aplicación móvil nativa para Android desarrollada en **Kotlin** que permite buscar, explorar, leer y gestionar mangas favoritos utilizando la API pública de **MangaDex**.

---

## 📱 Características Principales

- 🔍 **Búsqueda e Inicio de Descubrimiento**:
  - **Pantalla de Inicio**: Panel dedicado al descubrimiento con carruseles horizontales de **🔥 Tendencias y Populares** y **⚡ Últimos Actualizados**.
  - **Pantalla de Búsqueda Dedicada (`FragmentBusqueda`)**: Acceso rápido desde la barra de navegación inferior (*Bottom Navigation*), búsqueda por título, menú desplegable de Estado y chip indicador de **Tag Activo**.
- 📖 **Lector Interno Dual (Páginas y Webtoon)**:
  - Visor interactivo por páginas (`ViewPager2`) o desplazamiento vertical continuo (**Modo Webtoon** con `RecyclerView`).
  - Navegación fluida entre capítulos (`Anterior` / `Siguiente`), nombres de traductores (*Scanlation Groups*) y selector dinámico de idiomas por capítulo.
- 🏷️ **Navegación Global por Tags/Géneros**:
  - Al hacer clic en cualquier etiqueta o género en la app, se abre automáticamente la búsqueda prefiltrada por dicha etiqueta.
- 🎯 **Motor de Recomendaciones Inteligente**: Algoritmo propio local que calcula la similitud entre mangas basado en géneros (+4 pts), etiquetas (+3 pts), autor (+4 pts) y estado/clasificación (+1 pt).
- ⭐ **Biblioteca de Favoritos en Cuadrícula**:
  - Visualización en cuadrícula de 4 columnas (`GridLayoutManager`) con tarjetas prominentes y portadas de alta calidad (`MaterialCardView`).
  - Almacenamiento local persistente en `SharedPreferences` (JSON con Gson).
- 👤 **Perfil de Usuario e Historial de Lectura**:
  - Edición de datos del perfil guardados en `SharedPreferences`.
  - Historial automático de lectura ("Continuar Leyendo") para reanudar el último capítulo visto.

---

## 🛠️ Tecnologías y Arquitectura

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel) + Repository Pattern
- **UI & Layouts**: XML Layouts, ViewBinding, Material Components 3 (`BottomNavigationView`, `MaterialCardView`, `ExposedDropdownMenu`, `ChipGroup`, `TextInputLayout`, `ViewPager2`)
- **Red & API**: Retrofit 2 + Gson Converter + OkHttp Logging Interceptor
- **Concurrencia**: Corrutinas de Kotlin + StateFlow + Lifecycle Scope
- **Carga de Imágenes**: Coil (Caché optimizada en RAM 25% y Disco 250MB)
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
   git clone https://github.com/takkoh/BibliotecaDigital.git
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
BibliotecaDigital/
├── app/src/main/
│   ├── java/com/mangalibrary/
│   │   ├── Constantes.kt
│   │   ├── MainActivity.kt
│   │   ├── MangaLibraryApp.kt
│   │   ├── Fragmentos/
│   │   │   ├── FragmentInicio.kt          (Descubrimiento: Tendencias y Recientes)
│   │   │   ├── FragmentBusqueda.kt        (Búsqueda dedicada, filtro de estado y tag activo)
│   │   │   ├── FragmentDetalleManga.kt    (Detalle con idiomas dinámicos, tags interactivos y orden Asc/Desc)
│   │   │   ├── FragmentMisFavoritos.kt    (Cuadrícula de 4 columnas con tarjetas prominentes)
│   │   │   ├── FragmentRecomendaciones.kt
│   │   │   └── FragmentCuenta.kt
│   │   ├── data/
│   │   │   ├── api/MangaDexApi.kt
│   │   │   ├── repository/MangaDexRepository.kt
│   │   │   ├── model/MangaModels.kt
│   │   │   └── local/ (ReadingHistoryManager, FavoritesManager)
│   │   ├── domain/
│   │   │   └── RecommendationEngine.kt
│   │   └── ui/
│   │       ├── EditarPerfilActivity.kt
│   │       ├── LectorActivity.kt          (Lector dual: Páginas + Webtoon vertical)
│   │       ├── MangaUiState.kt
│   │       ├── MangaViewModel.kt
│   │       └── adapters/
│   │           ├── ChapterAdapter.kt
│   │           ├── MangaAdapter.kt
│   │           ├── MangaCarouselAdapter.kt
│   │           ├── ReaderPageAdapter.kt
│   │           └── RecommendationAdapter.kt
│   └── res/
│       ├── layout/
│       │   ├── activity_lector.xml
│       │   ├── fragment_busqueda.xml
│       │   ├── fragment_manga_detail.xml
│       │   ├── fragment_manga_search.xml
│       │   ├── item_chapter.xml
│       │   ├── item_manga_carousel.xml
│       │   └── ...
│       ├── menu/mi_menu.xml               (Barra de navegación inferior con acceso a Búsqueda)
│       └── values/
└── docs/
    ├── CHANGELOG.md                       (Registro histórico acumulativo de cambios)
    └── IMPLEMENTATION_REPORT.md           (Informe técnico por fases de implementación)
```
