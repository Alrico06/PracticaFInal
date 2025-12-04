package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryRepository implements IRepository {

    private String filename;

    public BinaryRepository(String filename) {
        this.filename = filename;
    }

    @Override
    public void save(List<Question> questions) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(questions);
        } catch (Exception e) {
            System.err.println("Error saving binary data: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Question> load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Question>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // Sin datos o error → colección vacía
        }
    }
}
