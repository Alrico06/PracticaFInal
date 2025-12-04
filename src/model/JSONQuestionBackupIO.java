package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONQuestionBackupIO {

    private String path;

    public JSONQuestionBackupIO() {
        this.path = System.getProperty("user.home") + "/backup.json";
    }

    public void exportQuestions(List<Question> questions) {
        try (FileWriter writer = new FileWriter(path)) {
            Gson gson = new Gson();
            gson.toJson(questions, writer);
        } catch (Exception e) {
            throw new RuntimeException("Error exporting JSON: " + e.getMessage());
        }
    }

    public List<Question> importQuestions() {
        try (FileReader reader = new FileReader(path)) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Question>>() {}.getType();

            List<Question> list = gson.fromJson(reader, listType);

            if (list == null) return new ArrayList<>();

            return list;

        } catch (Exception e) {
            throw new RuntimeException("Error importing JSON: " + e.getMessage());
        }
    }
}
