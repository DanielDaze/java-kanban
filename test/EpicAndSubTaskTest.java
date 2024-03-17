import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class EpicAndSubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2025, 12, 12, 21, 30), epic1.getId());
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(50), LocalDateTime.of(2022, 11, 10, 11, 24), epic1.getId());
        taskManager.createSubTask(subTask2);
        epic1.getSubTasksIds().add(subTask1.getId());
        epic1.getSubTasksIds().add(subTask2.getId());
    }

    @Test
    void shouldBecomeString() {
        System.out.println(taskManager.getSubTaskById(2).toString());
    }

    @Test
    void shouldGetEpicIdFromSubTask() {
        Assertions.assertEquals(1, taskManager.getSubTaskById(2).getEpicId());
    }
}
