package com.github.danya1705.categorytreebot.bot;

import com.github.danya1705.categorytreebot.command.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final CommandDispatcher dispatcher;

    public Bot(CommandDispatcher dispatcher) {
        super(System.getenv("BotToken"));
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

        String returnMessage = dispatcher.handleUpdate(update, this);
        if (returnMessage != null) {
            sendMessage(update.getMessage().getChatId(), returnMessage);
        }
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Произошла ошибка при попытке отправить сообщение в бот.", e);
        }
    }

}