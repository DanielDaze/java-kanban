package service;

public interface Managers {
    public TaskManager getDefault();

    public InMemoryHistoryManager getDefaultHistory();
}
