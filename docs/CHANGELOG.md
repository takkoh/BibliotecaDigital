# Registro de Cambios (CHANGELOG) - Biblioteca Digital Manga

Documento histórico y detallado de todas las modificaciones, correcciones de merge/compilación y nuevas funcionalidades implementadas en la aplicación nativa para Android en Kotlin.

---

## 🚀 Versión Actual / Últimas Mejoras (Tarjetas Prominentes en Pantalla de Favoritos - Grid de 2 Columnas)

### 🛠️ Archivos Modificados
1. **`../app/src/main/java/com/mangalibrary/Fragmentos/FragmentMisFavoritos.kt`**
   - Actualizada la pantalla de favoritos para utilizar un `GridLayoutManager` de 4 columnas junto con el `MangaCarouselAdapter` (`item_manga_carousel.xml`), mostrando los mangas guardados como una estantería de libros elegante con portadas prominentes y toda la información original (título, autor, estado y géneros).

---

## 🚀 Mejoras Anteriores (Tarjetas Prominentes en Carruseles de Inicio)

### 🆕 Nuevos Archivos y Recursos
1. **`../app/src/main/res/layout/item_manga_carousel.xml`**
   - Nuevo diseño de tarjeta vertical (*CardView*) de mayor tamaño optimizado para carruseles, **conservando toda la información original**.
2. **`../app/src/main/java/com/mangalibrary/ui/adapters/MangaCarouselAdapter.kt`**
   - Adaptador para renderizar tarjetas verticales prominentes.

---

## 🚀 Mejoras Anteriores (Búsqueda en Barra de Navegación, Tags Activos y Inicio de Descubrimiento)

### 🆕 Nuevos Archivos y Recursos
1. **`../app/src/main/res/menu/mi_menu.xml`**
   - Añadida la opción de **Búsqueda** a la barra de navegación inferior (*Bottom Navigation*).
2. **`../app/src/main/java/com/mangalibrary/Fragmentos/FragmentBusqueda.kt` y `app/src/main/res/layout/fragment_busqueda.xml`**
   - Pantalla de búsqueda dedicada con barra prominente, filtro de estado y chip indicador de **Tag Activo** con botón de cierre.

---

## 🚀 Mejoras Anteriores (Lector Webtoon, Filtros e Idiomas Dinámicos)

### 🆕 Nuevos Archivos
1. **`../app/src/main/java/com/mangalibrary/ui/LectorActivity.kt`**
   - Actividad del lector interactivo de capítulos con soporte dual: modo Paged (`ViewPager2`) y modo Webtoon (desplazamiento vertical continuo con `RecyclerView`).

---

## 📦 Versión Base & Resolución de Conflictos de Merge

### 🔍 Resolución de Conflictos de Fusión (*Merge Conflicts*)
Se resolvieron exitosamente los conflictos de merge en los 9 archivos clave de la base.
1. **`EditarPerfilActivity.kt`** y **`ReadingHistoryManager.kt`**.

---

## ✨ Resumen General de Funcionalidades
- ✅ **Compilación 100% Exitosa**.
- ✅ **Favoritos en Cuadrícula (*Grid 2 Columnas*)**: Mangas guardados con tarjetas prominentes y portadas grandes, manteniendo toda la información.
- ✅ **Tarjetas Prominentes en Carruseles de Inicio**.
- ✅ **Búsqueda en Barra de Navegación Inferior**.
- ✅ **Indicador de Tag Activo**.
- ✅ **Selector Dinámico de Idiomas**.
- ✅ **Visor Dual (Páginas y Webtoon)** con navegación anterior/siguiente.
- ✅ **Historial de Lectura y Edición de Perfil**.
