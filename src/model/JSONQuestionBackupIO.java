package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de exportación/importación en JSON.
 */
public class JSONQuestionBackupIO implements QuestionBackupIO {

    private final Gson gson;
    private final Path dataDir;

    public JSONQuestionBackupIO() {
        this.dataDir = Path.of(System.getProperty("user.home"));
        try {
            Files.createDirectories(dataDir);
        } catch (Exception ignored) {
        }
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .create();
    }

    @Override
    public void exportQuestions(List<Question> questions, String fileName) throws QuestionBackupIOException {
        Path target = resolvePath(fileName);
        try (FileWriter writer = new FileWriter(target.toFile())) {
            gson.toJson(questions, writer);
        } catch (Exception e) {
            throw new QuestionBackupIOException("Error exporting JSON to " + target, e);
        }
    }

    @Override
    public List<Question> importQuestions(String fileName) throws QuestionBackupIOException {
        Path target = resolvePath(fileName);
        try (FileReader reader = new FileReader(target.toFile())) {
            Type listType = new TypeToken<List<Question>>() {}.getType();
            List<Question> list = gson.fromJson(reader, listType);
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            throw new QuestionBackupIOException("Error importing JSON from " + target, e);
        }
    }

    @Override
    public String getBackupIODescription() {
        return "JSON backup in user home directory";
    }

    private Path resolvePath(String fileName) {
        String normalized = (fileName == null || fileName.isBlank()) ? "backup.json" : fileName;
        return dataDir.resolve(normalized);
    }
}
