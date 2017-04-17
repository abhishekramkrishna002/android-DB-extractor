package com.zinios.sqlitedbtest.db;

import static com.zinios.sqlitedbtest.db.TableGroup.GroupOperation.DISCARD_COLUMN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;

public class TableGroup {

    public static enum GroupOperation {
        SELECT,
        SUM,
        COUNT,//Operation Not supported
        MAX,
        DISCARD_COLUMN
    }

    public static class GroupTupel {

        int keyIndex;
        HashMap<GroupOperation, Integer[]> groupOperations;

        public GroupTupel(int keyIndex, HashMap<GroupOperation, Integer[]> groupOperations) {
            this.keyIndex = keyIndex;
            this.groupOperations = groupOperations;
        }

    }

    public static ArrayList<String[]> group(ArrayList<String[]> rowSet, GroupTupel groupTupel) throws Exception {
        HashMap<String, String[]> resultMap = new HashMap<>();
        for (String[] row : rowSet) {
            String key = row[groupTupel.keyIndex];
            String[] oldRow = resultMap.containsKey(key) ? resultMap.get(key) : null;
            String[] newRow = row;
            ArrayList<String> resultRow = new ArrayList<String>();
            for (int rowIndex = 0; rowIndex < ((oldRow!=null)? oldRow.length:newRow.length) ; rowIndex++) {
                GroupOperation gpo = getGroupOperationFromIndex(rowIndex, groupTupel.groupOperations);
                resultRow.add(resultMap.containsKey(key) ? 
                            operate(oldRow[rowIndex], newRow[rowIndex], gpo):
                            newRow[rowIndex]);
            }
            String[] resultRowArray = new String[resultRow.size()];
            resultRow.toArray(resultRowArray);
            if (resultMap.containsKey(key)) {
                resultMap.replace(key, resultRowArray);
            } else {
                resultMap.put(key, resultRowArray);
            }
        }
        ArrayList<String[]> results = new ArrayList<String[]>(resultMap.values());
        return results;
    }

    public static GroupOperation getGroupOperationFromIndex(
            int index,
            HashMap<GroupOperation, Integer[]> groupOperations) throws Exception {
        Iterator operationsIterator = groupOperations.entrySet().iterator();
        while (operationsIterator.hasNext()) {
            Map.Entry operation = (Map.Entry) operationsIterator.next();
            if (ArrayUtils.contains((Integer[]) operation.getValue(), index)) {
                return (GroupOperation) operation.getKey();
            }
        }
        return DISCARD_COLUMN;
    }

    public static String operate(String oldValue, String newValue, GroupOperation operation) throws Exception {

        switch (operation) {
            case SELECT:
                return newValue;
            case SUM:
                System.out.println(oldValue + " " + newValue);
                return String.valueOf(NumberHelper.getNumber(newValue) + NumberHelper.getNumber(oldValue));
            case MAX:
                float oldRowValue = NumberHelper.getNumber(oldValue);
                float newRowValue = NumberHelper.getNumber(newValue);
                return String.valueOf(oldRowValue < newRowValue ? newRowValue : oldRowValue);
            case DISCARD_COLUMN:
                return newValue;
            default:
                return null;

        }
    }

}
