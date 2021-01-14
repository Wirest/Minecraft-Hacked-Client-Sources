/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.lwjgl.input.Keyboard
 */
package cn.kody.debug.ui.guilogin;

import cn.kody.debug.Client;
import cn.kody.debug.ui.altlogin.PasswordField;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.input.Keyboard;

public final class GuiLogin
extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private GuiTextField username;
    private GuiButton exit;
    private GuiButton loginButton;
    public static String usernames;
    public static String passwords;

    public GuiLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                usernames = this.username.getText();
                passwords = DigestUtils.md5Hex((String)this.password.getText());
//                if (!Client.sendGet("https://hanabi.alphaantileak.cn:1337/debug/StaffMenu/checkhwid.php?user=" + usernames + "&pass=" + passwords + "&hwid=" + Client.sigma(), null).equals(Client.encode(Client.sigma()))) {
                    try {
                        Client.sendPost("https://hanabi.alphaantileak.cn:1337/debug/StaffMenu/addhwid.php?user=" + this.username.getText() + "&pass=" + DigestUtils.md5Hex((String)this.password.getText()) + "&hwid=" + Client.sigma(), null);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
//                }
//                if (Client.sendGet("https://hanabi.alphaantileak.cn:1337/debug/StaffMenu/checkhwid.php?user=" + usernames + "&pass=" + passwords + "&hwid=" + Client.sigma(), null).equals(Client.encode(Client.sigma()))) break;
                new Notice("\u60a8\u597d!\u9519\u4e86\u54e6!");
                break;
            }
            case 1: {
                Runtime.getRuntime().exit(0);
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        FontRenderer font = this.mc.fontRendererObj;
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(font, "\u4f60\u9700\u8981\u767b\u5f55", this.width / 2, 20, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(font, "\u7528\u6237\u540d", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(font, "\u5bc6\u7801", this.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        FontRenderer font = this.mc.fontRendererObj;
        int var3 = this.height / 4 + 24;
        this.loginButton = new GuiButton(0, this.width / 2 - 100, 140, 80, 20, "\u767b\u5f55");
        this.exit = new GuiButton(1, this.width / 2 - 100 + 120, 140, 80, 20, "\u9000\u51fa");
        this.buttonList.add(this.loginButton);
        this.buttonList.add(this.exit);
        this.username = new GuiTextField(var3, font, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(font, this.width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents((boolean)true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException var4) {
            var4.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        if (character == '\u001b') {
            Runtime.getRuntime().exit(0);
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    public class Notice
    extends JFrame {
        public Notice(String notice) {
            JLabel label = new JLabel(notice);
            Button bt = new Button("\u786e\u5b9a");
            
//            bt.getActionListeners(new ActionEvent(source, id, label)){
//            	
//            }
            
//            bt.addActionListener(new ActionEvent(GuiLogin.this, notice){
//                GuiLogin val$this$0;
//                String val$notice;
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    if (this.val$notice.contains("\u6210\u529f\uff01")) {
//                        System.exit(0);
//                    } else {
//                        Notice.this.setVisible(false);
//                    }
//                }
//            });
            this.add(label);
            this.add(bt);
            this.setLayout(new FlowLayout());
            this.setTitle("\u63d0\u793a");
            this.setSize(300, 80);
            this.setLocationRelativeTo(null);
            this.setAlwaysOnTop(true);
            this.setResizable(false);
            this.setVisible(true);
        }

    }

}

