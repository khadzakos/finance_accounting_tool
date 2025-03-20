package hse.accounting.file.importer;

import java.io.File;

public interface Importer<T> {
    void importData(File file) throws Exception;
}