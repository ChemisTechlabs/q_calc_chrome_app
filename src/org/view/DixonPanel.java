/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.control.DixonControl;
import org.model.Dixon;
import org.model.DixonException;

/**
 *
 * @author guilherme
 */
public class DixonPanel extends javax.swing.JPanel {

    private GridBagConstraints gbc;
    private BoxLayout boxLayoutPanelValues;
    private FlowLayout flowLayoutBottomPanel;
    //default dimensions of components
    private Dimension dimensionField = new Dimension(200, 30);
    private Dimension dimensionDeleteButton = new Dimension(30, 30);
    private Dimension maxDimensionRowPanel = new Dimension(500, 36);
    private static ArrayList<JTextField> fields = new ArrayList<>();
    private JButton buttonCalc, buttonClear, buttonAdd;
    private JPanel panelBottom, panelValues, panelResults;
    private Insets insetsButtons = new Insets(0, 5, 0, 5);
    
    //listeners
    private ActionListener genericActionListener;
    private FocusListener genericFocusListener;
    /**
     * Creates new form DixonPanel
     */
    public DixonPanel() {
        initListeners();
        initComponents();
        
    }

    private void createField() {
        JTextField field = new JTextField("0.00");
        field.setPreferredSize(dimensionField);
        fields.add(field);

        JButton buttonDelete = new JButton("-");
        buttonDelete.setName("buttonDelete");
        buttonDelete.setPreferredSize(dimensionDeleteButton);
        buttonDelete.addActionListener(genericActionListener);

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowPanel.setMaximumSize(maxDimensionRowPanel);
        rowPanel.setBackground(Color.CYAN);
        rowPanel.setName("rowPanel");

        rowPanel.add(fields.get(fields.size() - 1));
        rowPanel.add(buttonDelete);
        panelValues.add(rowPanel);
    }

    private void removeRow(Component rowPanel) {
        int rowIndex = 0;
        Container parentContainer = rowPanel.getParent();

        for (Component c : parentContainer.getComponents()) {
            if (c.getName().equals("rowPanel")) {
                if (c.equals(rowPanel)) {
                    fields.remove(rowIndex);
                    break;
                }
                rowIndex++;
            }
        }
        parentContainer.remove(rowPanel);
        parentContainer.validate();
        parentContainer.repaint();
    }

    private ArrayList<Double> getValues() throws NumberFormatException {
        ArrayList<Double> values = new ArrayList<>();
        for (JTextField field : fields) {
            values.add(Double.parseDouble(field.getText()));
        }
        return values;
    }

    //temporary method
    private void showResults() {
        Dixon dixon95 = new Dixon(getValues());
        Dixon dixon99 = new Dixon(getValues());

        String text95;
        try {
            Double[] result95 = DixonControl.getInstance().calc(dixon95, 95);
            text95 = String.format("Test 95%: Approved!\nLower end: %d\nUpper end: %d\nRemoved values:", result95[0], result95[1]);

            for (Double val : dixon95.getRemoved()) {
                text95 = text95.concat("\n" + val);
            }
        } catch (DixonException e) {
            System.out.println(e.getMessage());
            text95 = "Test 95%: reproved!";
        }

        String text99;
        try {
            Double[] result99 = DixonControl.getInstance().calc(dixon99, 99);
            text99 = String.format("Test 99%: Approved!\nLower end: %d\nUpper end: %d\nRemoved values:", result99[0], result99[1]);

            for (Double val : result99) {
                text99 = text99.concat("\n" + val);
            }
        } catch (DixonException e) {
            text99 = "Test 99%: reproved!";
        }

        JOptionPane.showMessageDialog(null, text95 + "\n" + text99);
    }

    private void buttonDeleteActionListener(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (fields.size() > 3) {
            removeRow(button.getParent());
        } else {
            System.out.println("Minimo 3 linhas");
        }
    }

    private void buttonClearActionListener(ActionEvent e) {
        for (JTextField field : fields) {
            field.setText("0.00");
        }
    }

    private void buttonCalcActionListener(ActionEvent e) {
        showResults();
    }

    private void buttonAddFieldActionListener(ActionEvent e){
        createField();
    }
    
    private void initComponents() {
        //init layouts
        flowLayoutBottomPanel = new FlowLayout(FlowLayout.LEFT, 5, 5);
        gbc = new GridBagConstraints();

        //init panels
        panelBottom = new JPanel(flowLayoutBottomPanel);
        panelBottom.setMinimumSize(new Dimension(400, 40));
        panelBottom.setPreferredSize(new Dimension(400, 40));
        //panelBottom.setBackground(Color.red);

        panelValues = new JPanel();
        boxLayoutPanelValues = new BoxLayout(panelValues, BoxLayout.Y_AXIS);
        panelValues.setLayout(boxLayoutPanelValues);
        panelValues.setPreferredSize(new Dimension(300, 400));
        panelValues.setBackground(Color.GREEN);

        panelResults = new JPanel();
        panelResults.setPreferredSize(new Dimension(300, 400));
        panelResults.setBackground(Color.BLUE);

        //init panel
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(400, 400));

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 0.5;
        add(panelValues, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 0.5;
        add(panelResults, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridheight = 40;
        gbc.gridwidth = 2;
        add(panelBottom, gbc);

        //init panel bottom
        buttonCalc = new JButton("Calc");
        buttonCalc.setName("buttonCalc");
        buttonCalc.setMargin(insetsButtons);
        buttonCalc.addActionListener(genericActionListener);

        buttonClear = new JButton("Clear fields");
        buttonClear.setName("buttonClear");
        buttonClear.setMargin(insetsButtons);
        buttonClear.addActionListener(genericActionListener);

        buttonAdd = new JButton("Add field");
        buttonAdd.setName("buttonAdd");
        buttonAdd.setMargin(insetsButtons);
        buttonAdd.addActionListener(genericActionListener);

        panelBottom.add(buttonCalc);
        panelBottom.add(buttonClear);

        //init components panelValues
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowPanel.setMaximumSize(maxDimensionRowPanel);
        rowPanel.setBackground(Color.CYAN);
        rowPanel.setName("rowPanelFunctions");

        rowPanel.add(buttonAdd);
        panelValues.add(rowPanel);

        //create minimum number of fields 
        createField();
        createField();
        createField();

        //init components panelResults
        //panelResults.set
    }

    private void initListeners() {
        genericActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component component = (Component) e.getSource();

                if (component.getName().equals("buttonDelete")) {
                    buttonDeleteActionListener(e);
                } else if (component.getName().equals("buttonClear")) {
                    buttonClearActionListener(e);
                } else if (component.getName().equals("buttonCalc")) {
                    buttonCalcActionListener(e);
                } else if (component.getName().equals("buttonAdd")){
                    buttonAddFieldActionListener(e);
                }
            }
        };
    }
}