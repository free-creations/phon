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

import de.free_creations.actions.contest.AllocateLocationForEvent;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Event;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.nbPhon4Netbeans.LocationNode;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TimeSlotCollection;
import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * The time table in the Contest Edit Window (ContestTopComponent).
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class TimeTable extends JTable {

  private final TransferHandler transferHandler = new TransferHandler() {
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {

        return (support.isDataFlavorSupported(LocationNode.LOCATION_NODE_FLAVOR));

    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
      try {
        Integer key = (Integer) support.getTransferable().getTransferData(LocationNode.LOCATION_NODE_FLAVOR);
        int col = getSelectedColumn();
        int row = getSelectedRow();
        if (col > 0) {
          Integer eventId = (Integer) getValueAt(row, col);
          if (eventId != null) {
            AllocateLocationForEvent action = new AllocateLocationForEvent(eventId, key);
            action.apply(0);
            return true;
          }
        }
        return false;

      } catch (DataBaseNotReadyException | ClassCastException | UnsupportedFlavorException | IOException ex) {
        return false;
      }
    }
  };

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

  private final MouseListener timeTableMouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
      if (evt.getClickCount() == 2) {
        int col = columnAtPoint(evt.getPoint());
        int row = rowAtPoint(evt.getPoint());

      }
    }
  };
  private final MouseListener headerMouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
      if (evt.getClickCount() == 2) {
        int col = columnAtPoint(evt.getPoint());

      }
    }
  };

  private final TableCellRenderer tableCellRenderer = new TableCellRenderer() {

    private final JLabel emptyLabel = new JLabel();

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column) {
      if (java.beans.Beans.isDesignTime()) {
        return new JLabel("designTime");
      }

      JComponent c = getBaseComponent(table, value, isSelected, row, column);
      if (hasFocus) {
        c.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
      } else {
        c.setBorder(null);
      }
      return c;
    }

    private JComponent getBaseComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row, int column) {
      if (!isCellVisible(row, column)) {
        // there is no time-slot for this cell
        return emptyLabel;
      }
      if (column == 0) {
        return getRowHeaderComponent(value);
      } else {
        TimeTableCellPanel panel = getTimeTableCellPanel(table, row, column);
        panel.setValue(value);
        panel.setSelected(isSelected);
        return panel;
      }
    }

    private final HashMap<String, JLabel> colHeaderCache = new HashMap<>();

    private JComponent getRowHeaderComponent(Object value) {
      if (value instanceof String) {
        String text = (String) value;
        if (!colHeaderCache.containsKey(text)) {
          JLabel newLabel = new JLabel(text);
          colHeaderCache.put(text, newLabel);
        }
        return colHeaderCache.get(text);
      } else {
        return emptyLabel;
      }
    }
  };

  private class TimeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final TimeTableCellEditorPanel panel;

    private TimeTableCellEditor() {
      this.panel = new TimeTableCellEditorPanel();
    }

    @Override
    public Object getCellEditorValue() {
      return panel.getEventId();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
      panel.setValue(value);
      panel.setSelected(isSelected);
      return panel;
    }
  }

  private final TimeTableCellEditor tableCellEditor;

  public TimeTable() {
    super();
    TimeTableCellEditor _tableCellEditor = null;
    if (java.beans.Beans.isDesignTime()) {
      setModel(makeDesignTimeModel());
    } else {
      tableHeader.addMouseListener(headerMouseListener);
      addMouseListener(timeTableMouseListener);
      setCellSelectionEnabled(true);

      TimeTableModel timeTableModel = new TimeTableModel(null);
      setModel(timeTableModel);
      setDefaultRenderer(Integer.class, tableCellRenderer);
      _tableCellEditor = new TimeTableCellEditor();
      setTransferHandler(transferHandler);

    }
    tableCellEditor = _tableCellEditor;
    rowHeight = 42 + 2;
    gridColor = Color.lightGray;
    showHorizontalLines = true;
    showVerticalLines = true;
  }

  public void setContestId(Integer contestId) {
    TableModel oldModel = getModel();
    if (oldModel instanceof TimeTableModel) {
      TimeTableModel oldTimeModel = (TimeTableModel) oldModel;
      if (Objects.equals(contestId, oldTimeModel.getContestId())) {
        return;
      }
      oldTimeModel.stopListening();
    }

    TimeTableModel timeTableModel = new TimeTableModel(contestId);
    setModel(timeTableModel);
    timeTableModel.startListening();
  }

  /**
   * Avoid selection on the left-most column (the row-header column).
   *
   * @param row
   * @param column
   * @return true for all cells except those in column 0.
   */
//  @Override
//  public boolean isCellSelected(int row, int column) {
//    return (column > 0) ? super.isCellSelected(row, column) : false;
//  }

  /**
   * Cells that correspond to a time-slot that cannot be found in the "ZEIT"
   * table are overlaid with a blank label.
   *
   * @param renderer
   * @param row
   * @param column
   * @return
   */
//  @Override
//  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//    if (java.beans.Beans.isDesignTime()) {
//      return super.prepareRenderer(renderer, row, column);
//    }
//    if (isCellVisible(row, column)) {
//      if (column > 0) {
//        return new TimeTableCellPanel();
//      }
//      return super.prepareRenderer(renderer, row, column);
//    } else {
//      return new JLabel();
//    }
//  }
  /**
   * Provides the panels to be displayed in a cell (in browse mode).
   *
   * Each panel for a given row-col is cached.
   *
   * @param table
   * @param row
   * @param column
   * @return the cached panel (or construct a new new one if none can be found
   * in cache)
   */
  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {
    if (column > 0) {
      return tableCellRenderer;
    }
    // else...
    return super.getCellRenderer(row, column);
  }

  private TimeTableCellPanel getTimeTableCellPanel(JTable table, int row, int column) {

    int hash = column * maxRows + row;
    if (!cellCache.containsKey(hash)) {
      TimeTableCellPanel timeTableCellPanel = new TimeTableCellPanel(
              new Color(170, 170, 170),
              table.getSelectionBackground(),
              table.getSelectionForeground());
      CellAdaptor cellAdaptor = new CellAdaptor(row, column);
      timeTableCellPanel.addPropertyChangeListener(cellAdaptor);
      cellCache.put(hash, timeTableCellPanel);
    }
    return cellCache.get(hash);
  }
  private final HashMap<Integer, TimeTableCellPanel> cellCache = new HashMap<>();
  private static final int maxRows = 10007;

  @Override
  public TableCellEditor getCellEditor(int row, int column) {
    if (column > 0) {
      if (tableCellEditor != null) {
        return tableCellEditor;
      }
    }
    return super.getCellEditor(row, column);

  }

  @Override
  public JPopupMenu getComponentPopupMenu() {
    int col = columnAtPoint(getMousePosition());
    int row = rowAtPoint(getMousePosition());
    if (col > 0) {
      TimeTableCellPanel timeTableCellPanel = getTimeTableCellPanel(this, row, col);
      return timeTableCellPanel.getComponentPopupMenu();
    } else {
      return null;
    }
  }

  /**
   * Cells that correspond to a time-slot that cannot be found in the "ZEIT"
   * table are overlaid with a blank label.
   *
   * @param editor
   * @param row
   * @param column
   * @return
   */
  @Override
  public Component prepareEditor(TableCellEditor editor, int row, int column) {
    if (java.beans.Beans.isDesignTime()) {
      return super.prepareEditor(editor, row, column);
    }
    if (isCellVisible(row, column)) {
      return super.prepareEditor(editor, row, column);
    } else {
      return new JLabel();
    }
  }

  /**
   * Decide which cells should be blanked out (be invisible).
   *
   * Cells that correspond to a time-slot that cannot be found in the "ZEIT"
   * table are overlaid with a blank label.
   *
   * @param row
   * @param column
   * @return true if the cell should be as normal.
   */
  private boolean isCellVisible(int row, int column) {
    if (column == 0) {
      return true;
    }
    try {
      TimeSlot t = Manager.getTimeSlotCollection().findEntity(column - 1, row);
      if (t == null) {
        return false;
      } else {
        return true;
      }
    } catch (DataBaseNotReadyException ex) {
      return false;
    }
  }

  /**
   * Returns the data model which shall be displayed at design time.
   *
   * @return a data model shows data similar to the runtime version.
   */
  private TableModel makeDesignTimeModel() {
    return (new javax.swing.table.DefaultTableModel(
            new Object[][]{
              {"Vormittag", true, null, null, null, true, null, null},
              {"Nachmittag", null, true, null, true, null, null, null},
              {"Abend", null, null, true, false, null, null, null}},
            new String[]{
              ".Design.", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
            }) {
              @Override
              public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex > 0) {
                  return Boolean.class;
                } else {
                  return String.class;
                }
              }
            });
  }

  /**
   * The table model used in the TimeTable.
   */
  public class TimeTableModel extends AbstractTableModel implements PropertyChangeListener {

    private final Integer contestId;
    private final String[] timeOfDayNames;
    private final String[] dayNames;
    private final int columnCount; // the number of data columns (first column not included)
    private final int rowCount; // the number of data rows (header row not included)

    public TimeTableModel(Integer contestId) {
      this.contestId = contestId;

      TimeSlotCollection tt = Manager.getTimeSlotCollection();

      dayNames = tt.dayNames();
      columnCount = dayNames.length;

      timeOfDayNames = tt.timeOfDayNames();
      rowCount = timeOfDayNames.length;
    }

    @Override
    public int getRowCount() {
      return rowCount;
    }

    @Override
    public int getColumnCount() {
      return columnCount + 1;
    }

    /**
     * Get the value that shall be displayed in the given position (including
     * the row headers).
     *
     * @param rowIndex
     * @param columnIndex the column relative to the table.
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      if (columnIndex == 0) {
        return timeOfDayNames[rowIndex];
      } else {
        return getEventIdFor(rowIndex, columnIndex);
      }
    }

    private Integer getEventIdFor(int rowIndex, int columnIndex) {
      if (contestId == null) {
        return null;
      }
      TimeSlot t = getTimeSlotFor(rowIndex, columnIndex);
      if (t == null) {
        return null;
      }
      //--- retrieve the events which happen at the given time-slot.
      List<Event> eventList = t.getEventList();
      //--- within those events, search for the (first) event that happens 
      //    for the given contest (note: here we could check that never two or
      //    more events happen for the same contest at the same time(slot).
      for (Event e : eventList) {
        Contest contest = e.getContest();
        if (contest != null) {
          if (Objects.equals(contest.getContestId(), contestId)) {
            return e.getEventId();
          }
        }
      }
      return null;
    }

    @Override
    public String getColumnName(int column) {
      if (column == 0) {
        return "";
      } else {
        return dayNames[column - 1];
      }
    }

    /**
     *
     * @param columnIndex
     * @return String for column 0, Integer for all other.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
      if (columnIndex > 0) {
        return String.class;
      } else {
        return Integer.class;
      }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return ((column > 0) && (contestId != null));
    }

    /**
     * un-subscribe to listen on changes on the contest.
     */
    private void stopListening() {
      if (contestId != null) {
        Contest.removePropertyChangeListener(this, contestId);
      }
    }

    /**
     * Subscribe to listen on changes on the contest.
     */
    public void startListening() {
      if (contestId != null) {
        Contest.addPropertyChangeListener(this, contestId);
      }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    private Integer getContestId() {
      return contestId;
    }

    private TimeSlot getTimeSlotFor(int rowIndex, int columnIndex) {
      if (columnIndex < 1) {
        return null;
      }
      int day = columnIndex - 1;
      int timeOfDay = rowIndex;
      try {
        return Manager.getTimeSlotCollection().findEntity(day, timeOfDay);
      } catch (DataBaseNotReadyException ex) {
        return null;
      }
    }
  }
}
