package model;

public class SubTask extends Task {
    private int epicId; //Показывает, к какому эпику относится

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public SubTask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epicId = epic.id;
    }
}
