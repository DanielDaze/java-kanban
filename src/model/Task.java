package model;

public class Task {
    protected String title; // Название
    protected String description; // Описание
    protected int id;
    protected Status status; // Статус (значение enum Status)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public Task(String title, String description, Status status) { //Конструктор
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
