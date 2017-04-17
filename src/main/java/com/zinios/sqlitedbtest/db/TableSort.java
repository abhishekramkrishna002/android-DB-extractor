package com.zinios.sqlitedbtest.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TableSort {

    public static enum SORTWAY {
        ASC, DESC
    }

    public static class SortTuple {

        int keyIndex;
        SORTWAY sortway;

        public SortTuple(int keyIndex, SORTWAY sortway) {
            this.keyIndex = keyIndex;
            this.sortway = sortway;
        }
    }

    public static ArrayList<String[]> sort(ArrayList<String[]> rowSet, 
            final SortTuple sortTuple) throws Exception {
        Collections.sort(rowSet, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                switch (sortTuple.sortway) {
                    case ASC:
                        return o1[sortTuple.keyIndex].compareTo(o2[sortTuple.keyIndex]);
                    case DESC:
                        return o2[sortTuple.keyIndex].compareTo(o1[sortTuple.keyIndex]);
                }
                return 0;
            }
        });
        return rowSet;
    }

}
