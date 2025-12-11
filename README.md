Aplicación de Gestión de Preguntas (MVC)
========================================

Aplicación de consola para administrar un banco de preguntas tipo test: crear, editar, borrar, importar/exportar en JSON, generar automáticamente con Gemini u otros creadores y ejecutar exámenes con calificación.

Arquitectura (paquetes)
-----------------------
- `app.App`: arranque de la aplicación.
- `controller/`: coordinación vista ↔ modelo.
- `view/`: interfaz de consola (`InteractiveView`).
- `model/`: dominio (`Question`, `Option`, `ExamSession`, `ExamResult`).
- `repository/`: persistencia binaria (`BinaryRepository`, `IRepository`).
- `backup/`: importación/exportación JSON.
- `questionCreator/`: generadores automáticos (`GeminiQuestionCreator`, `SimpleQuestionCreator`).

Almacenamiento
--------------
- Principal: archivo binario `questions.bin` en el directorio home del usuario.
- Backups: JSON en el mismo directorio (nombre por defecto `backup.json`).

Requisitos
----------
- Java 17 o superior.
- `com.coti.tools.Esdia` en el classpath (entrada por consola).
- Opcional Gemini: fat-jar de integración GenAI en el classpath.

Compilación y ejecución rápidas
-------------------------------
```bash
javac -d bin $(find src -name "*.java")
java -cp bin App
```

Generación automática (opcional)
--------------------------------
```bash
java -cp bin App -question-creator MODEL_ID API_KEY
```
Se permiten varios modelos separados por comas en `MODEL_ID`.

Funciones principales
---------------------
- CRUD completo de preguntas (autor, enunciado, 4 opciones con justificación y correcta).
- Listados globales o filtrados por tema.
- Importación y exportación JSON desde el directorio home.
- Generación automática con cualquier `QuestionCreator` disponible (Gemini incluido).
- Modo examen con selección de tema, número de preguntas y cálculo de nota (si no hay preguntas se muestra un aviso).

Proyecto desarrollado por Álvaro Rico Refoyo.
