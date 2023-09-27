package com.github.danya1705.categorytreebot.bot;

import com.github.danya1705.categorytreebot.command.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class BotListener extends TelegramLongPollingBot {

    private final CommandDispatcher dispatcher;

    public BotListener(@Value("${telegram.bot.token}") String token, CommandDispatcher dispatcher) {
        super(token);
        this.dispatcher = dispatcher;
    }

    @Override
    public String getBotUsername() {
        return "Category tree bot";
    }

    /**
     * Получает обновление от телеграм-бота и передает его на обработку диспетчеру команд.
     * В случае возвращения от диспетчера сообщения об ошибке отправляет его в бот.
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.info("Get update " + update.toString());
            dispatcher.handleUpdate(update);
    }
}