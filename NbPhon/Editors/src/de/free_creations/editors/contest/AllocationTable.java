/*
 * Copyright 2013 Harald Postner <Harald at free-creations.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.free_creations.editors.contest;

import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.JobCollection;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TimeSlotCollection;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class AllocationTable extends JTable {

  private static final Color disabledColor = new Color(170, 170, 170);
  private static final Color headerColorLight = new Color(226, 230, 233);
  private static final Color headerColorDark = new Color(199, 207, 214);
  //private final AllocationPersonsComboBox comboBox;

  /**
   * The cell key is used as the "value" of a cell.
   *
   * It is not the value displayed to the user, but rather an indication to
   * which event this cell relates. The table renderer will use this item to
   * determine what is be to displayed.
   */
  public static class CellKey {

    public final Integer eventId;
    public final String jobId;

    public CellKey(Integer eventId, String jobId) {
      this.eventId = eventId;
      this.jobId = jobId;
    }

    @Override
    public String toString() {
      return "{" + "eventId=" + eventId + ", jobId=" + jobId + '}';
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 67 * hash + Objects.hashCode(this.eventId);
      hash = 67 * hash + Objects.hashCode(this.jobId);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final CellKey other = (CellKey) obj;
      if (!Objects.equals(this.eventId, other.eventId)) {
        return false;
      }
      if (!Objects.equals(this.jobId, other.jobId)) {
        return false;
      }
      return true;
    }

  }

  /**
   * The CellAdaptor forwards "PROP_VALUE_CHANGED" messages from an individual
   * cell to the table model.
   */
  private class CellAdaptor implements PropertyChangeListener {

    private final int row;
    private final int col;

    public CellAdaptor(int row, int col) {
      this.row = row;
      this.col = col;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (TimeTableCellPanel.PROP_VALUE_CHANGED.equals(evt.getPropertyName())) {
        ((AbstractTableModel) getModel()).fireTableCellUpdated(row, col);
      }
    }
  }

  private class AllocationTableCellRenderer extends DefaultTableCellRenderer {

    private final JLabel headerRenderer = new JLabel();

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column) {

      JLabel result = headerRenderer;
      headerRenderer.setOpaque(true);
      headerRenderer.setBackground(Color.WHITE); // the default

      AllocationTableModel model = (AllocationTableModel) getModel();
      String defaultText = (value == null) ? "" : value.toString();
      headerRenderer.setText(defaultText);
      if (column < 2) {
        if (model.getRowType(row) == RowType.dayOfTimeRow) {
          headerRenderer.setBackground(headerColorDark);
        } else {
          headerRenderer.setBackground(headerColorLight);
        }
      } else {
        if (model.getRowType(row) == RowType.dayOfTimeRow) {
          headerRenderer.setBackground(headerColorDark);
        } else {
          if (value instanceof CellKey) {
            AllocationTableCellPanel dataLabel = getAllocationTableCellPanel(table, row, column);
            dataLabel.setKey((CellKey) value);
            dataLabel.setSelected(isSelected);
            result = dataLabel;
          }
        }
      }
      if (hasFocus) {
        result.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
      } else {
        result.setBorder(null);
      }
      return result;
    }
  };

  private final AllocationTableCellRenderer renderer;

  private class AllocationTableCellEditor extends DefaultCellEditor {

    public AllocationTableCellEditor(AllocationPersonsComboBox comboBox) {
      super(comboBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
      return null;
//      Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);
//      if (editor instanceof AllocationPersonsComboBox) {
//        AllocationPersonsComboBox cb = (AllocationPersonsComboBox) editor;
//        if (value instanceof Allocation) {
//          Allocation t = (Allocation) value;
//          cb.setSelectedPerson(t.getPerson());
//        } else {
//          cb.setSelectedPerson(null);
//        }
//      }
//      return editor;

    }
  };

  //private final AllocationTableCellEditor tableCellEditor;
  /**
   * The row-type indicates whether a row displays a time-of-day or a kind of
   * job.
   */
  protected enum RowType {

    dayOfTimeRow,
    jobNameRow
  }

  public AllocationTable() {
    super();
    // provisorily we set the renderer to null; if not in design time it will 
    // be set to a new AllocationTableCellRenderer.
    AllocationTableCellRenderer _renderer = null;
    if (java.beans.Beans.isDesignTime()) {
      initDesignTimeDisplay();
    } else {
      AllocationTableModel allocationTableModel = new AllocationTableModel(null);
      setModel(allocationTableModel);
      AllocationPersonsComboBox comboBox = new AllocationPersonsComboBox();
      AllocationTableCellEditor tableCellEditor = new AllocationTableCellEditor(comboBox);
      setDefaultEditor(Object.class, tableCellEditor);
      _renderer = new AllocationTableCellRenderer();
    }
    renderer = _renderer;
    rowHeight = 24 + 6;
    gridColor = Color.lightGray;
    showHorizontalLines = true;
    showVerticalLines = true;
    getColumnModel().getColumn(0).setMaxWidth(300);// makes the first colomn smaller (concrete value seems to be ignored)
    getColumnModel().getColumn(1).setMaxWidth(300);
    setCellSelectionEnabled(true);
  }

  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {
    if (renderer == null) {
      return super.getCellRenderer(row, column);
    } else {
      return renderer;
    }
  }

  private AllocationTableCellPanel getAllocationTableCellPanel(JTable table, int row, int column) {
    int hash = column * maxRows + row;
    if (!cellCache.containsKey(hash)) {
      AllocationTableCellPanel allocationTableCellPanel = new AllocationTableCellPanel(
              new Color(170, 170, 170),
              table.getSelectionBackground(),
              table.getSelectionForeground());
      CellAdaptor cellAdaptor = new CellAdaptor(row, column);
      allocationTableCellPanel.addPropertyChangeListener(cellAdaptor);
      cellCache.put(hash, allocationTableCellPanel);
    }
    return cellCache.get(hash);
  }
  private final HashMap<Integer, AllocationTableCellPanel> cellCache = new HashMap<>();
  private static final int maxRows = 10007;

  void setContestId(Integer key) {
    TableModel oldModel = getModel();
    if (oldModel instanceof AllocationTableModel) {
      AllocationTableModel oldAllocModel = (AllocationTableModel) oldModel;
      Integer oldKey = oldAllocModel.getContestId();
      if (Objects.equals(oldKey, key)) {
        return;
      }
      oldAllocModel.stopListening();
    }
    AllocationTableModel allocationTableModel = new AllocationTableModel(key);
    setModel(allocationTableModel);
    allocationTableModel.startListening();
  }

  /**
   * We use the prepare-renderer-method to color the cells.
   *
   * @param renderer
   * @param row
   * @param column
   * @return
   */
  private void initDesignTimeDisplay() {
    setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{
              {"Vormittag", null, null, null, null, null, null, null, null},
              {null, "Lehrkraft", null, null, null, null, null, null, null},
              {null, "Empfang", null, null, null, null, null, null, null},
              {null, "Saaldienst", null, null, null, null, null, null, null},
              {null, "Springer", null, null, null, null, null, null, null},
              {"Nachmittag", null, null, null, null, null, null, null, null},
              {null, "Lehrkraft", null, null, null, null, null, null, null},
              {null, "Empfang", null, null, null, null, null, null, null},
              {null, "Saaldienst", null, null, null, null, null, null, null},
              {null, "Springer", null, null, null, null, null, null, null},
              {"Abend", null, null, null, null, null, null, null, null},
              {null, "Lehrkraft", null, null, null, null, null, null, null},
              {null, "Empfang", null, null, null, null, null, null, null},
              {null, "Saaldienst", null, null, null, null, null, null, null},
              {null, "Springer", null, null, null, null, null, null, null}
            },
            new String[]{
              ".Design.", "Funktion", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
            }) {
              @Override
              public Class<?> getColumnClass(int columnIndex) {
                return String.class;
              }
            });
    setColumnSelectionAllowed(true);
    getColumnModel().getColumn(0).setPreferredWidth(100);
    getColumnModel().getColumn(1).setPreferredWidth(100);
  }

  /**
   * The table model used in the AllocationTable.
   */
  public class AllocationTableModel extends AbstractTableModel {

    private final String[] timeOfDayNames;
    private final int timeOfDayCount;
    private final String[] dayNames;
    private final int dayCount;
    private final Integer ContestId;
    private final String[] jobNames;
    private final String[] jobKeys;
    private final int jobCount;

    AllocationTableModel(Integer juryId) {
      this.ContestId = juryId;
      TimeSlotCollection tt = Manager.getTimeSlotCollection();
      dayNames = tt.dayNames();
      dayCount = dayNames.length;
      timeOfDayNames = tt.timeOfDayNames();
      timeOfDayCount = timeOfDayNames.length;
      JobCollection jj = Manager.getJobCollection();
      jobNames = jj.jobNames();
      jobKeys = jj.jobKeys();
      jobCount = jobNames.length;

    }

    public Integer getContestId() {
      return ContestId;
    }

    @Override
    public int getRowCount() {
      return timeOfDayCount * (jobCount + 1);
    }

    @Override
    public int getColumnCount() {
      return dayCount + 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      switch (columnIndex) {
        case 0:
          if (getRowType(rowIndex) == RowType.dayOfTimeRow) {
            return getDayOfTimeHeader(rowIndex);
          } else {
            return "";
          }
        case 1:
          if (getRowType(rowIndex) == RowType.jobNameRow) {
            return getJobName(rowIndex);
          } else {
            return "";
          }
        default:
          if (getRowType(rowIndex) == RowType.jobNameRow) {
            CellKey cellKey = getCellKey(rowIndex, columnIndex);
            if (cellKey == null) {
              return "none";
            } else {
              return cellKey;
            }
          } else {
            return "";
          }
      }
    }

    public RowType getRowType(int rowIndex) {
      if ((rowIndex % (jobCount + 1)) == 0) {
        return RowType.dayOfTimeRow;
      } else {
        return RowType.jobNameRow;
      }
    }

    private Object getDayOfTimeHeader(int rowIndex) {
      int tdi = timeOfDayIndex(rowIndex);
      return timeOfDayNames[tdi];
    }

    /**
     * The job name to display for a given row.
     *
     * @param row
     * @return
     */
    private String getJobName(int row) {
      int fi = JobIndex(row);
      return jobNames[fi];
    }

    /**
     * The jobId that is
     *
     * @param row
     * @return
     */
    private String getJobKey(int row) {
      int fi = JobIndex(row);
      return jobKeys[fi];
    }

    @Override
    public String getColumnName(int column) {
      if (column < 2) {
        return "";
      } else {
        return dayNames[column - 2];
      }
    }

    private Event getEventFor(int row, int col) {
      if (ContestId == null) {
        return null;
      }
      int timeOfDay = timeOfDayIndex(row);
      int day = dayIndex(col);
      try {
        TimeSlot t = Manager.getTimeSlotCollection().findEntity(day, timeOfDay);
        if (t == null) {
          return null;
        }
        Contest c = Manager.getContestCollection().findEntity(ContestId);
        if (c == null) {
          return null;
        }
        Event e = Manager.getEventCollection().findEntity(c, t);
        return e;
      } catch (DataBaseNotReadyException ex) {
        return null;
      }
    }

    private CellKey getCellKey(int row, int col) {
      Event e = getEventFor(row, col);
      if (e == null) {
        return null;
      }
      String j = getJobKey(row);
      CellKey cellKey = new CellKey(e.getEventId(), j);

      return cellKey;
    }

    private int timeOfDayIndex(int rowIndex) {
      return (rowIndex / (jobCount + 1));
    }

    private int JobIndex(int row) {
      return (row % (jobCount + 1)) - 1;
    }

    private int dayIndex(int col) {
      assert (col > 1);
      return col - 2;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      if (column < 2) {
        return false;
      }
      if (getRowType(row) == RowType.dayOfTimeRow) {
        return false;
      }
      return true;
    }

    private void stopListening() {
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void startListening() {
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      System.out.println(String.format("setValueAt(%s, %s, %s)",
              aValue,
              rowIndex,
              columnIndex));
    }
  }
}
