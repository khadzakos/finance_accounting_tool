package hse.accounting.command;

import hse.accounting.facade.CategoryFacade;

public class DeleteCategoryCommand implements Command {
    private final CategoryFacade facade;
    private final Long categoryId;

    public DeleteCategoryCommand(CategoryFacade facade, Long categoryId) {
        this.facade = facade;
        this.categoryId = categoryId;
    }

    @Override
    public void execute() {
        facade.deleteCategory(facade.getCategory(categoryId));
    }
}
