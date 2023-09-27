package com.github.danya1705.categorytreebot.service;

import com.github.danya1705.categorytreebot.entity.Category;
import com.github.danya1705.categorytreebot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<String> viewTree() {

        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(e -> e.getPath() + "/" + e.getId()))
                .map(e -> addSpaces(e.getName(), e.getLevel()))
                .toList();
    }

    public String addCategory(String parent, String name) {

        Category cat = categoryRepository.findByName(parent)
                .map(p -> {
                    Category newCat = new Category();
                    newCat.setName(name);
                    newCat.setPath(p.getPath() + "/" + p.getId());
                    return newCat;
                })
                .orElse(null);

        if (cat != null) {
            return "Новая категория " + categoryRepository.save(cat).getName() + " успешно добавлена";
        } else {
            return "Не найдена родительская категория " + parent;
        }
    }

    public String addCategory(String name) {
        Category cat = new Category();
        cat.setName(name);
        cat.setPath("");
        return "Новая категория " + categoryRepository.save(cat).getName() + " успешно добавлена";
    }

    public String removeCategory(String name) {

        return categoryRepository.findByName(name).map(cat -> {
            String s = cat.getPath() + "/" + cat.getId();
            categoryRepository.deleteByPathStartsWith(s);
            categoryRepository.delete(cat);
            return "Категория " + name + " и её потомки успешно удалены";
        }).orElse("Не найдена категория" + name);
    }

    private String addSpaces(String str, long level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("          ");
        }
        sb.append(str);
        return sb.toString();
    }
}
