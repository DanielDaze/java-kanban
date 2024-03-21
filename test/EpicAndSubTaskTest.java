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
    }

    @Test
    void epicStatusShouldBeNEW() {
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 21, 30), 1);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 19, 30), 1);
        taskManager.createSubTask(subTask2);
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask1.getId());
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask2.getId());
        taskManager.getEpicById(1);
        Assertions.assertEquals(taskManager.getEpics().get(1).getStatus(), Status.NEW);
    }

    @Test
    void epicStatusShouldBeDONE() {
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.DONE, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 21, 30), 1);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.DONE, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 19, 30), 1);
        taskManager.createSubTask(subTask2);
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask1.getId());
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask2.getId());
        taskManager.getEpicById(1);
        Assertions.assertEquals(taskManager.getEpics().get(1).getStatus(), Status.DONE);
    }

    @Test
    void epicStatusShouldBeIN_PROGRESS() {
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 21, 30), 1);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 19, 30), 1);
        taskManager.createSubTask(subTask2);
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask1.getId());
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask2.getId());
        taskManager.getEpicById(1);
        Assertions.assertEquals(taskManager.getEpics().get(1).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void epicDateTimeTest() {
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 21, 30), 1);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2023, 12, 12, 19, 30), 1);
        taskManager.createSubTask(subTask2);
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask1.getId());
        taskManager.getEpics().get(1).getSubTasksIds().add(subTask2.getId());
        taskManager.getEpicById(1);
        Assertions.assertEquals(taskManager.getEpics().get(1).getDuration(), Duration.ofMinutes(60));
        Assertions.assertEquals(taskManager.getEpics().get(1).getEndTime(), LocalDateTime.of(2023, 12, 12, 19, 30).plus(taskManager.getEpics().get(1).getDuration()));
    }
}
