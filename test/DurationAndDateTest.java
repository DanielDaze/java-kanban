import exception.DateTimeConflict;
import model.Status;
import model.task.Epic;
import model.task.SubTask;
import model.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class DurationAndDateTest {
    @Test
    public void shouldGetStartTimeDurationEndTime() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 3, 16, 8, 30));
        taskManager.createTask(task1);
        taskManager.getTaskById(1);
        Epic epic = new Epic("эпик", "описание эпика", Status.IN_PROGRESS);
        taskManager.createEpic(epic);
        taskManager.getEpics().get(2).getSubTasksIds().add(3);
        taskManager.getEpics().get(2).getSubTasksIds().add(4);
        SubTask subTask = new SubTask("сабтаск", "описание саба", Status.IN_PROGRESS, Duration.ofMinutes(45), LocalDateTime.of(2024, 5, 10, 19, 50), epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.getSubTaskById(3);
        SubTask subtask2 = new SubTask("сабтаск", "описание саба", Status.IN_PROGRESS, Duration.ofMinutes(5), LocalDateTime.of(2024, 5, 10, 17, 50), epic.getId());
        taskManager.createSubTask(subtask2);
        taskManager.getAll();
        Assertions.assertEquals(task1.getStartTime().plus(task1.getDuration()), taskManager.getTaskById(1).getEndTime());
        Assertions.assertEquals(epic.getStartTime().plus(epic.getDuration()), taskManager.getEpicById(2).getEndTime());
        Assertions.assertEquals(subTask.getStartTime().plus(subTask.getDuration()), taskManager.getSubTaskById(3).getEndTime());

        Assertions.assertTrue(taskManager.getPrioritizedTasks().getFirst().getStartTime().isBefore(taskManager.getPrioritizedTasks().getLast().getStartTime()));
    }

    @Test
    public void overlappingTaskShouldntBeCreated() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("1 задача", "описание 1 задачи", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 3, 16, 8, 30));
        taskManager.createTask(task1);
        taskManager.getTaskById(1);
        Task overlappingTask = new Task("1 задача", "описание 1 задачи", Status.NEW, Duration.ofMinutes(50), LocalDateTime.of(2024, 3, 16, 8, 20));
        Assertions.assertThrows(DateTimeConflict.class, () -> taskManager.createTask(overlappingTask));
        Assertions.assertEquals(1, taskManager.getTasks().size());
    }
}
