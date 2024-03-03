package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksIds;


    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksIds=" + subTasksIds +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return id == epic.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds);
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.subTasksIds = new ArrayList<>();
    }
}