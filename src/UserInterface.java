public interface UserInterface {
    void newGameStarted(Field field);

    void update();

    Field load() throws Exception;

    void save() throws Exception;
}
