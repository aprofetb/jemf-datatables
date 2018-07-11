package io.jemf.datatables;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterUtils {
  final static Logger log = LoggerFactory.getLogger(FilterUtils.class);

  public static Map<String, Filter> getFiltersFromQueryParams(Map<String, List<String>> queryParams) {
    Map<String, Filter> filters = new HashMap<>();
    for (Map.Entry<String, List<String>> e : queryParams.entrySet())
      filters.put(e.getKey(), new Filter(e.getKey(), e.getValue()));
    return filters;
  }

  public static List<String> getFilterValues(Map<String, Filter> filters, String filterName) {
    if (filters == null || StringUtils.isBlank(filterName))
      return Collections.<String>emptyList();
    Filter filter = filters.get(filterName);
    if (filter == null)
      return Collections.<String>emptyList();
    Search search = filter.getSearch();
    return search != null && search.getValue() != null ? search.getValue() : Collections.<String>emptyList();
  }

  public static List<String> getFilterValues(Map<String, Filter> filters, String filterName, String escape) {
    if (escape == null || !escape.matches("java|javascript|sql|xml|xml10|xml11|html|html3|html4"))
      throw new IllegalArgumentException(String.format("Unknown value for parameter @escape: '%s'", escape));
    List<String> filterValues = getFilterValues(filters, filterName);
    List<String> escapedFilterValues = new ArrayList<>(filterValues.size());
    for (String value : filterValues) {
      if (StringUtils.isNotEmpty(value)) {
        if ("java".equalsIgnoreCase(escape))
          value = StringEscapeUtils.escapeJava(value);
        else if ("javascript".equalsIgnoreCase(escape))
          value = StringEscapeUtils.escapeJson(value);
        else if ("sql".equalsIgnoreCase(escape))
          value = value.replaceAll("'", "''");
        else if ("xml".equalsIgnoreCase(escape) || "xml11".equalsIgnoreCase(escape))
          value = StringEscapeUtils.escapeXml11(value);
        else if ("xml10".equalsIgnoreCase(escape))
          value = StringEscapeUtils.escapeXml10(value);
        else if ("html".equalsIgnoreCase(escape) || "html4".equalsIgnoreCase(escape))
          value = StringEscapeUtils.escapeHtml4(value);
        else if ("html3".equalsIgnoreCase(escape))
          value = StringEscapeUtils.escapeHtml3(value);
      }
      escapedFilterValues.add(value);
    }
    return escapedFilterValues;
  }

  public static String getFilterFirstValue(Map<String, Filter> filters, String filterName) {
    List<String> filterValues = getFilterValues(filters, filterName);
    return filterValues != null && filterValues.size() > 0 ? filterValues.get(0) : null;
  }

  public static boolean getFilterFirstValueAsBoolean(Map<String, Filter> filters, String filterName) {
    return Boolean.valueOf(getFilterFirstValue(filters, filterName));
  }

  public static Date getFilterFirstValueAsDate(Map<String, Filter> filters, String filterName, String pattern) {
    try {
      String value = getFilterFirstValue(filters, filterName);
      return value != null ? new SimpleDateFormat(pattern).parse(value) : null;
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public static String getFilterSepByComma(Map<String, Filter> filters, String filterName) {
    StringBuilder filterSepByComma = new StringBuilder();
    if (filters != null && filters.containsKey(filterName)) {
      Filter filter = filters.get(filterName);
      for (String value : filter.getSearch().getValue()) {
        if (StringUtils.isEmpty(value))
          continue;
        if (filterSepByComma.length() > 0)
          filterSepByComma.append(",");
        filterSepByComma.append(String.format("'%s'", value));
      }
    }
    return filterSepByComma.toString();
  }
}
