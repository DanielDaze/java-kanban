package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    int generateId();

    void getAll();

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task newTask, int id);

    void updateEpic(Epic newEpic, int id);

    void updateSubTask(SubTask newSubTask, int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void printSubTasksInEpic(int epicId);

    void calculateStatusForEpics();
}
