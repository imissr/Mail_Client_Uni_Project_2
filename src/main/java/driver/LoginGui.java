package driver;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.border.TitledBorder;

import client.Client;
import util.*;

import javax.swing.JCheckBox;

import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.mail.internet.InternetAddress;


import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoginGui implements ActionListener {

    private JFrame frame;

  private boolean flag = false;


    // to sychronised thread work
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private boolean needUpdateFlag = false;


    // instance client to call the methode
    private final Client instance;

    // to check if cc email are empty or not
    private boolean ccEmail;

    // list of subject
    private List<String> listOfMailSubject;

    // list of Message that are in reverse order
    private List<Message> listofMailMessages = new ArrayList<>();


    // model for the jlist
    private DefaultListModel<String> model;


    // first panel

    private JPanel panel;
    private JPanel Absenderdaten;
    private JLabel nameL;
    private JTextField nameTextF;
    public JLabel username;
    public JTextField usernameTextF;
    public JPasswordField passwordtextF;
    public JLabel password;
    public JLabel pop3Adress;
    public JTextField pop3AdressTextF;
    private JCheckBox passwordsave;
    public JTextField pop3Port;
    public JTextField smtpPort;
    public JTextField smtpAdressTextF;
    private JLabel smtpAdress;
    private JButton loginBtu;
    private JButton closeBtu;

    // second panel
    private JPanel panel1;
    private JTextField ccTextF;
    private JTextField toTextF;
    private JTextField subjectTextF;
    private JLabel toTextL;
    private JLabel ccTextL;
    private JTextArea messageTextA;
    private JLabel subjectTextL;
    private JButton sendBtu;
    private JMenuBar menuBar;
    private JMenu nachrichtenMenu;
    private JButton backBtu;
    private JMenuItem abrechenMenuItem;

    //thrid panel
    private JTextField subjectTextFMain;
    private JTextField fromTextFMain;
    private JTextField toTextFMain;
    private JPanel mainWindow;
    private JMenuBar menuBarMain;
    private JList<String> mailList;
    private JButton reciveEmailBtu;
    private JButton sendMailBtu;
    private JPanel emailInfo;
    private JLabel subjectL;
    private JLabel fromL;
    private JLabel toL;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane1;
    private JTextPane emailReviewr;


    // panel4
    private JTextField infoMenuNameTextF;
    private JTextField infoMenuEmailTextF;
    private JTextField infoMenuVersionTextF;
    private JTextField infoMenuMessageCountTextF;
   
    private JPanel panel4;
    private JLabel infoMenuNameL;
    private JLabel infoMenuEmailL;
   
    private JLabel infoMenuVersionL;
    private JLabel infoMenuMessagecountL;
    
    private JMenu mnNewMenu;
    private JMenuItem mntmNewMenuItem_1;
    private JMenuItem mntmNewMenuItem_2;
    private JMenu mnNewMenu_1;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginGui window = new LoginGui();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginGui() {
        this.instance = Client.getInstance();
        initialize();

        try {
            Settings.deserializePassword();
        } catch (ClassNotFoundException | IOException e2) {
            e2.printStackTrace();
        }

        passwordsave.setSelected(Settings.getInstance().savePassword);
        // Initialize password field
        if (Settings.getInstance().savePassword) {
            passwordtextF.setText(Settings.getInstance().password);
            usernameTextF.setText(Settings.getInstance().username);
        } else {
            passwordtextF.setText("");
            usernameTextF.setText("");
        }

        frame.addWindowListener(new WindowListener() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Settings.serializePassword(Utility.charArrayToString(passwordtextF.getPassword()), passwordsave.isSelected(), usernameTextF.getText());
                    // MessageS.serializeMessages(usernameTextF.getText(),Utility.charArrayToString(passwordtextF.getPassword()),
                    // listofMailMessages);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.getWindow().dispose();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowActivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }
        });


    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        // First panel..................................................................

        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 745, 377);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        panel = new JPanel();
        panel.setBounds(0, 0, 729, 338);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        Absenderdaten = new JPanel();
        Absenderdaten.setLayout(null);
        Absenderdaten.setBorder(new TitledBorder(null, "Absenderdatem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        Absenderdaten.setBounds(10, 11, 667, 103);
        panel.add(Absenderdaten);

        nameL = new JLabel("Name");
        nameL.setBounds(34, 39, 85, 24);
        Absenderdaten.add(nameL);

        nameTextF = new JTextField("<Vorname> <Nachname>");
        nameTextF.setColumns(10);
        nameTextF.setBounds(129, 40, 451, 22);
        Absenderdaten.add(nameTextF);

        username = new JLabel("Benutzerkennung");
        username.setBounds(20, 134, 85, 24);
        panel.add(username);

        usernameTextF = new JTextField();
        usernameTextF.setText("minawe.mohamad.khaled@uni-jena.de");
        usernameTextF.setColumns(10);
        usernameTextF.setBounds(115, 136, 451, 22);
        panel.add(usernameTextF);

        passwordtextF = new JPasswordField();
        passwordtextF.setBounds(115, 169, 451, 24);
        panel.add(passwordtextF);

        password = new JLabel("Passwort");
        password.setBounds(20, 169, 85, 24);
        panel.add(password);

        pop3Adress = new JLabel("Posteingang");
        pop3Adress.setBounds(20, 204, 85, 24);
        panel.add(pop3Adress);

        pop3AdressTextF = new JTextField();
        pop3AdressTextF.setText("pop3.uni-jena.de");
        pop3AdressTextF.setColumns(10);
        pop3AdressTextF.setBounds(115, 204, 451, 22);
        panel.add(pop3AdressTextF);

        passwordsave = new JCheckBox("Passwort speichern");

        passwordsave.setBounds(580, 170, 134, 23);
        panel.add(passwordsave);

        pop3Port = new JTextField();
        pop3Port.setText("110");
        pop3Port.setColumns(10);
        pop3Port.setBounds(592, 204, 85, 22);
        panel.add(pop3Port);

        smtpPort = new JTextField();
        smtpPort.setText("587");
        smtpPort.setColumns(10);
        smtpPort.setBounds(592, 238, 85, 22);
        panel.add(smtpPort);

        smtpAdressTextF = new JTextField();
        smtpAdressTextF.setText("smtp.uni-jena.de");
        smtpAdressTextF.setColumns(10);
        smtpAdressTextF.setBounds(115, 237, 451, 22);
        panel.add(smtpAdressTextF);

        smtpAdress = new JLabel("Postausgang");
        smtpAdress.setBounds(20, 241, 85, 24);
        panel.add(smtpAdress);

        loginBtu = new JButton("OK");
        loginBtu.setBounds(15, 296, 271, 30);
        loginBtu.addActionListener(this);
        panel.add(loginBtu);

        closeBtu = new JButton("Abbrechen");
        closeBtu.setBounds(464, 298, 237, 26);
        closeBtu.addActionListener(this);
        panel.add(closeBtu);

        // second
        // panel....................................................................................

        panel1 = new JPanel();
        panel1.setVisible(false);
        panel1.setBounds(0, 0, 733, 393);
        frame.getContentPane().add(panel1);
        panel1.setLayout(null);

        toTextL = new JLabel("An");
        toTextL.setBounds(31, 44, 27, 14);
        panel1.add(toTextL);

        ccTextL = new JLabel("CC");
        ccTextL.setBounds(28, 124, 33, 14);
        panel1.add(ccTextL);

        ccTextF = new JTextField();
        ccTextF.setColumns(10);
        ccTextF.setBounds(71, 117, 453, 28);
        panel1.add(ccTextF);

        toTextF = new JTextField();
        toTextF.setToolTipText("to send more than one email type please seprat it with ';'");
        toTextF.setColumns(10);
        toTextF.setBounds(68, 37, 659, 69);
        panel1.add(toTextF);

        messageTextA = new JTextArea();
        messageTextA.setBounds(0, 198, 733, 195);
        panel1.add(messageTextA);

        subjectTextL = new JLabel("Betreff");
        subjectTextL.setBounds(22, 164, 46, 14);
        panel1.add(subjectTextL);

        subjectTextF = new JTextField();
        subjectTextF.setColumns(10);
        subjectTextF.setBounds(71, 156, 453, 31);
        panel1.add(subjectTextF);

        sendBtu = new JButton("Senden");
        sendBtu.setBounds(548, 120, 147, 28);
        sendBtu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                {
                    ccEmail = ccTextF.getText().isEmpty();
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                    try {

                        Address[] recp = Utility.spltitedEmail(toTextF.getText());

                        if (ccEmail) {
                            instance.sendmailWithCC(usernameTextF.getText(), subjectTextF.getText(), messageTextA.getText(), nameTextF.getText(), null, ccEmail, recp);
                            subjectTextF.setText("");
                            messageTextA.setText("");
                            ccTextF.setText("");
                            toTextF.setText("");
                        } else {
                            if (instance.emailcheckGui(ccTextF.getText(), "please Enter a Valid cc Email")) {
                                instance.sendmailWithCC(usernameTextF.getText(), subjectTextF.getText(), messageTextA.getText(), nameTextF.getText(), new InternetAddress(ccTextF.getText()), ccEmail, recp);
                                subjectTextF.setText("");
                                messageTextA.setText("");
                                ccTextF.setText("");
                                toTextF.setText("");
                            }

                        }


                    } catch (MessagingException e1) {

                        Utility.infoBox("An error accourd while sending the Message make " +
                                "sure that Email are sperated with the right figuer " + " ", "MessagingException");
                        subjectTextF.setText("");
                        messageTextA.setText("");
                        ccTextF.setText("");
                        toTextF.setText("");

                    } catch (UnsupportedEncodingException e1) {

                        Utility.infoBox("UnsupportedEncodingException", "UnsupportedEncodingException");
                    }
                }
                   });

                }

            }

    });

        panel1.add(sendBtu);

        backBtu = new JButton("zur\u00FCck");
        backBtu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.setVisible(true);
                subjectTextF.setText("");
                messageTextA.setText("");
                ccTextF.setText("");
                toTextF.setText("");
                panel.setVisible(false);
                panel1.setVisible(false);
                frame.setBounds(100, 100, 789, 508);
            }
        });
        backBtu.setBounds(548, 161, 147, 26);
        panel1.add(backBtu);

        menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 733, 26);
        panel1.add(menuBar);

        nachrichtenMenu = new JMenu("Nachrichten");
        menuBar.add(nachrichtenMenu);

        abrechenMenuItem = new JMenuItem("Abbrechen");
        abrechenMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.setVisible(true);
                subjectTextF.setText("");
                messageTextA.setText("");
                ccTextF.setText("");
                toTextF.setText("");
                panel.setVisible(false);
                panel1.setVisible(false);
                frame.setBounds(100, 100, 789, 508);

            }
        });

        nachrichtenMenu.add(abrechenMenuItem);

        // thrid panel..................................................................

        mainWindow = new JPanel();
        mainWindow.setVisible(false);
        mainWindow.setBounds(0, 0, 773, 469);
        frame.getContentPane().add(mainWindow);
        mainWindow.setLayout(null);

        menuBarMain = new JMenuBar();
        menuBarMain.setBounds(0, 0, 773, 26);
        mainWindow.add(menuBarMain);

        mnNewMenu = new JMenu("Option");
        menuBarMain.add(mnNewMenu);

        mntmNewMenuItem_1 = new JMenuItem("Beenden");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Settings.serializePassword(Utility.charArrayToString(passwordtextF.getPassword()), passwordsave.isSelected(), usernameTextF.getText());
                    // MessageS.serializeMessages(usernameTextF.getText(),Utility.charArrayToString(passwordtextF.getPassword()),
                    // listofMailMessages);
                } catch (IOException e1) {
                    Utility.infoBox("Error while serializing the password" , "IOException");
                }
                frame.dispose();
            }
        });

        mnNewMenu.add(mntmNewMenuItem_1);

        mntmNewMenuItem_2 = new JMenuItem("LogOut");
        mntmNewMenuItem_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executorService.submit(new Runnable() {

                    @Override
                    public void run() {

                        frame.setBounds(100, 100, 745, 377);
                        passwordtextF.setText("");
                        usernameTextF.setText("");
                        passwordsave.setSelected(false);
                        model.removeAllElements();

                        mainWindow.setVisible(false);
                        panel.setVisible(true);

                    }
                });

            }
        });

        mnNewMenu.add(mntmNewMenuItem_2);

        mnNewMenu_1 = new JMenu("Info");
        mnNewMenu_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    frame.setBounds(100, 100, 741, 373);
                    mainWindow.setVisible(false);
                    panel4.setVisible(true);
                    infoMenuNameTextF.setText(nameTextF.getText());
                    infoMenuEmailTextF.setText(usernameTextF.getText());
                    infoMenuVersionTextF.setText("0.1");
                    infoMenuMessageCountTextF.setText(String.valueOf(listofMailMessages.size()));
                }
            }
        });
        menuBarMain.add(mnNewMenu_1);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 49, 773, 142);
        mainWindow.add(scrollPane);

//List to put email inside of it
        mailList = new JList();
        mailList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    executorService.submit(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                int selectedItemIndex = mailList.getSelectedIndex();
                                if ((instance.getNewEmailList().size() > 0)
                                        && listofMailMessages.get(selectedItemIndex).getSubject().equalsIgnoreCase(listofMailMessages.get(selectedItemIndex).getSubject())
                                        && listofMailMessages.get(selectedItemIndex).getContent() == (listofMailMessages.get(selectedItemIndex).getContent())) {
                                    String modelString = model.elementAt(selectedItemIndex);
                                    String newWord = Utility.removeSpecificWord(modelString, "New Email!!");
                                    System.out.println(newWord);
                                    model.removeElementAt(selectedItemIndex);
                                    model.add(selectedItemIndex, newWord);
                                    mailList.setModel(model);
                                    instance.getNewEmailList().remove(selectedItemIndex);

                                }

                                Address[] from = listofMailMessages.get(selectedItemIndex).getFrom();
                                Address[] rcp = listofMailMessages.get(selectedItemIndex).getRecipients(Message.RecipientType.TO);

                                emailReviewr.setText(instance.readMessage(listofMailMessages.get(selectedItemIndex)));
                                if (listofMailMessages.get(selectedItemIndex).getSubject().equals("")) {
                                    subjectTextFMain.setText("no Subject");

                                } else {

                                    subjectTextFMain.setText(listofMailMessages.get(selectedItemIndex).getSubject());

                                }

                                toTextFMain.setText(rcp[0].toString());
                                fromTextFMain.setText(from[0].toString());

                            } catch (MessagingException e1) {
                                Utility.infoBox("error occurred while fetching the messages" , "MessagingException");
                            } catch (IOException e1) {
                                Utility.infoBox("Input Error" , "MessagingException");
                            }

                        }
                    });

                }

            }
        });


        scrollPane.setViewportView(mailList);

        // TODO : fetching method for the jlist

        reciveEmailBtu = new JButton("Aktulasierien");
        reciveEmailBtu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                executorService.submit(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            flag = false;
                            updateEmailInbox();
                            update();
                            if (needUpdateFlag) {
                                model.removeAllElements();
                                model = new DefaultListModel<>();
                                defaultModelListTolist(model);
                                mailList.setModel(model);
                            }if(flag){
                                model.removeAllElements();
                                model = new DefaultListModel<>();
                                defaultModelListTolist(model);
                                mailList.setModel(model);
                                instance.backUp(usernameTextF.getText());

                            }


                        } catch (MessagingException e) {
                            Utility.infoBox("error occured while updating your email Inbox please make sure you are connected to the internet","MessagingException");

                        } catch (IOException e) {
                            Utility.infoBox("error occured while backuping your file please try again !!! " , "IOException");

                        }

                    }
                });

            }
        });

        reciveEmailBtu.setBounds(0, 26, 110, 23);
        mainWindow.add(reciveEmailBtu);

        sendMailBtu = new JButton("Senden");
        sendMailBtu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                panel1.setVisible(true);
                mainWindow.setVisible(false);
                frame.setBounds(100, 100, 749, 432);
            }
        });
        sendMailBtu.setBounds(162, 26, 115, 23);
        mainWindow.add(sendMailBtu);

        emailInfo = new JPanel();
        emailInfo.setBounds(0, 195, 773, 85);
        mainWindow.add(emailInfo);
        emailInfo.setLayout(null);

        subjectL = new JLabel("Betreff:");
        subjectL.setFont(new Font("Tahoma", Font.PLAIN, 15));
        subjectL.setBounds(10, 11, 108, 22);
        emailInfo.add(subjectL);

        fromL = new JLabel("Von:");
        fromL.setFont(new Font("Tahoma", Font.PLAIN, 15));
        fromL.setBounds(10, 33, 108, 22);
        emailInfo.add(fromL);

        toL = new JLabel("An:");
        toL.setFont(new Font("Tahoma", Font.PLAIN, 15));
        toL.setBounds(10, 55, 108, 22);
        emailInfo.add(toL);

        subjectTextFMain = new JTextField();
        subjectTextFMain.setEditable(false);
        subjectTextFMain.setBounds(74, 14, 664, 20);
        emailInfo.add(subjectTextFMain);
        subjectTextFMain.setColumns(10);

        fromTextFMain = new JTextField();
        fromTextFMain.setEditable(false);
        fromTextFMain.setBounds(74, 36, 664, 20);
        emailInfo.add(fromTextFMain);
        fromTextFMain.setColumns(10);

        toTextFMain = new JTextField();
        toTextFMain.setEditable(false);
        toTextFMain.setBounds(74, 58, 664, 20);
        emailInfo.add(toTextFMain);
        toTextFMain.setColumns(10);

        scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(0, 278, 773, 191);
        mainWindow.add(scrollPane1);

        emailReviewr = new JTextPane();
        scrollPane1.setViewportView(emailReviewr);

        // DesktopFrame
        // Panel4......................................................................................

        panel4 = new JPanel();
        panel4.setVisible(false);
        panel4.setBounds(0, 0, 729, 338);
        frame.getContentPane().add(panel4);
        panel4.setLayout(null);

        infoMenuNameL = new JLabel("Name");
        infoMenuNameL.setFont(new Font("Tahoma", Font.PLAIN, 16));
        infoMenuNameL.setBounds(20, 27, 87, 44);
        panel4.add(infoMenuNameL);

        infoMenuEmailL = new JLabel("Email");
        infoMenuEmailL.setFont(new Font("Tahoma", Font.PLAIN, 16));
        infoMenuEmailL.setBounds(20, 82, 87, 44);
        panel4.add(infoMenuEmailL);

        infoMenuVersionL = new JLabel("Version");
        infoMenuVersionL.setFont(new Font("Tahoma", Font.PLAIN, 16));
        infoMenuVersionL.setBounds(20, 137, 87, 44);
        panel4.add(infoMenuVersionL);

        infoMenuMessagecountL = new JLabel("Messages Anzahl");
        infoMenuMessagecountL.setFont(new Font("Tahoma", Font.PLAIN, 16));
        infoMenuMessagecountL.setBounds(10, 192, 138, 44);
        panel4.add(infoMenuMessagecountL);

        JButton InfoMenuBackButton = new JButton("zur\u00FCck");
        InfoMenuBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel4.setVisible(false);
                mainWindow.setVisible(true);
                frame.setBounds(100, 100, 789, 508);

            }
        });
        InfoMenuBackButton.setBounds(447, 252, 246, 30);
        panel4.add(InfoMenuBackButton);

        infoMenuNameTextF = new JTextField();
        infoMenuNameTextF.setEditable(false);
        infoMenuNameTextF.setBounds(137, 41, 320, 20);
        panel4.add(infoMenuNameTextF);
        infoMenuNameTextF.setColumns(10);

        infoMenuEmailTextF = new JTextField();
        infoMenuEmailTextF.setEditable(false);
        infoMenuEmailTextF.setBounds(135, 82, 322, 20);
        panel4.add(infoMenuEmailTextF);
        infoMenuEmailTextF.setColumns(10);

        infoMenuVersionTextF = new JTextField();
        infoMenuVersionTextF.setEditable(false);
        infoMenuVersionTextF.setBounds(137, 151, 320, 20);
        panel4.add(infoMenuVersionTextF);
        infoMenuVersionTextF.setColumns(10);

        infoMenuMessageCountTextF = new JTextField();
        infoMenuMessageCountTextF.setEditable(false);
        infoMenuMessageCountTextF.setBounds(137, 206, 320, 20);
        panel4.add(infoMenuMessageCountTextF);
        infoMenuMessageCountTextF.setColumns(10);
    }
    // Button For the first
    // panel......................................................................................


    // every 15 min

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginBtu) {


            if (instance.portcheckGui(pop3Port.getText(), smtpPort.getText())) {

                executorService.submit(new Runnable() {
                    public void run() {
                        try {

                            model = new DefaultListModel<>();
                            instance.initConnectProperties(pop3AdressTextF.getText(), pop3Port.getText());
                            instance.connect(usernameTextF.getText(), Utility.charArrayToString(passwordtextF.getPassword()));
                            instance.initConnectPropertiesSmtp(smtpAdressTextF.getText(), smtpPort.getText());
                            instance.connectSmtp(usernameTextF.getText(), Utility.charArrayToString(passwordtextF.getPassword()));

                            instance.creatFolder(usernameTextF.getText());


                            panel.setVisible(false);
                            panel1.setVisible(false);
                            mainWindow.setVisible(true);
                            frame.setBounds(100, 100, 789, 508);
                            update();
                            defaultModelListTolist(model);
                            mailList.setModel(model);
                            if(flag){
                                instance.backUp(usernameTextF.getText());
                            }
                            flag = false;



                        } catch (AuthenticationFailedException e1) {

                            Utility.infoBox("Credentials don't match! Try Again", "AuthenticationFailedException");

                        } catch (MessagingException e1) {

                            Utility.infoBox("Domain or port are not correct! " + "Try Again", "MessagingException");


                        } catch (IOException e1) {
                            Utility.infoBox("a problem while writing the password in the file", "bufferwrisater poasrblem");
                        }


                    }
                });
            }

        }

        if (e.getSource() == closeBtu) {
            try {
                Settings.serializePassword(Utility.charArrayToString(passwordtextF.getPassword()), passwordsave.isSelected(), usernameTextF.getText());

            } catch (IOException e1) {
                Utility.infoBox("Problem occurred while saving your password" , "IOException" );
            }
            frame.dispose();
        }

    }


    public void defaultModelListTolist(DefaultListModel<String> model) throws MessagingException, IOException {

        {
            listOfMailSubject = getListOfSubject();
            for (String string : listOfMailSubject) {
                model.addElement(string);
            }
        }


    }


    public List<String> getListOfSubject() throws MessagingException, IOException {

        update();

        List<String> list = new ArrayList<>();


        List<Message> messagesSubject = listofMailMessages;


        for (Message message : messagesSubject) {

            if (message.getSubject() != null) {

                if (message.getSubject().equals("")) {

                    list.add("This message has no subject");
                } else {

                    list.add(message.getSubject() + " ");

                }

            } else {
                list.add("This message has no subject");

            }

        }


        return overwriteValues(list, instance.getNewEmailList());

    }

    public List<String> overwriteValues(List<String> values, List<Message> indices) {
        for (int i = 0; i < indices.size(); i++) {
            values.set(i, values.get(i) + " New Email!! ");
        }

        return values;
    }


    public void updateEmailInbox() throws MessagingException, IOException {
        instance.reconnect(usernameTextF.getText(), Utility.charArrayToString(passwordtextF.getPassword()), pop3AdressTextF.getText(), pop3Port.getText());
        instance.updateMessages();


    }

    public void update() throws MessagingException, IOException {

        updateEmailInbox();

        if (!instance.checkIfFileExist()) {
            listofMailMessages = Arrays.asList(instance.getEmailInbox().getMessages());
            needUpdateFlag = false;
            Collections.reverse(listofMailMessages);
            flag = true;

        } else {
            List<Message> temp = instance.readMessageFromBinFile();
            int oldsize = temp.size();
            System.out.println("oldsize:" + oldsize);
            int size = Arrays.asList(instance.getEmailInbox().getMessages()).size();
            System.out.println("size:" + size);
            if (oldsize == size) {
                listofMailMessages = temp;
                needUpdateFlag = false;
                Collections.reverse(listofMailMessages);


            } else if (oldsize > size) {
                instance.backUp(usernameTextF.getText());
                listofMailMessages = temp;
                needUpdateFlag = false;
                Collections.reverse(listofMailMessages);
            } else {

                needUpdateFlag = true;
                instance.backUpAfter(usernameTextF.getText());
                listofMailMessages = instance.readMessageFromBinFile();
                Collections.reverse(listofMailMessages);

            }
        }
    }


}

	




