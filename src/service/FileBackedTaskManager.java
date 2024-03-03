package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;

import static model.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    Path savedData = Path.of("savedData.csv");
    Path historyData = Path.of("historyData.csv");

    private void save() throws ManagerSaveException {
        HashMap<Integer, Task> tasks = getTasks();
        HashMap<Integer, Epic> epics = getEpics();
        HashMap<Integer, SubTask> subTasks = super.getSubTasks();
        try (FileWriter writer = new FileWriter(String.valueOf(savedData))) {
            writer.write("id,type,name,status,description,epic/subtasks" + "\n");
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
                task.getDescription() +
                "\n";
    }

    private String epicToString(Epic epic) {
        StringBuilder builder = new StringBuilder();
        builder.append(epic.getId()).append(",");
        builder.append(EPIC).append(",");
        builder.append(epic.getTitle()).append(",");
        builder.append(epic.getStatus()).append(",");
        builder.append(epic.getDescription()).append(",");
        for (Integer id : epic.getSubTasksIds()) {
            builder.append(id).append(" ");
        }
        builder.append("\n");
        return builder.toString();
    }

    private String subTaskToString(SubTask subTask) {
        return subTask.getId() + "," +
                SUBTASK + "," +
                subTask.getTitle() + "," +
                subTask.getStatus() + "," +
                subTask.getDescription() + "," +
                subTask.getEpicId() +
                "\n";
    }

    private String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder("id,type,name,status,description,epic/subtasks" + "\n");
        for (Task task : manager.getHistory()) {
            if (task instanceof Epic epic) {
                history.append(epicToString(epic)).append("\n");
            } else if (task instanceof SubTask subTask) {
                history.append(subTaskToString(subTask)).append("\n");
            } else {
                history.append(taskToString(task)).append("\n");
            }
        }
        return history.toString();
    }

    private void restoreHistory() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(historyData.toString(), StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String value = reader.readLine();
                getHistory().add(fromString(value));
            }
        }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");

        switch (split[1]) {
            case "EPIC" -> {
                Status status = Status.valueOf(split[3]);
                Epic epic = new Epic(split[2], split[4], status);
                epic.setId(Integer.parseInt(split[0]));
                String[] idsString = split[5].split(" ");
                for (int i = 0; i < idsString.length - 1; i++) {
                    epic.getSubTasksIds().add(Integer.valueOf(idsString[i]));
                }
                return epic;
            }
            case "SUBTASK" -> {
                Status status = Status.valueOf(split[3]);
                SubTask subTask = new SubTask(split[2], split[4], status, Integer.parseInt(split[5]));
                subTask.setId(Integer.parseInt(split[0]));
                return subTask;
            }
            case "TASK" -> {
                Status status = Status.valueOf(split[3]);
                Task task = new Task(split[2], split[4], status);
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
                    createEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    createSubTask((SubTask) task);
                } else if (task != null) {
                    createTask(task);
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
        save();
        return getTasks().get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(getEpics().get(id));
        save();
        return getEpics().get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(getSubTasks().get(id));
        save();
        return getSubTasks().get(id);
    }
}