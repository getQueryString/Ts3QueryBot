package Events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;

//import java.util.HashMap;

//import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
//import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import Main.History;
import Main.Main;

import javax.sound.sampled.Line;

public class Event {

    public static void loadEvents() {
        // int whoAmI = Load.api.whoAmI().getId();
        Main.api.registerAllEvents();
        Main.api.addTS3Listeners(new TS3Listener() {

            @Override
            public void onChannelCreate(ChannelCreateEvent e) {
                Main.checkChannel(e.getInvokerUniqueId(), Main.api.getChannelInfo(e.getChannelId()).getName());

                if (ChannelProperty.CHANNEL_FLAG_PERMANENT != null) {
                    Main.api.deleteChannel(e.getChannelId());

                }
                if (ChannelProperty.CHANNEL_FLAG_SEMI_PERMANENT != null) {
                    Main.api.deleteChannel(e.getChannelId());
                }
            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent arg0) {

            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent arg0) {

            }

            @Override
            public void onChannelEdit(ChannelEditedEvent arg0) {

            }

            @Override
            public void onChannelMoved(ChannelMovedEvent e) {

            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent arg0) {

            }

            @Override
            public void onClientJoin(ClientJoinEvent e) {
                Client c = Main.api.getClientInfo(e.getClientId());
                Main.clientChannelHistory.put(c.getId(), new History());

                FileWriter writer;
                File dat = new File(Main.configData);

                try {
                    writer = new FileWriter(dat, true);
                    writer.write(" CLIENT-NAME: " + c.getNickname() + " | UNIQUE-ID: " + c.getUniqueIdentifier()
                            + " | COUNTRY: " + c.getCountry() + " | CLIENT-ADDRESS: " + c.getIp() + " | DATABASE-ID: "
                            + c.getDatabaseId() + " | PLATFORM: " + c.getPlatform() + " | FIRST-CONNECTION: "
                            + c.getCreatedDate() + " | LAST-CONNECTION: " + c.getLastConnectedDate() + " ");
                    writer.write(System.getProperty("line.separator"));

                    writer.flush();
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();

                }
                Main.api.sendPrivateMessage(c.getId(),
                        "Moin! Schreib mir \"ruhe\", um den Ruhe-Rang zu erhalten.\nDeine Daten wurden aus Sicherheitsgründen gespeichert!");

                System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                        + ") joined the server(Events.Event.java)");

                FileWriter writer1;
                File dat1 = new File(Main.consoleConfig);
                try {
                    writer1 = new FileWriter(dat1, true);
                    writer1.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                            + ") joined the server(Events.Event.java)");
                    writer1.write(System.getProperty("line.separator"));
                    writer1.flush();
                    writer1.close();
                } catch (IOException e1) {
                    e1.printStackTrace();

                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent e) {
                // Utils.deleteChannel();
            }

            @Override
            public void onClientMoved(ClientMovedEvent e) {
                Client c = Main.api.getClientInfo(e.getClientId());
                // Utils.deleteChannel();
                if (e.getTargetChannelId() == 4 && Main.onlineSup.size() >= 0) {
                    Main.api.sendPrivateMessage(e.getClientId(),
                            "Sich auf den Server befindenen Teamler wurden benachrichtigt.");

                    System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                            + ") joined the support channel -> needed help(Events.Event.java)");

                    FileWriter writer;
                    File dat = new File(Main.consoleConfig);

                    try {
                        writer = new FileWriter(dat, true);
                        writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                + ") joined the support channel -> needed help(Events.Event.java)");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();

                    }

                    for (Client sup : Main.api.getClients()) {
                        if (!(sup.isServerQueryClient()) && (sup.isInServerGroup(7))) {
                            Main.api.pokeClient(sup.getId(), c.getNickname() + " benötigt Support!");

                            System.out.println("[" + Main.OutputTime() + "] " + sup.getNickname() + " (" + sup.getUniqueIdentifier()
                                    + ") were poked because a user entered channel 4(Events.Event.java)");

                            try {
                                writer = new FileWriter(dat, true);
                                writer.write("[" + Main.OutputTime() + "] " + sup.getNickname() + " (" + sup.getUniqueIdentifier()
                                        + ") were poked because a user entered channel 4(Events.Event.java)");
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                                writer.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();

                            }
                        }
                    }
                }

                History history = Main.clientChannelHistory.get(c.getId());
                history.addChannel();
                if (history.isChannelhopping() == 2) {
                    Main.api.pokeClient(c.getId(), "Bitte unterlasse das Channelhopping!");

                    System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                            + ") was warned because he has reached a Main.History-level of 4(+)(Events.Event.java)");

                    FileWriter writer;
                    File dat = new File(Main.consoleConfig);

                    try {
                        writer = new FileWriter(dat, true);
                        writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                + ") was warned because he has reached a Main.History-level of 4(+)(Events.Event.java)");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();

                    }
                } else if (history.isChannelhopping() == 1) {
                    Main.api.kickClientFromServer("Channelhopping", c.getId());

                    System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                            + ") was kicked from the server because he has reached a Main.History-level of 7(+)(Events.Event.java)");

                    FileWriter writer;
                    File dat = new File(Main.consoleConfig);

                    try {
                        writer = new FileWriter(dat, true);
                        writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                + ") was kicked from the server because he has reached a Main.History-level of 7(+)(Events.Event.java)");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();

                    }
                }

                /*
                 * if (e.getTargetChannelId() == 1 || e.getTargetChannelId() == 2) {
                 * Utils.createChannel(c, Main.api.getChannelInfo(c.getChannelId()).getName());
                 * }
                 */

            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {

            }

            @Override
            public void onServerEdit(ServerEditedEvent arg0) {

            }

            public void onTextMessage(TextMessageEvent e) {

                Client c = Main.api.getClientInfo(e.getInvokerId());

                if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                    String msg = e.getMessage().toLowerCase();

                    if (msg.equalsIgnoreCase("ruhe")) {
                        if (c.isInServerGroup(7)) {
                            if (c.isInServerGroup(10)) {
                                Main.api.removeClientFromServerGroup(10, c.getDatabaseId());
                                Main.api.sendPrivateMessage(c.getId(), "Dir wurde der Quiet-Rang entzogen!");

                                System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                        + ") has withdrawn the quiet-rank(Events.Event.java)");

                                FileWriter writer;
                                File dat = new File(Main.consoleConfig);

                                try {
                                    writer = new FileWriter(dat, true);
                                    writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                            + ") has withdrawn the quiet-rank(Events.Event.java)");
                                    writer.write(System.getProperty("line.separator"));
                                    writer.flush();
                                    writer.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();

                                }

                            } else {
                                Main.api.addClientToServerGroup(10, c.getDatabaseId());
                                Main.api.sendPrivateMessage(c.getId(), "Dir wurde der Quiet-Rang gegeben!");

                                System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                        + ") has been given the quiet-rank(Events.Event.java)");

                                FileWriter writer;
                                File dat = new File(Main.consoleConfig);

                                try {
                                    writer = new FileWriter(dat, true);
                                    writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                            + ") has been given the quiet-rank(Events.Event.java)");
                                    writer.write(System.getProperty("line.separator"));
                                    writer.flush();
                                    writer.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();

                                }
                            }

                        } else {
                            Main.api.sendPrivateMessage(c.getId(), "Keine Rechte!");
                        }
                    }
                }

                if (e.getTargetMode() == TextMessageTargetMode.CLIENT
                        || e.getTargetMode() == TextMessageTargetMode.CHANNEL) {

                    if (e.getMessage().equalsIgnoreCase("!listuser") && c.isInServerGroup(7)) {
                        Main.api.sendPrivateMessage(c.getId(), " ");
                        Main.api.sendPrivateMessage(c.getId(), "*-[b][u] Aktuelle User [/u][/b]-*");
                        for (Client c11 : Main.api.getClients()) {
                            if (!c11.isInServerGroup(6) && !c11.isInServerGroup(7)) {
                                Main.api.sendPrivateMessage(c.getId(), "  [URL=" + c11.getClientURI() + "]"
                                        + c11.getNickname() + "[/URL]	[" + c11.getCountry() + "]");

                            }
                        }
                        System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                + ") has had all normal users displayed on the server(Events.Event.java)");

                        FileWriter writer;
                        File dat = new File(Main.consoleConfig);

                        try {
                            writer = new FileWriter(dat, true);
                            writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                    + ") has had all normal users displayed on the server(Events.Event.java)");
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    if (e.getMessage().equalsIgnoreCase("!listteam")) {
                        if (c.isInServerGroup(7)) {
                            Main.api.sendPrivateMessage(c.getId(), " ");
                            Main.api.sendPrivateMessage(c.getId(), "*-[b][u] Aktuelle Teammitglieder [/u][/b]-*");
                            for (Client c11 : Main.api.getClients()) {
                                if (c11.isInServerGroup(6) || c11.isInServerGroup(7)) {
                                    Main.api.sendPrivateMessage(c.getId(), "  [URL=" + c11.getClientURI() + "]"
                                            + c11.getNickname() + "[/URL]	[" + c11.getCountry() + "]");

                                }
                            }
                            System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                    + ") has had all team members displayed on the server(Events.Event.java)");

                            FileWriter writer;
                            File dat = new File(Main.consoleConfig);

                            try {
                                writer = new FileWriter(dat, true);
                                writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                        + ") has had all team members displayed on the server(Events.Event.java)");
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                                writer.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();

                            }
                        }
                    }
                    if (e.getMessage().equalsIgnoreCase("!listall")) {
                        if (c.isInServerGroup(7)) {
                            Main.api.sendPrivateMessage(c.getId(), " ");
                            Main.api.sendPrivateMessage(c.getId(), "*-[b][u] Aktuelle Clients [/u][/b]-*");
                            for (Client c11 : Main.api.getClients()) {
                                Main.api.sendPrivateMessage(c.getId(), "  [URL=" + c11.getClientURI() + "]"
                                        + c11.getNickname() + "[/URL]	[" + c11.getCountry() + "]");

                            }
                            System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                    + ") has had all users displayed on the server(Events.Event.java)");

                            FileWriter writer;
                            File dat = new File(Main.consoleConfig);

                            try {
                                writer = new FileWriter(dat, true);
                                writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                        + ") has had all users displayed on the server(Events.Event.java)");
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                                writer.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();

                            }
                        }
                    }
                }
                Path dat = Paths.get(Main.configData);

                Path datcopy = Paths.get(Main.path + "/PlayerJoinNotation (COPY) " + Main.OutputTime() + ".config");

                if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {

                    if (e.getMessage().equalsIgnoreCase("!copyuserconfig")) {
                        if (c.getUniqueIdentifier().equals("UNIQUEID") && c.isInServerGroup(7)
                                && c.getChannelId() == 87) {
                            try {
                                /*
                                 * Socket sock = new Socket("localhost", 30033); byte[] byteArray = new
                                 * byte[30033]; InputStream is = sock.getInputStream(); FileOutputStream fos =
                                 * new FileOutputStream( Main.path + "/PlayerJoinNotation (COPY) " + dateTime +
                                 * ".config"); BufferedOutputStream bos = new BufferedOutputStream(fos); int
                                 * byteRead = is.read(byteArray, 0, byteArray.length); bos.write(byteArray, 0,
                                 * byteRead); bos.close(); sock.close();
                                 */
                                Files.copy(dat, datcopy, StandardCopyOption.REPLACE_EXISTING,
                                        StandardCopyOption.COPY_ATTRIBUTES);
                                System.out.println("!! [" + Main.OutputTime() + "] " + c.getNickname() + " ("
                                        + c.getUniqueIdentifier() + ") copied " + Main.configData + " to " + Main.path
                                        + "(Events.Event.java)");

                                FileWriter writer;
                                File dat1 = new File(Main.consoleConfig);

                                try {
                                    writer = new FileWriter(dat1, true);
                                    writer.write("!! [" + Main.OutputTime() + "] " + c.getNickname() + " ("
                                            + c.getUniqueIdentifier() + ") copied " + Main.configData + " to "
                                            + Main.path + "(Events.Event.java)");
                                    writer.write(System.getProperty("line.separator"));
                                    writer.flush();
                                    writer.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();

                                }
                            } catch (IOException e2) {
                                // System.out.println("Exception error while copying: " + e1);
                                e2.printStackTrace();
                            }
                        } else {
                            System.out.println("! [" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                    + ") tried to copy " + dat + "(Events.Event.java)");

                            FileWriter writer;
                            File dat2 = new File(Main.consoleConfig);

                            try {
                                writer = new FileWriter(dat2, true);
                                writer.write("! [" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                        + ") tried to copy " + dat + "(Events.Event.java)");
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                                writer.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();

                            }
                        }
                    }
                }

                Path dat1 = Paths.get(Main.consoleConfig);

                Path datcopy1 = Paths.get(Main.path + "/Console (COPY) " + Main.OutputTime() + ".config");
                if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                    if (e.getMessage().equalsIgnoreCase("!copyconsoleconfig")) {
                        if (c.getUniqueIdentifier().equals("UNIQUEID") && c.isInServerGroup(7)
                                && c.getChannelId() == 87) {
                            try {
                                Files.copy(dat1, datcopy1, StandardCopyOption.REPLACE_EXISTING,
                                        StandardCopyOption.COPY_ATTRIBUTES);
                                System.out.println("!! [" + Main.OutputTime() + "] " + c.getNickname() + " ("
                                        + c.getUniqueIdentifier() + ") copied " + Main.consoleConfig + " to "
                                        + Main.path + "(Events.Event.java)");

                                FileWriter writer1;
                                File dat3 = new File(Main.consoleConfig);

                                try {
                                    writer1 = new FileWriter(dat3, true);
                                    writer1.write("!! [" + Main.OutputTime() + "] " + c.getNickname() + " ("
                                            + c.getUniqueIdentifier() + ") copied " + Main.consoleConfig + " to "
                                            + Main.path + "(Events.Event.java)");
                                    writer1.write(System.getProperty("line.separator"));
                                    writer1.flush();
                                    writer1.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();

                                }
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }

                        } else {
                            System.out.println("! [" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                    + ") tried to copy " + dat1 + "(Events.Event.java)");

                            FileWriter writer;
                            File dat2 = new File(Main.consoleConfig);

                            try {
                                writer = new FileWriter(dat2, true);
                                writer.write("! [" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                        + ") tried to copy " + dat1 + "(Events.Event.java)");
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                                writer.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();

                            }
                        }
                    }

                }

                if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                    if (e.getMessage().equalsIgnoreCase("!version") && c.isInServerGroup(7)
                            && !c.isServerQueryClient()) {
                        Main.api.sendPrivateMessage(c.getId(), " ");
                        Main.api.sendPrivateMessage(c.getId(), "*-[b][u] Aktuelle queryBot-Version [/u][/b]-*");
                        Main.api.sendPrivateMessage(c.getId(), "  " + Main.BotVersion);
                        System.out.println("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                + ") had the server version printed out(Events.Event.java)");

                        FileWriter writer;
                        File dat2 = new File(Main.consoleConfig);

                        try {
                            writer = new FileWriter(dat2, true);
                            writer.write("[" + Main.OutputTime() + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
                                    + ") had the server version printed out(Events.Event.java)");
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();

                        }
                    }
                }

                if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                    if (e.getMessage().equalsIgnoreCase("!shutdown")
                            && c.getUniqueIdentifier().equals("UNIQUEID")
                            && !c.isServerQueryClient()) {

                        Main.api.sendPrivateMessage(c.getId(), "Der Bot wird nun [color=red]heruntergefahren[/color]!");
                        Main.api.logout();
                        Main.api.createServerSnapshot();
                        System.out.println("  [" + Main.OutputTime() + "] " + "TS3-Bot disconnected(Events.Event.java)");

                        FileWriter writer;
                        File dat2 = new File(Main.consoleConfig);

                        try {
                            writer = new FileWriter(dat2, true);
                            writer.write("  [" + Main.OutputTime() + "] " + "TS3-Bot disconnected(Events.Event.java)");
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            writer.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();

                        }
                    }
                }
            }
        });
    }
}