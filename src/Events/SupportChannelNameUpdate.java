package Events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import Main.Main;

public class SupportChannelNameUpdate {

    public static void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Main.onlineSup.clear();
                for (Client c : Main.api.getClients()) {
                    if (c.isInServerGroup(7)) {
                        // || c.isInServerGrou(7)) {
                        Main.onlineSup.add(c.getId());
                    }
                }
                if (Main.onlineSup.size() == 0) {
                    if (!Main.api.getChannelInfo(4).getName().contains("Support Warteraum (Geschlossen)")) {
                        Main.property.put(ChannelProperty.CHANNEL_NAME, "Support Warteraum (Geschlossen)");
                        Main.property.put(ChannelProperty.CHANNEL_MAXCLIENTS, "0");
                        Main.property.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "0");
                        Main.property.put(ChannelProperty.CHANNEL_DESCRIPTION,
                                "[center][size=15][color=red][b]Support Geschlossen[/b][/color][/size]\n\n[size=11]Es sind momentan keine Mitglieder online[/size]");
                        Main.api.editChannel(4, Main.property);
                        Main.property.clear();

                        System.out.println(
                                "» [" + Main.OutputTime() + "] channel 4 has been closed(Events.SupportChannelNameUpdate.java:38)");

                        FileWriter writer;
                        File dat = new File(Main.consoleConfig);
                        try {
                            writer = new FileWriter(dat, true);
                            writer.write("» [" + Main.OutputTime()
                                    + "] channel 4 has been closed(Events.SupportChannelNameUpdate.java:38)");
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    if (!Main.api.getChannelInfo(4).getName().contains("Support Warteraum (Geöffnet)")) {
                        Main.property.put(ChannelProperty.CHANNEL_NAME, "Support Warteraum (Geöffnet)");
                        Main.property.put(ChannelProperty.CHANNEL_MAXCLIENTS, "1");
                        Main.property.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "1");

                        if (Main.onlineSup.size() == 1) {
                            Main.property.put(ChannelProperty.CHANNEL_DESCRIPTION,
                                    "[center][size=15][color=green][b]Support Geöffnet[/b][/color][/size]");
                        }
                        Main.api.editChannel(4, Main.property);
                        Main.property.clear();

                        System.out.println(
                                "» [" + Main.OutputTime() + "] channel 4 was opened(Events.SupportChannelNameUpdate.java:70)");

                        FileWriter writer;
                        File dat = new File(Main.consoleConfig);
                        try {
                            writer = new FileWriter(dat, true);
                            writer.write(
                                    "» [" + Main.OutputTime() + "] channel 4 was opened(Events.SupportChannelNameUpdate.java:70)");
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }, 1000, 1 * 1000);
    }
}