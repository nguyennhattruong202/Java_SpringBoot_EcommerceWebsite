package com.ecommerce.service.implement;

import com.ecommerce.common.CategoryPageInfo;
import com.ecommerce.entity.Category;
import com.ecommerce.enums.Pagination;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CategoryServiceImplement implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImplement(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllEnabled() {
        return categoryRepository.findAllEnabled();
    }

    @Override
    public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByAliasEnable(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find any category with alias " + alias);
        }
        return category;
    }

    @Override
    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();
        Category parent = child.getParent();
        while (parent != null) {
            listParents.add(0, parent);
            parent = parent.getParent();
        }
        listParents.add(child);
        return listParents;
    }

    @Override
    public String checkCategoryTitle(Long id, String title, String alias) {
        Category categoryByTitle = categoryRepository.findByTitle(title);
        if (id == null || id == 0) {
            if (categoryByTitle != null) {
                return "DuplicateTitle";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByTitle != null && !Objects.equals(categoryByTitle.getId(), id)) {
                return "DuplicateTitle";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && !Objects.equals(categoryByAlias.getId(), id)) {
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    @Override
    public List<Category> listCategoriesUserInForm() {
        List<Category> categoriesUserInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findAll();
        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUserInForm.add(Category.copyIdAndTitle(category));
                Set<Category> children = category.getChildren();
                for (Category subCat : children) {
                    String name = "--" + subCat.getTitle();
                    categoriesUserInForm.add(Category.copyIdAndTitle(subCat.getId(), name));
                    listSubCategoriesUsedInForm(categoriesUserInForm, subCat, 1);
                }
            }
        }
        return categoriesUserInForm;
    }

    @Override
    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.TOP_CATEGORIES_PER_PAGE.getValue());
        Page<Category> pageCategories = categoryRepository.findRootCategories(pageable);
        List<Category> rooCategories = pageCategories.getContent();
        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());
        return listHierarchicalCategories(rooCategories);
    }

    @Override
    public String convertCyrillic(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'і', 'е', 'ж', 'з', 'ѕ', 'и', 'ј', 'к', 'л', 'ґ', 'м', 'н', 'є',
            'о', 'п', 'р', 'с', 'т', 'ї', 'у', 'ф', 'х', 'ц', 'ч', 'џ', 'ш', 'А', 'Б', 'В', 'Г', 'Д', 'І', 'Е', 'Ж',
            'З', 'Ѕ', 'И', 'Ј', 'К', 'Л', 'Ґ', 'М', 'Н', 'Є', 'О', 'П', 'Р', 'С', 'Т', 'Ї', 'У', 'Ф', 'Х', 'Ц', 'Ч',
            'Џ', 'Ш', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "i", "e", "zh", "z", "y", "i", "j", "k", "l", "g", "m", "n", "e",
            "o", "p", "r", "s", "t", "ї", "u", "f", "h", "c", "ch", "x", "h", "A", "B", "V", "G", "D", "І", "E", "Zh",
            "Z", "Y", "I", "J", "K", "L", "G", "M", "N", "E", "O", "P", "R", "S", "T", "I", "U", "F", "H", "C", ":",
            "X", "{", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "_"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    @Override
    public Category getCategory(Long id) throws CategoryNotFoundException {
        try {
            return categoryRepository.getReferenceById(id);
        } catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("Could not find any category with id " + id);
        }
    }

    @Override
    public Category saveCategory(Category category) {
        Category parent = category.getParent();
        if (parent != null) {
            String allParentIds = parent.getAllParentsIDs() == null ? "-" : parent.getAllParentsIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            category.setAllParentsIDs(allParentIds);
        }
        if (category.getAlias() == null || category.getAlias().isEmpty()) {
            String defaultAlias = category.getTitle().toLowerCase();
            category.setAlias(convertCyrillic(defaultAlias).replaceAll(" ", "_"));
        } else {
            category.setAlias(category.getAlias().replaceAll(" ", "_").toLowerCase());
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Couldn't find any category with id " + id);
        }
        categoryRepository.deleteById(id);
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUserInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getTitle();
            categoriesUserInForm.add(Category.copyIdAndTitle(subCategory.getId(), name));
            listSubCategoriesUsedInForm(categoriesUserInForm, subCategory, newSubLevel);
        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();
        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = rootCategory.getChildren();
            for (Category subCategory : children) {
                String title = "--" + subCategory.getTitle();
                hierarchicalCategories.add(Category.copyFull(subCategory, title));
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
            Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getTitle();
            hierarchicalCategories.add(Category.copyFull(subCategory, name));
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
        }
    }
}
