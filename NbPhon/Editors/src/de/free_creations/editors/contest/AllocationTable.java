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

import de.free_creations.dbEntities.Funktionen;
import de.free_creations.dbEntities.Contest;
import de.free_creations.dbEntities.Teameinteilung;
import de.free_creations.dbEntities.Zeit;
import de.free_creations.nbPhon4Netbeans.PersonNode;
import de.free_creations.nbPhonAPI.DataBaseNotReadyException;
import de.free_creations.nbPhonAPI.JobCollection;
import de.free_creations.nbPhonAPI.Manager;
import de.free_creations.nbPhonAPI.TimeSlotCollection;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.beans.BeanInfo;
import javax.swing.ImageIcon;

/**
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class AllocationTable extends JTable {

  private static final Color disabledColor = new Color(170, 170, 170);
  private static final Color headerColorLight = new Color(226, 230, 233);
  private static final Color headerColorDark = new Color(199, 207, 214);
  //private final AllocationPersonsComboBox comboBox;

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
      Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);
      if (editor instanceof AllocationPersonsComboBox) {
        AllocationPersonsComboBox cb = (AllocationPersonsComboBox) editor;
        if (value instanceof Teameinteilung) {
          Teameinteilung t = (Teameinteilung) value;
          cb.setSelectedPerson(t.getPersonid());
        } else {
          cb.setSelectedPerson(null);
        }
      }
      return editor;

    }
  };

  //private final AllocationTableCellEditor tableCellEditor;
  protected enum RowType {

    dayOfTimeRow,
    functionRow
  }

  public AllocationTable() {
    super();
    if (java.beans.Beans.isDesignTime()) {
      initDesignTimeDisplay();
    } else {
      AllocationTableModel allocationTableModel = new AllocationTableModel(null);
      setModel(allocationTableModel);
      AllocationPersonsComboBox comboBox = new AllocationPersonsComboBox();
      AllocationTableCellEditor tableCellEditor = new AllocationTableCellEditor(comboBox);
      setDefaultEditor(Object.class, tableCellEditor);
    }
    rowHeight = 16 + 6;
    gridColor = Color.lightGray;
    showHorizontalLines = true;
    showVerticalLines = true;
    getColumnModel().getColumn(0).setMaxWidth(300);// makes the first colomn smaller (concrete value seems to be ignored)
    getColumnModel().getColumn(1).setMaxWidth(300);
    setCellSelectionEnabled(true);
  }

  void setJuryId(Integer key) {
    TableModel oldModel = getModel();
    if (oldModel instanceof AllocationTableModel) {
      AllocationTableModel oldAllocModel = (AllocationTableModel) oldModel;
      Integer oldKey = oldAllocModel.getJuryId();
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
  @Override
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

    if (java.beans.Beans.isDesignTime()) {
      return super.prepareRenderer(renderer, row, column);
    } else {
      JLabel preparedRenderer = new JLabel();
      preparedRenderer.setOpaque(true);
      preparedRenderer.setBackground(Color.WHITE); // the default

      AllocationTableModel model = (AllocationTableModel) getModel();
      Object valueAt = model.getValueAt(row, column);
      String defaultText = (valueAt == null) ? "" : valueAt.toString();
      preparedRenderer.setText(defaultText);
      if (column < 2) {
        if (model.getRowType(row) == RowType.dayOfTimeRow) {
          preparedRenderer.setBackground(headerColorDark);
        } else {
          preparedRenderer.setBackground(headerColorLight);
        }
      } else {

        if (model.getRowType(row) == RowType.dayOfTimeRow) {
          preparedRenderer.setBackground(headerColorDark);
        } else {
          Teameinteilung alloc = model.getAllocationValue(row, column);
          PersonNode node =
                  (alloc == null)
                  ? null
                  : model.getNodeFor(alloc.getPersonid().getPersonid());
          if (node != null) {
            preparedRenderer.setText(node.getDisplayName());
            Image image = node.getIcon(BeanInfo.ICON_COLOR_16x16);
            ImageIcon icon = new ImageIcon(image);
            preparedRenderer.setIcon(icon);
          } else {
            preparedRenderer.setBackground(disabledColor);
          }

          if (column == getSelectedColumn()) {
            if (row == getSelectedRow()) {
              preparedRenderer.setBackground(getSelectionBackground());
              preparedRenderer.setForeground(getSelectionForeground());
            }
          }
        }
      }
      return preparedRenderer;
    }

  }

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
    private final Integer juryId;
    private final String[] functionNames;
    private final int functionCount;

    AllocationTableModel(Integer juryId) {
      this.juryId = juryId;
      TimeSlotCollection tt = Manager.getTimeSlotCollection();
      dayNames = tt.dayNames();
      dayCount = dayNames.length;
      timeOfDayNames = tt.timeOfDayNames();
      timeOfDayCount = timeOfDayNames.length;
      JobCollection ff = Manager.getJobCollection();
      functionNames = ff.jobNames();
      functionCount = functionNames.length;

    }

    public Integer getJuryId() {
      return juryId;
    }

    @Override
    public int getRowCount() {
      return timeOfDayCount * (functionCount + 1);
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
          if (getRowType(rowIndex) == RowType.functionRow) {
            return getFunctionHeader(rowIndex);
          } else {
            return "";
          }
        default:
          if (getRowType(rowIndex) == RowType.functionRow) {
            Teameinteilung allocationValue = getAllocationValue(rowIndex, columnIndex);
            if (allocationValue == null) {
              return "none";
            } else {
              return allocationValue;
            }
          } else {
            return "";
          }
      }
    }

    public RowType getRowType(int rowIndex) {
      if ((rowIndex % (functionCount + 1)) == 0) {
        return RowType.dayOfTimeRow;
      } else {
        return RowType.functionRow;
      }
    }

    private Object getDayOfTimeHeader(int rowIndex) {
      int tdi = timeOfDayIndex(rowIndex);
      return timeOfDayNames[tdi];
    }

    private Object getFunctionHeader(int rowIndex) {
      int fi = functionIndex(rowIndex);
      return functionNames[fi];
    }

    @Override
    public String getColumnName(int column) {
      if (column < 2) {
        return "";
      } else {
        return dayNames[column - 2];
      }
    }

    private Teameinteilung getAllocationValue(int rowIndex, int columnIndex) {
      if (juryId == null) {
        return null;
      }
      Contest j;
      Funktionen f;
      Zeit t;
      try {
        j = Manager.getContestCollection().findEntity(juryId);
        f = Manager.getJobCollection().findEntity(functionIndex(rowIndex));
        t = Manager.getTimeSlotCollection().findEntity(
                dayIndex(columnIndex), timeOfDayIndex(rowIndex));
      } catch (DataBaseNotReadyException ignored) {
        return null;
      }
      List<Teameinteilung> aa = j.getTeameinteilungList();//the allocations for this jury

      for (Teameinteilung a : aa) {
        if (Objects.equals(a.getZeit(), t)) {
          if (Objects.equals(a.getFunktionen(), f)) {
            return a;
          }
        }
      }
      return null;
    }

    private int timeOfDayIndex(int rowIndex) {
      return (rowIndex / (functionCount + 1));
    }

    private int functionIndex(int rowIndex) {
      return (rowIndex % (functionCount + 1)) - 1;
    }

    private int dayIndex(int columnIndex) {
      assert (columnIndex > 1);
      return columnIndex - 2;
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
    private final HashMap<Integer, PersonNode> personNodeCache = new HashMap<>();

    private PersonNode getNodeFor(Integer personId) {
      if (!personNodeCache.containsKey(personId)) {
        PersonNode newNode = new PersonNode(personId, Manager.getPersonCollection());
        personNodeCache.put(personId, newNode);
      }
      return personNodeCache.get(personId);
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
