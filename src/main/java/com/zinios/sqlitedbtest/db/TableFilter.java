package com.zinios.sqlitedbtest.db;

import java.util.ArrayList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class TableFilter {

    public static enum FILTER_CONDITION {
        LESSER_THAN,
        GREATER_THAN,
        EQUAL_TO,
        NOT_EQUAL_TO,
        LESSER_THAN_EQUAL_TO,
        GREATER_THAN_EQUAL_TO,
        BETWEEN,
        IN_BETWEEN,
        IN
    }

    public static enum FILTER_TYPE {
        DATE_FILTER,
        NUMBER_FILTER,
        STRING_FILTER
    }

    public static class FilterTuple<T> {

        int keyIndex;
        FILTER_CONDITION filterCondition;
        T filterValue;
        T[] filterValues;

        public FilterTuple(int keyIndex, FILTER_CONDITION filterCondition, T filterValue) {
            this.keyIndex = keyIndex;
            this.filterCondition = filterCondition;
            this.filterValue = filterValue;
        }

        public FilterTuple(int keyIndex, FILTER_CONDITION filterCondition, T[] filterValues) {
            this.keyIndex = keyIndex;
            this.filterCondition = filterCondition;
            this.filterValues = filterValues;
        }
    }

    public static ArrayList<String[]> filter(ArrayList<String[]> rowSet,
            FILTER_TYPE filterType,
            ArrayList<FilterTuple> filters) throws Exception {

        switch (filterType) {
            case NUMBER_FILTER:
                return numberFilter(rowSet, filters);
            case STRING_FILTER:
                return stringFilter(rowSet, filters);
            case DATE_FILTER:
                return stringFilter(rowSet, filters);
        }
        return null;
    }

    private static ArrayList<String[]> numberFilter(ArrayList<String[]> rowSet,
            ArrayList<FilterTuple> filters) throws Exception {
        ArrayList<String[]> resultSet = new ArrayList<>();
        for (String[] row : rowSet) {
            boolean isSelected = false;
            for (FilterTuple<Float> filter : filters) {
                String field = row[filter.keyIndex].trim();
                float floatField = 0.0f;
                if (!NumberHelper.isNumber(field)) {
                    throw new Exception("Invalid Number");
                } else {
                    floatField = NumberHelper.getNumber(field);
                }
                switch (filter.filterCondition) {
                    case LESSER_THAN:
                        isSelected = floatField < filter.filterValue;
                        break;
                    case GREATER_THAN:
                        isSelected = floatField > filter.filterValue;
                        break;
                    case EQUAL_TO:
                        isSelected = floatField == filter.filterValue;
                        break;
                    case NOT_EQUAL_TO:
                        isSelected = floatField != filter.filterValue;
                        break;
                    case LESSER_THAN_EQUAL_TO:
                        isSelected = floatField <= filter.filterValue;
                        break;
                    case GREATER_THAN_EQUAL_TO:
                        isSelected = floatField >= filter.filterValue;
                        break;
                    case BETWEEN:
                        isSelected = (floatField > filter.filterValues[0]
                                && floatField < filter.filterValues[1]);
                        break;
                    case IN_BETWEEN:
                        isSelected = (floatField >= filter.filterValues[0]
                                && floatField <= filter.filterValues[1]);
                        break;
                    case IN:
                        isSelected = ArrayUtils.contains(filter.filterValues, floatField);
                        break;

                    default:
                        throw new Exception("Unsupported Number Filter Operation");
                }
                if (!isSelected) {
                    break;
                }
            }
            if (isSelected) {
                resultSet.add(row);
            }
        }
        return resultSet;
    }

    private static ArrayList<String[]> stringFilter(ArrayList<String[]> rowSet,
            ArrayList<FilterTuple> filters) throws Exception {
        ArrayList<String[]> resultSet = new ArrayList<>();
        for (String[] row : rowSet) {
            boolean isSelected = false;
            for (FilterTuple<String> filter : filters) {
                String field = row[filter.keyIndex].trim();
                switch (filter.filterCondition) {
                    case EQUAL_TO:
                        isSelected = field.contentEquals(filter.filterValue) || field.matches(filter.filterValue);
                        break;
                    case NOT_EQUAL_TO:
                        isSelected = !field.contentEquals(filter.filterValue) || !field.matches(filter.filterValue);
                        break;
                    case IN:
                        for (String f : filter.filterValues) {
                            isSelected = field.contentEquals(f);
                            if (isSelected) {
                                break;
                            }
                        }
                        break;
                    default:
                        throw new Exception("Unsupported String Filter Operation");
                }
                if (!isSelected) {
                    break;
                }
            }
            if (isSelected) {
                resultSet.add(row);
            }
        }
        return resultSet;
    }

    private static ArrayList<String[]> dateFilter(ArrayList<String[]> rowSet,
            ArrayList<FilterTuple> filters) throws Exception {
        ArrayList<String[]> resultSet = new ArrayList<>();
        for (String[] row : rowSet) {
            boolean isSelected = false;
            for (FilterTuple<String> filter : filters) {
                String field = row[filter.keyIndex].trim();
                switch (filter.filterCondition) {
                    case EQUAL_TO:
                        isSelected = field.contentEquals(filter.filterValue) || field.matches(filter.filterValue);
                        break;
                    case NOT_EQUAL_TO:
                        isSelected = !field.contentEquals(filter.filterValue) || !field.matches(filter.filterValue);
                        break;
                    case IN:
                        for (String f : filter.filterValues) {
                            isSelected = field.contentEquals(f);
                            if (isSelected) {
                                break;
                            }
                        }
                        break;
                    default:
                        throw new Exception("Unsupported String Filter Operation");
                }
                if (!isSelected) {
                    break;
                }
            }
            if (isSelected) {
                resultSet.add(row);
            }
        }
        return resultSet;
    }

}
