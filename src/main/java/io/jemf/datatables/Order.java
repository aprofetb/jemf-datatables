package io.jemf.datatables;

import java.io.Serializable;

public class Order implements Comparable<Order>, Serializable {
  private static final long serialVersionUID = 4158873772574644195L;

  int index;
  int column;
  String dir;
  String dbColumnName;

  public Order() {
  }

  public Order(int index, int column, String dir) {
    this(index, column, dir, null);
  }

  public Order(int index, int column, String dir, String dbColumnName) {
    this.index = index;
    this.column = column;
    this.dir = dir;
    this.dbColumnName = dbColumnName;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public int getColumn() {
    return column;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }

  public String getDir() {
    return dir;
  }

  public void setDbColumnName(String dbColumnName) {
    this.dbColumnName = dbColumnName;
  }

  public String getDbColumnName() {
    return dbColumnName;
  }

  @Override
  public String toString() {
    return String.format("[%d,'%s']", column, dir);
  }

  public int compareTo(Order o) {
    return Integer.valueOf(index).compareTo(o.index);
  }
}
