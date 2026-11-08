# Registro de Cambios (CHANGELOG) - Biblioteca Digital Manga

Documento detallado de los cambios, correcciones de compilación y nuevas funcionalidades implementadas en la aplicación nativa para Android en Kotlin.

---

## 📅 Resumen General

Se completó la transición y corrección del proyecto a **Android Nativo con Kotlin y Gradle**, solucionando los errores de referencias no encontradas, implementando el lector de manga, la edición de perfil, optimizaciones de memoria y un sistema completo de filtros e historial de lectura.

---

## 🆕 Archivos Creados

1. **`app/src/main/java/com/mangalibrary/ui/LectorActivity.kt`**
   - Actividad del lector interactivo de capítulos con `ViewPager2`.
   - Consulta las páginas de la API At-Home de MangaDex y las muestra con precarga fluida.
   - Incluye navegación entre capítulos (`Anterior` / `Siguiente`) e indicador de páginas (`1 / N`).

2. **`app/src/main/java/com/mangalibrary/ui/EditarPerfilActivity.kt`**
   - Actividad para editar la información del perfil del usuario.
   - Carga y actualiza los datos persistidos en `SharedPreferences` (`user_profile_prefs`).

3. **`app/src/main/res/layout/activity_editar_perfil.xml`**
   - Diseño XML con componentes Material 3 (`TextInputLayout` y `TextInputEditText`) para nombre, correo, teléfono, fecha de nacimiento, fecha de miembro y estado.

4. **`app/src/main/java/com/mangalibrary/data/local/ReadingHistoryManager.kt`**
   - Gestor de persistencia local en `SharedPreferences` para guardar automáticamente el último capítulo leído por manga.

5. **`CHANGELOG.md`**
   - Registro permanente de cambios dentro del proyecto.

---

## 🛠️ Archivos Modificados

1. **`app/src/main/java/com/mangalibrary/MangaLibraryApp.kt`**
   - Implementada la interfaz `ImageLoaderFactory` de **Coil**.
   - Configurado `MemoryCache` (25% de RAM) y `DiskCache` (250 MB en disco) para prevenir errores de memoria (*OutOfMemoryError*).

2. **`app/src/main/java/com/mangalibrary/Fragmentos/FragmentDetalleManga.kt`**
   - Añadido el botón **"📖 Continuar Leyendo"** para reanudar la lectura del último capítulo leído.
   - Añadido el filtro dinámico de idiomas (*Todos*, *Español 🇪🇸*, *Inglés 🇬🇧*) sobre la lista de capítulos.

3. **`app/src/main/res/layout/fragment_manga_detail.xml`**
   - Añadidos los contenedores `ChipGroup` para filtro de idiomas y el botón `MaterialButton` para continuar la lectura.

4. **`app/src/main/java/com/mangalibrary/data/api/MangaDexApi.kt`**
   - Extendida la interfaz Retrofit para soportar consultas por `includedTags[]` y `status[]`.

5. **`app/src/main/java/com/mangalibrary/data/repository/MangaDexRepository.kt`**
   - Actualizado el método `searchManga` para enviar los parámetros de filtrado a los servidores de MangaDex.

6. **`app/src/main/java/com/mangalibrary/ui/MangaViewModel.kt`**
   - Incorporado el estado y la lógica para aplicar filtros combinados de género y estado de publicación.

7. **`app/src/main/res/layout/fragment_manga_search.xml`**
   - Añadido un carrusel deslizable con `ChipGroup` de estados (*En Emisión*, *Completado*) y géneros oficiales de MangaDex (*Acción, Aventura, Comedia, Fantasía, Romance, Isekai, Sci-Fi, Sobrenatural*).

8. **`app/src/main/java/com/mangalibrary/Fragmentos/FragmentInicio.kt`**
   - Vinculados los eventos de selección de los `Chips` de filtro con la ViewModel.

9. **`app/src/main/java/com/mangalibrary/ui/adapters/ChapterAdapter.kt`**
   - Actualizado para enviar la posición e identificadores de capítulos hacia `LectorActivity` y permitir la navegación entre capítulos.

10. **`app/src/main/res/layout/activity_lector.xml`**
    - Añadida la barra de controles inferior con botones `⏮ Anterior` y `Siguiente ⏭` y el texto indicador de páginas.

11. **`README.md`**
    - Actualizada la documentación general del repositorio a Android Nativo en Kotlin.

12. **`gradle.properties`**
    - Desactivada la caché de configuración propensa a conflictos en Gradle.

---

## ✨ Funcionalidades Principales Implementadas

- ✅ **Compilación 100% Exitosa**: Resueltos los símbolos no encontrados de `LectorActivity` y `EditarPerfilActivity`.
- ✅ **Visor de Manga Completo**: Lector con `ViewPager2`, indicador de página y navegación hacia el capítulo anterior o siguiente.
- ✅ **Gestión de Memoria con Coil**: Caché en RAM y disco para evitar bloqueos durante la lectura.
- ✅ **Filtro de Idiomas**: Separación de capítulos en español e inglés en el detalle del manga.
- ✅ **Filtros por Estado y Géneros (MangaDex API)**: Búsqueda avanzada por géneros (*Isekai, Sobrenatural, Romance, Acción, etc.*).
- ✅ **Historial de Lectura ("Continuar Leyendo")**: Retoma automáticamente el último capítulo leído de cada manga.
- ✅ **Edición de Perfil**: Formulario para actualizar la información del usuario almacenada en `SharedPreferences`.
