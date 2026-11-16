# Registro de Cambios (CHANGELOG) - Biblioteca Digital Manga

Documento historico y detallado de todas las modificaciones, correcciones de merge/compilacion y nuevas funcionalidades implementadas en la aplicacion nativa para Android en Kotlin.

---

## Version Actual / Ultimas Mejoras (Colores Profesionales, Modo Oscuro Funcional y Correccion de Favoritos)

### Nuevas Funcionalidades, Mejoras y Correcciones
1. **`app/src/main/res/values/colors.xml` & `app/src/main/res/values-night/colors.xml`**
   - **Paleta Profesional y Modo Oscuro**: Restauracion de los colores profesionales clasicos de la aplicacion (`#6200EE` primary, `#03DAC6` secondary, `#F6F7FB` background, `#FFFFFF` surface) y configuracion robusta para modo oscuro (`values-night/colors.xml` y `MangaLibraryApp.kt`) siguiendo las preferencias del sistema y el selector interno.
2. **`app/src/main/java/com/mangalibrary/Fragmentos/FragmentMisFavoritos.kt`**
   - **Correccion de Layout de Favoritos**: Cambio de `GridLayoutManager` a `LinearLayoutManager` vertical utilizando `MangaAdapter` (`item_manga.xml`), eliminando el problema de superposicion y apilamiento de tarjetas de manga en la vista de favoritos.

---

## Mejoras Anteriores (Contador Dinamico en Webtoon y Lint en CI)

### Nuevas Funcionalidades y Mejoras
1. **`app/src/main/java/com/mangalibrary/ui/LectorActivity.kt`**
   - **Contador Dinamico en Modo Webtoon**: Actualizacion en tiempo real del indicador de paginas mediante `RecyclerView.OnScrollListener`.
   - **Deduplicacion de Capitulos**: Filtrado inteligente por idioma y numero de capitulo para evitar duplicados entre grupos de traduccion.
2. **`.github/workflows/ci.yml`**
   - Incorporacion de la tarea de analisis estatico `./gradlew lintDebug` en GitHub Actions.

---
## Resumen General de Funcionalidades
- Compilacion y Ejecucion 100% Estables (`Build finished successfully`).
- Paleta de Colores Profesional y Modo Oscuro Totalmente Operativo.
- Lista Vertical de Favoritos Sin Solapamientos (`LinearLayoutManager` + `MangaAdapter`).
- Validacion Lint automatizada en CI/CD (`lintDebug`).
- Contador de Paginas Dinamico en Modo Webtoon.
- Navegacion Inteligente de Capitulos.
- Estado Vacio Dinamico para capitulos sin stock o sin coincidencia de idioma.
- Alternancia de Tema Modo Claro / Modo Oscuro con persistencia en `SharedPreferences`.
- Historial de Lectura y Edicion de Perfil.
