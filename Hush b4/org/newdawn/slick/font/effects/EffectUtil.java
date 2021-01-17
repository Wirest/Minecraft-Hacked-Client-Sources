// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPanel;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import javax.swing.JDialog;
import java.awt.EventQueue;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SpinnerModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import javax.swing.JColorChooser;
import java.awt.Color;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EffectUtil
{
    private static BufferedImage scratchImage;
    
    static {
        EffectUtil.scratchImage = new BufferedImage(256, 256, 2);
    }
    
    public static BufferedImage getScratchImage() {
        final Graphics2D g = (Graphics2D)EffectUtil.scratchImage.getGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, 256, 256);
        g.setComposite(AlphaComposite.SrcOver);
        g.setColor(Color.white);
        return EffectUtil.scratchImage;
    }
    
    public static ConfigurableEffect.Value colorValue(final String name, final Color currentValue) {
        return new DefaultValue(name, toString(currentValue)) {
            @Override
            public void showDialog() {
                final Color newColor = JColorChooser.showDialog(null, "Choose a color", EffectUtil.fromString(this.value));
                if (newColor != null) {
                    this.value = EffectUtil.toString(newColor);
                }
            }
            
            @Override
            public Object getObject() {
                return EffectUtil.fromString(this.value);
            }
        };
    }
    
    public static ConfigurableEffect.Value intValue(final String name, final int currentValue, final String description) {
        return new DefaultValue(name, String.valueOf(currentValue)) {
            @Override
            public void showDialog() {
                final JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentValue, -32768, 32767, 1));
                if (this.showValueDialog(spinner, description)) {
                    this.value = String.valueOf(spinner.getValue());
                }
            }
            
            @Override
            public Object getObject() {
                return Integer.valueOf(this.value);
            }
        };
    }
    
    public static ConfigurableEffect.Value floatValue(final String name, final float currentValue, final float min, final float max, final String description) {
        return new DefaultValue(name, String.valueOf(currentValue)) {
            @Override
            public void showDialog() {
                final JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentValue, min, max, 0.10000000149011612));
                if (this.showValueDialog(spinner, description)) {
                    this.value = String.valueOf(((Double)spinner.getValue()).floatValue());
                }
            }
            
            @Override
            public Object getObject() {
                return Float.valueOf(this.value);
            }
        };
    }
    
    public static ConfigurableEffect.Value booleanValue(final String name, final boolean currentValue, final String description) {
        return new DefaultValue(name, String.valueOf(currentValue)) {
            @Override
            public void showDialog() {
                final JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(currentValue);
                if (this.showValueDialog(checkBox, description)) {
                    this.value = String.valueOf(checkBox.isSelected());
                }
            }
            
            @Override
            public Object getObject() {
                return Boolean.valueOf(this.value);
            }
        };
    }
    
    public static ConfigurableEffect.Value optionValue(final String name, final String currentValue, final String[][] options, final String description) {
        return new DefaultValue(name, currentValue.toString()) {
            @Override
            public void showDialog() {
                int selectedIndex = -1;
                final DefaultComboBoxModel model = new DefaultComboBoxModel();
                for (int i = 0; i < options.length; ++i) {
                    model.addElement(options[i][0]);
                    if (this.getValue(i).equals(currentValue)) {
                        selectedIndex = i;
                    }
                }
                final JComboBox comboBox = new JComboBox(model);
                comboBox.setSelectedIndex(selectedIndex);
                if (this.showValueDialog(comboBox, description)) {
                    this.value = this.getValue(comboBox.getSelectedIndex());
                }
            }
            
            private String getValue(final int i) {
                if (options[i].length == 1) {
                    return options[i][0];
                }
                return options[i][1];
            }
            
            @Override
            public String toString() {
                for (int i = 0; i < options.length; ++i) {
                    if (this.getValue(i).equals(this.value)) {
                        return options[i][0].toString();
                    }
                }
                return "";
            }
            
            @Override
            public Object getObject() {
                return this.value;
            }
        };
    }
    
    public static String toString(final Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null.");
        }
        String r = Integer.toHexString(color.getRed());
        if (r.length() == 1) {
            r = "0" + r;
        }
        String g = Integer.toHexString(color.getGreen());
        if (g.length() == 1) {
            g = "0" + g;
        }
        String b = Integer.toHexString(color.getBlue());
        if (b.length() == 1) {
            b = "0" + b;
        }
        return String.valueOf(r) + g + b;
    }
    
    public static Color fromString(final String rgb) {
        if (rgb == null || rgb.length() != 6) {
            return Color.white;
        }
        return new Color(Integer.parseInt(rgb.substring(0, 2), 16), Integer.parseInt(rgb.substring(2, 4), 16), Integer.parseInt(rgb.substring(4, 6), 16));
    }
    
    private abstract static class DefaultValue implements ConfigurableEffect.Value
    {
        String value;
        String name;
        
        public DefaultValue(final String name, final String value) {
            this.value = value;
            this.name = name;
        }
        
        @Override
        public void setString(final String value) {
            this.value = value;
        }
        
        @Override
        public String getString() {
            return this.value;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            if (this.value == null) {
                return "";
            }
            return this.value.toString();
        }
        
        public boolean showValueDialog(final JComponent component, final String description) {
            final ValueDialog dialog = new ValueDialog(component, this.name, description);
            dialog.setTitle(this.name);
            dialog.setLocationRelativeTo(null);
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JComponent focusComponent = component;
                    if (focusComponent instanceof JSpinner) {
                        focusComponent = ((JSpinner.DefaultEditor)((JSpinner)component).getEditor()).getTextField();
                    }
                    focusComponent.requestFocusInWindow();
                }
            });
            dialog.setVisible(true);
            return dialog.okPressed;
        }
    }
    
    private static class ValueDialog extends JDialog
    {
        public boolean okPressed;
        
        public ValueDialog(final JComponent component, final String name, final String description) {
            this.okPressed = false;
            this.setDefaultCloseOperation(2);
            this.setLayout(new GridBagLayout());
            this.setModal(true);
            if (component instanceof JSpinner) {
                ((JSpinner.DefaultEditor)((JSpinner)component).getEditor()).getTextField().setColumns(4);
            }
            final JPanel descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new GridBagLayout());
            this.getContentPane().add(descriptionPanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            descriptionPanel.setBackground(Color.white);
            descriptionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
            final JTextArea descriptionText = new JTextArea(description);
            descriptionPanel.add(descriptionText, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
            descriptionText.setWrapStyleWord(true);
            descriptionText.setLineWrap(true);
            descriptionText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            descriptionText.setEditable(false);
            final JPanel panel = new JPanel();
            this.getContentPane().add(panel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 0, new Insets(5, 5, 0, 5), 0, 0));
            panel.add(new JLabel(String.valueOf(name) + ":"));
            panel.add(component);
            final JPanel buttonPanel = new JPanel();
            this.getContentPane().add(buttonPanel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
            final JButton okButton = new JButton("OK");
            buttonPanel.add(okButton);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent evt) {
                    ValueDialog.this.okPressed = true;
                    ValueDialog.this.setVisible(false);
                }
            });
            final JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(cancelButton);
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent evt) {
                    ValueDialog.this.setVisible(false);
                }
            });
            this.setSize(new Dimension(320, 175));
        }
    }
}
