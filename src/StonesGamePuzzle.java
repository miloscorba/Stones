import java.io.File;

public class StonesGamePuzzle {

    private UserInterface userInterface;

    public StonesGamePuzzle() {
        userInterface = new ConsoleUI();

        File f = new File("savedPlay.bin");
        if(f.exists()){
            try {
                userInterface.newGameStarted(userInterface.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Field field = new Field(3,3);
            userInterface.newGameStarted(field);
        }
    }

    public static void main(String[] args) {
        new StonesGamePuzzle();
    }

}
