package hse.accounting.command;

import hse.accounting.facade.CategoryFacade;

public class EditCategoryCommand implements Command {
    private final CategoryFacade facade;
    private final Long categoryId;
    private final String name;

    public EditCategoryCommand(CategoryFacade facade, Long categoryId, String name) {
        this.facade = facade;
        this.categoryId = categoryId;
        this.name = name;
    }

    @Override
    public void execute() {
        facade.updateCategoryName(facade.getCategory(categoryId), name);
    }
}
