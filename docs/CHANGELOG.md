# Registro de Cambios (CHANGELOG) - Biblioteca Digital Manga

Documento histórico y detallado de todas las modificaciones, correcciones de merge/compilación y nuevas funcionalidades implementadas en la aplicación nativa para Android en Kotlin.

---

## 🚀 Versión Actual / Últimas Mejoras (Sincronización Total, Limpieza y Cuadrícula de Favoritos)

### 🛠️ Archivos Modificados y Ajustes
1. **`app/src/main/java/com/mangalibrary/Fragmentos/FragmentMisFavoritos.kt`**
   - Configurada la pantalla de favoritos con `GridLayoutManager(requireContext(), 4)` junto con `MangaCarouselAdapter` (`item_manga_carousel.xml`), mostrando los mangas guardados en una cuadrícula prominente de 4 columnas.
2. **`app/src/main/res/menu/bottom_nav_menu.xml` [ELIMINADO]**
   - Eliminado el archivo de menú redundante y no utilizado. `mi_menu.xml` queda como el único recurso activo para la barra de navegación inferior (`BottomNavigationView`).
3. **`.gitignore`**
   - Actualizado para ignorar artefactos de compilación (`app/build/`, `.gradle/`) y carpetas locales de IDEs (`.idea/`, `.vs/`, `.kotlin/`).

---

## 🚀 Mejoras Anteriores (Tarjetas Prominentes en Carruseles de Inicio)

### 🆕 Nuevos Archivos y Recursos
1. **`app/src/main/res/layout/item_manga_carousel.xml`**
   - Diseño de tarjeta vertical (*CardView*) de mayor tamaño optimizado para carruseles, conservando toda la información original (título, portadas de alta calidad, autor, estado y chips de géneros).
2. **`app/src/main/java/com/mangalibrary/ui/adapters/MangaCarouselAdapter.kt`**
   - Adaptador para renderizar tarjetas verticales prominentes en inicio y favoritos.

---

## 🚀 Mejoras Anteriores (Búsqueda en Barra de Navegación, Tags Activos e Inicio de Descubrimiento)

### 🆕 Nuevos Archivos y Recursos
1. **`app/src/main/res/menu/mi_menu.xml`**
   - Añadida la opción de **Búsqueda** a la barra de navegación inferior (*Bottom Navigation*).
2. **`app/src/main/java/com/mangalibrary/Fragmentos/FragmentBusqueda.kt` y `app/src/main/res/layout/fragment_busqueda.xml`**
   - Pantalla de búsqueda dedicada con barra prominente, filtro de estado y chip indicador de **Tag Activo** con botón de cierre.

---

## 🚀 Mejoras Anteriores (Lector Webtoon, Filtros e Idiomas Dinámicos)

### 🆕 Nuevos Archivos
1. **`app/src/main/java/com/mangalibrary/ui/LectorActivity.kt`**
   - Actividad del lector interactivo de capítulos con soporte dual: modo Paged (`ViewPager2`) y modo Webtoon (desplazamiento vertical continuo con `RecyclerView`).

---

## 📦 Versión Base & Resolución de Conflictos de Merge

### 🔍 Resolución de Conflictos de Fusión (*Merge Conflicts*)
Se resolvieron exitosamente los conflictos de merge en los archivos clave de la base:
- `FragmentDetalleManga.kt`, `FragmentInicio.kt`, `FragmentMisFavoritos.kt`, `MainActivity.kt`, `MangaLibraryApp.kt`, `MangaViewModel.kt`, `ChapterAdapter.kt`, `activity_lector.xml`, `fragment_manga_detail.xml`, `fragment_manga_search.xml`, `item_chapter.xml`.

---

## ✨ Resumen General de Funcionalidades
- ✅ **Compilación 100% Exitosa (`Build finished successfully`)**.
- ✅ **Favoritos en Cuadrícula (*Grid 4 Columnas*)**: Mangas guardados con tarjetas prominentes y portadas grandes.
- ✅ **Tarjetas Prominentes en Carruseles de Inicio** (Tendencias y Recientes).
- ✅ **Búsqueda en Barra de Navegación Inferior**.
- ✅ **Indicador de Tag Activo** y navegación global por tags.
- ✅ **Selector Dinámico de Idiomas por Capítulo**.
- ✅ **Visor Dual (Páginas y Webtoon)** con navegación anterior/siguiente.
- ✅ **Nombres de Traductores (*Scanlation Groups*)** en capítulos.
- ✅ **Historial de Lectura y Edición de Perfil**.
