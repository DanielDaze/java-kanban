package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public boolean checkIfAllNEW() {
        boolean allNEW = false;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == Status.NEW) {
                allNEW = true;
            }
        }
        return allNEW;
    }

    public boolean checkIfAllDONE() {
        boolean allDONE = false;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == Status.NEW) {
                allDONE = true;
            }
        }
        return allDONE;
    }

    public void setEpicStatus() {
        if (subTasks.isEmpty() || checkIfAllNEW()) {
            status = Status.NEW;
        } else if (checkIfAllDONE()) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }
}