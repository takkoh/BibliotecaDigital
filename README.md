# Biblioteca Digital Manga (MangaLibrary)

Aplicación móvil nativa para Android desarrollada en **Kotlin** que permite buscar, explorar, leer y gestionar mangas favoritos utilizando la API pública de **MangaDex**.

---

## 📱 Características Principales

- 🎨 **Alternancia de Tema Modo Claro / Modo Oscuro**:
  - Cambio en tiempo real entre **Modo Claro** ☀️ y **Modo Oscuro** 🌙 mediante el botón vectorial de la barra superior, con persistencia automática en `SharedPreferences`.
- 🔍 **Búsqueda e Inicio de Descubrimiento**:
  - **Pantalla de Inicio**: Panel dedicado al descubrimiento con carruseles horizontales de **🔥 Tendencias y Populares** y **⚡ Últimos Actualizados**.
  - **Pantalla de Búsqueda Dedicada (`FragmentBusqueda`)**: Acceso rápido desde la barra de navegación inferior (*Bottom Navigation*), búsqueda por título, menú desplegable de Estado y chip indicador de **Tag Activo**.
- 📖 **Lector Interno Dual (Páginas y Webtoon) e Inmersión**:
  - Visor interactivo por páginas (`ViewPager2`) o desplazamiento vertical continuo (**Modo Webtoon** con `RecyclerView`).
  - **Zoom y Paneo Táctil (`PhotoView`)**: Gestos avanzados de acercamiento (*pinch-to-zoom*) y desplazamiento en las páginas de lectura.
  - **Contador Dinámico en Webtoon**: Indicador flotante actualizado en tiempo real durante el desplazamiento vertical continuo.
  - **Modo Inmersivo de Pantalla Completa**: Ocultado/mostrado de las barras del sistema y controles de lectura al tocar cualquier página.
  - Navegación fluida entre capítulos (`Anterior` / `Siguiente`), nombres de traductores (*Scanlation Groups*) y selector dinámico de idiomas por capítulo.
- 🖼️ **Iconos Gráficos Personalizados (`drawable/`)**:
  - Barra de navegación inferior decorada con recursos gráficos propios (`ic_inicio`, `ic_search`, `ic_recomendacion`, `ic_cuenta`) y vector de tema `ic_brightness_medium`.
- 🏷️ **Navegación Global por Tags/Géneros**:
  - Al hacer clic en cualquier etiqueta o género en la app, se abre automáticamente la búsqueda prefiltrada por dicha etiqueta.
- 🎯 **Motor de Recomendaciones Inteligente**: Algoritmo propio local que calcula la similitud entre mangas basado en géneros (+4 pts), etiquetas (+3 pts), autor (+4 pts) y estado/clasificación (+1 pt).
- ⭐ **Biblioteca de Favoritos en Cuadrícula**:
  - Visualización en cuadrícula de 4 columnas (`GridLayoutManager`) con tarjetas prominentes y portadas de alta calidad (`MaterialCardView`).
  - Almacenamiento local persistente en `SharedPreferences` (JSON con Gson).
- 👤 **Perfil de Usuario e Historial de Lectura**:
  - Formulario de edición de datos del perfil (`EditarPerfilActivity`) estilizado con componentes de **Material 3** (`TextInputLayout.OutlinedBox`, `MaterialCardView`).
  - Historial automático de lectura ("Continuar Leyendo") para reanudar el último capítulo visto.

---

## 🛠️ Tecnologías y Arquitectura

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel) + Repository Pattern
- **UI & Layouts**: XML Layouts, ViewBinding, Material Components 3 (`Theme.Material3.DayNight.NoActionBar`, `BottomNavigationView`, `MaterialCardView`, `ExposedDropdownMenu`, `ChipGroup`, `TextInputLayout`, `ViewPager2`, `PhotoView`)
- **Red & API**: Retrofit 2 + Gson Converter + OkHttp Logging Interceptor
- **Concurrencia**: Corrutinas de Kotlin + StateFlow + Lifecycle Scope
- **Carga de Imágenes**: Coil (Caché optimizada en RAM 25% y Disco 250MB)
- **Persistencia Local**: SharedPreferences + Gson
- **Calidad de Código & CI/CD**: GitHub Actions Actions CI con análisis estático Gradle Lint (`lintDebug`)

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
│   │   ├── MainActivity.kt            (Soporte de cambio de Tema Claro/Oscuro)
│   │   ├── MangaLibraryApp.kt         (Caché Coil e inicialización de Tema)
│   │   ├── Fragmentos/
│   │   │   ├── FragmentInicio.kt          (Descubrimiento: Tendencias y Recientes)
│   │   │   ├── FragmentBusqueda.kt        (Búsqueda dedicada, filtro de estado y tag activo)
│   │   │   ├── FragmentDetalleManga.kt    (Detalle con idiomas dinámicos, tags interactivos y orden Asc/Desc)
│   │   │   ├── FragmentMisFavoritos.kt    (Cuadrícula de 4 columnas con tarjetas prominentes)
│   │   │   ├── FragmentRecomendaciones.kt
│   │   │   └── FragmentCuenta.kt          (Perfil de usuario)
│   │   ├── data/
│   │   │   ├── api/MangaDexApi.kt
│   │   │   ├── repository/MangaDexRepository.kt
│   │   │   ├── model/MangaModels.kt
│   │   │   └── local/ (ReadingHistoryManager, FavoritesManager)
│   │   ├── domain/
│   │   │   └── RecommendationEngine.kt
│   │   └── ui/
│   │       ├── EditarPerfilActivity.kt    (Formulario Material 3 de Edición de Perfil)
│   │       ├── LectorActivity.kt          (Lector dual: Webtoon, Inmersivo)
│   │       ├── MangaUiState.kt
│   │       ├── MangaViewModel.kt
│   │       └── adapters/
│   │           ├── ChapterAdapter.kt
│   │           ├── MangaAdapter.kt
│   │           ├── MangaCarouselAdapter.kt
│   │           ├── ReaderPageAdapter.kt
│   │           └── RecommendationAdapter.kt
│   └── res/
│       ├── drawable/                      (Iconos gráficos y vectores XML personalizados)
│       │   ├── ic_brightness_medium.xml
│       │   ├── ic_inicio.png
│       │   ├── ic_search.png
│       │   ├── ic_recomendacion.png
│       │   └── ic_cuenta.png
│       ├── layout/
│       │   ├── activity_editar_perfil.xml (Diseño del formulario Material 3)
│       │   ├── activity_lector.xml
│       │   ├── fragment_busqueda.xml
│       │   ├── fragment_cuenta.xml
│       │   ├── fragment_manga_detail.xml
│       │   ├── fragment_manga_search.xml
│       │   ├── item_chapter.xml
│       │   ├── item_manga_carousel.xml
│       │   └── ...
│       ├── menu/mi_menu.xml               (Barra de navegación inferior vinculada a drawable/)
│       └── values/
└── docs/
    ├── CHANGELOG.md                       (Registro histórico acumulativo de cambios)
    └── IMPLEMENTATION_REPORT.md           (Informe técnico por fases de implementación)
```
