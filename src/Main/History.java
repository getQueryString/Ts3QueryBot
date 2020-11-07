package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class History {

    int channels = 0;

    public History() {

    }

    public void addChannel() {
        channels++;
        System.out.println("[" + Main.OutputTime() + "] Main.History-level: " + channels + "(Main.History.java:18)");

        FileWriter writer;
        File dat = new File(Main.consoleConfig);

        try {
            writer = new FileWriter(dat, true);
            writer.write("[" + Main.OutputTime() + "] Main.History-level: " + channels + "(Main.History.java:18)");
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();

        }
    }

    public void removeChannel() {
        if (channels >= 0) {
            channels--;
        }

    }

    public int isChannelhopping() {
        if (channels == 5) {
            return 2;
        }
        if (channels >= 8) {
            return 1;
        } else {
            return 0;
        }
    }

}
