package com.github.danya1705.categorytreebot.config;

import com.github.danya1705.categorytreebot.bot.BotListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig extends DefaultBotOptions {

    @Bean
    public TelegramBotsApi telegramBotsApi(BotListener botListener) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(botListener);
        return api;
    }
}