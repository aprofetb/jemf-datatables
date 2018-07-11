package io.jemf.datatables;

import java.io.Serializable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import io.jemf.datatables.Column;
import io.jemf.datatables.DtRequest;
import io.jemf.datatables.Filter;
import io.jemf.datatables.Order;
import io.jemf.datatables.Search;

public class DtRequestTest {
  Map<String, List<String>> parameters;
  Map<String, Map<String, Serializable>> parsedMap;

  public DtRequestTest() {
  }

  @Before
  public void setUp()
    throws Exception {
    parameters = new HashMap<>();
    parameters.put("draw", Arrays.asList("1"));
    parameters.put("columns[0][data]", Arrays.asList("function"));
    parameters.put("columns[0][name]", Arrays.asList("programId"));
    parameters.put("columns[0][searchable]", Arrays.asList("true"));
    parameters.put("columns[0][orderable]", Arrays.asList("true"));
    parameters.put("columns[0][search][value]", Arrays.asList(""));
    parameters.put("columns[0][search][regex]", Arrays.asList("false"));
    parameters.put("columns[1][data]", Arrays.asList("function"));
    parameters.put("columns[1][name]", Arrays.asList("itemId"));
    parameters.put("columns[1][searchable]", Arrays.asList("true"));
    parameters.put("columns[1][orderable]", Arrays.asList("true"));
    parameters.put("columns[1][search][value]", Arrays.asList(""));
    parameters.put("columns[1][search][regex]", Arrays.asList("false"));
    parameters.put("columns[2][data]", Arrays.asList("function"));
    parameters.put("columns[2][name]", Arrays.asList(""));
    parameters.put("columns[2][searchable]", Arrays.asList("true"));
    parameters.put("columns[2][orderable]", Arrays.asList("false"));
    parameters.put("columns[2][search][value]", Arrays.asList(""));
    parameters.put("columns[2][search][regex]", Arrays.asList("false"));
    parameters.put("order[0][column]", Arrays.asList("1"));
    parameters.put("order[0][dir]", Arrays.asList("asc"));
    parameters.put("order[1][column]", Arrays.asList("2"));
    parameters.put("order[1][dir]", Arrays.asList("desc"));
    parameters.put("start", Arrays.asList("0"));
    parameters.put("length", Arrays.asList("10"));
    parameters.put("search[value]", Arrays.asList(""));
    parameters.put("search[regex]", Arrays.asList("false"));
    parameters.put("filters[visibleColumns][search][value][]", Arrays.asList("0", "1"));
    parameters.put("filters[visibleColumns][search][regex]", Arrays.asList("false"));
    parameters.put("filters[programId][search][regex]", Arrays.asList("false"));
    parameters.put("filters[itemId][search][regex]", Arrays.asList("false"));

    parsedMap = DtRequest.parseParameters(parameters);
  }

  /**
   * @see io.jemf.datatables.DtRequest#getColumns(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.io.Serializable>>)
   */
  @Test
  public void testGetColumns() {
    List<Column> columns = DtRequest.getColumns(parsedMap);
    for (Column column : columns) {
      assertNotNull(column);

      String keyPrefix = String.format("%s[%d]", DtRequest.COLUMNS.COLUMNS, column.getIndex());

      List<String> paramValues = parameters.get(String.format("%s[%s]", keyPrefix, DtRequest.COLUMNS.DATA));
      assertNotNull(paramValues);
      assertFalse(paramValues.isEmpty());
      assertEquals(paramValues.get(0), column.getData());

      paramValues = parameters.get(String.format("%s[%s]", keyPrefix, DtRequest.COLUMNS.NAME));
      assertNotNull(paramValues);
      assertFalse(paramValues.isEmpty());
      assertEquals(paramValues.get(0), column.getName());

      paramValues = parameters.get(String.format("%s[%s]", keyPrefix, DtRequest.COLUMNS.SEARCHABLE));
      if (paramValues != null && !paramValues.isEmpty())
        assertEquals(paramValues.get(0), Boolean.toString(column.isSearchable()));

      paramValues = parameters.get(String.format("%s[%s]", keyPrefix, DtRequest.COLUMNS.ORDERABLE));
      if (paramValues != null && !paramValues.isEmpty())
        assertEquals(paramValues.get(0), Boolean.toString(column.isOrderable()));

      checkSearch(keyPrefix, column.getSearch());
    }
  }

  private void checkSearch(String keyPrefix, Search search) {
    String searchValueKey = String.format("%s[%s][%s]", keyPrefix, DtRequest.SEARCH.SEARCH, DtRequest.SEARCH.VALUE);
    List<String> paramValues = parameters.get(searchValueKey);
    if (paramValues == null)
      paramValues = parameters.get(searchValueKey + "[]");
    if (paramValues != null && !paramValues.isEmpty()) {
      assertNotNull(search);
      List<String> searchValues = search.getValue();
      assertNotNull(searchValues);
      assertEquals(paramValues.size(), searchValues.size());
      for (int i = 0; i < searchValues.size(); i++)
        assertEquals(paramValues.get(i), searchValues.get(i));
    }

    paramValues = parameters.get(String.format("%s[%s][%s]", keyPrefix, DtRequest.SEARCH.SEARCH, DtRequest.SEARCH.REGEX));
    if (paramValues != null && !paramValues.isEmpty()) {
      assertNotNull(search);
      assertEquals(paramValues.get(0), Boolean.toString(search.isRegex()));
    }
  }

  /**
   * @see io.jemf.datatables.DtRequest#getOrder(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.io.Serializable>>)
   */
  @Test
  public void testGetOrder() {
    List<Order> orderList = DtRequest.getOrder(parsedMap);
    for (Order order : orderList) {
      assertNotNull(order);

      String keyPrefix = String.format("%s[%d]", DtRequest.ORDER.ORDER, order.getIndex());

      List<String> paramValues = parameters.get(String.format("%s[%s]", keyPrefix, DtRequest.ORDER.COLUMN));
      assertNotNull(paramValues);
      assertFalse(paramValues.isEmpty());
      assertEquals(paramValues.get(0), Integer.toString(order.getColumn()));

      paramValues = parameters.get(String.format("%s[%s]", keyPrefix, DtRequest.ORDER.DIR));
      assertNotNull(paramValues);
      assertFalse(paramValues.isEmpty());
      assertEquals(paramValues.get(0), order.getDir());
    }
  }

  /**
   * @see io.jemf.datatables.DtRequest#getSearch(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.io.Serializable>>)
   */
  @Test
  public void testGetSearch() {
    Search search = DtRequest.getSearch(parsedMap);
    assertNotNull(search);
    checkSearch(DtRequest.SEARCH.SEARCH, search);
  }

  /**
   * @see io.jemf.datatables.DtRequest#getFilters(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.io.Serializable>>)
   */
  @Test
  public void testGetFilters() {
    Map<String, Filter> filters = DtRequest.getFilters(parsedMap);
    for (Filter filter : filters.values()) {
      assertNotNull(filter);
      String keyPrefix = String.format("%s[%s]", DtRequest.FILTERS, filter.getName());
      checkSearch(keyPrefix, filter.getSearch());
    }
  }

  /**
   * @see io.jemf.datatables.DtRequest#getColumnByName(java.util.List<io.jemf.datatables.Column>,String)
   */
  @Test
  public void testGetColumnByName() {
    List<Column> columns = DtRequest.getColumns(parsedMap);
    for (Column column : columns)
      if (StringUtils.isNotEmpty(column.getName()))
        assertSame(column, DtRequest.getColumnByName(columns, column.getName()));
  }

  /**
   * @see io.jemf.datatables.DtRequest#getColumnIndexByName(java.util.List<io.jemf.datatables.Column>,String)
   */
  @Test
  public void testGetColumnIndexByName() {
    List<Column> columns = DtRequest.getColumns(parsedMap);
    for (Column column : columns)
      if (StringUtils.isNotEmpty(column.getName()))
        assertEquals(Integer.valueOf(column.getIndex()), DtRequest.getColumnIndexByName(columns, column.getName()));
  }

  /**
   * @see io.jemf.datatables.DtRequest#getColumnByIndex(java.util.List<io.jemf.datatables.Column>,int)
   */
  @Test
  public void testGetColumnByIndex() {
    List<Column> columns = DtRequest.getColumns(parsedMap);
    for (Column column : columns)
      if (StringUtils.isNotEmpty(column.getName()))
        assertEquals(column, DtRequest.getColumnByIndex(columns, column.getIndex()));
  }

  /**
   * @see io.jemf.datatables.DtRequest#getColumnNameByIndex(java.util.List<io.jemf.datatables.Column>,int)
   */
  @Test
  public void testGetColumnNameByIndex() {
    List<Column> columns = DtRequest.getColumns(parsedMap);
    for (Column column : columns)
      if (StringUtils.isNotEmpty(column.getName()))
        assertSame(column.getName(), DtRequest.getColumnNameByIndex(columns, column.getIndex()));
  }

  /**
   * @see io.jemf.datatables.DtRequest#parseParameters(java.util.Map<java.lang.String, java.util.List<java.lang.String>>)
   */
  @Test
  public void testParseParameters() {
    assertTrue(parsedMap.size() < parameters.size());
    for (Map.Entry<String, Map<String, Serializable>> e : parsedMap.entrySet())
      checkParsedMap(e.getKey(), e.getValue());
  }

  @SuppressWarnings("unchecked")
  private void checkParsedMap(String parentKey, Map<String, Serializable> content) {
    for (Map.Entry<String, Serializable> e : content.entrySet()) {
      String currentKey = StringUtils.isBlank(parentKey) ? e.getKey() : String.format("%s[%s]", parentKey, e.getKey());
      Serializable entryValue = e.getValue();
      if (entryValue instanceof Map) {
        checkParsedMap(currentKey, (Map<String, Serializable>) entryValue);
      } else {
        boolean isArray = entryValue instanceof List;
        if (isArray)
          currentKey += "[]";
        assertTrue(parameters.containsKey(currentKey));
        List<String> paramValues = parameters.get(currentKey);
        if (isArray) {
          List<String> list = (List<String>) entryValue;
          assertEquals(paramValues.size(), list.size());
          for (int i = 0; i < list.size(); i++)
            assertEquals(paramValues.get(i), list.get(i));
        } else {
          assertEquals(paramValues.get(0), entryValue);
        }
      }
    }
  }
}
