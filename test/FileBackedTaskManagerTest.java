import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest {
    Path savedData;
    Path historyData;

    @Test
    void newManagerShouldReturnTasksFromPreviousManager() {
        try {
            savedData = Files.createTempFile("savedData", ".csv");
            historyData = Files.createTempFile("historyData", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager taskManager = new FileBackedTaskManager(savedData, historyData);
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW);
        taskManager.createTask(task1);
        taskManager.getTaskById(1);
        Epic epic = new Epic("эпик", "описание эпика", Status.IN_PROGRESS);
        epic.getSubTasksIds().add(3);
        taskManager.createEpic(epic);
        taskManager.getEpicById(2);
        SubTask subTask = new SubTask("сабтаск", "описание саба", Status.IN_PROGRESS, 2);
        taskManager.createSubTask(subTask);
        taskManager.getSubTaskById(3);
        FileBackedTaskManager restoredManager = new FileBackedTaskManager(savedData, historyData);
        restoredManager.loadFromFile();
        restoredManager.getAll();
        for (Task task : restoredManager.getHistory()) {
            System.out.println(task);
        }
    }
}
