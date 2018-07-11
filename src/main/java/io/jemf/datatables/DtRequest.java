package io.jemf.datatables;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DtRequest implements Serializable {
  private static final long serialVersionUID = -1274699611475914704L;

  public static final class COLUMNS {
    public final static String
      COLUMNS = "columns",
      DATA = "data",
      NAME = "name",
      SEARCHABLE = "searchable",
      ORDERABLE = "orderable";
  }

  public static final class SEARCH {
    public final static String
      SEARCH = "search",
      VALUE = "value",
      REGEX = "regex";
  }

  public static final class ORDER {
    public final static String
      ORDER = "order",
      COLUMN = "column",
      DIR = "dir";
  }

  public static final String FILTERS = "filters";

  final Map<String, Map<String, Serializable>> parsedMap;
  List<Column> columns;
  List<Order> order;
  Search search;
  Map<String, Filter> filters;

  public DtRequest() {
    this(null);
  }

  public DtRequest(Map<String, List<String>> parameters) {
    this(parameters, false);
  }

  public DtRequest(Map<String, List<String>> parameters, boolean immediate) {
    parsedMap = parseParameters(parameters);
    if (immediate) {
      columns = getColumns(parsedMap);
      order = getOrder(parsedMap);
      search = getSearch(parsedMap);
      filters = getFilters(parsedMap);
    }
  }

  public Map<String, Map<String, Serializable>> getParsedMap() {
    return parsedMap;
  }

  public List<Column> getColumns() {
    if (columns == null)
      columns = getColumns(parsedMap);
    return columns;
  }

  public List<Order> getOrder() {
    if (order == null)
      order = getOrder(parsedMap);
    return order;
  }

  public Search getSearch() {
    if (search == null)
      search = getSearch(parsedMap);
    return search;
  }

  public Map<String, Filter> getFilters() {
    if (filters == null)
      filters = getFilters(parsedMap);
    return filters;
  }

  public static Column getColumnByName(List<Column> columns, String name) {
    if (columns == null || StringUtils.isBlank(name))
      return null;
    for (Column column : columns)
      if (name.equals(column.getName()))
        return column;
    return null;
  }

  public Column getColumnByName(String name) {
    return getColumnByName(getColumns(), name);
  }

  public static Integer getColumnIndexByName(List<Column> columns, String name) {
    Column column = getColumnByName(columns, name);
    return column != null ? column.getIndex() : null;
  }

  public Integer getColumnIndexByName(String name) {
    return getColumnIndexByName(getColumns(), name);
  }

  public static Column getColumnByIndex(List<Column> columns, int index) {
    if (columns == null)
      return null;
    for (Column column : columns)
      if (index == column.getIndex())
        return column;
    return null;
  }

  public Column getColumnByIndex(int index) {
    return getColumnByIndex(getColumns(), index);
  }

  public static String getColumnNameByIndex(List<Column> columns, int index) {
    Column column = getColumnByIndex(columns, index);
    return column != null ? column.getName() : null;
  }

  public String getColumnNameByIndex(int index) {
    return getColumnNameByIndex(getColumns(), index);
  }

  public DtRequest addColumn(Column column) {
    getColumns().add(column);
    return this;
  }

  public DtRequest addOrder(Order order) {
    getOrder().add(order);
    return this;
  }

  public DtRequest setSearch(Search search) {
    this.search = search;
    return this;
  }

  public DtRequest putFilter(String filterName, Filter filter) {
    getFilters().put(filterName, filter);
    return this;
  }

  public static List<Column> getColumns(Map<String, Map<String, Serializable>> parsedMap) {
    Map<String, Serializable> content = parsedMap.get(DtRequest.COLUMNS.COLUMNS);
    if (content == null || content.isEmpty())
      return new ArrayList<Column>();
    List<Column> columns = new ArrayList<>(content.size());
    for (Map.Entry<String, Serializable> e : content.entrySet()) {
      String key = e.getKey();
      Column column = new Column();
      int index = Integer.valueOf(key);
      column.setIndex(index);
      Map<String, Serializable> mapL1 = (Map<String, Serializable>) e.getValue();
      column.setData((String) mapL1.get(DtRequest.COLUMNS.DATA));
      column.setName((String) mapL1.get(DtRequest.COLUMNS.NAME));
      column.setSearchable(!"false".equals(mapL1.get(DtRequest.COLUMNS.SEARCHABLE)));
      column.setOrderable(!"false".equals(mapL1.get(DtRequest.COLUMNS.ORDERABLE)));
      Map<String, Serializable> mapL2 = (Map<String, Serializable>) mapL1.get(DtRequest.SEARCH.SEARCH);
      column.setSearch(new Search((String) mapL2.get(DtRequest.SEARCH.VALUE), Boolean.valueOf((String) mapL2.get(DtRequest.SEARCH.REGEX))));
      columns.add(column);
    }
    Collections.sort(columns);
    return columns;
  }

  public static List<Order> getOrder(Map<String, Map<String, Serializable>> parsedMap) {
    Map<String, Serializable> content = parsedMap.get(DtRequest.ORDER.ORDER);
    if (content == null || content.isEmpty())
      return new ArrayList<Order>();
    List<Order> order = new ArrayList<>(content.size());
    for (Map.Entry<String, Serializable> e : content.entrySet()) {
      String key = e.getKey();
      Order o = new Order();
      int index = Integer.valueOf(key);
      o.setIndex(index);
      Map<String, Serializable> mapL1 = (Map<String, Serializable>) e.getValue();
      o.setColumn(Integer.valueOf((String) mapL1.get(DtRequest.ORDER.COLUMN)));
      o.setDir((String) mapL1.get(DtRequest.ORDER.DIR));
      order.add(o);
    }
    Collections.sort(order);
    return order;
  }

  public static Search getSearch(Map<String, Map<String, Serializable>> parsedMap) {
    Map<String, Serializable> content = parsedMap.get(DtRequest.SEARCH.SEARCH);
    return content != null ? new Search((String) content.get(DtRequest.SEARCH.VALUE), Boolean.valueOf((String) content.get(DtRequest.SEARCH.REGEX))) : null;
  }

  public static Map<String, Filter> getFilters(Map<String, Map<String, Serializable>> parsedMap) {
    Map<String, Serializable> content = parsedMap.get(FILTERS);
    if (content == null || content.isEmpty())
      return new HashMap<>();
    Map<String, Filter> filters = new HashMap<>(content.size());
    for (Map.Entry<String, Serializable> e : content.entrySet()) {
      String filterName = e.getKey();
      Filter filter = new Filter();
      Map<String, Serializable> mapL1 = (Map<String, Serializable>) e.getValue();
      filter.setName(filterName);
      Map<String, Serializable> mapL2 = (Map<String, Serializable>) mapL1.get(DtRequest.SEARCH.SEARCH);
      Serializable value = mapL2.get(DtRequest.SEARCH.VALUE);
      boolean regex = Boolean.valueOf((String) mapL2.get(DtRequest.SEARCH.REGEX));
      Search search = value instanceof String ? new Search((String) value, regex) : new Search((List<String>) value, regex);
      filter.setSearch(search);
      filters.put(filterName, filter);
    }
    return filters;
  }

  public static Map<String, Map<String, Serializable>> parseParameters(Map<String, List<String>> parameters) {
    if (parameters == null || parameters.isEmpty())
      return new HashMap<>();
    Map<String, Map<String, Serializable>> parsedMap = new HashMap<>();
    for (Map.Entry<String, List<String>> e : parameters.entrySet()) {
      String key = e.getKey();
      List<String> values = e.getValue();
      if (values == null || values.isEmpty())
        continue;

      String[] keys = key.split("\\]\\[|\\]|\\[");
      boolean isArray = key.endsWith("[]");
      String mapName = keys[0];

      Map<String, Serializable> content = parsedMap.get(mapName);
      if (content == null) {
        content = new HashMap<>();
        parsedMap.put(mapName, content);
      }

      for (int i = 1; i < keys.length; i++) {
        String iKey = keys[i];
        if (i < keys.length - 1) {
          if (!content.containsKey(iKey))
            content.put(iKey, new HashMap<>());
          content = (Map<String, Serializable>) content.get(iKey);
        } else {
          if (isArray && !(values instanceof Serializable))
            values = new ArrayList<>(values);
          content.put(iKey, isArray ? (Serializable) values : values.get(0));
        }
      }
    }
    return parsedMap;
  }
}
