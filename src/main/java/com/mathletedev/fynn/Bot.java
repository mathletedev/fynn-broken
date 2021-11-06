package com.mathletedev.fynn;

import javax.security.auth.login.LoginException;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public final class Bot extends ListenerAdapter {
	public static void main(String[] args) throws LoginException {
		Dotenv dotenv = Dotenv.load();

		JDABuilder.createLight(dotenv.get("BOT_TOKEN"), GatewayIntent.GUILD_MESSAGES)
				.addEventListeners(new Bot()).setActivity(Activity.playing("Monty Hall")).build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();

		if (message.getContentRaw().equals("?ping")) {
			MessageChannel channel = event.getChannel();
			long time = System.currentTimeMillis();

			channel.sendMessage("Ping?").queue(res -> {
				res.editMessageFormat("Pong!\n%d ms", System.currentTimeMillis() - time).queue();
			});
		}
	}
}
