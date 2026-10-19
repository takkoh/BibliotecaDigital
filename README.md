# MangaLibrary

Aplicación móvil para buscar y explorar mangas mediante la API pública de MangaDex. El proyecto utiliza Expo y React Native para la interfaz, con un módulo nativo de Kotlin para Android.

## Requisitos previos

Instala las siguientes herramientas antes de comenzar:

- [Git](https://git-scm.com/downloads)
- [Node.js LTS](https://nodejs.org/) y npm
- Una cuenta de GitHub con permisos para crear repositorios y configurar ramas

Verifica las instalaciones:

```bash
git --version
node --version
npm --version
```

## 1. Inicializar el proyecto

### 1.1 Crear la carpeta y el proyecto Expo

Desde una terminal, ejecuta:

```bash
mkdir MangaLibrary
cd MangaLibrary
git init
```

Crea la aplicación Expo dentro de `/app`:

```bash
npx create-expo-app@latest app --template blank-typescript
```

Instala sus dependencias:

```bash
cd app
npm install
cd ..
```

El directorio `/app` contendrá el proyecto Expo. El repositorio Git se mantendrá en la carpeta raíz `MangaLibrary`.

### 1.2 Crear la documentación

```bash
mkdir docs
```

Guarda la especificación del proyecto en:

```text
docs/especificaciones.pdf
```

La especificación debe documentar la arquitectura, las funcionalidades previstas, el uso de MangaDex API y la integración nativa con Kotlin.

## 2. Crear repositorio GitHub

### 2.1 Crear el repositorio remoto

En GitHub, crea un repositorio llamado `MangaLibrary`.

Se recomienda crearlo vacío, sin README, licencia ni `.gitignore` generados automáticamente, porque esos archivos se crearán localmente.

Conecta el repositorio local con GitHub:

```bash
git remote add origin https://github.com/USUARIO/MangaLibrary.git
```

Sustituye `USUARIO` por el nombre de tu cuenta u organización.

### 2.2 Configurar el nombre de la rama principal

```bash
git branch -M main
```

Crea la rama de desarrollo:

```bash
git switch -c develop
```

La estrategia de ramas será:

- `main`: versión estable del proyecto.
- `develop`: integración del desarrollo.
- `feature/nombre`: nuevas funcionalidades.
- `fix/nombre`: correcciones de errores.

Las nuevas funcionalidades se desarrollarán en ramas `feature/*` creadas desde `develop`. Después se integrarán mediante pull requests hacia `develop`.

Cuando `develop` esté listo para una entrega estable, se abrirá un pull request hacia `main`.

### 2.3 Crear el archivo de configuración de Git

En la raíz, crea `.gitignore`:

```gitignore
# Dependencias
node_modules/

# Variables de entorno
.env
.env.*
!.env.example
app/.env
app/.env.*
!app/.env.example

# Expo
.expo/
dist/
web-build/

# Android y artefactos de compilación
android/
ios/
*.apk
*.aab

# Herramientas
.DS_Store
Thumbs.db
.vscode/
*.log
```

Este proyecto utiliza el flujo administrado de Expo y genera los directorios nativos cuando se necesitan. Si en el futuro decides mantener modificaciones manuales persistentes en `android/`, revisa la estrategia antes de ignorar o regenerar ese directorio.

### 2.4 Proteger la rama `main`

En GitHub:

1. Abre el repositorio.
2. Entra en **Settings → Rules → Rulesets** o en la sección de protección de ramas, según la interfaz disponible.
3. Crea una regla dirigida a la rama `main`.
4. Activa la exigencia de pull requests antes de integrar cambios.
5. Exige al menos una aprobación si el tamaño del equipo lo permite.
6. Activa la exigencia de que las comprobaciones de CI finalicen correctamente.
7. Bloquea los force pushes y la eliminación de la rama.
8. Guarda la regla.

Si GitHub permite seleccionar comprobaciones obligatorias, selecciona las que publique el workflow de CI una vez que haya ejecutado correctamente.

**Importante:** configura primero el workflow y ejecuta al menos una vez las comprobaciones. Después podrás seleccionarlas como requisitos obligatorios.

### 2.5 Crear una plantilla de pull request

Crea:

```text
.github/pull_request_template.md
```

Contenido:

```markdown
## Descripción

<!-- Explica brevemente el cambio realizado. -->

## Tipo de cambio

- [ ] Nueva funcionalidad
- [ ] Corrección de error
- [ ] Documentación
- [ ] Configuración o mantenimiento

## Cambios realizados

- 

## Validación

- [ ] El linter finaliza correctamente.
- [ ] La comprobación de TypeScript finaliza correctamente.
- [ ] He probado los cambios relevantes.
- [ ] La documentación está actualizada.

## Checklist

- [ ] Los commits siguen Conventional Commits.
- [ ] No se han añadido secretos ni credenciales.
- [ ] El cambio respeta el alcance del proyecto.
```

## 3. Estructura del proyecto

La estructura inicial del repositorio será:

```text
MangaLibrary/
├── .github/
│   ├── workflows/
│   │   └── ci.yml
│   └── pull_request_template.md
├── app/
│   ├── app/
│   ├── package.json
│   └── tsconfig.json
├── docs/
│   └── especificaciones.pdf
├── .env.example
├── .gitignore
├── .commitlintrc.json
├── package.json
├── package-lock.json
└── README.md
```

El contenido de `/app` depende de la plantilla Expo elegida. La carpeta `modules/manga-recommendations/` se agregará cuando se cree el módulo nativo de Kotlin.

### 3.1 Configurar las variables de entorno

Crea `.env.example` en la raíz:

```dotenv
EXPO_PUBLIC_MANGADEX_API_URL=https://api.mangadex.org
EXPO_PUBLIC_CACHE_TTL=604800
```

La primera variable define la URL base de MangaDex API. La segunda establece una duración de caché de siete días, expresada en segundos.

Como Expo se ejecutará desde `/app`, crea una copia para el entorno local:

**Windows (PowerShell):**

```powershell
Copy-Item .env.example app/.env
```

**Linux o macOS:**

```bash
cp .env.example app/.env
```

El archivo `app/.env` no debe subirse al repositorio.

En el código TypeScript se pueden leer las variables así:

```typescript
const MANGADEX_API_URL =
  process.env.EXPO_PUBLIC_MANGADEX_API_URL ??
  'https://api.mangadex.org';

const CACHE_TTL = Number(
  process.env.EXPO_PUBLIC_CACHE_TTL ?? 604800
);
```

No se deben guardar contraseñas, tokens privados ni claves secretas en variables `EXPO_PUBLIC_`, ya que quedan disponibles en el código de la aplicación.

### 3.2 Crear el `package.json` raíz para las herramientas

Desde la raíz del repositorio:

```bash
npm init -y
npm install --save-dev @commitlint/cli @commitlint/config-conventional
```

Esto crea el `package.json` raíz y su `package-lock.json`. Las dependencias de Expo permanecen en `/app`.

Añade la siguiente configuración al `package.json` raíz, conservando las dependencias generadas por npm:

```json
{
  "name": "mangalibrary-repository",
  "private": true,
  "scripts": {
    "commitlint": "commitlint"
  },
  "devDependencies": {
    "@commitlint/cli": "^20.0.0",
    "@commitlint/config-conventional": "^20.0.0"
  }
}
```

Si las versiones instaladas por npm son distintas, conserva las versiones que se encuentren realmente en tu archivo y en el lockfile. No reemplaces versiones instaladas sin necesidad.

Crea `.commitlintrc.json`:

```json
{
  "extends": ["@commitlint/config-conventional"]
}
```

### 3.3 Configurar los scripts de calidad en Expo

Dentro de `/app/package.json`, verifica que existan scripts equivalentes a los siguientes:

```json
{
  "scripts": {
    "start": "expo start",
    "android": "expo run:android",
    "lint": "expo lint",
    "typecheck": "tsc --noEmit"
  }
}
```

Integra estos scripts en el archivo existente, sin eliminar las demás entradas. Si la plantilla elegida no incluye ESLint o el comando `expo lint`, instala y configura ESLint siguiendo la documentación de la versión de Expo utilizada antes de activar el pipeline.

## 4. Configurar el pipeline CI/CD

El pipeline de integración continua (CI) verificará que los cambios cumplan las reglas de commits, que el código pase el linter y que TypeScript no detecte errores de tipos o sintaxis.

Este pipeline no publicará automáticamente una aplicación ni generará una APK de producción. La compilación nativa puede añadirse en una etapa posterior.

### 4.1 Crear el workflow

Crea el archivo:

```text
.github/workflows/ci.yml
```

Contenido:

```yaml
name: CI

on:
  push:
    branches:
      - main
      - develop
      - "feature/**"
      - "fix/**"
  pull_request:
    branches:
      - main
      - develop

permissions:
  contents: read

jobs:
  commitlint:
    name: Validate commit messages
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: 22

      - name: Install root dependencies
        run: npm ci

      - name: Validate commits
        shell: bash
        env:
          EVENT_NAME: ${{ github.event_name }}
          BASE_SHA: ${{ github.event.pull_request.base.sha }}
          BEFORE_SHA: ${{ github.event.before }}
          HEAD_SHA: ${{ github.sha }}
        run: |
          if [[ "$EVENT_NAME" == "pull_request" ]]; then
            npx commitlint \
              --from "$BASE_SHA" \
              --to "$HEAD_SHA" \
              --verbose
          elif [[ "$BEFORE_SHA" =~ ^0+$ ]]; then
            npx commitlint --last --verbose
          else
            npx commitlint \
              --from "$BEFORE_SHA" \
              --to "$HEAD_SHA" \
              --verbose
          fi

  validate-app:
    name: Validate Expo application
    runs-on: ubuntu-latest

    defaults:
      run:
        working-directory: app

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: 22
          cache: npm
          cache-dependency-path: app/package-lock.json

      - name: Install dependencies
        run: npm ci

      - name: Run linter
        run: npm run lint

      - name: Validate TypeScript
        run: npm run typecheck
```

El workflow comprueba los mensajes de los commits incluidos en cada push o pull request, además de ejecutar el linter y la comprobación de TypeScript en `/app`.

Para mantener reproducible el entorno, confirma en Git ambos archivos de bloqueo: `package-lock.json` en la raíz y `app/package-lock.json`.

### 4.2 Convención de commits

El repositorio utilizará Conventional Commits.

Formato:

```text
tipo(ámbito): descripción breve
```

Ejemplos válidos:

```text
feat(search): add manga search screen
fix(api): handle failed MangaDex requests
docs(readme): document project setup
chore(ci): configure GitHub Actions
test(recommendations): add similarity tests
```

Tipos recomendados:

| Tipo | Uso |
|---|---|
| `feat` | Nueva funcionalidad. |
| `fix` | Corrección de errores. |
| `docs` | Documentación. |
| `test` | Pruebas automatizadas. |
| `refactor` | Reestructuración sin cambiar el comportamiento esperado. |
| `chore` | Mantenimiento y configuración. |

Un mensaje como `arreglos varios` no cumple la convención. Utiliza un tipo válido, dos puntos y una descripción.

### 4.3 Qué valida la CI

- **Commitlint:** verifica la convención de los mensajes de commit.
- **ESLint:** identifica problemas de estilo y errores que las reglas configuradas puedan detectar.
- **TypeScript:** ejecuta `tsc --noEmit` para comprobar tipos y errores de compilación del código TypeScript.
- **Pull requests:** ejecuta las comprobaciones antes de integrar cambios en `main` o `develop`.

Estas comprobaciones no demuestran por sí solas que la aplicación funcione correctamente en Android. Las pruebas del módulo Kotlin y la compilación nativa deberán verificarse en un entorno Android o añadirse como un job específico posteriormente.

## 5. Primer commit

### 5.1 Comprobar la estructura

Verifica que existan:

- [ ] Proyecto Expo en `/app`.
- [ ] especificaciones en `/docs/especificaciones.pdf`.
- [ ] `.env.example` en la raíz.
- [ ] `.gitignore`.
- [ ] `package.json` y `package-lock.json` en la raíz.
- [ ] `app/package.json` y `app/package-lock.json`.
- [ ] `.commitlintrc.json`.
- [ ] `.github/workflows/ci.yml`.
- [ ] `.github/pull_request_template.md`.
- [ ] `README.md` con las instrucciones de instalación y desarrollo.

### 5.2 Validar localmente

Desde la raíz, instala las herramientas de Git:

```bash
npm ci
```

Después instala y valida Expo:

```bash
cd app
npm ci
npm run lint
npm run typecheck
cd ..
```

Si alguno de los scripts no existe, corrige la configuración de `/app/package.json` antes de continuar.

### 5.3 Crear el primer commit

El primer commit debe cumplir Conventional Commits:

```bash
git add .
git commit -m "chore(init): initialize MangaLibrary project"
```

Comprueba el estado:

```bash
git status
git log -1 --oneline
```

### 5.4 Publicar el repositorio

Publica la rama `develop`:

```bash
git push -u origin develop
```

Después publica `main` con el commit inicial:

```bash
git branch main
git push -u origin main
```

Ambas ramas pueden apuntar al mismo commit inicial. A partir de ese momento, desarrolla en ramas `feature/*` o `fix/*` y utiliza pull requests para integrar los cambios.

Si ya has creado y publicado `main`, no vuelvas a crearla con `git branch main`; cambia a ella y verifica su estado antes de continuar.

### 5.5 Verificar la CI en GitHub

1. Abre la pestaña **Actions** del repositorio.
2. Comprueba que el workflow `CI` se ejecutó.
3. Revisa los resultados de `Validate commit messages` y `Validate Expo application`.
4. Si falla, consulta los logs, corrige el error y vuelve a enviar los cambios.
5. Una vez que las comprobaciones hayan pasado, configura los requisitos de CI en la protección de `main`.

## 6. Integración de Kotlin con Expo

La integración nativa se realizará después de que el setup básico y la CI estén funcionando.

### 6.1 Crear el módulo local

Desde el directorio del proyecto Expo:

```bash
cd app
npx create-expo-module@latest --local
```

Cuando el asistente pregunte por el nombre, utiliza `manga-recommendations`. Selecciona Android como plataforma de destino si el asistente permite elegir plataformas.

### 6.2 Generar y compilar Android

Desde `/app`, ejecuta:

```bash
npx expo prebuild --platform android
npx expo run:android
```

El equipo debe tener Android Studio, Android SDK y un emulador o dispositivo configurado.

### 6.3 Implementar y probar el código Kotlin

Modifica el archivo Kotlin generado por el módulo y expón las funciones necesarias mediante Expo Modules API. La aplicación React Native accederá a ellas a través del punto de entrada TypeScript generado.

Cada vez que cambie el código nativo, vuelve a compilar:

```bash
npx expo run:android
```

Cuando solo cambie JavaScript o TypeScript, normalmente bastará con reiniciar o actualizar el servidor de desarrollo.

Consulta la documentación oficial de [Expo Modules API](https://docs.expo.dev/modules/get-started/) antes de modificar la estructura generada.

## 7. Flujo de trabajo diario

1. Actualiza `develop`:

   ```bash
   git switch develop
   git pull origin develop
   ```

2. Crea una rama para la tarea:

   ```bash
   git switch -c feature/manga-search
   ```

3. Desarrolla y valida los cambios en `/app`.
4. Crea commits que cumplan Conventional Commits.
5. Publica la rama:

   ```bash
   git push -u origin feature/manga-search
   ```

6. Abre un pull request hacia `develop`.
7. Espera a que la CI finalice correctamente y resuelve los comentarios.
8. Integra el cambio mediante el pull request.

Las correcciones se trabajarán con ramas `fix/*`. La rama `main` se reservará para versiones estables y no se utilizará como rama de trabajo diario.

## 8. Recursos

- [Expo — Crear un proyecto](https://docs.expo.dev/get-started/create-a-project/)
- [Expo — Módulos nativos](https://docs.expo.dev/modules/get-started/)
- [Expo — Compilación local](https://docs.expo.dev/guides/local-app-development/)
- [MangaDex API](https://api.mangadex.org/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Commitlint](https://commitlint.js.org/)
- [GitHub Actions](https://docs.github.com/en/actions)
- [Protección de ramas en GitHub](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository)
