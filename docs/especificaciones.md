MangaLibrary — Documento de especificaciones
1. Descripción del proyecto

MangaLibrary es una aplicación móvil orientada a consultar y mostrar información sobre mangas mediante la API pública de MangaDex.

2. Objetivo

Desarrollar una aplicación que permita consultar información de mangas de manera organizada y presentar los resultados en una interfaz móvil.

3. Tecnologías utilizadas
Expo
React Native
JavaScript o TypeScript, según la implementación del proyecto
API pública de MangaDex
4. API externa

Nombre: MangaDex API

URL base: https://api.mangadex.org

La API se utiliza para obtener información relacionada con mangas. Las solicitudes y los datos mostrados dependerán de las funcionalidades implementadas en la aplicación.

5. Variables de configuración

La configuración de ejemplo se encuentra en el archivo .env.example.

EXPO_PUBLIC_MANGADEX_API_URL: dirección base de la API.
EXPO_PUBLIC_APP_NAME: nombre de la aplicación.
EXPO_PUBLIC_APP_VERSION: versión de la aplicación.
EXPO_PUBLIC_CACHE_TTL: tiempo de caché expresado en segundos.
6. Estructura del repositorio
app/: código fuente de la aplicación.
docs/: documentación del proyecto.
docs/especificaciones.md: documento de especificaciones.
.env.example: ejemplo de las variables de configuración.
7. Instalación y ejecución
Instalar Node.js y npm, si todavía no están instalados.
Instalar las dependencias del proyecto con npm install.
Configurar las variables de entorno necesarias.
Iniciar el proyecto Expo con npx expo start.

Los comandos deben ejecutarse desde la carpeta que contiene package.json.

8. Funcionalidades

Las funcionalidades de consulta, búsqueda, visualización y almacenamiento en caché deben documentarse de acuerdo con lo que realmente esté implementado en el código fuente.

9. Consideraciones

La documentación debe mantenerse actualizada conforme evolucione el proyecto. Las variables de configuración son ejemplos y deben coincidir con las utilizadas por la aplicación.