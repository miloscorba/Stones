import java.io.Serializable;

public class StoneMoveable extends Stone implements Serializable {
    private int row;
    private int column;
    private int valueOfStone;

    public StoneMoveable(int row, int column) {
        this.valueOfStone = 0;
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
