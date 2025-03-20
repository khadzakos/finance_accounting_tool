package hse.accounting.command;

import hse.accounting.domain.Category;
import hse.accounting.facade.CategoryFacade;
import javafx.util.Pair;

import java.util.List;

public class ViewAllCategoriesCommand implements Command {
    private final CategoryFacade facade;

    public ViewAllCategoriesCommand(CategoryFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        List<Category> categories = facade.getAllCategories();
        for (Category category : categories) {
            System.out.printf("""
                    --------------------------------
                    ID: %d
                    Название категории: %s
                    Тип: %s
                   """, category.getId(), category.getName(), category.getType() == Category.Type.INCOME ? "Доход" : "Расход");
        }
    }
}
