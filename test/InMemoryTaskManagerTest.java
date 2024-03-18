import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2021, 10, 8, 11, 30));
        taskManager.createTask(task1);
        Task task2 = new Task("2 задача", "описание 2 задачи", Status.NEW, Duration.ofMinutes(8), LocalDateTime.of(2022, 10, 8, 11, 30));
        taskManager.createTask(task2);

        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, Duration.ofMinutes(17), LocalDateTime.of(2023, 10, 8, 11, 30), epic1.getId());
        taskManager.createSubTask(subTask1);
        taskManager.getEpics().get(3).getSubTasksIds().add(4);

        Epic epic2 = new Epic("2 эпик", "описание 2 эпика", Status.NEW);
        taskManager.createEpic(epic2);
        SubTask subTask2 = new SubTask("2 подзадача", "2 эпик", Status.NEW, Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 8, 11, 30), epic2.getId());
        taskManager.createSubTask(subTask2);
        taskManager.getEpics().get(5).getSubTasksIds().add(6);
    }
    @Test
    void shouldGetAll() {
        taskManager.getAll();
    }

    @Test
    void shouldClearEachList() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubTasks();

        Assertions.assertEquals(0, taskManager.getTasks().size());
        Assertions.assertEquals(0, taskManager.getEpics().size());
        Assertions.assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    void shouldGetNewTasks() {
        Task newTask = new Task("изм_задача", "изм_описание", Status.IN_PROGRESS);
        newTask.setId(1);
        taskManager.updateTask(newTask, 1);
        Assertions.assertEquals(newTask, taskManager.getTaskById(1));

        Epic newEpic = new Epic("изм_задача", "изм_описание", Status.IN_PROGRESS);
        newEpic.setId(3);
        newEpic.getSubTasksIds().add(4);
        taskManager.updateEpic(newEpic, 3);
        Assertions.assertEquals(newEpic, taskManager.getEpicById(3));

        SubTask newSubTask = new SubTask("изм_задача", "изм_описание", Status.DONE, Duration.ofMinutes(10), LocalDateTime.of(2024, 8, 8, 8, 8), 3);
        newSubTask.setId(4);
        taskManager.updateSubTask(newSubTask, 4);
        Assertions.assertEquals(newSubTask, taskManager.getSubTaskById(4));
    }

    @Test
    void tasksShouldBeEqualById() {
        Task task = new Task("задача", "описание", Status.NEW);
        task.setId(1);
        Assertions.assertEquals(task, taskManager.getTaskById(1));

        Epic epic = new Epic("задача", "описание", Status.NEW);
        epic.setId(3);
        Assertions.assertEquals(epic, taskManager.getEpicById(3));

        SubTask subTask = new SubTask("задача", "описание", Status.NEW, Duration.ofMinutes(12), LocalDateTime.of(2024, 9, 9, 9, 9), 2);
        subTask.setId(4);
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(4));
    }

    @Test
    void taskMapsShouldBeEmpty() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubTasks();
        Assertions.assertEquals(0, taskManager.getTasks().size());
        Assertions.assertEquals(0, taskManager.getEpics().size());
        Assertions.assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    void checkTasksDeletion() {
        taskManager.removeTaskById(1);
        Assertions.assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void checkSubTasksDeletion() {
        taskManager.removeSubTaskById(6);
        Assertions.assertEquals(1, taskManager.getSubTasks().size());
    }

    @Test
    void checkEpicsDeletion() {
        taskManager.removeEpicById(3);
        taskManager.removeEpicById(5);
        Assertions.assertEquals(0, taskManager.getEpics().size());
    }
}
