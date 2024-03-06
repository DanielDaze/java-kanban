import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();

    @BeforeEach
    void beforeEach() {
        historyManager.add(new Task("Задача", "описание", Status.NEW));
    }

    @Test
    void historySizeShouldBeEqual() {
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }
}
