package kernbeisser.CustomComponents;

import kernbeisser.Tools;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class ObjectTable <T> extends JTable {
    private ArrayList<ObjectSelectionListener<T>> selectionListeners = new ArrayList<>();
    private ArrayList<T> objects = new ArrayList<>();
    private ArrayList<Column<T>> columns = new ArrayList<>();
    public ObjectTable(Collection<Column<T>> columns){
        this.columns.addAll(columns);
    }
    public ObjectTable(Column<T> ... columns){
        this.columns.addAll(Arrays.asList(columns));
    }
    ObjectTable(Collection<T> fill, Collection<Column<T>> columns){
        this.columns.addAll(columns);
        if(fill!=null)
            objects.addAll(fill);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(getSelectedRow()==-1)return;
                T selected = objects.get(getSelectedRow());
                for (ObjectSelectionListener<T> listener : selectionListeners) {
                    listener.selected(selected);
                }
            }
        });
        repaintUI();
    }
    public void addColumn(Column<T> column){
        columns.add(column);
    }
    public int indexOf(T t){
        return objects.lastIndexOf(t);
    }
    public boolean contains(T t){
        return objects.contains(t);
    }
    public T get(T t){
        for (T object : objects) {
            if(object.hashCode()==t.hashCode())return object;
        }
        return null;
    }
    public void addSelectionListener(ObjectSelectionListener<T> listener){
        selectionListeners.add(listener);
    }
    public void addAll(Collection<T> in){
        objects.addAll(in);
        repaintUI();
    }
    public void add(T in){
        objects.add(in);
        repaintUI();
    }
    public void remove(T t){
        objects.remove(t);
    }
    public void remove(int id){
        objects.remove(id);
    }
    public void repaintUI(){
        Object[][] values = new Object[objects.size()][columns.size()];
        for (int c = 0; c < columns.size(); c++) {
            for (int i = 0; i < objects.size(); i++) {
                values[i][c] = columns.get(c).getValue(objects.get(i));
            }
        }
        String[] names = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            names[i]=columns.get(i).getName();
        }
        setModel(new DefaultTableModel(values,names){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}
