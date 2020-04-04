package base_loader.temp_base_checker;

import base_loader.temp_base_structure.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TempBaseChecker {

    private TempBase tempBase;

    public TempBaseChecker(@NotNull TempBase tempBase) {
        this.tempBase = tempBase;
    }

    @NotNull
    private <T> String getTempName(@NotNull T temp) {
        return temp.getClass().getSimpleName();
    }

    private <T extends TempEntity> void checkTempEntity(@NotNull T tempEntity) throws BaseStructureException {
        String tempEntityName = tempEntity.getTempEntityName();
        if (!tempEntityName.matches("[_\\w][_\\w\\d]*")) {
            throw new BaseStructureException(getTempName(tempEntity) + " with incorrect name: '" + tempEntityName + "'");
        }
    }

    private <T extends TempEntity, V extends TempEntityContainer<T>> void checkTempEntityCollection(@NotNull V tempEntityContainer) throws BaseStructureException {
        List<T> tempEntities = tempEntityContainer.getTempEntities();
        Set<String> tempEntityNames = new HashSet<>();
        for (T tempEntity : tempEntities) {
            String tempEntityName = tempEntity.getTempEntityName();
            if (tempEntityNames.contains(tempEntityName)) {
                throw new BaseStructureException(getTempName(tempEntityContainer) + " contains "
                        + getTempName(tempEntity) + " with same names: '" + tempEntityName + "'");
            }
            tempEntityNames.add(tempEntityName);
        }
    }

    public void check() throws BaseStructureException {
        checkTempBase(tempBase);
    }

    private void checkTempBase(@NotNull TempBase tempBase) throws BaseStructureException  {
        checkTempEntityCollection(tempBase);
        for (TempScheme tempScheme : tempBase.getSchemas()) {
            checkTempScheme(tempScheme);
        }
    }

    private void checkTempScheme(@NotNull TempScheme tempScheme) throws BaseStructureException  {
        checkTempEntity(tempScheme);
        checkTempEntityCollection(tempScheme);
        for (TempTable tempTable : tempScheme.getTables()) {
            checkTempTable(tempTable);
        }
    }

    private void checkTempTable(@NotNull TempTable tempTable) throws BaseStructureException  {
        checkTempEntity(tempTable);
        checkTempEntityCollection(tempTable);
        for (TempColumn tempColumn : tempTable.getColumns()) {
            checkTempColumn(tempColumn);
        }
    }

    private void checkTempColumn(@NotNull TempColumn tempColumn) throws BaseStructureException {
        checkTempEntity(tempColumn);
        String type = tempColumn.getType();
        if (!type.matches("boolean|integer|date|varchar\\(\\d+\\)")) {
            throw new BaseStructureException(getTempName(tempColumn) + " with incorrect type: '" + type + "'");
        }
    }
}
