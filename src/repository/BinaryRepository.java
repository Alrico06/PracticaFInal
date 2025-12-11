package repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Question;

public class BinaryRepository implements IRepository {

    private final Path filePath;
    private List<Question> cache;

    public BinaryRepository(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            fileName = "questions.bin";
        }
        Path homeDir = Path.of(System.getProperty("user.home"));
        try {
            Files.createDirectories(homeDir);
        } catch (Exception e) {

        }
        this.filePath = homeDir.resolve(fileName);
        this.cache = loadFromDisk();
    }

    @Override
    public Question addQuestion(Question q) throws RepositoryException {
        cache.add(q);
        return q;
    }

    @Override
    public void removeQuestion(Question q) throws RepositoryException {
        cache.removeIf(existing -> existing.getId().equals(q.getId()));
    }

    @Override
    public Question modifyQuestion(Question q) throws RepositoryException {
        boolean replaced = false;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(q.getId())) {
                cache.set(i, q);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            throw new RepositoryException("Question not found: " + q.getId());
        }
        return q;
    }

    @Override
    public List<Question> getAllQuestions() throws RepositoryException {
        return new ArrayList<>(cache);
    }

    @Override
    public void saveAll(List<Question> questions) throws RepositoryException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            cache = new ArrayList<>(questions);
            out.writeObject(cache);
        } catch (Exception e) {
            throw new RepositoryException("Error saving binary data", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Question> loadFromDisk() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            Object obj = in.readObject();
            if (obj instanceof List) {
                return (List<Question>) obj;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
