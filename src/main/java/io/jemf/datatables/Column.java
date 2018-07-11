package io.jemf.datatables;

import java.io.Serializable;

public class Column implements Comparable<Column>, Serializable {
  private static final long serialVersionUID = -7869910221149844346L;

  int index;
  String data;
  String name;
  boolean searchable;
  boolean orderable;
  Search search;

  public Column() {
  }

  public Column(int index, String data, String name, boolean searchable, boolean orderable, Search search) {
    this.index = index;
    this.data = data;
    this.name = name;
    this.searchable = searchable;
    this.orderable = orderable;
    this.search = search;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setSearchable(boolean searchable) {
    this.searchable = searchable;
  }

  public boolean isSearchable() {
    return searchable;
  }

  public void setOrderable(boolean orderable) {
    this.orderable = orderable;
  }

  public boolean isOrderable() {
    return orderable;
  }

  public void setSearch(Search search) {
    this.search = search;
  }

  public Search getSearch() {
    return search;
  }

  public boolean isSearchEmpty() {
    return search == null || search.isEmpty();
  }

  public int compareTo(Column o) {
    return Integer.valueOf(index).compareTo(o.index);
  }
}
