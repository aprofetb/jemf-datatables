package io.jemf.datatables;

import java.io.Serializable;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Search implements Serializable {
  private static final long serialVersionUID = -9014168005321968998L;

  public static final int OPERATOR_CONTAINS = 1;
  public static final int OPERATOR_EQUALS = 2;

  List<String> value;
  boolean regex;
  boolean caseSensitive;
  int operator;

  public Search() {
  }

  public Search(String value, boolean regex) {
    this(Arrays.asList(value), regex);
  }

  public Search(List<String> value, boolean regex) {
    this.value = value;
    this.regex = regex;
    this.caseSensitive = false;
    this.operator = OPERATOR_CONTAINS;
  }

  public void setValue(List<String> value) {
    this.value = value;
  }

  public List<String> getValue() {
    return value;
  }

  public String getFirstValue() {
    return value != null && value.size() > 0 ? value.get(0) : "";
  }

  public void setRegex(boolean regex) {
    this.regex = regex;
  }

  public boolean isRegex() {
    return regex;
  }

  public void setCaseSensitive(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public void setOperator(int operator) {
    this.operator = operator;
  }

  public int getOperator() {
    return operator;
  }

  public boolean isContainsOperator() {
    return operator == OPERATOR_CONTAINS;
  }

  public boolean isEmpty() {
    return value == null || value.size() == 0 || (value.size() == 1 && StringUtils.isEmpty(value.get(0)));
  }
}
