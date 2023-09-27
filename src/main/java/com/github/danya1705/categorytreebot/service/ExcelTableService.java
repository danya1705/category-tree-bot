package com.github.danya1705.categorytreebot.service;

import com.github.danya1705.categorytreebot.entity.Category;
import com.github.danya1705.categorytreebot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelTableService {

    private final CategoryRepository categoryRepository;

    /**
     * Создает xlsx файл, в который выгружает базу данных
     * @return Путь к файлу
     */
    public String createExcelTable() {

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "tree.xlsx";

        try (OutputStream os = Files.newOutputStream(Paths.get(fileLocation));
             Workbook wb = new Workbook(os, "CategoryTreeBot", "1.0")) {

            Worksheet ws = wb.newWorksheet("Sheet 1");

            List<List<String>> catList = categoryRepository.findAll().stream()
                    .sorted(Comparator.comparing(e -> e.getPath() + "/" + e.getId()))
                    .map(e -> List.of(e.getName(), String.valueOf(e.getLevel())))
                    .toList();

            for (int i = 0; i < catList.size(); i++) {
                List<String> cat = catList.get(i);
                ws.value(i, Integer.parseInt(cat.get(1)), cat.get(0));
            }
        } catch (IOException e) {
                log.error("Произошла ошибка при попытке выгрузить БД в файл.", e);
        }

        return fileLocation;
    }

    /**
     * Принимает URL-ссылку на загруженный в телеграм файл с excel-таблицей, и выгружает данные из неё.
     * @return Данные в формате Map, где ключ это номер строки, а значение хранит список с ячейками.
     */
    private Map<Integer, List<String>> getExcelTableFromTg(String filePath) {

        Map<Integer, List<String>> data = new HashMap<>();

        try (InputStream file = new URI(filePath).toURL().openStream();
             ReadableWorkbook wb = new ReadableWorkbook(file)) {

            Sheet sheet = wb.getFirstSheet();

            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r -> {
                    data.put(r.getRowNum(), new ArrayList<>());

                    for (Cell cell : r) {
                        if (cell == null) {
                            data.get(r.getRowNum()).add(null);
                        } else {
                            data.get(r.getRowNum()).add(cell.getRawValue());
                        }
                    }
                });
            }
        } catch (FileNotFoundException e) {
            log.error("Не найден файл с загруженной таблицей на сервере телеграмма.", e);
        } catch (IOException e) {
            log.error("Ошибка при чтении из файла с загруженной таблицей.", e);
        } catch (URISyntaxException e) {
            log.error("Ссылка на файл с загруженной таблицей имеет неправильный формат.", e);
        }
        return data;
    }

    /**
     * Обрабатывает полученный словарь с данными и загружает его в базу данных
     */
    public void parseExcelTable(String filePath) {

        Map<Integer, List<String>> data = getExcelTableFromTg(filePath);

        categoryRepository.deleteAll();

        List<String> path = new ArrayList<>();
        path.add("");

        for (int i = 0; i < data.size(); i++) {

            List<String> row = data.get(i);

            if (row != null) {

                int level = row.size();

                Category cat = new Category();
                cat.setName(row.get(level - 1));
                cat.setPath(path.get(level - 1));
                path.add(level, path.get(level - 1) + "/" + categoryRepository.save(cat).getId());
            }
        }
    }
}
