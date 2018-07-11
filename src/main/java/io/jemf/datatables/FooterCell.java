package io.jemf.datatables;

import java.io.Serializable;

public class FooterCell implements Serializable {
  private static final long serialVersionUID = -6486464123949636952L;

  String label;
  String tooltip;
  Object value;

  public FooterCell() {
  }

  public FooterCell(String label, String tooltip) {
    this.label = label;
    this.tooltip = tooltip;
  }

  public FooterCell(String label, String tooltip, Object value) {
    this(label, tooltip);
    this.value = value;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }
}
