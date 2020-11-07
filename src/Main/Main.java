package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import Events.Afk;
import Events.Event;
import Events.SupportChannelNameUpdate;
import Events.checkNickname;

public class Main {

    // HashMaps
    public static HashMap<Integer, History> clientChannelHistory = new HashMap<>();
    public static Map<ChannelProperty, String> property = new HashMap<ChannelProperty, String>();

    // ArrayLists
    public static ArrayList<String> words = new ArrayList<>();
    public static ArrayList<Integer> onlineSup = new ArrayList<>();

    // Strings
    public static final String BotVersion = "1.8.2020.13.48";
    public static String tf = "dd.MM.yyyy HH-mm-ss-SS";
    public static String configData = "PlayerJoinNotation.config";
    public static String path = System.getProperty("user.home") + "\\Desktop";
    public static String consoleConfig = "Console.config";

    // Bot configuration
    public static final TS3Config config = new TS3Config();
    public static final TS3Query query = new TS3Query(config);
    public static final TS3Api api = query.getApi();

    // Output time method
    public static String OutputTime() {
        DateTimeFormatter fm1 = DateTimeFormatter.ofPattern(tf);
        LocalDateTime dT1 = LocalDateTime.now();
        String dTime1 = dT1.format(fm1);
        return dTime1;
    }

    public static void main(String[] args) {
        config.setHost("localhost");
        config.setFloodRate(FloodRate.UNLIMITED);
        query.connect();

        System.out.println("»  [" + OutputTime() + "] TS3-Bot connected(Main.Main.java)");

        FileWriter writer;
        File dat = new File(consoleConfig);

        try {
            writer = new FileWriter(dat, true);
            writer.write("»  [" + OutputTime() + "] TS3-Bot connected(Main.Main.java)");
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();

        }

        api.login("serveradmin", "PASSWORD");
        api.selectVirtualServerByPort(9987);
        api.setNickname("TS3QueryBot");
        Event.loadEvents();
        updateHistory();
        // AFKMover.start();
        Afk.start();
        // Utils.loadChannels();
        addWords();
        checkNickname.start();
        SupportChannelNameUpdate.start();

        System.out.println("»  [" + OutputTime() + "] TS3-Bot started(Main.Main.java)");

        try {
            writer = new FileWriter(dat, true);
            writer.write("»  [" + OutputTime() + "] TS3-Bot started(Main.Main.java)");
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();

        }
    }

    public static void updateHistory() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @SuppressWarnings("rawtypes")
            @Override
            public void run() {
                for (Map.Entry e : clientChannelHistory.entrySet()) {
                    ((History) e.getValue()).removeChannel();

                }
                System.out.println("[" + OutputTime() + "] " + "ChannelHistory updated(Main.Main.java)");

                FileWriter writer;
                File dat = new File(consoleConfig);

                try {
                    writer = new FileWriter(dat, true);
                    writer.write("[" + OutputTime() + "] " + "ChannelHistory updated(Main.Main.java)");
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();

                }

            }
        }, 60000, 60000);
    }

    public static void addWords() {
        words.add("popo");

    }

    public static void checkClient(Client c) {
        String name = c.getNickname().toLowerCase();
        if (words.contains(name)) {
            api.kickClientFromServer("Bitte such dir einen vernünftigen Nicknamen aus!", c.getId());

            System.out.println("[" + OutputTime() + "] name=? (" + c.getUniqueIdentifier()
                    + ") was kicked out of the server because it was not calling itself properly - name="
                    + c.getNickname() + "(Main.Main.java)");

            FileWriter writer;
            File dat = new File(consoleConfig);

            try {
                writer = new FileWriter(dat, true);
                writer.write("[" + OutputTime() + "] name=? (" + c.getUniqueIdentifier()
                        + ") was kicked out of the server because it was not calling itself properly - name="
                        + c.getNickname() + "(Main.Main.java)");
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();

            }
        }
    }

    public static void checkChannel(String id, String name) {
        Client c = api.getClientByUId(id);
        if (words.contains(name)) {
            if (ChannelProperty.CHANNEL_FLAG_TEMPORARY != null) {
                api.kickClientFromChannel(c.getId());
            }
            api.pokeClient(c.getId(), "Bitte such dir einen vernünftigen Channel-Namen aus!");

            System.out.println("[" + OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                    + ") created channel was deleted because it did not properly name the channel name(Main.Main.java)");

            FileWriter writer;
            File dat = new File(consoleConfig);

            try {
                writer = new FileWriter(dat, true);
                writer.write("[" + OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                        + ") created channel was deleted because it did not properly name the channel name(Main.Main.java)");
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();

            }
        }
    }

}