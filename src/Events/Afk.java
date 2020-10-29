package Events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import Main.Main;

public class Afk {

	public static void start() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Client c : Main.api.getClients()) {
					if (c.getChannelId() != 2) {
						if (c.isInputMuted()
								&& c.isOutputMuted() /* || c.isInputHardware() || c.isOutputHardware() */) {
							Main.api.moveClient(c.getId(), 2);
							Main.api.sendPrivateMessage(c.getId(),
									"Du wurdest in den AFK-Channel gemoved, da du stummgeschaltet bist!");

							DateTimeFormatter fm = DateTimeFormatter.ofPattern(Main.tf);
							LocalDateTime dT = LocalDateTime.now();
							String dTime = dT.format(fm);
							System.out.println("[" + dTime + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
									+ ") has been moved to channel \"afk\" -> is muted(Events.Afk.java:28)");

							FileWriter writer;
							File dat = new File(Main.consoleConfig);

							try {
								writer = new FileWriter(dat, true);
								writer.write("[" + dTime + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
										+ ") has been moved to channel \"afk\" -> is muted(Events.Afk.java:28)");
								writer.write(System.getProperty("line.separator"));
								writer.flush();
								writer.close();
							} catch (IOException e1) {
								e1.printStackTrace();

							}
						}
						if (c.getChannelId() != 2) {
							if (c.isAway()) {
								Main.api.moveClient(c.getId(), 2);
								Main.api.sendPrivateMessage(c.getId(),
										"Du wurdest in den AFK-Channel gemoved, da du abwesend bist!");

								DateTimeFormatter fm = DateTimeFormatter.ofPattern(Main.tf);
								LocalDateTime dT = LocalDateTime.now();
								String dTime = dT.format(fm);
								System.out.println("[" + dTime + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
										+ ") has been moved to channel \"afk\" -> is away(Events.Afk.java:55)");

								FileWriter writer;
								File dat = new File(Main.consoleConfig);

								try {
									writer = new FileWriter(dat, true);
									writer.write("[" + dTime + "] " + c.getNickname() + " (" + c.getUniqueIdentifier()
											+ ") has been moved to channel \"afk\" -> is away(Events.Afk.java:55)");
									writer.write(System.getProperty("line.separator"));
									writer.flush();
									writer.close();
								} catch (IOException e1) {
									e1.printStackTrace();

								}
							}
						}

					}
				}

			}

		}, 1000, 1000);
	}

}