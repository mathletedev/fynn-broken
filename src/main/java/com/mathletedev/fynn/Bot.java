package com.mathletedev.fynn;

import java.time.Instant;
import javax.security.auth.login.LoginException;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class Bot extends ListenerAdapter {
	public static void main(String[] args) throws LoginException {
		Dotenv dotenv = Dotenv.load();

		JDABuilder.createLight(dotenv.get("BOT_TOKEN"), GatewayIntent.GUILD_MESSAGES)
				.addEventListeners(new Bot()).setActivity(Activity.playing("Monty Hall")).build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		User author = message.getAuthor();
		if (!event.isFromGuild() || author.isBot())
			return;

		String content = message.getContentRaw();

		Expression exp = new ExpressionBuilder(content).build();

		if (!exp.validate().isValid()) {
			message.reply("Unable to solve that expression...").queue();
			return;
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("**Expression:**```" + content + "```\n**Result:**```"
				+ String.valueOf(exp.evaluate()) + "```");
		eb.setFooter(event.getMember().getNickname(), author.getAvatarUrl());
		eb.setTimestamp(Instant.now());
		eb.setColor(0x5865f2);

		message.reply(eb.build()).queue();
	}
}
