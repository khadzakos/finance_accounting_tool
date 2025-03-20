package hse.accounting.command;

import hse.accounting.facade.CategoryFacade;

public class ViewAllCategoriesCommand implements Command {
    private final CategoryFacade facade;

    public ViewAllCategoriesCommand(CategoryFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.getAllCategories().forEach(System.out::println);
    }
}
