package base_loader.temp_base_structure;

import java.util.List;

public interface TempEntityContainer<T extends TempEntity> {

    List<T> getTempEntities();
}
