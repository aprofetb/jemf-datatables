package io.jemf.datatables;

import java.util.List;
import java.util.Map;

public class DtResponse {
  int draw;
  long recordsTotal;
  long recordsFiltered;
  List<?> data;
  String error;
  Map<Integer, List<FooterCell>> footer;

  public DtResponse(int draw, long recordsTotal, long recordsFiltered, List<?> data) {
    this(draw, recordsTotal, recordsFiltered, data, null);
  }

  public DtResponse(int draw, long recordsTotal, long recordsFiltered, List<?> data, Map<Integer, List<FooterCell>> footer) {
    this.draw = draw;
    this.recordsTotal = recordsTotal;
    this.recordsFiltered = recordsFiltered;
    this.data = data;
    this.footer = footer;
  }

  public DtResponse(int draw, String error) {
    this.draw = draw;
    this.error = error;
  }

  public void setDraw(int draw) {
    this.draw = draw;
  }

  public int getDraw() {
    return draw;
  }

  public void setRecordsTotal(long recordsTotal) {
    this.recordsTotal = recordsTotal;
  }

  public long getRecordsTotal() {
    return recordsTotal;
  }

  public void setRecordsFiltered(long recordsFiltered) {
    this.recordsFiltered = recordsFiltered;
  }

  public long getRecordsFiltered() {
    return recordsFiltered;
  }

  public void setData(List<?> data) {
    this.data = data;
  }

  public List<?> getData() {
    return data;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setFooter(Map<Integer, List<FooterCell>> footer) {
    this.footer = footer;
  }

  public Map<Integer, List<FooterCell>> getFooter() {
    return footer;
  }
}
