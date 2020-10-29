package Events;

import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import Main.Main;

public class checkNickname {

	public static void start() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Client c : Main.api.getClients()) {
					if (c != null) {
						if (!(c.isServerQueryClient())) {
							Main.checkClient(c);
						}
					}
				}
			}
		}, 2000, 2000);

	}
}
