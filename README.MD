Proyecto de Fondos BTG Pactual
Este proyecto implementa una API REST para la gestión de fondos de inversión de BTG Pactual. Los usuarios pueden suscribirse a fondos, cancelar suscripciones y consultar el historial de transacciones.

Tabla de Contenidos
Requisitos previos
Configuración del proyecto
Configuración de la base de datos (MongoDB)
Compilación del proyecto
Ejecución de la aplicación
Uso de la API
Ejecución de pruebas
Detener la aplicación y limpiar
Solución de problemas
Requisitos previos
Asegúrate de tener instalados los siguientes componentes:

Java JDK 17 o superior
Gradle 6.8 o superior
Docker y Docker Compose
Git (opcional, para clonar el repositorio)
Configuración del proyecto
Clona el repositorio o descarga el código fuente:

git clone https://github.com/vestrada22/bgtpactual-backend
cd btg-pactual-fondos
Configura las propiedades de la aplicación:

Abre el archivo src/main/resources/application.properties.
Ajustar las propiedades para el envio de correos
Ajustar propiedades envio de SMS con Twilio, requerira registrarse gratuitamente para obtener estos datos.
Ajusta las propiedades de MongoDB según sea necesario (por defecto, debería funcionar con la configuración de Docker proporcionada).
Configuración de la base de datos (MongoDB)
Asegúrate de que Docker esté en ejecución.

Desde la raíz del proyecto, ejecuta el siguiente comando para iniciar MongoDB o inicia la aplicación esto ejecutara el comando para levantar docker:

docker-compose up -d
Este comando iniciará un contenedor de MongoDB con la configuración especificada en el archivo docker-compose.yml.

Compilación del proyecto
Para compilar el proyecto, ejecuta el siguiente comando en la raíz del proyecto:

./gradlew build
Este comando compilará el código, ejecutará las pruebas y generará un archivo JAR ejecutable.

Ejecución de la aplicación
Para ejecutar la aplicación, usa uno de los siguientes comandos:
Desde un IDE como IntelliJ solo configura la aplicación y la ejecutas, de esta manera funcionará localmente.

Ejecutar el JAR generado:

java -jar build/libs/btg-pactual-fondos-0.0.1-SNAPSHOT.jar
Alternativamente, puedes usar el comando de Gradle:

./gradlew bootRun
La aplicación debería estar accesible en http://localhost:8080.

Uso de la API
Una vez que la aplicación esté en ejecución, puedes interactuar con la API utilizando herramientas como cURL, Postman o cualquier cliente HTTP. Aquí algunos ejemplos de endpoints:

Obtener todos los fondos:


GET http://localhost:8080/api/funds
Suscribirse a un fondo:

POST http://localhost:8080/api/funds/{fundId}/subscribe
Cancelar suscripción:

POST http://localhost:8080/api/funds/{fundId}/cancel
Ver historial de transacciones:

GET http://localhost:8080/api/funds/transactions
Consulta la documentación de la API o los controladores para obtener más detalles sobre los endpoints disponibles y sus parámetros.

Ejecución de pruebas
Para ejecutar las pruebas unitarias y de integración, usa el siguiente comando:

./gradlew test
Detener la aplicación y limpiar
Para detener la aplicación, presiona Ctrl+C en la terminal donde se está ejecutando.

Para detener y eliminar el contenedor de MongoDB, ejecuta:

docker-compose down

CloudFormation: Para el despligue en CloudFormation se creó un archivo bgtpactual-cloudformation.yml el cual tiene la configuraciones necesaria para el despliegue.
Se debe cambiar algunos datos datos como el nombre del keypair generado en AWS.


Solución de problemas
Si encuentras problemas al configurar o ejecutar la aplicación, intenta lo siguiente:

Asegúrate de que todos los requisitos previos estén instalados y actualizados.
Verifica que el contenedor de MongoDB esté en ejecución con docker ps.
Revisa los logs de la aplicación para ver si hay mensajes de error específicos.
Asegúrate de que el puerto 8080 no esté siendo utilizado por otra aplicación.
Si el problema persiste, abre un issue en el repositorio del proyecto con una descripción detallada del problema y los pasos para reproducirlo.

Mejoras en el formato:
Se ha añadido un índice para facilitar la navegación.
Uso de bloques de código (```) para comandos y endpoints.
Subtítulos claros para cada sección.