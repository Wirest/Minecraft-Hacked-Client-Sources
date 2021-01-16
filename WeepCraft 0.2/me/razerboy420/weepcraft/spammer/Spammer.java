/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.spammer;

import darkmagician6.EventManager;
import darkmagician6.EventTarget;
import darkmagician6.events.EventTick;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.WebUtils;
import me.razerboy420.weepcraft.util.Wrapper;

public class Spammer
extends JFrame {
    public JButton sendbutton;
    public JSlider timer;
    public JTextPane message;
    Timer timer1 = new Timer();
    int count = 0;

    public Spammer() {
        this.init();
    }

    public void init() {
        this.setLocationRelativeTo(null);
        URL link = null;
        try {
            link = new URL("http://weepcraft.xyz/clientstuff/spammertext.html");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
        this.setTitle("WeepCraft Spammer");
        this.setSize(new Dimension(400, 500));
        this.getContentPane().setLayout(new FlowLayout());
        this.sendbutton = new JButton();
        this.sendbutton.setFont(new Font("Lucida Sans Typewriter", 0, 11));
        this.sendbutton.setText("Send");
        this.sendbutton.setAlignmentX(2.0f);
        this.sendbutton.setAlignmentY(375.0f);
        this.sendbutton.setPreferredSize(new Dimension(360, 30));
        this.setDefaultCloseOperation(2);
        this.message = new JTextPane();
        this.message.setFont(new Font("Lucida Sans Typewriter", 0, 11));
        if (link == null) {
            this.message.setText("WeepCraft!");
        } else {
            this.message.setText(WebUtils.read(link));
        }
        this.message.setAlignmentX(2.0f);
        this.message.setAlignmentY(62.0f);
        this.message.setPreferredSize(new Dimension(360, 380));
        this.message.setEditable(true);
        this.timer = new JSlider();
        this.timer.setMinimum(0);
        this.timer.setMaximum(50);
        this.timer.setAlignmentX(2.0f);
        this.timer.setAlignmentY(42.0f);
        this.timer.setPreferredSize(new Dimension(360, 20));
        this.timer.setValue(0);
        this.getContentPane().add(this.timer);
        this.getContentPane().add(this.message);
        this.getContentPane().add(this.sendbutton);
        this.sendbutton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Spammer.this.sendbuttonspam(evt);
            }
        });
        this.timer.addPropertyChangeListener(new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                Spammer.this.sendbutton.setText("Send " + Spammer.this.timer.getValue() + "s");
            }
        });
    }

    public void sendbuttonspam(ActionEvent evt) {
        this.count = 0;
        EventManager.register(this);
    }

    @EventTarget
    public void onTick(EventTick event) {
        String[] args = this.message.getText().split("\n");
        if (this.count > args.length) {
            EventManager.unregister(this);
            return;
        }
        String sending = args[this.count];
        if (sending.startsWith("@")) {
            sending = sending.replaceFirst("@", "");
        }
        this.timer1.update();
        if (this.timer1.hasPassed(this.timer.getValue() * 1000)) {
            Wrapper.getPlayer().sendChatMessage(sending);
            ++this.count;
            this.timer1.reset();
        }
    }

    public static void main() {
        try {
            UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
            int length = installedLookAndFeels.length;
            int i = 0;
            if (i < length) {
                UIManager.LookAndFeelInfo info = installedLookAndFeels[3];
                UIManager.setLookAndFeel(info.getClassName());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                new Spammer().setVisible(true);
            }
        });
    }

}

