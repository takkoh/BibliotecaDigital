# MangaLibrary — Documento de especificaciones

## 1. Descripción del proyecto

MangaLibrary es una aplicación móvil destinada a consultar, explorar y visualizar información sobre mangas mediante la API pública de MangaDex. La aplicación utilizará Expo y React Native para desarrollar la interfaz y gestionar las interacciones del usuario, mientras que Kotlin se integrará mediante un módulo nativo para Android.

La aplicación no contará con un servidor propio ni almacenará una copia completa del catálogo de MangaDex. Los datos se obtendrán principalmente mediante solicitudes a la API y se podrán conservar temporalmente en caché cuando resulte necesario.

## 2. Objetivo

Desarrollar una aplicación móvil que permita buscar mangas, consultar sus detalles, explorar sus capítulos y recibir recomendaciones basadas en similitudes entre géneros y etiquetas.

El proyecto utilizará una arquitectura híbrida que combine React Native y Expo con un módulo nativo de Android desarrollado en Kotlin. La integración permitirá demostrar la comunicación entre el código multiplataforma y el código nativo.

## 3. Tecnologías utilizadas

- **Expo:** herramientas para configurar, desarrollar y compilar la aplicación.
- **React Native:** construcción de la interfaz móvil mediante JavaScript o TypeScript.
- **Kotlin:** implementación de funciones nativas para Android.
- **Expo Modules API:** mecanismo de integración entre Kotlin y React Native.
- **MangaDex API:** fuente principal de información sobre los mangas.
- **Node.js y npm:** entorno para instalar dependencias y ejecutar las herramientas de desarrollo.
- **Android Studio y Android SDK:** compilación, ejecución y depuración del código nativo para Android.

La aplicación tendrá como plataforma objetivo Android para la funcionalidad desarrollada en Kotlin. Si posteriormente se desea ofrecer la misma funcionalidad nativa en iOS, será necesario implementar una alternativa compatible con esa plataforma, por ejemplo, mediante Swift.

## 4. API externa

**Nombre:** MangaDex API

**URL base:** `https://api.mangadex.org`

La API se utilizará para obtener los datos necesarios para las siguientes funcionalidades:

- Búsqueda y exploración de mangas.
- Consulta de títulos, descripciones, géneros y etiquetas.
- Obtención de información detallada de un manga.
- Consulta de capítulos y sus metadatos.
- Obtención de los datos necesarios para generar recomendaciones.

Las solicitudes se realizarán desde la aplicación. La disponibilidad de los datos dependerá de los endpoints, parámetros, permisos y límites establecidos por MangaDex.

La aplicación no necesita una API key para realizar consultas públicas ordinarias, aunque determinados endpoints o funcionalidades pueden tener requisitos adicionales.

## 5. Variables de configuración

El archivo `.env.example` documentará las variables de configuración utilizadas por el proyecto.

```dotenv
EXPO_PUBLIC_MANGADEX_API_URL=https://api.mangadex.org
EXPO_PUBLIC_APP_NAME=MangaLibrary
EXPO_PUBLIC_APP_VERSION=1.0.0
EXPO_PUBLIC_CACHE_TTL=604800
```

| Variable | Descripción |
|---|---|
| `EXPO_PUBLIC_MANGADEX_API_URL` | URL base de MangaDex API. |
| `EXPO_PUBLIC_APP_NAME` | Nombre de la aplicación. |
| `EXPO_PUBLIC_APP_VERSION` | Versión de la aplicación. |
| `EXPO_PUBLIC_CACHE_TTL` | Tiempo de conservación de la caché en segundos; el ejemplo corresponde a siete días. |

Las variables con el prefijo `EXPO_PUBLIC_` estarán disponibles en el código JavaScript o TypeScript de Expo. No deberán utilizarse para almacenar secretos.

Kotlin no recibirá automáticamente estas variables. Si el módulo nativo necesita alguna configuración, esta deberá transmitirse explícitamente desde React Native o definirse mediante la configuración nativa correspondiente.

Las variables deberán coincidir con las que realmente utilice el código fuente.

## 6. Arquitectura de la aplicación

La arquitectura se dividirá en tres componentes principales:

1. **Capa de interfaz:** Expo y React Native mostrarán las pantallas, recibirán las búsquedas y gestionarán la navegación.
2. **Capa de datos:** el código JavaScript o TypeScript consultará MangaDex API y transformará los resultados para mostrarlos en la aplicación.
3. **Capa nativa:** un módulo Expo implementado en Kotlin realizará el cálculo local de similitud entre etiquetas para ordenar candidatos a recomendación.

El módulo Kotlin no sustituirá a MangaDex API ni almacenará el catálogo. Recibirá los datos necesarios desde React Native, procesará las etiquetas localmente y devolverá puntuaciones para que la interfaz muestre las recomendaciones.

Flujo de datos:

`Usuario → Interfaz React Native → MangaDex API → Resultados → Módulo Kotlin → Puntuaciones → Interfaz`

La caché será temporal y se limitará a los datos necesarios para mejorar el rendimiento. No se implementará una base de datos propia con el catálogo completo.

## 7. Funcionalidades

### 7.1 Búsqueda y exploración

El usuario podrá buscar mangas por título y consultar resultados obtenidos desde MangaDex API.

### 7.2 Detalles del manga

La aplicación mostrará la información disponible del manga seleccionado, como título, descripción, portada, géneros y etiquetas.

### 7.3 Exploración de capítulos

La aplicación consultará y mostrará los capítulos disponibles y sus metadatos. La lectura del contenido completo de los capítulos solo se implementará si se define expresamente dentro del alcance del proyecto y se cumplen las condiciones aplicables.

### 7.4 Recomendaciones locales

El módulo Kotlin calculará una puntuación de similitud a partir de las etiquetas del manga seleccionado y las de otros candidatos obtenidos de la API.

La puntuación podrá basarse en la cantidad de etiquetas compartidas. React Native utilizará el resultado para ordenar las recomendaciones.

Este proceso no requiere una API de inteligencia artificial, un modelo generativo, una suscripción ni el envío de preferencias a un proveedor de IA.

## 8. Integración de Kotlin con Expo: procedimiento paso a paso

La integración se realizará mediante Expo Modules API, que permite crear módulos nativos accesibles desde JavaScript o TypeScript.

### Paso 1. Preparar el entorno

Instalar y configurar:

- Node.js y npm.
- Android Studio.
- Android SDK y las herramientas de compilación correspondientes.
- Un emulador Android o un dispositivo físico con depuración USB habilitada.

Verificar las instalaciones desde una terminal:

```bash
node -v
npm -v
```

Abrir la terminal en la carpeta raíz del proyecto, donde se encuentra `package.json`.

### Paso 2. Instalar las dependencias del proyecto

Si el proyecto ya existe, instalar sus dependencias:

```bash
npm install
```

Comprobar que Expo funciona antes de añadir código nativo:

```bash
npx expo start
```

Detener el servidor después de verificar que el proyecto inicia correctamente.

### Paso 3. Crear un módulo nativo local

Desde la raíz del proyecto, ejecutar:

```bash
npx create-expo-module@latest --local
```

El asistente solicitará información sobre el módulo. Se utilizará un nombre descriptivo, por ejemplo, `manga-recommendations`.

El módulo deberá incluir soporte para Android. Si el generador permite seleccionar plataformas, elegir Android para esta implementación.

El comando generará una estructura similar a la siguiente:

```text
MangaLibrary/
├── app/
├── docs/
│   └── especificaciones.md
├── .env.example
├── package.json
└── app.json
```

La estructura exacta puede variar según la versión del generador.

### Paso 4. Implementar la lógica nativa en Kotlin

Dentro de `modules/manga-recommendations/android/`, localizar el archivo Kotlin generado por Expo. Habitualmente estará en una ruta semejante a:

`modules/manga-recommendations/android/src/main/java/expo/modules/mangarecommendations/MangaRecommendationsModule.kt`

El nombre y el paquete exactos dependerán de las opciones seleccionadas durante la generación.

En el módulo se implementará una función que reciba dos listas de etiquetas y devuelva cuántas coinciden.

Ejemplo ilustrativo de la lógica:

```kotlin
Function("countSharedTags") {
    preferredTags: List<String>,
    mangaTags: List<String> ->
    val preferred = preferredTags.map {
        it.trim().lowercase()
    }.toSet()

    mangaTags.map {
        it.trim().lowercase()
    }.toSet().count {
        it in preferred
    }
}
```

Este fragmento debe colocarse dentro de `ModuleDefinition` del módulo generado, conservando los imports y la estructura requeridos por la versión de Expo Modules API utilizada.

La función normaliza las etiquetas, elimina duplicados y devuelve la cantidad de coincidencias. No consulta Internet ni almacena información de manera permanente.

### Paso 5. Exponer la función a React Native

El módulo debe registrar la función dentro de su definición nativa para que Expo pueda reconocerla.

La parte relevante de la estructura Kotlin tendrá una forma semejante a esta:

```kotlin
class MangaRecommendationsModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("MangaRecommendations")

        Function("countSharedTags") {
            preferredTags: List<String>,
            mangaTags: List<String> ->
            val preferred = preferredTags.map {
                it.trim().lowercase()
            }.toSet()

            mangaTags.map {
                it.trim().lowercase()
            }.toSet().count {
                it in preferred
            }
        }
    }
}
```

Se deberá adaptar la función al archivo generado, evitando duplicar la clase si el generador ya la creó.

### Paso 6. Conectar el módulo con el código TypeScript

El módulo local genera un punto de entrada TypeScript que permite acceder a la funcionalidad nativa. Se debe revisar `modules/manga-recommendations/index.ts` y conservar el mecanismo de importación generado.

En el código de la aplicación se utilizará la exportación del módulo. La ruta del import dependerá de la ubicación de la pantalla y de la configuración de alias del proyecto.

Ejemplo conceptual:

```typescript
import MangaRecommendations from
  '@/modules/manga-recommendations';
```

Si el proyecto no tiene configurado el alias `@`, se deberá utilizar una ruta relativa correcta.

Después de importar el módulo, la aplicación podrá invocar la función y utilizar la puntuación para ordenar los candidatos:

```typescript
const score = MangaRecommendations.countSharedTags(
  selectedTags,
  candidateTags
);
```

El ejemplo supone que el archivo TypeScript del módulo expone el método con ese nombre y que su definición coincide con la función Kotlin.

### Paso 7. Generar el proyecto nativo de Android

Si el proyecto todavía no tiene el directorio nativo de Android, ejecutar:

```bash
npx expo prebuild --platform android
```

Este comando genera el proyecto Android que se necesita para compilar la aplicación e incluir el módulo Kotlin.

Si ya existe un directorio `android/`, se deberá comprobar que la configuración esté sincronizada con el módulo nuevo antes de compilar.

No se debe ejecutar `npx expo prebuild --clean` sin revisar previamente los cambios nativos existentes, ya que la regeneración limpia puede eliminar modificaciones manuales.

### Paso 8. Instalar y ejecutar la aplicación

Con Android Studio, el SDK y un emulador o dispositivo configurados, ejecutar:

```bash
npx expo run:android
```

Este comando compila e instala la aplicación Android, incluyendo el módulo Kotlin, y permite iniciar el servidor de desarrollo.

No basta con abrir la aplicación en Expo Go: Expo Go no puede incorporar directamente un módulo nativo personalizado que no esté incluido en su propio binario.

### Paso 9. Verificar la comunicación

Realizar una prueba controlada con dos listas de etiquetas.

Por ejemplo:

- Etiquetas preferidas: `["Action", "Fantasy", "Adventure"]`
- Etiquetas del manga candidato: `["Fantasy", "Romance", "Action"]`
- Resultado esperado: `2`

Comprobar que:

1. React Native puede importar el módulo.
2. La llamada llega correctamente a Kotlin.
3. Kotlin devuelve la puntuación esperada.
4. La interfaz recibe el resultado y puede utilizarlo para ordenar los candidatos.

### Paso 10. Mantener el ciclo de desarrollo

Cuando solo cambie el código TypeScript o JavaScript, utilizar:

```bash
npx expo start
```

Cuando cambie el código Kotlin o la configuración nativa, recompilar la aplicación:

```bash
npx expo run:android
```

Los cambios nativos requieren reconstruir el binario para que se reflejen en la aplicación instalada.

## 9. Estructura del repositorio

La estructura final deberá contener, como mínimo:

- `app/`: pantallas y navegación de la aplicación.
- `modules/manga-recommendations/`: módulo local de Expo con el código Kotlin y su interfaz TypeScript.
- `docs/especificaciones.md`: documentación del proyecto.
- `.env.example`: plantilla de configuración.
- `package.json`: dependencias y scripts del proyecto.
- `app.json`: configuración de Expo.

El directorio `android/` podrá generarse localmente mediante Prebuild. Su inclusión en el repositorio dependerá de la estrategia de trabajo elegida para mantener el proyecto nativo.

## 10. Instalación y ejecución

Para preparar el proyecto desde una copia del repositorio:

1. Instalar Node.js, npm y las herramientas necesarias de Android.
2. Abrir una terminal en la raíz del proyecto.
3. Instalar dependencias:

   ```bash
   npm install
   ```

4. Revisar la configuración de Expo y las variables utilizadas.
5. Si todavía no existe el proyecto nativo, generar Android:

   ```bash
   npx expo prebuild --platform android
   ```

6. Compilar e instalar la aplicación:

   ```bash
   npx expo run:android
   ```

7. Para iniciar posteriormente el servidor de desarrollo sin recompilar el código nativo:

   ```bash
   npx expo start
   ```

Los comandos deben ejecutarse desde la carpeta que contiene `package.json`, salvo que se indique expresamente otra ruta.

## 11. Consideraciones y limitaciones

- El módulo Kotlin está destinado a Android; no ofrece por sí solo una implementación nativa para iOS.
- La aplicación utilizará MangaDex API como fuente principal y no mantendrá una copia completa de su catálogo.
- La caché deberá limitarse a los datos necesarios y respetar las condiciones aplicables al uso de la API.
- La lógica de recomendaciones será local y basada en similitudes de etiquetas, sin utilizar servicios de IA generativa.
- No se implementarán cuentas de usuario, biblioteca personal ni historial de lectura en el alcance actual.
- El contenido de los capítulos, sus portadas y demás recursos deberán utilizarse respetando las condiciones de MangaDex y los derechos correspondientes.
- La documentación, los nombres de las funciones y las variables deberán actualizarse cuando cambie la implementación.

## 12. Referencias técnicas

- Expo Modules API: https://docs.expo.dev/modules/get-started/
- Creación de módulos locales: https://docs.expo.dev/more/create-expo-module/
- Desarrollo y compilación local: https://docs.expo.dev/guides/local-app-development/
- Desarrollo con código nativo personalizado: https://docs.expo.dev/workflow/customizing/
- MangaDex API: https://api.mangadex.org/
