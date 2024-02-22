package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    void printAll();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubTasks();

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    void create(Task task);

    void create(Epic epic);

    void create(SubTask subTask);

    void updateTask(Task newTask, int id);

    void updateEpic(Epic newEpic, int id);

    void updateSubTask(SubTask newSubTask, int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void printSubTasksInEpic(int epicId);
}
