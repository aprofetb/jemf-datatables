package io.jemf.datatables;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Filter implements Serializable {
  private static final long serialVersionUID = -5435546039208939371L;

  String name;
  Search search;

  public Filter() {
  }

  public Filter(String name, Search search) {
    this.name = name;
    this.search = search;
  }

  public Filter(String name, List<String> value, boolean regex) {
    this(name, new Search(value, regex));
  }

  public Filter(String name, List<String> value) {
    this(name, value, false);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setSearch(Search search) {
    this.search = search;
  }

  public Search getSearch() {
    return search;
  }

  @Override
  public String toString() {
    if (StringUtils.isBlank(name) || search == null || search.getValue() == null)
      return "";
    List<String> queryParams = new ArrayList<>(search.getValue().size());
    for (String value : search.getValue())
      if (StringUtils.isNotEmpty(value))
        queryParams.add(String.format("%s=%s", name, value));
    return StringUtils.join(queryParams.toArray(), "&");
  }

  public boolean isSearchEmpty() {
    return search == null || search.isEmpty();
  }
}
