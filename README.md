# Biblioteca Digital Manga (MangaLibrary)

Aplicacion movil nativa para Android desarrollada en **Kotlin** que permite buscar, explorar, leer y gestionar mangas favoritos utilizando la API publica de **MangaDex**.

---

## Caracteristicas Principales

- **Alternancia de Tema Modo Claro / Modo Oscuro**:
  - Cambio en tiempo real entre Modo Claro y Modo Oscuro mediante el boton vectorial de la barra superior, con persistencia automatica en `SharedPreferences`.
- **Busqueda e Inicio de Descubrimiento**:
  - **Pantalla de Inicio**: Panel dedicado al descubrimiento con carruseles horizontales de Tendencias, Populares y Ultimos Actualizados.
  - **Pantalla de Busqueda Dedicada (`FragmentBusqueda`)**: Acceso rapido desde la barra de navegacion inferior, busqueda por titulo, menu desplegable de Estado y chip indicador de Tag Activo.
- **Lector Interno Dual (Paginas y Webtoon) e Inmersion**:
  - Visor interactivo por paginas (`ViewPager2`) o desplazamiento vertical continuo (Modo Webtoon con `RecyclerView`).
  - **Zoom y Paneo Tactil (`PhotoView`)**: Gestos avanzados de acercamiento y desplazamiento en las paginas de lectura.
  - **Contador Dinamico en Webtoon**: Indicador flotante actualizado en tiempo real durante el desplazamiento vertical continuo.
  - **Modo Inmersivo de Pantalla Completa**: Ocultado y mostrado de las barras del sistema y controles de lectura al tocar cualquier pagina.
  - Navegacion fluida entre capitulos (`Anterior` / `Siguiente`), nombres de traductores y selector dinamico de idiomas por capitulo.
- **Iconos Graficos Personalizados (`drawable/`)**:
  - Barra de navegacion inferior decorada con recursos graficos propios y vector de tema `ic_brightness_medium`.
- **Navegacion Global por Tags y Generos**:
  - Al hacer clic en cualquier etiqueta o genero en la aplicacion, se abre automaticamente la busqueda prefiltrada por dicha etiqueta.
- **Motor de Recomendaciones Inteligente**: Algoritmo propio local que calcula la similitud entre mangas basado en generos, etiquetas, autor y estado o clasificacion.
- **Biblioteca de Favoritos**:
  - Visualizacion en lista vertical organizada con tarjetas prominentes y portadas de alta calidad (`MaterialCardView`).
  - Almacenamiento local persistente en `SharedPreferences` (JSON con Gson).
- **Perfil de Usuario e Historial de Lectura**:
  - Formulario de edicion de datos del perfil (`EditarPerfilActivity`) estilizado con componentes de Material 3.
  - Historial automatico de lectura ("Continuar Leyendo") para reanudar el ultimo capitulo visto.

---

## Tecnologias y Arquitectura

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel) + Repository Pattern
- **UI & Layouts**: XML Layouts, ViewBinding, Material Components 3 (`Theme.Material3.DayNight.NoActionBar`, `BottomNavigationView`, `MaterialCardView`, `ExposedDropdownMenu`, `ChipGroup`, `TextInputLayout`, `ViewPager2`, `PhotoView`)
- **Red & API**: Retrofit 2 + Gson Converter + OkHttp Logging Interceptor
- **Concurrencia**: Corrutinas de Kotlin + StateFlow + Lifecycle Scope
- **Carga de Imagenes**: Coil (Cache optimizada en RAM 25% y Disco 250MB)
- **Persistencia Local**: SharedPreferences + Gson
- **Calidad de Codigo & CI/CD**: GitHub Actions CI con analisis estatico Gradle Lint (`lintDebug`)

---

## Requisitos Previos

- **Android Studio**: Ladybug / Jellyfish o superior
- **JDK**: Java 17 o 21
- **Android SDK**: `compileSdk = 35`, `minSdk = 24`, `targetSdk = 35`
- Dispositivo Android fisico o emulador con API 24 (Android 7.0) o superior.

---

## Compilacion y Ejecucion

1. Clona el repositorio:
   ```bash
   git clone https://github.com/takkoh/BibliotecaDigital.git
   cd BibliotecaDigital
   ```

2. Abre el proyecto en **Android Studio**.

3. Compila la aplicacion desde la terminal o usando Gradle:
   ```bash
   ./gradlew assembleDebug
   ```

4. Ejecuta la aplicacion en un dispositivo o emulador conectado desde Android Studio (boton **Run 'app'**).

---

## Estructura del Proyecto

```text
BibliotecaDigital/
├── app/src/main/
│   ├── java/com/mangalibrary/
│   │   ├── Constantes.kt
│   │   ├── MainActivity.kt            (Soporte de cambio de Tema Claro/Oscuro)
│   │   ├── MangaLibraryApp.kt         (Cache Coil e inicializacion de Tema)
│   │   ├── Fragmentos/
│   │   │   ├── FragmentInicio.kt          (Descubrimiento: Tendencias y Recientes)
│   │   │   ├── FragmentBusqueda.kt        (Busqueda dedicada, filtro de estado y tag activo)
│   │   │   ├── FragmentDetalleManga.kt    (Detalle con idiomas dinamicos, tags interactivos y orden Asc/Desc)
│   │   │   ├── FragmentMisFavoritos.kt    (Lista vertical de favoritos con tarjetas prominentes)
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
│   │       ├── EditarPerfilActivity.kt    (Formulario Material 3 de Edicion de Perfil)
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
│       ├── drawable/                      (Iconos graficos y vectores XML personalizados)
│       ├── layout/
│       │   ├── activity_editar_perfil.xml (Diseno del formulario Material 3)
│       │   ├── activity_lector.xml
│       │   ├── fragment_busqueda.xml
│       │   ├── fragment_cuenta.xml
│       │   ├── fragment_manga_detail.xml
│       │   ├── fragment_manga_search.xml
│       │   ├── item_chapter.xml
│       │   ├── item_manga_carousel.xml
│       │   └── ...
│       ├── menu/mi_menu.xml               (Barra de navegacion inferior vinculada a drawable/)
│       └── values/
└── docs/
    ├── CHANGELOG.md                       (Registro historico acumulativo de cambios)
    └── IMPLEMENTATION_REPORT.md           (Informe tecnico por fases de implementacion)
```
