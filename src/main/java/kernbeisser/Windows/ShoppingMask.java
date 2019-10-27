/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kernbeisser.Windows;

import kernbeisser.*;
import kernbeisser.CustomComponents.Column;
import kernbeisser.CustomComponents.DBTable;
import kernbeisser.CustomComponents.ObjectTable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author julik
 */
public class ShoppingMask extends javax.swing.JPanel {
    private EntityManager em = DBConnection.getEntityManager();
    private EntityTransaction et = em.getTransaction();
    private Purchase purchase;
    private int temporaryValue;
    private SaleSession saleSession;
    private ObjectTable<ShoppingItem> shoppingCartTable;
    private DBTable withoutBarcodeTable;
    /**
     * Creates new form ShoppingMask
     */
    public ShoppingMask(SaleSession saleSession) {
        initComponents();
        setFilters();
        this.saleSession=saleSession;
        temporaryValue =saleSession.getCustomer().getUserGroup().getValue();
        refreshUser(saleSession.getCustomer());
        itemNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Item i = searchItem();
                if(i!=null){
                    selectedItem.setText(i.getName());
                    unit.setText(new Translator().translate(i.getUnit()));
                    price.setText(i.calculatePrice()/100f+"\u20AC");
                }else {
                    selectedItem.setText("Kein Ergebniss");
                    price.setText("0.00\u20AC");
                    unit.setText("");
                }
            }
        });
        withoutBarcodeTable = new DBTable<>("select i from Item i where barcode is not null",
                Column.create("Name",Item::getName),
                Column.create("Artikel-Nummmer",Item::getKbNumber),
                Column.create("Preis",Item::calculatePrice)
        );
        itemsWithoutBarcodePanel.add(new JScrollPane(withoutBarcodeTable));
        withoutBarcodeTable.addSelectionListener((e)->{
            itemNumber.setText(((Item)e).getKbNumber()+"");
            itemAmount.requestFocus();
        });
        withoutBarcodeTable.repaintUI();
        shoppingCartTable = new ObjectTable<>(
                Column.create("Name", ShoppingItem::getName),
                Column.create("Anzahl", ShoppingItem::getItemAmount),
                Column.create("Preis", e-> e.getRawPrice()/100f+"\u20AC")
        );
        shoppingCartTable.repaintUI();
        shoppingCartPanel.add(new JScrollPane(shoppingCartTable));
    }
    private void refreshUser(User user){
        userName.setText("Benutzer: "+user.getFirstName()+" "+user.getSurname()+" ("+user.getUsername()+")");
        userGroupValue.setText("Guthaben: "+user.getUserGroup().getValue()/100f+"\u20AC");
        userValueNow.setText("Jetziges Guthaben: "+user.getUserGroup().getValue()/100f+"\u20AC");
        userGroupMembers.setText("Benutzer-Gruppe: "+Tools.toSting(user.getUserGroup().getMembers(),e -> user.getFirstName()+","));
    }
    private void addToShoppingCart(ShoppingItem i){
        if(i.getRawPrice() > 2000) {
            if(JOptionPane.showConfirmDialog(
                    this,
                    "Ist der eingegebene Preis von " + i.getRawPrice() / 100f + "\u20AC korrekt?",
                    "Sehr hoher Preis!", JOptionPane.INFORMATION_MESSAGE)!=0)return;
        }
        ShoppingItem inside = shoppingCartTable.get(i);
        if(inside!=null){
            inside.setRawPrice(i.getRawPrice()+inside.getRawPrice());
            inside.setAmount(i.getAmount()+inside.getAmount());
            shoppingCartTable.repaintUI();
        }else {
            shoppingCartTable.add(i);
        }
        temporaryValue-=i.getRawPrice();
        if(temporaryValue<0)
            userValueLater.setForeground(Color.RED);
        else
            userValueLater.setForeground(Color.GREEN);
        userValueLater.setText("Guthaben nach dem Einkauf: "+temporaryValue/100f+"\u20AC");
    }
    private void setFilters(){
        Tools.setDoubleFilter(rawPrice);
        Tools.setDoubleFilter(hiddenItemDeposit);
        Tools.setDoubleFilter(hiddenItemPrice);
        Tools.setRealNumberFilter(itemAmount);
        Tools.setRealNumberFilter(hiddenItemAmount);
    }

    private Item searchItem(){
        if(itemNumber.getText().length()<1)return null;
        EntityManager em = DBConnection.getEntityManager();
        try{
            return em.createQuery("select i from Item i where kbNumber like '"+itemNumber.getText()+"'",Item.class).getSingleResult();
        }catch (NoResultException e) {
            try{
                return em.createQuery("select i from Item i where barcode like '%"+itemNumber.getText()+ "'",Item.class).setMaxResults(1).getSingleResult();
            }catch (NoResultException e1){
                return null;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        discountSelection = new javax.swing.ButtonGroup();
        kindOfSelection = new javax.swing.ButtonGroup();
        depositActionSelection = new javax.swing.ButtonGroup();
        hiddenItemDepositBG = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        userName = new javax.swing.JLabel();
        userGroupValue = new javax.swing.JLabel();
        userGroupMembers = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        deposit = new javax.swing.JRadioButton();
        depositIn = new javax.swing.JRadioButton();
        depositOut = new javax.swing.JRadioButton();
        rawPrice = new javax.swing.JTextField();
        organics = new javax.swing.JRadioButton();
        bakeryProduct = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        itemNumber = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        itemAmount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        unit = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        hiddenItemName = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        hiddenItemPrice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        hiddenItemVATlow = new javax.swing.JRadioButton();
        hiddenItemVAThigh = new javax.swing.JRadioButton();
        hiddenItemDeposit = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        hiddenItemAmount = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        addHiddenItem = new javax.swing.JButton();
        selectedItem = new javax.swing.JLabel();
        price = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        discountNormalPrice = new javax.swing.JRadioButton();
        discountContainerPrice = new javax.swing.JRadioButton();
        discountHalfPrice = new javax.swing.JRadioButton();
        discountCustom = new javax.swing.JRadioButton();
        customDiscount = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        lockCustomDiscount = new javax.swing.JCheckBox();
        shoppingCartPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        deleteSelectedItem = new javax.swing.JToggleButton();
        clearShoppingSession = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        pay = new javax.swing.JButton();
        shoppingSessionSum = new javax.swing.JLabel();
        userValueNow = new javax.swing.JLabel();
        userValueLater = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        itemsWithoutBarcodePanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        editBarcodeField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        itemNameInfo = new javax.swing.JLabel();
        itemNumberInfo = new javax.swing.JLabel();
        itemPriceInfo = new javax.swing.JLabel();
        itemAmountInfo = new javax.swing.JLabel();
        editBarcode = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Nutzer Informationen"));

        userName.setText("Name: name + surname");

        userGroupValue.setText("Guthaben:");

        userGroupMembers.setText("Nutzergruppe mit:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(userGroupMembers)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userName)
                            .addComponent(userGroupValue))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(userName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userGroupValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userGroupMembers))
        );

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Artikel"));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Obst, Gemüse und Pfand"));

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kindOfSelection.add(deposit);
        deposit.setText("Pfand");

        depositActionSelection.add(depositIn);
        depositIn.setSelected(true);
        depositIn.setText("Zurückgeben");

        depositActionSelection.add(depositOut);
        depositOut.setText("Ausleihen");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deposit)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(depositIn)
                        .addGap(45, 45, 45)
                        .addComponent(depositOut)))
                .addGap(0, 44, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(deposit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depositIn)
                    .addComponent(depositOut))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        rawPrice.setText("0.00");
        rawPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRawPrice(evt);
            }
        });

        kindOfSelection.add(organics);
        organics.setSelected(true);
        organics.setText("Obst & Gemüse");

        kindOfSelection.add(bakeryProduct);
        bakeryProduct.setText("Backwaren");

        jLabel11.setText("Preis[€]");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bakeryProduct)
                    .addComponent(rawPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(organics)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rawPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(organics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bakeryProduct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Artikel"));

        itemNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNumberActionPerformed(evt);
            }
        });

        jLabel12.setText("Artikelnummer oder Barcode");

        itemAmount.setText("1");
        itemAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAmountActionPerformed(evt);
            }
        });

        jLabel14.setText("Menge");

        unit.setText("Einheit");

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Nicht ausgezeichnete Artikel"));

        jLabel19.setText("Artikelname");

        hiddenItemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hiddenItemNameActionPerformed(evt);
            }
        });

        jLabel20.setText("Preis[€]");

        hiddenItemPrice.setText("0.00");
        hiddenItemPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hiddenItemPriceActionPerformed(evt);
            }
        });

        jLabel21.setText("MWSt");

        hiddenItemDepositBG.add(hiddenItemVATlow);
        hiddenItemVATlow.setSelected(true);
        hiddenItemVATlow.setText("7%");

        hiddenItemDepositBG.add(hiddenItemVAThigh);
        hiddenItemVAThigh.setText("19%");

        hiddenItemDeposit.setText("0.00");
        hiddenItemDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hiddenItemDepositActionPerformed(evt);
            }
        });

        jLabel22.setText("Pfand[€]");

        hiddenItemAmount.setText("1");
        hiddenItemAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hiddenItemAmountActionPerformed(evt);
            }
        });

        jLabel23.setText("Menge");

        addHiddenItem.setForeground(new java.awt.Color(0, 153, 0));
        addHiddenItem.setText("Dem Warenkorp Hinzufügen");
        addHiddenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHiddenItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(hiddenItemVATlow)
                        .addGap(30, 30, 30)
                        .addComponent(hiddenItemVAThigh))
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(hiddenItemPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                .addComponent(hiddenItemDeposit, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(hiddenItemAmount, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(hiddenItemName))
                            .addComponent(jLabel22))
                        .addGap(18, 18, 18)
                        .addComponent(addHiddenItem)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hiddenItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hiddenItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hiddenItemVATlow)
                    .addComponent(hiddenItemVAThigh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addGap(11, 11, 11)
                .addComponent(hiddenItemDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hiddenItemAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addHiddenItem))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        selectedItem.setText("Ausgewählter Artikel");

        price.setText("Preis");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(itemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectedItem, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel14))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(itemAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(unit, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unit))
                .addGap(4, 4, 4)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectedItem)
                    .addComponent(price))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Einzelposition", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 651, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Gebinde", jPanel3);

        jLabel15.setText("Artikelsuchen");

        jLabel16.setText("Suchen nach:");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Barcode");

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Artikelnummer");

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Artikelname");

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Preisliste");

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Ergebnisse der Suche"));
        jPanel12.setLayout(new java.awt.GridLayout(1, 1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox3)
                                    .addComponent(jCheckBox2))
                                .addGap(52, 52, 52)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox5)
                                    .addComponent(jCheckBox4))))
                        .addGap(0, 44, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox5))
                .addGap(9, 9, 9)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Suchen", jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Rabatte"));

        discountSelection.add(discountNormalPrice);
        discountNormalPrice.setText("Normalpreis");
        discountNormalPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountNormalPriceActionPerformed(evt);
            }
        });

        discountSelection.add(discountContainerPrice);
        discountContainerPrice.setText("Preis für vorbestellte Gebinde");
        discountContainerPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountContainerPriceActionPerformed(evt);
            }
        });

        discountSelection.add(discountHalfPrice);
        discountHalfPrice.setText("Halber Preis");
        discountHalfPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountHalfPriceActionPerformed(evt);
            }
        });

        discountSelection.add(discountCustom);
        discountCustom.setText("Reduziert um");
        discountCustom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountCustomActionPerformed(evt);
            }
        });

        jLabel10.setText("%");

        lockCustomDiscount.setText("Reduktion für Folgeartikel beibehalten");
        lockCustomDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockCustomDiscountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(discountCustom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(discountContainerPrice)
                        .addComponent(discountHalfPrice, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(discountNormalPrice, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lockCustomDiscount)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(discountNormalPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(discountContainerPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(discountHalfPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountCustom)
                    .addComponent(customDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lockCustomDiscount)
                .addContainerGap())
        );

        shoppingCartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Artikel im Warenkorb"));
        shoppingCartPanel.setLayout(new java.awt.GridLayout(1, 1));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Warenkorb bearbeiten"));

        deleteSelectedItem.setText("Markierten Artikel aus dem Warenkob entfernen");
        deleteSelectedItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedItemActionPerformed(evt);
            }
        });

        clearShoppingSession.setText("Einkaufsliste leeren");
        clearShoppingSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearShoppingSessionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clearShoppingSession, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(deleteSelectedItem, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearShoppingSession)
                    .addComponent(deleteSelectedItem))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Einkauf beenden"));

        pay.setForeground(new java.awt.Color(0, 153, 0));
        pay.setText("Bezahlen");
        pay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payActionPerformed(evt);
            }
        });

        shoppingSessionSum.setText("Preis:");

        userValueNow.setText("Jetziges Guthaben:");

        userValueLater.setText("Guthaben nach dem Einkauf:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(userValueLater)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pay))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shoppingSessionSum)
                            .addComponent(userValueNow))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(shoppingSessionSum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userValueNow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pay)
                    .addComponent(userValueLater))
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("Einkauf für name + surname(username)");

        itemsWithoutBarcodePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Artikel ohne Barcode"));
        itemsWithoutBarcodePanel.setLayout(new java.awt.GridLayout(1, 1));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Artikel Barcode bearbeiten"));

        editBarcodeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBarcodeFieldActionPerformed(evt);
            }
        });

        jLabel5.setText("Barcode");

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Artikel Informationen"));

        itemNameInfo.setText("Artikelname: x");

        itemNumberInfo.setText("Artikelnummer: x");

        itemPriceInfo.setText("Artikelpreis: x");

        itemAmountInfo.setText("Artikelmenge: x");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemNameInfo)
                    .addComponent(itemPriceInfo)
                    .addComponent(itemNumberInfo)
                    .addComponent(itemAmountInfo))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemNameInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemNumberInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemPriceInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemAmountInfo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        editBarcode.setForeground(new java.awt.Color(0, 153, 0));
        editBarcode.setText("Bearbeiten");
        editBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBarcodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editBarcodeField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(editBarcode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editBarcodeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editBarcode)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shoppingCartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemsWithoutBarcodePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemsWithoutBarcodePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shoppingCartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(1, 1, 1))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void hiddenItemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hiddenItemNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hiddenItemNameActionPerformed

    private void hiddenItemAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hiddenItemAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hiddenItemAmountActionPerformed

    private void addRawPrice(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRawPrice
        Checker c = new Checker();
        int price = 0;
        try {
            price = c.checkPrice(rawPrice);
        }catch (IncorrectInput e){
            Tools.ping(e.getComponent());
            return;
        }
        if (organics.isSelected()) addToShoppingCart(ShoppingItem.getOrganic(price));
        else if (bakeryProduct.isSelected()) addToShoppingCart(ShoppingItem.getBakeryProduct(price));
        else if (deposit.isSelected())addToShoppingCart(ShoppingItem.getDeposit(depositIn.isSelected()?-price:price));
        rawPrice.setText("0.00");
    }//GEN-LAST:event_addRawPrice


    private void itemNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNumberActionPerformed
        Item search = searchItem();
        if(search==null){
            Tools.ping(itemNumber);
            return;
        }
        ShoppingItem item = new ShoppingItem(search);
        item.setItemAmount(Integer.parseInt(itemAmount.getText()));
        item.setRawPrice(search.calculatePrice()*item.getItemAmount());
        addToShoppingCart(item);
        itemNumber.requestFocus();
        itemAmount.setText("1");
        itemNumber.setText("");
    }//GEN-LAST:event_itemNumberActionPerformed

    private void itemAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAmountActionPerformed
        itemNumberActionPerformed(evt);
    }//GEN-LAST:event_itemAmountActionPerformed

    private void hiddenItemPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hiddenItemPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hiddenItemPriceActionPerformed

    private void hiddenItemDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hiddenItemDepositActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hiddenItemDepositActionPerformed

    private void addHiddenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHiddenItemActionPerformed
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setItemAmount(Integer.parseInt(hiddenItemAmount.getText()));
        shoppingItem.setName(hiddenItemName.getText());
        shoppingItem.setVatLow(hiddenItemVATlow.isSelected());
        Checker c = new Checker();
        try {
            shoppingItem.setRawPrice((int)((c.checkPrice(hiddenItemPrice)+c.checkPrice(hiddenItemDeposit))*(1+(hiddenItemVATlow.isSelected()?VAT.LOW.get()/100f:VAT.HIGH.get()/100f))));
        } catch (IncorrectInput incorrectInput) {
            Tools.ping(hiddenItemPrice);
            Tools.ping(hiddenItemDeposit);
        }
        shoppingItem.setItemAmount(Integer.parseInt(hiddenItemAmount.getText()));
        addToShoppingCart(shoppingItem);
    }//GEN-LAST:event_addHiddenItemActionPerformed

    private void discountNormalPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountNormalPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountNormalPriceActionPerformed

    private void discountContainerPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountContainerPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountContainerPriceActionPerformed

    private void discountHalfPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountHalfPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountHalfPriceActionPerformed

    private void discountCustomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountCustomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountCustomActionPerformed

    private void lockCustomDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockCustomDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lockCustomDiscountActionPerformed

    private void clearShoppingSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearShoppingSessionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearShoppingSessionActionPerformed

    private void deleteSelectedItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteSelectedItemActionPerformed

    private void payActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payActionPerformed

    private void editBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editBarcodeActionPerformed

    private void editBarcodeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBarcodeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editBarcodeFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addHiddenItem;
    private javax.swing.JRadioButton bakeryProduct;
    private javax.swing.JButton clearShoppingSession;
    private javax.swing.JSpinner customDiscount;
    private javax.swing.JToggleButton deleteSelectedItem;
    private javax.swing.JRadioButton deposit;
    private javax.swing.ButtonGroup depositActionSelection;
    private javax.swing.JRadioButton depositIn;
    private javax.swing.JRadioButton depositOut;
    private javax.swing.JRadioButton discountContainerPrice;
    private javax.swing.JRadioButton discountCustom;
    private javax.swing.JRadioButton discountHalfPrice;
    private javax.swing.JRadioButton discountNormalPrice;
    private javax.swing.ButtonGroup discountSelection;
    private javax.swing.JButton editBarcode;
    private javax.swing.JTextField editBarcodeField;
    private javax.swing.JTextField hiddenItemAmount;
    private javax.swing.JTextField hiddenItemDeposit;
    private javax.swing.ButtonGroup hiddenItemDepositBG;
    private javax.swing.JTextField hiddenItemName;
    private javax.swing.JTextField hiddenItemPrice;
    private javax.swing.JRadioButton hiddenItemVAThigh;
    private javax.swing.JRadioButton hiddenItemVATlow;
    private javax.swing.JTextField itemAmount;
    private javax.swing.JLabel itemAmountInfo;
    private javax.swing.JLabel itemNameInfo;
    private javax.swing.JTextField itemNumber;
    private javax.swing.JLabel itemNumberInfo;
    private javax.swing.JLabel itemPriceInfo;
    private javax.swing.JPanel itemsWithoutBarcodePanel;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField6;
    private javax.swing.ButtonGroup kindOfSelection;
    private javax.swing.JCheckBox lockCustomDiscount;
    private javax.swing.JRadioButton organics;
    private javax.swing.JButton pay;
    private javax.swing.JLabel price;
    private javax.swing.JTextField rawPrice;
    private javax.swing.JLabel selectedItem;
    private javax.swing.JPanel shoppingCartPanel;
    private javax.swing.JLabel shoppingSessionSum;
    private javax.swing.JLabel unit;
    private javax.swing.JLabel userGroupMembers;
    private javax.swing.JLabel userGroupValue;
    private javax.swing.JLabel userName;
    private javax.swing.JLabel userValueLater;
    private javax.swing.JLabel userValueNow;
    // End of variables declaration//GEN-END:variables
}
