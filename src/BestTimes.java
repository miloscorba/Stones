import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BestTimes implements Iterable<BestTimes.PlayerTime>, Serializable {
    private List<PlayerTime> playerTimes = new ArrayList<>();
    private String name;
    private int time;
    private String FILENAME = "bestTimes.bin";

    public BestTimes() {
        try {
            File f = new File(FILENAME);
            if(f.exists())
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Iterator<PlayerTime> iterator() {
        return playerTimes.iterator();
    }

    public void addPlayerTime(String name, int time) {
        playerTimes.add(new PlayerTime(name, time));
        Collections.sort(playerTimes);
        try {
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        int i =0;
        playerTimes.forEach(player -> {System.out.println(". " + player.getName() + " " + player.getTime() / 60 + "min " + player.getTime() % 60 + "sec");});
        return "\n";
    }

    public void save() throws  Exception {
        try (FileOutputStream os = new FileOutputStream(FILENAME, false);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(playerTimes);
        }
    }

    public void load() throws Exception {
        try (FileInputStream is = new FileInputStream(FILENAME);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            playerTimes = (List<PlayerTime>) ois.readObject();
        }
    }

    public static class PlayerTime implements Comparable<PlayerTime>, Serializable {
        private final String name;
        private final int time;

        public String getName() {
            return name;
        }

        public int getTime() {
            return time;
        }

        public PlayerTime(String name, int time) {
            this.name = name;
            this.time = time;

        }

        @Override
        public int compareTo(PlayerTime o) {
            return time - o.getTime();
        }
    }



}