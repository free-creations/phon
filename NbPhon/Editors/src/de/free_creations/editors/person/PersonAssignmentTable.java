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
package de.free_creations.editors.person;

import de.free_creations.dbEntities.Person;
import de.free_creations.dbEntities.Allocation;
import de.free_creations.dbEntities.Availability;
import de.free_creations.dbEntities.TimeSlot;
import de.free_creations.editors.contest.TimeTableCellPanel;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TimeSlotCollection;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Shows the allocations of one person...
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonAssignmentTable extends JTable {

  private static final Color disabledColor = new Color(170, 170, 170);
  private static final Color headerColorLight = new Color(226, 230, 233);
  private static final Color headerColorDark = new Color(199, 207, 214);

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

  private class PersonAssignmentTableCellRenderer extends DefaultTableCellRenderer {

    private final JLabel defaultRenderer = new JLabel();

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column) {

      JLabel result = defaultRenderer;
      defaultRenderer.setOpaque(true);
      defaultRenderer.setBackground(Color.WHITE); // the default

      String defaultText = (value == null) ? "" : value.toString();
      defaultRenderer.setText(defaultText);
      if (column < 1) {
        // the column header
        defaultRenderer.setBackground(headerColorLight);
      } else {
        PersonAssignmentTableCellPanel tableCellPanel = getTableCellPanel(row, column);
        tableCellPanel.setValue(value);
        tableCellPanel.setSelected(isSelected);
        result = tableCellPanel;
      }
      if (hasFocus) {
        result.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
      } else {
        result.setBorder(null);
      }
      return result;
    }
  };
  private final PersonAssignmentTableCellRenderer personAssignmentTableCellRenderer;

  public PersonAssignmentTable() {
    super();
    PersonAssignmentTableCellRenderer _rendrer = null;
    if (java.beans.Beans.isDesignTime()) {
      setModel(makeDesignTimeModel());
      rowHeight = 2 * rowHeight;
      gridColor = Color.lightGray;
    } else {
      _rendrer = new PersonAssignmentTableCellRenderer();
      setCellSelectionEnabled(true);
      showHorizontalLines = true;
      rowHeight = 2 * rowHeight;
      gridColor = Color.lightGray;

      AssignemtTableModel timeTableModel = new AssignemtTableModel(null);
      setModel(timeTableModel);

    }
    personAssignmentTableCellRenderer = _rendrer;
  }

  /**
   * overwritten to in order to use the cell renderer defined in constructor
   *
   * @param row
   * @param column
   * @return
   */
  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {
    if (personAssignmentTableCellRenderer == null) {
      return super.getCellRenderer(row, column);
    } else {
      return personAssignmentTableCellRenderer;
    }
  }

  /**
   * Cache for the panels that are used to display a single cell.
   *
   * @param table
   * @param row
   * @param column
   * @return
   */
  private PersonAssignmentTableCellPanel getTableCellPanel(int row, int column) {
    int hash = column * maxRows + row;
    if (!cellCache.containsKey(hash)) {
      PersonAssignmentTableCellPanel allocationTableCellPanel = new PersonAssignmentTableCellPanel(
              disabledColor,
              getSelectionBackground(),
              getSelectionForeground());

      CellAdaptor cellAdaptor = new CellAdaptor(row, column);
      allocationTableCellPanel.addPropertyChangeListener(cellAdaptor);
      cellCache.put(hash, allocationTableCellPanel);
    }
    return cellCache.get(hash);
  }
  private final HashMap<Integer, PersonAssignmentTableCellPanel> cellCache = new HashMap<>();
  private static final int maxRows = 10007;

  public void setPersonId(Integer personId) {
    TableModel oldModel = getModel();
    if (oldModel instanceof AssignemtTableModel) {
      AssignemtTableModel oldTimeModel = (AssignemtTableModel) oldModel;
      if (Objects.equals(personId, oldTimeModel.getPersonId())) {
        return;
      }
      oldTimeModel.stopListening();
    }

    AssignemtTableModel timeTableModel = new AssignemtTableModel(personId);
    setModel(timeTableModel);
    timeTableModel.startListening();
  }

  /**
   * Returns the data model which shall be displayed at design time.
   *
   * @return a data model shows data similar to the runtime version.
   */
  private TableModel makeDesignTimeModel() {
    return (new javax.swing.table.DefaultTableModel(
            new Object[][]{
              {"Vormittag", "<html>BLOCKFLOETE-1<br>Lehrkraft</html>", "<html>KLAVIER-DUO-1<br>Lehrkraft</html>", "", "", "", "", "<html>Klavier-1<br>Saaldienst</html>"},
              {"Nachmittag", "<html>Gitarre<br>Lehrkraft</html>", "<html>Klavier-1<br>Lehrkraft</html>", "<html>KLAVIER-STREICH<br>Saaldienst</html>", "", "", "", "<html>Klavier-1<br>Saaldienst</html>"},
              {"Abend", "<html>Klavier-1<br>Lehrkraft</html>", "<html>Klavier-1<br>Lehrkraft</html>", "", "", "<html>KLAVIER-STREICH<br>Saaldienst</html>", "<html>KLAVIER-STREICH<br>Saaldienst</html>", "<html>KLAVIER-STREICH<br>Saaldienst</html>"}},
            new String[]{
              "Design", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
            }) {
              @Override
              public Class<?> getColumnClass(int columnIndex) {
                return String.class;
              }
            });
  }

  /**
   * The table model used in the TimeTable.
   */
  public class AssignemtTableModel extends AbstractTableModel implements PropertyChangeListener {

    private final Integer personId;
    private final String[] timeOfDayNames;
    private final String[] dayNames;
    private final int columnCount; // the number of data columns (first column not included)
    private final int rowCount; // the number of data rows (header row not included)

    public AssignemtTableModel(Integer personId) {
      this.personId = personId;
      TimeSlotCollection tt = Manager.getTimeSlotCollection();

      dayNames = tt.dayNames();
      columnCount = dayNames.length;

      timeOfDayNames = tt.timeOfDayNames();
      rowCount = timeOfDayNames.length;
      for (int row = 0; row < rowCount; row++) {
        for (int column = 0; column < columnCount; column++) {
          getTableCellPanel(row, column).startListening(getAvailabilityId(row, column));
        }
      }
    }

    @Override
    public int getRowCount() {
      return rowCount;
    }

    public Integer getPersonId() {
      return personId;
    }

    /**
     * Get the entity that shall be displayed in the given position.
     *
     * @param rowIndex
     * @param columnIndex the column relative to the table (one offset with
     * Manager.getTimeSlotCollection)
     * @return
     */
    public Allocation getAllocationEntity(int rowIndex, int columnIndex) {
      try {
        Person p = Manager.getPersonCollection().findEntity(personId);
        TimeSlot t = Manager.getTimeSlotCollection().findEntity(columnIndex - 1, rowIndex);
        return Manager.getAllocationCollection().findEntity(p, t);
      } catch (DataBaseNotReadyException ignored) {
      }
      return null;
    }

    /**
     * Returns the availability of the person set in dataModel for the time
     * corresponding to row and column.
     *
     * @param row
     * @param column
     * @return
     */
    private Integer getAvailabilityId(int row, int column) {
      Availability a = getAvailabiltyEntity(row, column);
      if (a != null) {
        return a.getAvailabilityId();
      }
      return null;
    }

    /**
     * Determine the availability of the person for the time-slot that
     * corresponds to the given position.
     *
     * @param rowIndex
     * @param columnIndex the column relative to the table (one offset with
     * Manager.getTimeSlotCollection)
     * @return
     */
    private Availability getAvailabiltyEntity(int rowIndex, int columnIndex) {
      try {
        Person p = Manager.getPersonCollection().findEntity(personId);
        TimeSlot t = Manager.getTimeSlotCollection().findEntity(columnIndex - 1, rowIndex);
        return Manager.getAvailabilityCollection().findEntity(p, t);
      } catch (DataBaseNotReadyException ignored) {
      }
      return null;
    }

    /**
     * Get the value that shall be displayed in the given position (not taking
     * into account the row headers).
     *
     * @param rowIndex
     * @param columnIndex the column relative to the table
     * @return
     */
    private Integer getAlloactionId(int rowIndex, int columnIndex) {
      assert (columnIndex > 0);
      Allocation a = getAllocationEntity(rowIndex, columnIndex);
      if (a != null) {
        return a.getAllocationId();
      } else {
        return null;
      }
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
        return getAlloactionId(rowIndex, columnIndex);
      }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      if (columnIndex > 0) {
//        Availability v = getAssignmentEntity(rowIndex, columnIndex);
//        if (v != null) {
//          if (aValue instanceof Boolean) {
//            v.setVerfuegbar((Boolean) aValue);
//          }
//        }
      }
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
     * @return String for column 0,
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
      if (columnIndex > 0) {
        return String.class;
      } else {
        return String.class;
      }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      //return (column > 0) ? true : false;
      return false;
    }

    /**
     * un-subscribe to listen on changes on the person.
     */
    private void stopListening() {
      if (personId != null) {
        Person.removePropertyChangeListener(this, personId);
      }
    }

    /**
     * Subscribe to listen on changes on the person.
     */
    public void startListening() {
      if (personId != null) {
        Person.addPropertyChangeListener(this, personId);
      }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      String propertyName = evt.getPropertyName();
      if (propertyName != null) {
        switch (propertyName) {
          case (Person.PROP_ALLOCATIONADDED):
          case (Person.PROP_ALLOCATIONREMOVED):
            fireTableDataChanged();
        }
      }
    }

  }
}
