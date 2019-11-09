/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernbeisser.Windows.ItemFilter;

import kernbeisser.*;
import kernbeisser.Windows.Finisher;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author julik
 */
public abstract class ItemFilter extends JFrame implements Finishable {
    private static int lastSelectionSupplier = -1;
    private static PriceListTree priceListTree = new PriceListTree();
    /**
     * Creates new form ItemFilter
     * which is a Window to Select PriceList & Supplier for the Item
     */
    public abstract void filterSelected(PriceList p,Supplier s);
    public ItemFilter() {
        EntityManager em = DBConnection.getEntityManager();
        addWindowListener(new Finisher(this));
        initComponents();
        priceListPane.setLayout(new BorderLayout());
        priceListPane.add(new JScrollPane(priceListTree));
        priceListTree.addTreeSelectionListener(e -> filterSelect());
        DefaultListModel<Supplier> suppliersModel = new DefaultListModel<>();
        em.createQuery("select s from Supplier s",Supplier.class).getResultStream().forEach(suppliersModel::addElement);
        em.close();
        suppliers.setModel(suppliersModel);
        if(lastSelectionSupplier!=-1){
            suppliers.setSelectedIndex(lastSelectionSupplier);
        }
        setVisible(true);
    }
    private void filterSelect(){
        EntityManager em = DBConnection.getEntityManager();
        Object o = priceListTree.getLastSelectedPathComponent();
        try {
            filterSelected(em.createQuery(
                    "select p from PriceList p where name like '" + o.toString() + "'", PriceList.class).getSingleResult(),
                    suppliers.getSelectedValue()
            );
        }catch (NoResultException ignored){}
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        suppliers = new JList<>();
        priceListPane = new JPanel();
        finish = new JButton();
        jLabel2 = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Bitte w�hlen sie einen Filter f�r die Artikel suche");

        jLabel1.setText("Lieferant");

        jScrollPane1.setViewportView(suppliers);

        GroupLayout priceListPaneLayout = new GroupLayout(priceListPane);
        priceListPane.setLayout(priceListPaneLayout);
        priceListPaneLayout.setHorizontalGroup(
            priceListPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );
        priceListPaneLayout.setVerticalGroup(
            priceListPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        finish.setText("Fertig");
        finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishActionPerformed(evt);
            }
        });

        jLabel2.setText("Preisliste");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(priceListPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(finish)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(priceListPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(finish)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishActionPerformed
        filterSelect();
        finish();

    }//GEN-LAST:event_finishActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton finish;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JPanel priceListPane;
    private JList<Supplier> suppliers;
    // End of variables declaration//GEN-END:variables
}