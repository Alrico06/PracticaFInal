package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Representa una pregunta del banco de preguntas.
 * Cada pregunta tiene un UUID único, autor, enunciado, temas y 4 opciones.
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String author;
    private String statement;
    private Set<String> topics;
    private List<Option> options;
    private LocalDateTime creationDate;

    /** Constructor usado cuando se crea una nueva pregunta */
    public Question(
            String author,
            String statement,
            Set<String> topics,
            List<Option> options
    ) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.statement = statement;
        this.topics = normalizeTopics(topics);
        this.options = options;
        this.creationDate = LocalDateTime.now();
    }

    /** Constructor usado cuando se importa desde JSON */
    public Question(
            UUID id,
            String author,
            String statement,
            Set<String> topics,
            List<Option> options,
            LocalDateTime creationDate
    ) {
        this.id = id;
        this.author = author;
        this.statement = statement;
        this.topics = normalizeTopics(topics);
        this.options = options;
        this.creationDate = creationDate;
    }

    // ---- NORMALIZADOR DE TEMAS ---- //
    private Set<String> normalizeTopics(Set<String> raw) {
        Set<String> result = new HashSet<>();
        for (String t : raw) {
            if (t != null && !t.isBlank()) {
                result.add(t.trim().toUpperCase());
            }
        }
        return result;
    }

    // ------------------ GETTERS ------------------ //

    public UUID getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatement() {
        return statement;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public List<Option> getOptions() {
        return options;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // ---------------- MODIFICADORES ---------------- //

    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    public void setStatement(String newStatement) {
        this.statement = newStatement;
    }

    public void setTopics(Set<String> newTopics) {
        this.topics = normalizeTopics(newTopics);
    }

    public void setOptions(List<Option> newOptions) {
        this.options = newOptions;
    }

    @Override
    public String toString() {
        return "Question{id=" + id + ", statement='" + statement + "'}";
    }
}
