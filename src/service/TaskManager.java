package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int seq = 0;

    public int generateId() {
        return ++seq;
    }
    public void getAll() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subTaskId : epic.getSubTasksIds()) {
                subTasks.remove(subTaskId);
            }
        }
    }

    public void clearSubTasks() {
        subTasks.clear();
        calculateStatusForEpics();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
    }

    public void updateTask(Task newTask, int id) {
        tasks.get(id).setId(newTask.getId());
        tasks.get(id).setTitle(newTask.getTitle());
        tasks.get(id).setDescription(newTask.getDescription());
        tasks.get(id).setStatus(newTask.getStatus());
    }

    public void updateEpic(Epic newEpic, int id) {
        epics.get(id).setId(newEpic.getId());
        epics.get(id).setTitle(newEpic.getTitle());
        epics.get(id).setDescription(newEpic.getDescription());
        epics.get(id).setStatus(newEpic.getStatus());
    }

    public void updateSubTask(SubTask newSubTask, int id) {
        subTasks.get(id).setId(newSubTask.getId());
        subTasks.get(id).setTitle(newSubTask.getTitle());
        subTasks.get(id).setDescription(newSubTask.getDescription());
        subTasks.get(id).setStatus(newSubTask.getStatus());
        calculateStatusForEpics();
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        for (Integer subTaskId : getEpicById(id).getSubTasksIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    public void removeSubTaskById(int id) {
        subTasks.remove(id);
        calculateStatusForEpics();
    }

    public void printSubTasksInEpic(int epicId) {
        System.out.println(epics.get(epicId).getSubTasksIds());
    }

    public void calculateStatusForEpics() {
        for (Epic epic : epics.values()) {
            int checkNumber = 0;
            boolean checkIfAllNEW = false;
            boolean checkIfAllDONE = false;
            for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
                if (subTasks.get(epic.getSubTasksIds().get(i)).getStatus() == Status.NEW) {
                    checkNumber++;
                    if (checkNumber == epic.getSubTasksIds().size()) {
                        checkIfAllNEW = true;
                    }
                }
            }
            for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
                if (subTasks.get(epic.getSubTasksIds().get(i)).getStatus() == Status.DONE) {
                    checkNumber++;
                    if (checkNumber == epic.getSubTasksIds().size()) {
                        checkIfAllDONE = true;
                    }
                }
            }
            if (checkIfAllNEW) {
                epic.setStatus(Status.NEW);
            } else if (checkIfAllDONE) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public TaskManager() { // Дефолтный конструктор
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
}
