package hse.accounting.command;

import hse.accounting.domain.Category;
import hse.accounting.facade.CategoryFacade;

public class CreateCategoryCommand implements Command {
    private final CategoryFacade facade;
    private final String name;
    private final Category.Type type;

    public CreateCategoryCommand(CategoryFacade facade, String name, Category.Type type) {
        this.facade = facade;
        this.name = name;
        this.type = type;
    }

    @Override
    public void execute() {
        facade.createCategory(name, type);
    }
}
