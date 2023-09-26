package com.github.danya1705.categorytreebot.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CommandDispatcher {

    private final StartCommand startCommand;
    private final HelpCommand helpCommand;
    private final ViewTreeCommand viewTreeCommand;
    private final AddElementCommand addElementCommand;
    private final RemoveElementCommand removeElementCommand;
    private final DownloadCommand downloadCommand;
    private final UploadCommand uploadCommand;
    private final PerformUploadCommand performUploadCommand;
    private final UnknownCommand unknownCommand;

    /**
     * Словарь с командами и их описаниями
     */
    private final Map<String, String> commands = new HashMap<>(Map.of(
            "/help", "Отображает список доступных команд с описанием",
            "/viewTree", "Отображает дерево категорий",
            "/addElement [название элемента]", "Добавляет новую категорию в корень дерева",
            "/addElement [родительский элемент] [дочерний элемент]", "Добавляет новую категорию к родительской",
            "/removeElement [название элемента]", "Удаляет категорию и вскх её потомков",
            "/download", "Скачивает Excel документ с деревом категорий",
            "/upload", "Принимает Excel документ с деревом категорий и сохраняет все элементы в базе данных"
    ));

    /**
     * Словарь с флагами, указывающими на то, была ли введена в конкретном чате команда /upload
     */
    private final Map<String, Boolean> isWaitingForExcelFile = new HashMap<>();

    /**
     * Получает апдейт от бота, обрабатывает его и вызывает исполняющий метод соответствующей команды
     * @return Возвращает null если команда была распознана и обработана, или сообщение об ошибке, если нет
     */
    public String handleUpdate(Update update, AbsSender sender) {

        if (!update.hasMessage()) {
            return "Неправильный формат: апдейт не содержит поля Message";
        }

        String chatId = update.getMessage().getChatId().toString();

        /*
         * Обработка сообщения без текста.
         * Если бот ожидает загрузку файла с excel-таблицей, и в сообщении есть вложенный документ,
         * тогда запускается команда на загрузку этого документа.
         * В ином случае возвращается ошибка.
         */
        if (!update.getMessage().hasText()) {
            Message mess = update.getMessage();
            Boolean uploading = isWaitingForExcelFile.get(chatId);
            if (mess.hasDocument() && uploading != null && uploading) {
                performUploadCommand.execute(chatId, mess.getDocument(), isWaitingForExcelFile);
                return "Таблица успешно загружена";
            } else {
                return "Бот принимает только текстовые команды.\nДля получения списка команд введите /help";
            }
        }

        String messageText = update.getMessage().getText();

        if (messageText.charAt(0) != '/') {
            return "Бот принимает только команды.\nДля получения списка команд введите /help";
        }

        String[] messageParts = messageText.split(" ");

        switch (messageParts[0]) {
            case "/start" -> startCommand.execute(sender, chatId);
            case "/help" -> helpCommand.execute(sender, chatId, commands);
            case "/viewTree" -> viewTreeCommand.execute(sender, chatId);
            case "/addElement" -> addElementCommand.execute(sender, chatId, messageParts);
            case "/removeElement" -> removeElementCommand.execute(sender, chatId, messageParts);
            case "/download" -> downloadCommand.execute(sender, chatId);
            case "/upload" -> uploadCommand.execute(sender, chatId, isWaitingForExcelFile);
            default -> unknownCommand.execute(sender, chatId);
        }

        return null;
    }
}
