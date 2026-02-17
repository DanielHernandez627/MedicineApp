# MedicineApp 📱💊

## Descripción

MedicineApp es una aplicación móvil Android desarrollada como proyecto académico de Práctica de Ingeniería. La aplicación permite a los usuarios escanear códigos de barras de medicamentos para consultar información detallada, mantener un historial de consultas y reportar problemas o errores relacionados con medicamentos.

## 📋 Características Principales

- **Autenticación de Usuarios**: Sistema completo de registro e inicio de sesión con Firebase Authentication
- **Verificación de Email**: Validación de correo electrónico obligatoria antes del primer acceso
- **Escaneo de Códigos de Barras**: Utiliza la cámara del dispositivo para escanear códigos QR, CODE_128 y EAN_13
- **Consulta de Medicamentos**: Información detallada sobre medicamentos incluyendo:
  - Nombre del medicamento
  - Concentración
  - Tipo/Especialidad
  - Contraindicaciones y riesgos
  - Laboratorio fabricante
- **Historial de Consultas**: Registro completo de todas las búsquedas realizadas por el usuario
- **Reporte de Errores**: Sistema de reportes con captura de fotografías y descripción del problema
- **Gestión de Sesión**: Persistencia de sesión para acceso rápido

## 🛠️ Tecnologías Utilizadas

### Lenguaje y Plataforma
- **Kotlin** - Lenguaje principal de desarrollo
- **Android SDK** - API 23+ (Android 6.0 Marshmallow) hasta API 35
- **Java 11** - Compilación y target

### Bibliotecas y Frameworks

#### Firebase
- Firebase Authentication - Gestión de usuarios
- Firebase Auth KTX - Extensiones de Kotlin para Firebase

#### CameraX
- Camera Core, Camera2, Lifecycle, View y Extensions
- Procesamiento de imágenes en tiempo real

#### ML Kit
- Barcode Scanning - Detección de códigos de barras y QR

#### Jetpack Components
- ViewModel - Gestión de datos con ciclo de vida
- LiveData - Observación de datos reactivos
- ViewBinding - Enlace de vistas tipo seguro

#### Testing
- JUnit 4 - Pruebas unitarias
- Mockito - Mocking para tests
- Truth - Aserciones fluidas
- Kotlinx Coroutines Test - Testing de corrutinas
- Espresso - Pruebas de UI instrumentadas

## 🏗️ Arquitectura

La aplicación sigue el patrón de arquitectura **MVVM (Model-View-ViewModel)** con las siguientes capas:

```
├── Entities/           # Modelos de datos
├── Interfaces/         # Contratos e interfaces
├── Repositories/       # Capa de acceso a datos
├── Services/          # Lógica de negocio
├── viewModels/        # ViewModels para UI
├── Utilities/         # Utilidades y helpers
└── Activities/        # Pantallas de la aplicación
```

### Componentes Principales

#### Entities (Entidades)
- `Usuario` - Modelo de usuario
- `Medicamentos` - Información de medicamentos
- `MedicamentosConsulta` - Datos de consulta de medicamentos
- `HistorialMedicamentos` - Registro histórico
- `ReporteMedicamentos` - Reportes de problemas

#### Services (Servicios)
- `FirebaseAuthService` - Autenticación con Firebase
- `MedicineService` - Gestión de medicamentos
- `UserService` - Gestión de usuarios
- `HistoryService` - Historial de consultas
- `ReportService` - Manejo de reportes
- `SessionService` - Gestión de sesiones
- `SharedPrefsService` - Almacenamiento local

#### Repositories
- Capa de abstracción para acceso a datos
- Implementación del patrón Repository

## 📱 Pantallas de la Aplicación

1. **MainActivity** - Pantalla de bienvenida
2. **Login** - Inicio de sesión de usuarios
3. **registroUser** - Registro de nuevos usuarios
4. **MenuPrincipal** - Menú principal con opciones
5. **BarcodeScanner** - Escáner de códigos de barras
6. **ViewMedicine** - Visualización detallada de medicamentos
7. **Historic** - Historial de consultas del usuario
8. **ReportError** - Formulario de reporte de problemas

## 🔐 Permisos

La aplicación requiere los siguientes permisos:

- `INTERNET` - Conexión a servicios en la nube
- `CAMERA` - Escaneo de códigos de barras
- `READ_MEDIA_IMAGES` - Lectura de imágenes (Android 13+)
- `READ_MEDIA_VISUAL_USER_SELECTED` - Selección de medios (Android 14+)
- `READ_EXTERNAL_STORAGE` - Almacenamiento (Android 10-12)
- `WRITE_EXTERNAL_STORAGE` - Escritura (Android 9 e inferiores)

## 🧪 Testing

El proyecto incluye pruebas unitarias para los componentes principales:

- `AuthRepositoryTest` - Pruebas de autenticación
- `FirebaseAuthServiceTest` - Pruebas del servicio Firebase
- `HistoricTest` - Pruebas del historial
- `MenuPrincipalTest` - Pruebas del menú principal
- `ReportErrorTest` - Pruebas de reportes

### Ejecutar Tests

```bash
# Pruebas unitarias
./gradlew test

# Pruebas instrumentadas
./gradlew connectedAndroidTest
```

## 📦 Estructura del Proyecto

```
MedicineApp/
├── README.md
└── Aplicacion/
    ├── build.gradle.kts
    ├── settings.gradle.kts
    ├── gradle/
    │   └── libs.versions.toml
    └── app/
        ├── build.gradle.kts
        ├── google-services.json (no incluido en el repo)
        └── src/
            ├── main/
            │   ├── AndroidManifest.xml
            │   ├── java/com/madicine/deliverycontrol/
            │   │   ├── Activities (*.kt)
            │   │   ├── Entities/
            │   │   ├── Interfaces/
            │   │   ├── Repositories/
            │   │   ├── Services/
            │   │   ├── Utilities/
            │   │   └── viewModels/
            │   └── res/
            │       ├── drawable/
            │       ├── layout/
            │       ├── mipmap-*/
            │       ├── values/
            │       └── xml/
            ├── test/          # Pruebas unitarias
            └── androidTest/   # Pruebas instrumentadas
```

## 🔄 Flujo de la Aplicación

1. Usuario inicia la aplicación (MainActivity)
2. Si hay sesión activa, va directo al MenuPrincipal
3. Si no, debe iniciar sesión o registrarse
4. Tras registro, debe verificar su email
5. En el menú principal puede:
   - Escanear medicamentos
   - Ver lista de medicamentos
   - Consultar historial
   - Cerrar sesión
6. Al escanear un código, se muestra la información del medicamento
7. Opción de reportar problemas con fotografía y descripción

## 👥 Contribución

Este es un proyecto académico. Si deseas contribuir:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -am 'Agrega nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Crea un Pull Request

## 📄 Licencia

Este proyecto es de código abierto y está disponible para fines educativos.

---

**Proyecto Académico** - Práctica de Ingeniería  
**Versión**: 1.0  
**Última actualización**: Febrero 2026
