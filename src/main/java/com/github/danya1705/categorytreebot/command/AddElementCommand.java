package com.github.danya1705.categorytreebot.command;

import com.github.danya1705.categorytreebot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class AddElementCommand extends Command {

    private final CategoryService categoryService;

    void execute(AbsSender sender, String chatId, String[] messageParts) {

        String messageText;

        if (messageParts.length < 2 || messageParts.length > 3) {
            messageText = "Неверное количество аргументов у команды /addElements.\n" +
                    "Для просмотра описания команд введите /help";
        } else {
            if (messageParts.length == 2) {
                messageText = categoryService.addCategory(messageParts[1]);
            } else {
                messageText = categoryService.addCategory(messageParts[1], messageParts[2]);
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableHtml(true);
        message.setText(messageText);

        execute(sender, message);
    }
}
