Aplicación de Gestión de Preguntas (MVC)
========================================

Proyecto de consola para gestionar un banco de preguntas tipo test: creación, listado, edición, borrado, importación/exportación JSON, generación automática y modo examen.

Estructura de paquetes
----------------------
- `app.App`: arranque de la aplicación.
- `controller/`: coordinación vista ↔ modelo.
- `view/`: interfaz de consola (`InteractiveView`).
- `model/`: dominio (`Question`, `Option`, `ExamSession`, etc.).
- `repository/`: persistencia binaria (`BinaryRepository`).
- `backup/`: importación/exportación JSON.
- `questionCreator/`: generadores automáticos (incl. `GeminiQuestionCreator`).

Persistencia y backups
----------------------
- Datos principales: binario `questions.bin` en el directorio home del usuario.
- Backups: JSON en el mismo directorio home (nombre por defecto `backup.json`).

Ejecución
---------
```bash
javac -d bin $(find src -name "*.java")
java -cp bin App
```

Generación automática con Gemini (opcional)
-------------------------------------------
Inicia con:
```bash
java -cp bin App -question-creator MODEL_ID API_KEY
```
Se puede pasar varios modelos separados por comas en `MODEL_ID`.

Dependencias
------------
- Java 17+
- Librería `com.coti.tools.Esdia` en el classpath (entrada de usuario).
- Para Gemini: fat-jar de integración GenAI en el classpath (solo si se usa `GeminiQuestionCreator`).

Funciones clave
---------------
- CRUD de preguntas (autor, enunciado, 4 opciones con justificación y correcta).
- Listado por tema o global.
- Importar/exportar JSON.
- Generar preguntas automáticas (si hay `QuestionCreator` disponible).
- Modo examen con cálculo de nota y resumen.

Créditos
--------
Práctica final desarrollada por Álvaro Rico. Licencia MIT.
