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

import de.free_creations.dbEntities.Personen;
import de.free_creations.dbEntities.Teameinteilung;
import de.free_creations.dbEntities.Verfuegbarkeit;
import de.free_creations.dbEntities.Zeit;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TimeSlotCollection;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class PersonAssignmentTable extends JTable {

  private static final Color disabledColor = new Color(170, 170, 170);
  private final MouseListener assignemtTableMouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
      if (evt.getClickCount() == 2) {
        int col = columnAtPoint(evt.getPoint());
        int row = rowAtPoint(evt.getPoint());
        if (col == 0) {
          toggleRow(row);
        }
      }
    }
  };
  private final MouseListener headerMouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
      if (evt.getClickCount() == 2) {
        int col = columnAtPoint(evt.getPoint());
        if (col == 0) {
          toggleTable();
        } else {
          toggleColumn(col);
        }
      }
    }
  };

  public PersonAssignmentTable() {
    super();
    if (java.beans.Beans.isDesignTime()) {
      setModel(makeDesignTimeModel());
      rowHeight = 2 * rowHeight;
      gridColor = Color.lightGray;
    } else {
      tableHeader.addMouseListener(headerMouseListener);
      addMouseListener(assignemtTableMouseListener);
      setCellSelectionEnabled(true);
      showHorizontalLines = true;
      rowHeight = 2 * rowHeight;
      gridColor = Color.lightGray;

      AssignemtTableModel timeTableModel = new AssignemtTableModel(null);
      setModel(timeTableModel);

    }
  }

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

  private void toggleRow(int row) {
    if (dataModel instanceof AssignemtTableModel) {
      ((AssignemtTableModel) dataModel).toggleRow(row);
    }
  }

  private void toggleTable() {
    if (dataModel instanceof AssignemtTableModel) {
      ((AssignemtTableModel) dataModel).toggleTable();
    }
  }

  private void toggleColumn(int col) {
    if (dataModel instanceof AssignemtTableModel) {
      ((AssignemtTableModel) dataModel).toggleColumn(col);
    }
  }

  /**
   * Avoid selection on the left-most column (the row-header column).
   *
   * @param row
   * @param column
   * @return true for all cells except those in column 0.
   */
  @Override
  public boolean isCellSelected(int row, int column) {
    return (column > 0) ? super.isCellSelected(row, column) : false;
  }

  /**
   * Cells that correspond to a time-slot that cannot be found in the "ZEIT"
   * table are overlaid with a blank label.
   *
   * @param renderer
   * @param row
   * @param column
   * @return
   */
  @Override
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    if (isCellVisible(row, column)) {
      Component preparedRenderer = super.prepareRenderer(renderer, row, column);
      preparedRenderer.setBackground(Color.WHITE);
      if (column != 0) {
        if (!isPersonAvailable(row, column)) {
          preparedRenderer.setBackground(disabledColor);
        }else{
          
        }
      }
      return preparedRenderer;
    } else {
      JLabel label = new JLabel();
      label.setBackground(disabledColor);
      label.setOpaque(true);
      return label;
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
    if (isCellVisible(row, column)) {
      return super.prepareEditor(editor, row, column);
    } else {
      JLabel label = new JLabel();
      label.setBackground(disabledColor);
      label.setOpaque(true);
      return label;
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
    if (java.beans.Beans.isDesignTime()) {
      return true;
    }
    if (column == 0) {
      return true;
    }
    try {
      Zeit t = Manager.getTimeSlotCollection().findEntity(column - 1, row);
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
   * Get the availability-record for the time-slot corresponding to the row and
   * column.
   *
   * @param rowIndex
   * @param columnIndex the column relative to the table (one offset with
   * Manager.getTimeSlotCollection)
   * @return
   */
  private Verfuegbarkeit getVerfuegEntity(int rowIndex, int columnIndex) {
    if (dataModel instanceof AssignemtTableModel) {
      try {
        Integer personId = ((AssignemtTableModel) dataModel).getPersonId();
        Personen p = Manager.getPersonCollection().findEntity(personId);
        List<Verfuegbarkeit> emptyVv = Collections.emptyList();
        List<Verfuegbarkeit> vv = (p == null) ? emptyVv : p.getVerfuegbarkeitList();
        Zeit t = Manager.getTimeSlotCollection().findEntity(columnIndex - 1, rowIndex);
        for (Verfuegbarkeit v : vv) {
          if (Objects.equals(t, v.getZeitid())) {
            return v;
          }
        }
      } catch (DataBaseNotReadyException ignored) {
      }
    }
    return null;
  }

  /**
   * Get the availability for the time-slot corresponding to the row and column.
   *
   * @param rowIndex
   * @param columnIndex the column relative to the table
   * @return
   */
  private boolean isPersonAvailable(int rowIndex, int columnIndex) {
    assert (columnIndex > 0);
    Verfuegbarkeit v = getVerfuegEntity(rowIndex, columnIndex);
    if (v != null) {
      return v.isVerfuegbar();
    } else {
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
    private Teameinteilung getAssignmentEntity(int rowIndex, int columnIndex) {
      try {
        Personen p = Manager.getPersonCollection().findEntity(personId);
        List<Teameinteilung> emptyAa = Collections.emptyList();
        List<Teameinteilung> aa = (p == null) ? emptyAa : p.getTeameinteilungList();
        Zeit t = Manager.getTimeSlotCollection().findEntity(columnIndex - 1, rowIndex);
        for (Teameinteilung a : aa) {

          if (Objects.equals(t, a.getZeit())) {
            return a;
          }
        }
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
    private String getAssignmentValue(int rowIndex, int columnIndex) {
      assert (columnIndex > 0);
      Teameinteilung a = getAssignmentEntity(rowIndex, columnIndex);
      String result = "";
      if (a != null) {
        result = String.format("<html>%s<br>%s</html>",
                a.getJury().getWertung(),
                a.getFunktionen().getFunktionname());
      }
      return result;

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
        return getAssignmentValue(rowIndex, columnIndex);
      }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      if (columnIndex > 0) {
//        Verfuegbarkeit v = getAssignmentEntity(rowIndex, columnIndex);
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
        Personen.removePropertyChangeListener(this, personId);
      }
    }

    /**
     * Subscribe to listen on changes on the person.
     */
    public void startListening() {
      if (personId != null) {
        Personen.addPropertyChangeListener(this, personId);
      }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (Personen.PROP_VERFUEGBARKEIT.equals(evt.getPropertyName())) {
        fireTableDataChanged();
      }
    }

    public void toggleRow(int row) {
      if (personId != null) {
//        boolean newVal = !rowMeanValue(row);
//        for (int col = 1; col <= columnCount; col++) {
//          setValueAt(newVal, row, col);
//        }
      }
    }

    public void toggleTable() {
      if (personId != null) {
//        boolean newVal = !tableMeanValue();
//        for (int row = 0; row < rowCount; row++) {
//          for (int col = 1; col <= columnCount; col++) {
//            setValueAt(newVal, row, col);
//          }
//        }
      }
    }

    public void toggleColumn(int col) {
      if (personId != null) {
//        boolean newVal = !columnMeanValue(col);
//        for (int row = 0; row < rowCount; row++) {
//          setValueAt(newVal, row, col);
//        }
      }
    }
  }
}
