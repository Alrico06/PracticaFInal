📌 Descripción del proyecto

Este proyecto implementa un sistema de gestión de preguntas tipo test siguiendo el patrón Modelo–Vista–Controlador (MVC).
Permite crear, listar, modificar y eliminar preguntas, gestionar archivos JSON de importación/exportación, generar preguntas automáticas y ejecutar un modo examen.

El objetivo es desarrollar una aplicación estructurada, modular y fácilmente extensible, acorde al enunciado de la práctica final.

🏛 Arquitectura del proyecto (MVC)

El proyecto está organizado en tres paquetes principales:

src/
 ├─ controller/
 │    └─ Controller.java
 ├─ model/
 │    ├─ Model.java
 │    ├─ Question.java
 │    ├─ Option.java
 │    ├─ ExamResult.java
 │    ├─ IRepository.java
 │    ├─ BinaryRepository.java
 │    ├─ JSONQuestionBackupIO.java
 │    ├─ QuestionCreator.java
 │    └─ (posibles implementaciones adicionales)
 └─ view/
      ├─ BaseView.java
      └─ InteractiveView.java

✔ Modelo

Gestiona los datos y la lógica interna (preguntas, opciones, repositorios, carga/guardado).

✔ Vista

Implementada mediante consola (InteractiveView), es responsable de mostrar menús, recibir entradas del usuario y delegar toda la lógica al controlador.

✔ Controlador

Coordina la comunicación entre la vista y el modelo.
Actúa como "director de orquesta" del flujo del programa.

✨ Funciones principales
🔹 CRUD de preguntas

Crear preguntas (autor, temas, enunciado, 4 opciones, rationale y opción correcta)

Listar preguntas (todas o filtradas por tema)

Ver detalle de una pregunta

Modificar pregunta (autor, temas, enunciado, opciones)

Eliminar pregunta

🔹 Importación / Exportación JSON

Exporta todas las preguntas y temas a data/backup.json (o el nombre indicado) dentro del proyecto

Importa preguntas desde el mismo directorio data/

No importa elementos con UUID repetido

🔹 Generación automática de preguntas

Solo disponible si hay QuestionCreator cargado

Solicita un tema

Genera una pregunta automáticamente

Muestra una vista previa

Permite al usuario añadirla o descartarla

🔹 Modo Examen

Selección de número de preguntas

Filtrado por tema o “ALL”

Presentación secuencial de preguntas

Registro de aciertos / fallos / no respondidas

Cálculo de nota final sobre 10

Resumen detallado con instancia ExamResult

🧪 Uso
▶ Compilación
javac -d bin $(find src -name "*.java")

▶ Ejecución
java -cp bin app.App


(Dependiendo de tu estructura de proyecto.)

📦 Requisitos

Java 17+

Biblioteca com.coti.tools.Esdia para entradas seguras

Git (opcional, para control de versiones)

📚 Características técnicas destacadas

Arquitectura MVC estricta

Separación clara de responsabilidades

Repositorio binario y repositorio JSON

Normalización automática de temas (mayúsculas)

Manejo robusto de entradas del usuario

Control de errores en operaciones del modelo

Código modular, escalable y mantenible

🙌 Autor

Proyecto desarrollado por Álvaro Rico, como práctica final de Programación Orientada a Objetos.

📄 Licencia

Este proyecto está disponible bajo la licencia MIT.
Puedes usar, modificar y distribuir libremente citando al autor.

