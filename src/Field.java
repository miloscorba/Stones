import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Field represents playing field and game logic.
 */
public class Field implements Serializable {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private int rowCount;
    private int columnCount;
    private Stone[][] stones;
    public GameState state = GameState.PLAYING;
    private StoneMoveable stoneMoveable;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        generate();
        getMoveAbleStone();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public GameState getState() {
        return state;
    }

    public Stone getStone(int row, int column) {
        return stones[row][column];
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void generate() {
        int row, column = 0;
        int numberOfStones = getColumnCount()*getRowCount()-1;
        boolean stoneOfMoving = true;
        Random random = new Random();
        stones = null;
        stones = new Stone[this.rowCount][this.columnCount];

        while (numberOfStones != 0) {
            row = random.nextInt(rowCount);
            column = random.nextInt(columnCount);

            if(stoneOfMoving){
                if (stones[row][column] == null) {
                    stones[row][column] = new StoneMoveable(row, column);
                    stoneOfMoving = false;
                }
            }
            if (stones[row][column] == null) {
                stones[row][column] = new Stone(numberOfStones);
                numberOfStones--;
            }
        }
    }

    // (parameters) -> expression
    // (parameters) -> { statements; }

    public void move(Direction direction) {
        int getRow = stoneMoveable.getRow();
        int getColumn = stoneMoveable.getColumn();
        int i=0, j =0;

        switch (direction){
            case UP:
                i = 1; break;
            case DOWN:
                i = -1; break;
            case LEFT:
                j = 1; break;
            case RIGHT:
                j = -1; break;
        }

        try {
            int value = stones[getRow + i][getColumn + j].getValueOfStone();
            stones[getRow][getColumn] = new Stone(value);
            stones[getRow + i][getColumn + j] = new StoneMoveable(getRow + i, getColumn + j);
            getMoveAbleStone();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(ANSI_RED + "************************");
            System.out.println("Out of bound! Be carefull my friend." + ANSI_RESET);;
        }

        if(isSolved()){
            state = GameState.SOLVED;
        }
    }

    private void getMoveAbleStone(){
//        Stream<Stone> stream = Arrays.stream(stones).filter((Stone s) -> s.equals();
//        stream.forEach(stoneMoveable -> stoneMoveable instanceof StoneMoveable);
            //stoneMoveable = (StoneMoveable) stream.filter(stone -> stone instanceof StoneMoveable);


        for(Stone[] s : stones){
            for(Stone s1 : s){
                if(s1 instanceof StoneMoveable){
                    stoneMoveable = new StoneMoveable(((StoneMoveable) s1).getRow(), ((StoneMoveable) s1).getColumn());
                }
            }
        }
    }

    public boolean isSolved(){
        int counter = 1;
        for(int i = 0; i < getRowCount(); i++){
            for(int j = 0; j < getColumnCount(); j++){
                if(stones[i][j].getValueOfStone() != counter){
                    return false;
                } else {
                    if (counter == rowCount*columnCount-1){
                        return true;
                    }
                }
                counter++;
            }

        }
        return true;
    }

}

