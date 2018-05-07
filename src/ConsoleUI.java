import javax.print.attribute.standard.MediaSizeName;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ConsoleUI implements UserInterface, Serializable {

    private Field field;
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String FILENAME = "savedPlay.bin";
    File fileSaved = new File(FILENAME);
    Date startOfGame;
    TimeWatch watch = TimeWatch.start();
    BestTimes bestTimes = new BestTimes();

    long passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
    @Override
    public void newGameStarted(Field field) {
        this.field = field;
        do {
            update();
            try {
                processInput();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(field.getState() == GameState.SOLVED){
                System.out.println(ANSI_GREEN + "------------------------------------------------");
                System.out.println("Wohoooo! YOU WIN !!" + ANSI_RESET);
                System.out.println("Enter your NAME: ");
                Scanner scanner = new Scanner(System.in);
                String inputName = scanner.nextLine();

                bestTimes.addPlayerTime(inputName, (int) watch.time(TimeUnit.SECONDS));
                return;
            } else if(field.getState() == GameState.EXIT){
                return;
            } else if (field.getState() == GameState.NEWGAME){
                System.out.println(ANSI_YELLOW + "------------------------");
                System.out.println("New game created." + ANSI_RESET);
                field.generate();
            }

        } while(true);
    }

    @Override
    public void update(){
        prinHeaderLines();

        for (int row = 0; row < field.getRowCount(); row++){
            System.out.print("\n|");
            for(int column = 0; column < field.getColumnCount(); column++){
                //System.out.print(row + "---" + column);
                int valueOfStone = field.getStone(row, column).getValueOfStone();
                if(valueOfStone == 0){
                    System.out.printf("     |");
                }
                else if (valueOfStone < 10) {
                    System.out.printf("  " + valueOfStone + "  |");
                } else {
                    System.out.printf(" " + valueOfStone + "  |");
                }
            }
            System.out.println();
            prinHeaderLines();
        }
        long minute = watch.time(TimeUnit.MINUTES);
        long second = watch.time(TimeUnit.SECONDS) - minute*60;
        System.out.println(ANSI_YELLOW + "\nGame is ON: " + minute + " min " + second + " sec "+ ANSI_RESET);
    }

    private void processInput() throws IOException {
        System.out.println("\nMoves:" + ANSI_GREEN +  " w,s,a,d" + ANSI_RESET +
                "(up, down, left, right) \nOptions:" + ANSI_GREEN + " exit,new,time" + ANSI_RESET + "(save and exit, new game, best times)");
        System.out.println("Choose wisely: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        switch (input.toLowerCase()){
            case "w":
                field.move(Direction.UP);
                break;
            case "s":
                field.move(Direction.DOWN);
                break;
            case "a":
                field.move(Direction.LEFT);
                break;
            case "d":
                field.move(Direction.RIGHT);
                break;
            case "new":
                field.setState(GameState.NEWGAME);
                if(fileSaved.exists()) {
                    fileSaved.delete();
                }
                break;
            case "time":
                System.out.println(bestTimes.toString());
                break;
            case "exit":
                try {
                    save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void prinHeaderLines(){
        for(int i = 0; i < field.getColumnCount(); i++){
            System.out.printf(" -----");
        }
    }

    public void printPlayertime(){
        System.out.println(bestTimes.toString());
    }

    @Override
    public void save() throws  Exception {
        try (FileOutputStream os = new FileOutputStream(FILENAME, false);
             ObjectOutputStream oos = new ObjectOutputStream(os);) {
            oos.writeObject(field);
        }
        field.state = GameState.EXIT;
    }

    @Override
    public Field load() throws Exception {
        try (FileInputStream is = new FileInputStream(FILENAME);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Field) ois.readObject();
        }
    }
}
