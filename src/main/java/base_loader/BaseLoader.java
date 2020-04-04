package base_loader;

import base_loader.temp_base_checker.BaseStructureException;
import base_loader.temp_base_checker.TempBaseChecker;
import org.jetbrains.annotations.NotNull;
import base_structure.Base;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import base_loader.temp_base_structure.TempBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class BaseLoader {

    private File yamlFile;

    public BaseLoader(@NotNull File yamlFile) {
        this.yamlFile = yamlFile;
    }

    @NotNull
    public Base loadBase() throws FileNotFoundException, BaseStructureException {
        InputStream yamlStream = new FileInputStream(yamlFile);
        Yaml yaml = new Yaml(new Constructor(TempBase.class));
        TempBase tempBase = (TempBase)yaml.load(yamlStream);
        TempBaseChecker checker = new TempBaseChecker(tempBase);
        checker.check();
        return new Base(tempBase);
    }
}
