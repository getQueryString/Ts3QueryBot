// From another source

package Events;

import java.util.*;

import com.github.theholywaffle.teamspeak3.api.wrapper.*;

import Main.Main;

public class AFKMover {
	public static HashMap<String, Long> AFK = new HashMap<>();
	public static HashMap<String, Boolean> Moved = new HashMap<>();
	public static HashMap<String, Integer> Channel = new HashMap<>();
	public static int Away = 1000; // Laenge in Millisekunden

	public static void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				for (Client c : Main.api.getClients()) {
					if (!c.isServerQueryClient()) {
						if (c.isAway() || c.isInputMuted()) {
							if (!Moved.containsKey(c.getUniqueIdentifier())) {
								if (AFK.containsKey(c.getUniqueIdentifier())) {
									AFK.put(c.getUniqueIdentifier(), System.currentTimeMillis());
								} else {
									long current = AFK.get(c.getUniqueIdentifier());
									if ((System.currentTimeMillis() - current) >= Away) {
										Moved.put(c.getUniqueIdentifier(), true);
										Channel.put(c.getUniqueIdentifier(), c.getChannelId());
										AFK.remove(c.getUniqueIdentifier());
										Main.api.sendPrivateMessage(c.getId(),
												"[color=red]Du wurdest in den [B]AFK-Channel[/B] verschoben");
										Main.api.moveClient(c.getId(), 2); // Channal Id fuer den AFK Raum
									}
								}
							}
						} else {
							if (AFK.containsKey(c.getUniqueIdentifier())) {
								AFK.remove(c.getUniqueIdentifier());
							}
							if (Moved.containsKey(c.getUniqueIdentifier())) {
								if (Moved.get(c.getUniqueIdentifier())) {
									Main.api.moveClient(c.getId(), Channel.get(c.getUniqueIdentifier()));
									Moved.remove(c.getUniqueIdentifier());
									Channel.remove(c.getUniqueIdentifier());
								}
							}
						}
					}
				}
			}
		}, 1000, 5000);
	}
}