package model;

public class SubTask extends Task {
    private Epic epic; // Показывает, к какому эпику относится

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic=" + epic +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public SubTask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }
}