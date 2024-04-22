package service;

import exception.ManagerSaveException;
import model.*;
import model.task.Epic;
import model.task.SubTask;
import model.task.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static model.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String HEADER = "id,type,name,status,description,duration,startTime,epic/subtasks" + "\n";
    Path savedData;
    Path historyData;

    private void save() throws ManagerSaveException {
        HashMap<Integer, Task> tasks = getTasks();
        HashMap<Integer, Epic> epics = getEpics();
        HashMap<Integer, SubTask> subTasks = super.getSubTasks();
        try (FileWriter writer = new FileWriter(String.valueOf(savedData))) {
            writer.write(HEADER);
            for (Task task : tasks.values()) {
                writer.append(taskToString(task));
            }
            for (Epic epic : epics.values()) {
                writer.append(epicToString(epic));
            }
            for (SubTask subTask : subTasks.values()) {
                writer.append(subTaskToString(subTask));

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи информации в файл");
        }
    }

    private void saveHistory() {
        try (FileWriter writer = new FileWriter(String.valueOf(historyData))) {
            writer.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи информации в файл");
        }
    }

    private String taskToString(Task task) {
        return task.getId() + "," +
                TASK + "," +
                task.getTitle() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getDuration() + "," +
                task.getStartTime() +
                "\n";
    }

    private String epicToString(Epic epic) {
        StringBuilder builder = new StringBuilder();
        builder.append(epic.getId()).append(",");
        builder.append(EPIC).append(",");
        builder.append(epic.getTitle()).append(",");
        builder.append(epic.getStatus()).append(",");
        builder.append(epic.getDescription()).append(",");
        builder.append(epic.getDuration()).append(",");
        builder.append(epic.getStartTime()).append(",");
        epic.getSubTasksIds().forEach(id -> builder.append(id).append(" "));
        builder.append("\n");
        return builder.toString();
    }

    private String subTaskToString(SubTask subTask) {
        return subTask.getId() + "," +
                SUBTASK + "," +
                subTask.getTitle() + "," +
                subTask.getStatus() + "," +
                subTask.getDescription() + "," +
                subTask.getDuration() + "," +
                subTask.getStartTime() + "," +
                subTask.getEpicId() +
                "\n";
    }

    private String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder(HEADER);
        for (Task task : manager.getHistory()) {
            if (task instanceof Epic epic) {
                history.append(epicToString(epic));
            } else if (task instanceof SubTask subTask) {
                history.append(subTaskToString(subTask));
            } else {
                history.append(taskToString(task));
            }
        }
        return history.toString();
    }

    private void restoreHistory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(historyData.toString(), StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String value = reader.readLine();
                Task task = fromString(value);
                if (task != null) {
                    historyManager.add(task);
                }
            }
        }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");

        switch (split[1]) {
            case "SUBTASK" -> {
                Status status = Status.valueOf(split[3]);
                SubTask subTask = new SubTask(split[2], split[4], status, Duration.parse(split[5]), LocalDateTime.parse(split[6]), Integer.parseInt(split[7]));
                subTask.setId(Integer.parseInt(split[0]));
                return subTask;
            }
            case "EPIC" -> {
                Status status = Status.valueOf(split[3]);
                Epic epic = new Epic(split[2], split[4], status, Duration.parse(split[5]), LocalDateTime.parse(split[6]));
                epic.setId(Integer.parseInt(split[0]));
                String[] idsString = split[7].split(" ");
                if (idsString.length == 1) {
                    epic.getSubTasksIds().add(Integer.valueOf(idsString[0]));
                } else {
                    for (int i = 0; i < idsString.length - 1; i++) {
                        epic.getSubTasksIds().add(Integer.valueOf(idsString[i]));
                    }
                }
                return epic;
            }
            case "TASK" -> {
                Status status = Status.valueOf(split[3]);
                Task task = new Task(split[2], split[4], status, Duration.parse(split[5]), LocalDateTime.parse(split[6]));
                task.setId(Integer.parseInt(split[0]));
                return task;
            }
            default -> {
                return null;
            }
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(savedData.toString(), StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                Task task = fromString(reader.readLine());
                if (task instanceof Epic) {
                    restoreEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    restoreSubTask((SubTask) task);
                } else if (task != null) {
                    restoreTask(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            restoreHistory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void restoreTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    private void restoreEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    private void restoreSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
    }

    public FileBackedTaskManager(Path savedData, Path historyData) {
        this.savedData = savedData;
        this.historyData = historyData;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public void getAll() {
        super.getAll();
        save();
        saveHistory();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task newTask, int id) {
        super.updateTask(newTask, id);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        super.updateEpic(newEpic, id);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask, int id) {
        super.updateSubTask(newSubTask, id);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(getTasks().get(id));
        saveHistory();
        return getTasks().get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(getEpics().get(id));
        save();
        saveHistory();
        return getEpics().get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(getSubTasks().get(id));
        saveHistory();
        return getSubTasks().get(id);
    }

}