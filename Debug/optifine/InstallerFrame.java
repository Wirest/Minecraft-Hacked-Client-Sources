package optifine;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class InstallerFrame extends JFrame
{
    private JLabel ivjLabelOfVersion = null;
    private JLabel ivjLabelMcVersion = null;
    private JPanel ivjPanelCenter = null;
    private JButton ivjButtonInstall = null;
    private JButton ivjButtonClose = null;
    private JPanel ivjPanelBottom = null;
    private JPanel ivjPanelContentPane = null;
    InstallerFrame.IvjEventHandler ivjEventHandler = new InstallerFrame.IvjEventHandler();
    private JTextArea ivjTextArea = null;
    private JButton ivjButtonExtract = null;
    private JLabel ivjLabelFolder = null;
    private JTextField ivjFieldFolder = null;
    private JButton ivjButtonFolder = null;

    public InstallerFrame()
    {
        this.initialize();
    }

    private void customInit()
    {
        try
        {
            this.pack();
            this.setDefaultCloseOperation(3);
            File file1 = Utils.getWorkingDirectory();
            this.getFieldFolder().setText(file1.getPath());
            this.getButtonInstall().setEnabled(false);
            this.getButtonExtract().setEnabled(false);
            String s = Installer.getOptiFineVersion();
            Utils.dbg("OptiFine Version: " + s);
            String[] astring = Utils.tokenize(s, "_");
            String s1 = astring[1];
            Utils.dbg("Minecraft Version: " + s1);
            String s2 = Installer.getOptiFineEdition(astring);
            Utils.dbg("OptiFine Edition: " + s2);
            String s3 = s2.replace("_", " ");
            s3 = s3.replace(" U ", " Ultra ");
            s3 = s3.replace("L ", "Light ");
            this.getLabelOfVersion().setText("OptiFine " + s3);
            this.getLabelMcVersion().setText("for Minecraft " + s1);
            this.getButtonInstall().setEnabled(true);
            this.getButtonExtract().setEnabled(true);
            this.getButtonInstall().requestFocus();

            if (!Installer.isPatchFile())
            {
                this.getButtonExtract().setVisible(false);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            InstallerFrame installerframe = new InstallerFrame();
            Utils.centerWindow(installerframe, (Component)null);
            installerframe.show();
        }
        catch (Exception exception)
        {
            String s = exception.getMessage();

            if (s != null && s.equals("QUIET"))
            {
                return;
            }

            exception.printStackTrace();
            String s1 = Utils.getExceptionStackTrace(exception);
            s1 = s1.replace("\t", "  ");
            JTextArea jtextarea = new JTextArea(s1);
            jtextarea.setEditable(false);
            Font font = jtextarea.getFont();
            Font font1 = new Font("Monospaced", font.getStyle(), font.getSize());
            jtextarea.setFont(font1);
            JScrollPane jscrollpane = new JScrollPane(jtextarea);
            jscrollpane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog((Component)null, jscrollpane, "Error", 0);
        }
    }

    private void handleException(Throwable e)
    {
        String s = e.getMessage();

        if (s == null || !s.equals("QUIET"))
        {
            e.printStackTrace();
            String s1 = Utils.getExceptionStackTrace(e);
            s1 = s1.replace("\t", "  ");
            JTextArea jtextarea = new JTextArea(s1);
            jtextarea.setEditable(false);
            Font font = jtextarea.getFont();
            Font font1 = new Font("Monospaced", font.getStyle(), font.getSize());
            jtextarea.setFont(font1);
            JScrollPane jscrollpane = new JScrollPane(jtextarea);
            jscrollpane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog((Component)null, jscrollpane, "Error", 0);
        }
    }

    private JLabel getLabelOfVersion()
    {
        if (this.ivjLabelOfVersion == null)
        {
            try
            {
                this.ivjLabelOfVersion = new JLabel();
                this.ivjLabelOfVersion.setName("LabelOfVersion");
                this.ivjLabelOfVersion.setBounds(2, 5, 385, 42);
                this.ivjLabelOfVersion.setFont(new Font("Dialog", 1, 18));
                this.ivjLabelOfVersion.setHorizontalAlignment(0);
                this.ivjLabelOfVersion.setPreferredSize(new Dimension(385, 42));
                this.ivjLabelOfVersion.setText("OptiFine ...");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjLabelOfVersion;
    }

    private JLabel getLabelMcVersion()
    {
        if (this.ivjLabelMcVersion == null)
        {
            try
            {
                this.ivjLabelMcVersion = new JLabel();
                this.ivjLabelMcVersion.setName("LabelMcVersion");
                this.ivjLabelMcVersion.setBounds(2, 38, 385, 25);
                this.ivjLabelMcVersion.setFont(new Font("Dialog", 1, 14));
                this.ivjLabelMcVersion.setHorizontalAlignment(0);
                this.ivjLabelMcVersion.setPreferredSize(new Dimension(385, 25));
                this.ivjLabelMcVersion.setText("for Minecraft ...");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjLabelMcVersion;
    }

    private JPanel getPanelCenter()
    {
        if (this.ivjPanelCenter == null)
        {
            try
            {
                this.ivjPanelCenter = new JPanel();
                this.ivjPanelCenter.setName("PanelCenter");
                this.ivjPanelCenter.setLayout((LayoutManager)null);
                this.ivjPanelCenter.add(this.getLabelOfVersion(), this.getLabelOfVersion().getName());
                this.ivjPanelCenter.add(this.getLabelMcVersion(), this.getLabelMcVersion().getName());
                this.ivjPanelCenter.add(this.getTextArea(), this.getTextArea().getName());
                this.ivjPanelCenter.add(this.getLabelFolder(), this.getLabelFolder().getName());
                this.ivjPanelCenter.add(this.getFieldFolder(), this.getFieldFolder().getName());
                this.ivjPanelCenter.add(this.getButtonFolder(), this.getButtonFolder().getName());
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjPanelCenter;
    }

    private JButton getButtonInstall()
    {
        if (this.ivjButtonInstall == null)
        {
            try
            {
                this.ivjButtonInstall = new JButton();
                this.ivjButtonInstall.setName("ButtonInstall");
                this.ivjButtonInstall.setPreferredSize(new Dimension(100, 26));
                this.ivjButtonInstall.setText("Install");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjButtonInstall;
    }

    private JButton getButtonClose()
    {
        if (this.ivjButtonClose == null)
        {
            try
            {
                this.ivjButtonClose = new JButton();
                this.ivjButtonClose.setName("ButtonClose");
                this.ivjButtonClose.setPreferredSize(new Dimension(100, 26));
                this.ivjButtonClose.setText("Cancel");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjButtonClose;
    }

    private JPanel getPanelBottom()
    {
        if (this.ivjPanelBottom == null)
        {
            try
            {
                this.ivjPanelBottom = new JPanel();
                this.ivjPanelBottom.setName("PanelBottom");
                this.ivjPanelBottom.setLayout(new FlowLayout(1, 15, 10));
                this.ivjPanelBottom.setPreferredSize(new Dimension(390, 55));
                this.ivjPanelBottom.add(this.getButtonInstall(), this.getButtonInstall().getName());
                this.ivjPanelBottom.add(this.getButtonExtract(), this.getButtonExtract().getName());
                this.ivjPanelBottom.add(this.getButtonClose(), this.getButtonClose().getName());
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjPanelBottom;
    }

    private JPanel getPanelContentPane()
    {
        if (this.ivjPanelContentPane == null)
        {
            try
            {
                this.ivjPanelContentPane = new JPanel();
                this.ivjPanelContentPane.setName("PanelContentPane");
                this.ivjPanelContentPane.setLayout(new BorderLayout(5, 5));
                this.ivjPanelContentPane.setPreferredSize(new Dimension(394, 203));
                this.ivjPanelContentPane.add(this.getPanelCenter(), "Center");
                this.ivjPanelContentPane.add(this.getPanelBottom(), "South");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjPanelContentPane;
    }

    private void initialize()
    {
        try
        {
            this.setName("InstallerFrame");
            this.setSize(404, 236);
            this.setDefaultCloseOperation(0);
            this.setResizable(false);
            this.setTitle("OptiFine Installer");
            this.setContentPane(this.getPanelContentPane());
            this.initConnections();
        }
        catch (Throwable throwable)
        {
            this.handleException(throwable);
        }

        this.customInit();
    }

    public void onInstall()
    {
        try
        {
            File file1 = new File(this.getFieldFolder().getText());

            if (!file1.exists())
            {
                Utils.showErrorMessage("Folder not found: " + file1.getPath());
                return;
            }

            if (!file1.isDirectory())
            {
                Utils.showErrorMessage("Not a folder: " + file1.getPath());
                return;
            }

            Installer.doInstall(file1);
            Utils.showMessage("OptiFine is successfully installed.");
            this.dispose();
        }
        catch (Exception exception)
        {
            this.handleException(exception);
        }
    }

    public void onExtract()
    {
        try
        {
            File file1 = new File(this.getFieldFolder().getText());

            if (!file1.exists())
            {
                Utils.showErrorMessage("Folder not found: " + file1.getPath());
                return;
            }

            if (!file1.isDirectory())
            {
                Utils.showErrorMessage("Not a folder: " + file1.getPath());
                return;
            }

            boolean flag = Installer.doExtract(file1);

            if (flag)
            {
                Utils.showMessage("OptiFine is successfully extracted.");
                this.dispose();
            }
        }
        catch (Exception exception)
        {
            this.handleException(exception);
        }
    }

    public void onClose()
    {
        this.dispose();
    }

    private void connEtoC1(ActionEvent arg1)
    {
        try
        {
            this.onInstall();
        }
        catch (Throwable throwable)
        {
            this.handleException(throwable);
        }
    }

    private void connEtoC2(ActionEvent arg1)
    {
        try
        {
            this.onClose();
        }
        catch (Throwable throwable)
        {
            this.handleException(throwable);
        }
    }

    private void initConnections() throws Exception
    {
        this.getButtonFolder().addActionListener(this.ivjEventHandler);
        this.getButtonInstall().addActionListener(this.ivjEventHandler);
        this.getButtonExtract().addActionListener(this.ivjEventHandler);
        this.getButtonClose().addActionListener(this.ivjEventHandler);
    }

    private JTextArea getTextArea()
    {
        if (this.ivjTextArea == null)
        {
            try
            {
                this.ivjTextArea = new JTextArea();
                this.ivjTextArea.setName("TextArea");
                this.ivjTextArea.setBounds(15, 66, 365, 44);
                this.ivjTextArea.setEditable(false);
                this.ivjTextArea.setEnabled(true);
                this.ivjTextArea.setFont(new Font("Dialog", 0, 12));
                this.ivjTextArea.setLineWrap(true);
                this.ivjTextArea.setOpaque(false);
                this.ivjTextArea.setPreferredSize(new Dimension(365, 44));
                this.ivjTextArea.setText("This installer will install OptiFine in the official Minecraft launcher and will create a new profile \"OptiFine\" for it.");
                this.ivjTextArea.setWrapStyleWord(true);
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjTextArea;
    }

    private JButton getButtonExtract()
    {
        if (this.ivjButtonExtract == null)
        {
            try
            {
                this.ivjButtonExtract = new JButton();
                this.ivjButtonExtract.setName("ButtonExtract");
                this.ivjButtonExtract.setPreferredSize(new Dimension(100, 26));
                this.ivjButtonExtract.setText("Extract");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjButtonExtract;
    }

    private void connEtoC3(ActionEvent arg1)
    {
        try
        {
            this.onExtract();
        }
        catch (Throwable throwable)
        {
            this.handleException(throwable);
        }
    }

    private JLabel getLabelFolder()
    {
        if (this.ivjLabelFolder == null)
        {
            try
            {
                this.ivjLabelFolder = new JLabel();
                this.ivjLabelFolder.setName("LabelFolder");
                this.ivjLabelFolder.setBounds(15, 116, 47, 16);
                this.ivjLabelFolder.setPreferredSize(new Dimension(47, 16));
                this.ivjLabelFolder.setText("Folder");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjLabelFolder;
    }

    private JTextField getFieldFolder()
    {
        if (this.ivjFieldFolder == null)
        {
            try
            {
                this.ivjFieldFolder = new JTextField();
                this.ivjFieldFolder.setName("FieldFolder");
                this.ivjFieldFolder.setBounds(62, 114, 287, 20);
                this.ivjFieldFolder.setEditable(false);
                this.ivjFieldFolder.setPreferredSize(new Dimension(287, 20));
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjFieldFolder;
    }

    private JButton getButtonFolder()
    {
        if (this.ivjButtonFolder == null)
        {
            try
            {
                this.ivjButtonFolder = new JButton();
                this.ivjButtonFolder.setName("ButtonFolder");
                this.ivjButtonFolder.setBounds(350, 114, 25, 20);
                this.ivjButtonFolder.setMargin(new Insets(2, 2, 2, 2));
                this.ivjButtonFolder.setPreferredSize(new Dimension(25, 20));
                this.ivjButtonFolder.setText("...");
            }
            catch (Throwable throwable)
            {
                this.handleException(throwable);
            }
        }

        return this.ivjButtonFolder;
    }

    public void onFolderSelect()
    {
        File file1 = new File(this.getFieldFolder().getText());
        JFileChooser jfilechooser = new JFileChooser(file1);
        jfilechooser.setFileSelectionMode(1);
        jfilechooser.setAcceptAllFileFilterUsed(false);

        if (jfilechooser.showOpenDialog(this) == 0)
        {
            File file2 = jfilechooser.getSelectedFile();
            this.getFieldFolder().setText(file2.getPath());
        }
    }

    private void connEtoC4(ActionEvent arg1)
    {
        try
        {
            this.onFolderSelect();
        }
        catch (Throwable throwable)
        {
            this.handleException(throwable);
        }
    }

    class IvjEventHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == InstallerFrame.this.getButtonClose())
            {
                InstallerFrame.this.connEtoC2(e);
            }

            if (e.getSource() == InstallerFrame.this.getButtonExtract())
            {
                InstallerFrame.this.connEtoC3(e);
            }

            if (e.getSource() == InstallerFrame.this.getButtonFolder())
            {
                InstallerFrame.this.connEtoC4(e);
            }

            if (e.getSource() == InstallerFrame.this.getButtonInstall())
            {
                InstallerFrame.this.connEtoC1(e);
            }
        }
    }
}
