package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksIds;
    private LocalDateTime endTime;

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (endTime == null) {
            endTime = startTime.plus(duration);
        }
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksIds=" + subTasksIds +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
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

    public Epic(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
        this.subTasksIds = new ArrayList<>();
    }
}