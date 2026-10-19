BibliotecaDigital

Aplicación móvil de biblioteca digital de manga desarrollada con Expo, React Native y TypeScript.

Objetivo

Permitir a los usuarios buscar mangas, consultar sus detalles, explorar capítulos y recibir recomendaciones basadas en similitudes entre sus características, según las funcionalidades implementadas.

Tecnologías
Expo
React Native
TypeScript
API de MangaDex
Git y GitHub
GitHub Actions
Requisitos previos
Node.js y npm
Git
Expo Go o un entorno de emulación compatible
Instalación

Clonar el repositorio:

git clone https://github.com/takkoh/BibliotecaDigital.git
cd BibliotecaDigital


Instalar las dependencias de la aplicación:

cd app
npm install

Configuración

El archivo .env.example contiene una plantilla para configurar la dirección base de la API de MangaDex.

Crea un archivo .env dentro de app/ si la aplicación lee las variables desde esa ubicación. Copia las variables necesarias desde la plantilla y ajusta su configuración según el código de la aplicación.

Variable principal:

EXPO_PUBLIC_MANGADEX_API_URL=https://api.mangadex.org


Las variables con el prefijo EXPO_PUBLIC_ pueden ser visibles en la aplicación. No deben contener contraseñas, tokens privados ni otras credenciales secretas.

Ejecución

Desde la carpeta app/, ejecuta:

npx expo start


Sigue las instrucciones de Expo para abrir la aplicación en un dispositivo compatible o emulador.

En Windows también puedes utilizar npm.cmd y npx.cmd si PowerShell presenta problemas con los comandos habituales.

Estructura del repositorio
app/: código fuente de la aplicación móvil.
docs/: documentación técnica y especificaciones.
.env.example: plantilla de configuración.
.github/workflows/: automatizaciones de integración continua.
README.md: instrucciones generales del proyecto.
Ramas de Git
main: rama principal.
develop: rama de desarrollo.
Estado del proyecto

Proyecto académico en desarrollo.

Las funcionalidades descritas deben corresponder con las que estén implementadas actualmente en el código fuente.