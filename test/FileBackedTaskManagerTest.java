import model.task.Epic;
import model.Status;
import model.task.SubTask;
import model.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

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
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 3, 16, 8, 30));
        taskManager.createTask(task1);
        taskManager.getTaskById(1);
        Epic epic = new Epic("эпик", "описание эпика", Status.IN_PROGRESS);
        taskManager.createEpic(epic);
        taskManager.getEpicById(2).getSubTasksIds().add(3);
        SubTask subTask = new SubTask("сабтаск", "описание саба", Status.IN_PROGRESS, Duration.ofMinutes(45), LocalDateTime.of(2024, 5, 10, 19, 50), epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.getSubTaskById(3);
        taskManager.getAll();
        FileBackedTaskManager restoredManager = new FileBackedTaskManager(savedData, historyData);
        restoredManager.loadFromFile();
        restoredManager.getAll();
        for (Task task : restoredManager.getHistory()) {
            System.out.println(task);
        }
        Assertions.assertEquals(3, taskManager.getTasks().size() + taskManager.getEpics().size() + taskManager.getSubTasks().size());
    }
}
