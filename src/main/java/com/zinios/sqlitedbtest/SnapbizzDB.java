package com.zinios.sqlitedbtest;

import com.zinios.sqlitedbtest.db.TableFilter;
import com.zinios.sqlitedbtest.db.TableGroup;
import com.zinios.sqlitedbtest.db.TableSort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;

public class SnapbizzDB {

    public static ArrayList<String[]> CUSTOMER = new ArrayList() {
        {
            add(new String[]{"1", "John", "Bangalore", "12-02-1992"});
            add(new String[]{"2", "Mathew", "Chennai", "12-02-1992"});
            add(new String[]{"3", "Abhlash", "Delhi", "14-02-1992"});
            add(new String[]{"4", "Pavan", "Hyderbad", "15-02-1992"});
            add(new String[]{"5", "Keerthan", "Punjab", "16-02-1992"});
            add(new String[]{"6", "Niranjan", "Jammu", "17-02-1992"});
            add(new String[]{"7", "Prasad", "Bengal", "18-02-1992"});
            add(new String[]{"8", "David", "Kerala", "19-02-1992"});
            add(new String[]{"9", "Prince", "Haryana", "20-02-1992"});
        }
    };

    public static ArrayList<String[]> CUSTOMERS_DETAILS = new ArrayList() {
        {
            add(new String[]{"1", "100", "435", "100"});
            add(new String[]{"2", "234", "456", "150"});
            add(new String[]{"3", "234", "546", "100"});
            add(new String[]{"4", "143", "567", "100"});
            add(new String[]{"5", "256", "657", "100"});
            add(new String[]{"6", "590", "456", "100"});
            add(new String[]{"7", "432", "234", "100"});
            add(new String[]{"8", "459", "123", "100"});
            add(new String[]{"9", "145", "754", "100"});
        }
    };

    public static void printArrayList(List<String[]> rows, String message) {
        System.out.println(message + " :: start");
        for (String[] row : rows) {
            System.out.println(message + "<row> ");
            for (String column : row) {
                System.out.println("<column>" + column + " </column> ");
            }
            System.out.println("</row>");
        }
        System.out.println(message + " :: end");
    }

    public static void main(String[] args) {

        try {
            ArrayList<String[]> initCustomers = innerJoin(CUSTOMER, CUSTOMERS_DETAILS,
                    new HashMap<Integer, Integer>() {
                {
                    put(0, 0);
                }
            }, new int[]{0, 1, 2, 3}, new int[]{1, 2, 3});
            ArrayList<String[]> customers = initCustomers;
            /*
             * Filter Example
             */
//            ArrayList<String[]> customers = TableFilter.filter(initCustomers, 
//                    TableFilter.FILTER_TYPE.STRING_FILTER,
//                    new ArrayList<TableFilter.FilterTuple>() {
//                {
//                    add(new TableFilter.FilterTuple(3, 
//                            TableFilter.FILTER_CONDITION.IN, 
//                            new String[]{"15-02-1992"}));
//                }
//            });
            /*
             * Sort Example
             */
//            customers = TableSort.sort(customers, new TableSort.SortTuple(3, TableSort.SORTWAY.DESC));
            /*
             * Group by  Example
             */
            customers = TableGroup.group(customers, new TableGroup.GroupTupel(3, new HashMap<TableGroup.GroupOperation, Integer[]>() {
                {

                    put(TableGroup.GroupOperation.SELECT, new Integer[]{0, 1, 2, 3, 4, 5});
                    put(TableGroup.GroupOperation.MAX, new Integer[]{6});

                }
            }));

            printArrayList(customers, "customers");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String[]> innerJoin(ArrayList<String[]> leftRowSet,
            ArrayList<String[]> rightRowSet, HashMap<Integer, Integer> mapping,
            int[] leftSelections, int[] rightSelections) throws Exception {
        ArrayList<String[]> resultSet = new ArrayList<>();
        for (String[] leftRow : leftRowSet) {
            for (String[] rightRow : rightRowSet) {
                Iterator mappingIterator = mapping.entrySet().iterator();
                boolean canJoin = false;
                while (mappingIterator.hasNext()) {
                    Map.Entry mapper = (Map.Entry) mappingIterator.next();
                    int leftIndex = (int) mapper.getKey();
                    int rightIndex = (int) mapper.getValue();
                    if (!leftRow[leftIndex].toLowerCase().
                            contentEquals(rightRow[rightIndex].toLowerCase())) {
                        canJoin = false;
                        break;
                    }
                    canJoin = true;
                }

                if (canJoin || mapping.isEmpty()) {
                    String[] rowSet = (String[]) ArrayUtils.addAll(leftRow, rightRow);
                    /*
                    select :: start
                     */
                    String[] selectedColumnsFromRowSet = new String[leftSelections.length + rightSelections.length];
                    int columnIndex = 0;
                    for (int index : leftSelections) {
                        if (index <= rowSet.length - 1) {
                            selectedColumnsFromRowSet[columnIndex++] = rowSet[index];
                        } else {
                            throw new Exception("Selection parameter Index is out of range");
                        }
                    }
                    for (int index : rightSelections) {
                        if (index + leftSelections.length <= rowSet.length - 1) {
                            selectedColumnsFromRowSet[columnIndex++] = rowSet[index + leftSelections.length];
                        } else {
                            throw new Exception("Selection parameter Index is out of range");
                        }
                    }
                    resultSet.add(selectedColumnsFromRowSet);
                    /*
                    select :: end
                     */
                }
            }
        }
        return resultSet;
    }
}
