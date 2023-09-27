package com.github.danya1705.categorytreebot.command;

import com.github.danya1705.categorytreebot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand extends Command {

    private final CategoryService categoryService;

    void execute(AbsSender sender, String chatId) {

        StringBuilder messageBuilder = new StringBuilder("<b>Дерево категорий:</b>");

        categoryService.viewTree().forEach(s -> messageBuilder.append("\n").append(s));

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableHtml(true);
        message.setText(messageBuilder.toString());
        execute(sender, message);
    }
}
