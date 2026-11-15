# Registro de Cambios (CHANGELOG) - Biblioteca Digital Manga

Documento histórico y detallado de todas las modificaciones, correcciones de merge/compilación y nuevas funcionalidades implementadas en la aplicación nativa para Android en Kotlin.

---

## 🚀 Versión Actual / Últimas Mejoras (Contador de Páginas, CI/CD Lint & Corrección de Preview de Lector)

### 🆕 Nuevas Funcionalidades, Mejoras y Correcciones
1. **`app/src/main/java/com/mangalibrary/ui/LectorActivity.kt`**
   - **Contador Dinámico en Modo Webtoon**: Añadido `RecyclerView.OnScrollListener` en `rvWebtoonPages` para actualizar en tiempo real el indicador de páginas (`tvPageIndicator`) a medida que el usuario hace desplazamiento vertical continuo.
   - **Deduplicación Avanzada y Filtrado por Idioma**: Implementación de `deduplicateChapters()` con extracción numérica mediante `extractChapterFloat`, detección de prefijos (`extra`, `special`, `omake`, `oneshot`, `side`), y filtrado automático por el idioma del capítulo inicial. Esto evita duplicados entre *Scanlation Groups* y previene el cruce de idiomas cuando la lista de capítulos proviene del filtro por defecto "Todos".
2. **`.github/workflows/ci.yml`**
   - Agregada la tarea de validación estática `./gradlew lintDebug` dentro de la pipeline de GitHub Actions para verificar reglas de código, accesibilidad y posibles problemas en cada PR/push.
3. **`ReaderPageAdapter.kt`, `item_reader_page.xml` & Dependencias (`PhotoView`)**
   - **Carga Instantánea y Optimizada**: Restauración de la carga asíncrona directa mediante Coil (`load` con `crossfade`) en `item_reader_page.xml` y `ReaderPageAdapter`, garantizando velocidad máxima de aparición de imágenes en los modos Páginas y Webtoon.
   - **Soporte Adaptativo de Lector (Páginas vs Webtoon)**: Configuración dinámica en `ReaderPageAdapter` para aplicar `MATCH_PARENT` en modo Páginas (`ViewPager2`) y `WRAP_CONTENT` con `adjustViewBounds = true` en modo Webtoon (`RecyclerView`). Esto evita que las imágenes se recorten al hacer zoom en modo páginas y permite un desplazamiento vertical continuo y sin solapamientos en modo Webtoon.
   - **Corrección de Preview**: Sincronización de dependencias Gradle y compilación exitosa (`app:assembleDebug`) para resolver la advertencia de clase faltante en el previsualizador de layouts.

---

## 🚀 Mejoras Anteriores (Navegación Inteligente de Capítulos por Traductor)

### 🆕 Nuevas Funcionalidades y Mejoras de Lector
1. **`app/src/main/java/com/mangalibrary/ui/LectorActivity.kt` y `app/src/main/java/com/mangalibrary/ui/adapters/ChapterAdapter.kt`**
   - **Navegación Inteligente Anti-Duplicados**: Salto automático al **siguiente número de capítulo único**, evitando leer capítulos repetidos entre múltiples traductores (*Scanlation Groups*).
   - **Prioridad de Traductor**: Se prioriza mantener el mismo grupo de traducción e idioma.

---

## 🚀 Mejoras Anteriores (Estado Vacío de Capítulos y Corrección de Títulos)

### 🆕 Nuevas Funcionalidades y Mejoras de UI/UX
1. **`app/src/main/res/layout/fragment_manga_detail.xml` y `app/src/main/java/com/mangalibrary/Fragmentos/FragmentDetalleManga.kt`**
   - **Estado Vacío para Capítulos (`tv_no_chapters`)**: Mensaje informativo si un manga no posee capítulos o no hay resultados para el filtro de idioma.
   - **Reubicación del Botón Continuar Leyendo**: Ancho completo dentro de la tarjeta de cabecera.
2. **`app/src/main/java/com/mangalibrary/data/model/MangaModels.kt` y `app/src/main/java/com/mangalibrary/data/local/ReadingHistoryManager.kt`**
   - **Función `displayTitle()` y Sanitización**: Prevención de acumulación de títulos repetidos (*"Capítulo -: Capítulo 4"*).

---

## ✨ Resumen General de Funcionalidades
- ✅ **Compilación y Ejecución 100% Estables (`Build finished successfully`)**.
- ✅ **Validación Lint automatizada** en CI/CD (`lintDebug`).
- ✅ **Contador de Páginas Dinámico** en Modo Webtoon.
- ✅ **Navegación Inteligente de Capítulos** (salto automático a números de capítulo únicos manteniendo el traductor preferido).
- ✅ **Estado Vacío Dinámico** para capítulos sin stock o sin coincidencia de idioma.
- ✅ **Diseño Mejorado en Detalle de Manga** con botón de continuar leyendo a ancho completo.
- ✅ **Corrección de Títulos Acumulativos** en el historial de lectura (*Continuar Leyendo*).
- ✅ **Iconos Personalizados en `drawable/`** para el menú inferior y la barra de herramientas.
- ✅ **Alternancia de Tema Modo Claro / Modo Oscuro** con persistencia en `SharedPreferences`.
- ✅ **Rediseño UI/UX con Material 3 (M3)** en toda la carpeta `layout/`.
- ✅ **Logo Oficial de la App** integrado en el lanzador (*Launcher Icon*) y en la barra superior.
- ✅ **Modo Inmersivo de Pantalla Completa** con ocultado/mostrado de controles al tocar.
- ✅ **Favoritos en Cuadrícula (*Grid 4 Columnas*)**: Tarjetas prominentes y portadas grandes.
- ✅ **Tarjetas Prominentes en Carruseles de Inicio** (Tendencias y Recientes).
- ✅ **Búsqueda en Barra de Navegación Inferior**.
- ✅ **Indicador de Tag Activo** y navegación global por tags.
- ✅ **Selector Dinámico de Idiomas por Capítulo**.
- ✅ **Visor Dual (Páginas y Webtoon)** con navegación anterior/siguiente.
- ✅ **Nombres de Traductores (*Scanlation Groups*)** en capítulos.
- ✅ **Historial de Lectura y Edición de Perfil**.
