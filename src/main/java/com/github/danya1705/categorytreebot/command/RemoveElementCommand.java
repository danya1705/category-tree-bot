package com.github.danya1705.categorytreebot.command;

import com.github.danya1705.categorytreebot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class RemoveElementCommand extends Command {

    private final CategoryService categoryService;

    void execute(AbsSender sender, String chatId, String text) {

        Pattern pattern = Pattern.compile("^/removeElement <.+>$");
        Matcher matcher = pattern.matcher(text);
        String messageText;

        if (matcher.matches()) {
            String[] messageParts = text.split("[<>]");
            messageText = categoryService.removeCategory(messageParts[1]);
        } else {
            messageText = "Неверный формат команды /removeElements.\n" +
                    "Для просмотра описания команд введите /help";
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableHtml(true);
        message.setText(messageText);

        execute(sender, message);
    }
}
