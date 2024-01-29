import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

public class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("2 задача", "описание 2 задачи", Status.NEW);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("1 эпик", "описание 1 эпика", Status.NEW);
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("1 подзадача", "1 эпик", Status.NEW, epic1.getId());
        taskManager.createSubTask(subTask1);
        epic1.getSubTasksIds().add(taskManager.getSubTaskById(4).getId());
        Epic epic2 = new Epic("2 эпик", "описание 2 эпика", Status.NEW);
        SubTask subTask2 = new SubTask("2 подзадача", "1 эпик", Status.NEW, epic1.getId());
        taskManager.createSubTask(subTask2);
        epic2.getSubTasksIds().add(taskManager.getSubTaskById(5).getId());
    }
    @Test
    void shouldPrintAll() {
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

        SubTask newSubTask = new SubTask("изм_задача", "изм_описание", Status.DONE, 3);
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

        SubTask subTask = new SubTask("задача", "описание", Status.NEW, 2);
        subTask.setId(4);
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(4));
    }

    @Test
    void removedTasksShouldBeNull() {
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);
        Assertions.assertNull(taskManager.getTaskById(1));
        Assertions.assertNull(taskManager.getEpicById(3));
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

}
