/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.fredy.filerenamer.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.fredy.filerenamer.replacer.AttributeKey;
import org.fredy.filerenamer.replacer.Replacer;
import org.fredy.filerenamer.replacer.ReplacerFactory;
import org.fredy.filerenamer.replacer.ReplacerType;
import org.fredy.filerenamer.util.FileNameUtils;
import org.fredy.filerenamer.util.FileUtils;

/**
 * The FileRenamer frame.
 * @author fredy
 */
public class FileRenamerFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final ResourceBundle rb;
    private SelectionPanel selectionPanel;
    private FileListPanel fileListPanel;
    private ButtonPanel buttonPanel;

    public FileRenamerFrame() {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("filerenamer", locale);

        initLookAndFeel();
        initComponents();
        pack();
    }

    private void initLookAndFeel() {
        String lookAndFeel = "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, rb.getString(ResourceBundleKey.ERROR_TITLE
                .toString()), JOptionPane.ERROR_MESSAGE);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(rb.getString(ResourceBundleKey.LABEL_APPLICATION.toString()));
        setLayout(new MigLayout("", "[grow,fill]", "[][grow,fill][]"));

        selectionPanel = new SelectionPanel();
        selectionPanel.enableComponents(false);

        fileListPanel = new FileListPanel();
        fileListPanel.enableComponents(false);

        buttonPanel = new ButtonPanel();
        buttonPanel.enableComponents(false);

        add(selectionPanel, "wrap, grow");
        add(fileListPanel, "wrap, grow");
        add(buttonPanel, "wrap, grow");
    }

    public SelectionPanel getSelectionPanel() {
        return selectionPanel;
    }

    public FileListPanel getFileListPanel() {
        return fileListPanel;
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    class SelectionPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        // Buttons
        private ButtonGroup buttonGroup;
        private JRadioButton patternRadioButton;
        private JRadioButton allCharsUpperCaseRadioButton;
        private JRadioButton allCharsLowerCaseRadioButton;
        private JRadioButton firstCharUpperCaseRadioButton;
        private JRadioButton sequenceNumberRadioButton;
        private JRadioButton charactersRadioButton;
        // Pattern panel
        private JPanel patternPanel;
        private JTextField fromTextField;
        private JTextField toTextField;
        private JCheckBox regexCheckBox;
        // Sequence number panel
        private JPanel seqNumberPanel;
        private JSpinner indexSpinner;
        private JSpinner indexSeqNumberSpinner;
        private JSpinner startSeqNumberSpinner;
        private JSpinner endSeqNumberSpinner;
        private JCheckBox startCheckBox;
        private JCheckBox endCheckBox;
        private JCheckBox indexCheckBox;
        // Characters panel
        private JPanel charsPanel;
        private JSpinner charsIndexSpinner;
        private JTextField indexTextField;
        private JTextField startTextField;
        private JTextField endTextField;

        public SelectionPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill]", ""));
            initPanels();
            initRadioButtons();
        }

        private void initPanels() {
            patternPanel = new JPanel(new MigLayout("", "[][grow,fill]", ""));
            patternPanel.setBorder(BorderFactory.createEtchedBorder());
            fromTextField = new JTextField();
            toTextField = new JTextField();
            regexCheckBox = new JCheckBox("Enable regular expression");
            patternPanel.add(regexCheckBox, "span, wrap");
            patternPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_FROM
                    .toString())));
            patternPanel.add(fromTextField, "wrap, grow");
            patternPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_TO
                    .toString())));
            patternPanel.add(toTextField, "wrap, grow");

            seqNumberPanel = new JPanel(new MigLayout());
            seqNumberPanel.setBorder(BorderFactory.createEtchedBorder());
            startSeqNumberSpinner = new JSpinner();
            startSeqNumberSpinner.setEnabled(false);
            endSeqNumberSpinner = new JSpinner();
            endSeqNumberSpinner.setEnabled(false);
            indexSpinner = new JSpinner(new SpinnerNumberModel(new Integer(0),
                    new Integer(0), null, new Integer(1)));
            indexSpinner.setEnabled(false);
            indexSeqNumberSpinner = new JSpinner();
            startCheckBox = new JCheckBox();
            startCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enableStartSeqNumberComponents(startCheckBox.isSelected());
                }
            });
            endCheckBox = new JCheckBox();
            endCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enableEndSeqNumberComponents(endCheckBox.isSelected());
                }
            });
            indexCheckBox = new JCheckBox();
            indexCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enableIndexSeqNumberComponents(indexCheckBox.isSelected());
                }
            });
            seqNumberPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_ZERO_INDEX.toString())),
                    "wrap, span");
            seqNumberPanel.add(startCheckBox);
            seqNumberPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_START.toString())));
            seqNumberPanel.add(startSeqNumberSpinner, "wrap");
            seqNumberPanel.add(endCheckBox);
            seqNumberPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_END.toString())));
            seqNumberPanel.add(endSeqNumberSpinner, "wrap");
            seqNumberPanel.add(indexCheckBox);
            seqNumberPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_INDEX.toString())));
            seqNumberPanel.add(indexSpinner);
            seqNumberPanel.add(indexSeqNumberSpinner, "wrap");

            charsPanel = new JPanel(new MigLayout("", "[][][grow,fill]", ""));
            charsPanel.setBorder(BorderFactory.createEtchedBorder());
            startTextField = new JTextField();
            endTextField = new JTextField();
            indexTextField = new JTextField();
            charsIndexSpinner = new JSpinner(new SpinnerNumberModel(new Integer(0),
                    new Integer(0), null, new Integer(1)));
            charsPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_ZERO_INDEX.toString())),
                    "wrap, span");
            charsPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_START
                    .toString())));
            charsPanel.add(startTextField, "wrap, span, grow");
            charsPanel.add(new JLabel(rb
                    .getString(ResourceBundleKey.LABEL_END.toString())));
            charsPanel.add(endTextField, "wrap, span, grow");
            charsPanel.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_INDEX.toString())));
            charsPanel.add(charsIndexSpinner);
            charsPanel.add(indexTextField, "wrap, grow");

            enablePatternPanel(false);
            enableSeqNumberPanel(false);
            enableCharsPanel(false);
        }

        private void enableStartSeqNumberComponents(boolean enabled) {
            startSeqNumberSpinner.setEnabled(enabled);
        }

        private void enableEndSeqNumberComponents(boolean enabled) {
            endSeqNumberSpinner.setEnabled(enabled);
        }

        private void enableIndexSeqNumberComponents(boolean enabled) {
            indexSpinner.setEnabled(enabled);
            indexSeqNumberSpinner.setEnabled(enabled);
        }

        private void enablePatternPanel(boolean enabled) {
            fromTextField.setEnabled(enabled);
            toTextField.setEnabled(enabled);
            regexCheckBox.setEnabled(enabled);
        }

        private void enableSeqNumberPanel(boolean enabled) {
            startCheckBox.setEnabled(enabled);
            endCheckBox.setEnabled(enabled);
            indexCheckBox.setEnabled(enabled);
        }

        private void enableCharsPanel(boolean enabled) {
            charsIndexSpinner.setEnabled(enabled);
            indexTextField.setEnabled(enabled);
            startTextField.setEnabled(enabled);
            endTextField.setEnabled(enabled);
        }

        private void initRadioButtons() {
            buttonGroup = new ButtonGroup();

            patternRadioButton = new JRadioButton();
            patternRadioButton.setText(rb.getString(ResourceBundleKey.LABEL_PATTERN.toString()));
            patternRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enablePatternPanel(true);
                    enableSeqNumberPanel(false);
                    enableCharsPanel(false);
                }
            });
            buttonGroup.add(patternRadioButton);

            allCharsUpperCaseRadioButton = new JRadioButton();
            allCharsUpperCaseRadioButton.setText(
                    rb.getString(ResourceBundleKey.LABEL_ALLUPPERCASE.toString()));
            allCharsUpperCaseRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enablePatternPanel(false);
                    enableSeqNumberPanel(false);
                    enableCharsPanel(false);
                }
            });
            buttonGroup.add(allCharsUpperCaseRadioButton);

            allCharsLowerCaseRadioButton = new JRadioButton();
            allCharsLowerCaseRadioButton.setText(
                    rb.getString(ResourceBundleKey.LABEL_ALLLOWERCASE.toString()));
            allCharsLowerCaseRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enablePatternPanel(false);
                    enableSeqNumberPanel(false);
                    enableCharsPanel(false);
                }
            });
            buttonGroup.add(allCharsLowerCaseRadioButton);

            firstCharUpperCaseRadioButton = new JRadioButton();
            firstCharUpperCaseRadioButton.setText(
                    rb.getString(ResourceBundleKey.LABEL_FIRSTUPPERCASE.toString()));
            firstCharUpperCaseRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enablePatternPanel(false);
                    enableSeqNumberPanel(false);
                    enableCharsPanel(false);
                }
            });
            buttonGroup.add(firstCharUpperCaseRadioButton);

            sequenceNumberRadioButton = new JRadioButton();
            sequenceNumberRadioButton.setText(
                    rb.getString(ResourceBundleKey.LABEL_SEQUENCE_NUMBER.toString()));
            sequenceNumberRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enablePatternPanel(false);
                    enableSeqNumberPanel(true);
                    enableCharsPanel(false);
                }
            });
            buttonGroup.add(sequenceNumberRadioButton);

            charactersRadioButton = new JRadioButton();
            charactersRadioButton.setText(
                    rb.getString(ResourceBundleKey.LABEL_CHARACTERS.toString()));
            charactersRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enablePatternPanel(false);
                    enableSeqNumberPanel(false);
                    enableCharsPanel(true);
                }
            });
            buttonGroup.add(charactersRadioButton);

            add(patternRadioButton, "wrap");
            add(patternPanel, "wrap, grow");
            add(allCharsUpperCaseRadioButton, "wrap");
            add(allCharsLowerCaseRadioButton, "wrap");
            add(firstCharUpperCaseRadioButton, "wrap");
            add(sequenceNumberRadioButton, "wrap");
            add(seqNumberPanel, "wrap");
            add(charactersRadioButton, "wrap");
            add(charsPanel, "wrap");
        }

        public boolean validateForm() {
            if (patternRadioButton.isSelected()) {
                if (fromTextField.getText().equals("")) {
                    showErrorMessage(rb.getString(ResourceBundleKey.ERROR_MANDATORY_FROM.toString()));
                    return false;
                }
                if (!regexCheckBox.isSelected()) {
                    if (!FileNameUtils.isValidFileName(fromTextField.getText())) {
                        showErrorMessage(rb.getString(ResourceBundleKey.ERROR_INVALID_FILENAME.toString()));
                        return false;
                    }
                }
                if (!FileNameUtils.isValidFileName(toTextField.getText())) {
                    showErrorMessage(rb.getString(ResourceBundleKey.ERROR_INVALID_FILENAME.toString()));
                    return false;
                }
                return true;
            } else if (allCharsUpperCaseRadioButton.isSelected()) {
                return true;
            } else if (allCharsLowerCaseRadioButton.isSelected()) {
                return true;
            } else if (firstCharUpperCaseRadioButton.isSelected()) {
                return true;
            } else if (sequenceNumberRadioButton.isSelected()) {
                return true;
            } else if (charactersRadioButton.isSelected()) {
                if (!FileNameUtils.isValidFileName(startTextField.getText())) {
                    showErrorMessage(rb.getString(ResourceBundleKey.ERROR_INVALID_FILENAME.toString()));
                    return false;
                }
                if (!FileNameUtils.isValidFileName(indexTextField.getText())) {
                    showErrorMessage(rb.getString(ResourceBundleKey.ERROR_INVALID_FILENAME.toString()));
                    return false;
                }
                if (!FileNameUtils.isValidFileName(endTextField.getText())) {
                    showErrorMessage(rb.getString(ResourceBundleKey.ERROR_INVALID_FILENAME.toString()));
                    return false;
                }
                return true;
            } else { // No selection
                showErrorMessage(rb.getString(ResourceBundleKey.ERROR_MANDATORY_SELECTION.toString()));
                return false;
            }
        }

        public Replacer getReplacer() {
            Map<AttributeKey, Object> attrs = new HashMap<AttributeKey, Object>();
            if (patternRadioButton.isSelected()) {
                attrs.put(AttributeKey.TURN_ON_REGEX, regexCheckBox.isSelected());
                attrs.put(AttributeKey.FROM_PATTERN, fromTextField.getText());
                attrs.put(AttributeKey.TO_PATTERN, toTextField.getText());
                return ReplacerFactory.getInstance(ReplacerType.PATTERN, attrs);
            } else if (allCharsUpperCaseRadioButton.isSelected()) {
                return ReplacerFactory.getInstance(ReplacerType.ALL_UPPER_CASE, attrs);
            } else if (allCharsLowerCaseRadioButton.isSelected()) {
                return ReplacerFactory.getInstance(ReplacerType.ALL_LOWER_CASE, attrs);
            } else if (firstCharUpperCaseRadioButton.isSelected()) {
                return ReplacerFactory.getInstance(ReplacerType.FIRST_CHAR_UPPER_CASE,
                        attrs);
            } else if (sequenceNumberRadioButton.isSelected()) {
                if (startCheckBox.isSelected()) {
                    attrs.put(AttributeKey.INITIAL_NO_FOR_START, startSeqNumberSpinner.getValue());
                }

                if (endCheckBox.isSelected()) {
                    attrs.put(AttributeKey.INITIAL_NO_FOR_END, endSeqNumberSpinner.getValue());
                }

                if (indexCheckBox.isSelected()) {
                    attrs.put(AttributeKey.INITIAL_NO_FOR_INDEX, indexSeqNumberSpinner.getValue());
                    attrs.put(AttributeKey.INDEX_NO, indexSpinner.getValue());
                }
                return ReplacerFactory.getInstance(ReplacerType.SEQUENCE_NUMBER, attrs);
            } else if (charactersRadioButton.isSelected()) {
                attrs.put(AttributeKey.CHARS_FOR_START, startTextField.getText());
                attrs.put(AttributeKey.CHARS_FOR_END, endTextField.getText());
                attrs.put(AttributeKey.CHARS_FOR_INDEX, indexTextField.getText());
                attrs.put(AttributeKey.INDEX_NO, charsIndexSpinner.getValue());
                return ReplacerFactory.getInstance(ReplacerType.CHARACTERS, attrs);
            } else { // No selection
                return null;
            }
        }

        public void enableComponents(boolean enabled) {
            enableStartSeqNumberComponents(enabled);
            enableEndSeqNumberComponents(enabled);
            enableIndexSeqNumberComponents(enabled);
            enablePatternPanel(enabled);
            enableSeqNumberPanel(enabled);
            enableCharsPanel(enabled);
        }
    }

    class FileListPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private JList fileList;
        private DefaultListModel fileListModel;
        private JButton upButton;
        private JButton downButton;
        private JButton deleteButton;

        public FileListPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill][]", "[grow,fill]"));
            initComponents();
        }

        private void initComponents() {
            fileListModel = new DefaultListModel();
            fileList = new JList(fileListModel);
            fileList.setBorder(BorderFactory.createEtchedBorder());
            fileList.setTransferHandler(new TransferHandler() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                public boolean importData(JComponent comp, Transferable t) {
                    if (!(comp instanceof JList)) {
                        return false;
                    }
                    if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        return false;
                    }

                    try {
                        List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        addFiles(files.toArray(new File[files.size()]));
                        fileListPanel.enableComponents(true);
                        buttonPanel.enableComponents(true);
                        FileRenamerFrame.this.pack();

                        return true;
                    } catch (UnsupportedFlavorException ufe) {
                        showErrorMessage(ufe.getMessage());
                    } catch (IOException ioe) {
                        showErrorMessage(ioe.getMessage());
                    }
                    return false;
                }

                public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
                    if (comp instanceof JList) {
                        for (int i = 0; i < transferFlavors.length; i++) {
                            if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            JScrollPane scrollPane = new JScrollPane(fileList);
            add(scrollPane, "grow");

            upButton = new JButton(rb.getString(ResourceBundleKey.LABEL_UP.toString()));
            upButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedIndices = fileList.getSelectedIndices();
                    if (selectedIndices.length == 1) {
                        int index = selectedIndices[0];
                        if (index > 0) {
                            File f = (File) fileListModel.get(index);
                            File tmp = (File) fileListModel.get(index-1);
                            fileListModel.setElementAt(f, index-1);
                            fileListModel.setElementAt(tmp, index);
                            fileList.setSelectedIndex(index-1);
                        }
                    }
                }
            });

            downButton = new JButton(rb.getString(ResourceBundleKey.LABEL_DOWN.toString()));
            downButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedIndices = fileList.getSelectedIndices();
                    if (selectedIndices.length == 1) {
                        int index = selectedIndices[0];
                        if (index < fileListModel.size() - 1) {
                            File f = (File) fileListModel.get(index);
                            File tmp = (File) fileListModel.get(index+1);
                            fileListModel.setElementAt(f, index+1);
                            fileListModel.setElementAt(tmp, index);
                            fileList.setSelectedIndex(index+1);
                        }
                    }
                }
            });

            deleteButton = new JButton(rb.getString(ResourceBundleKey.LABEL_DELETE.toString()));
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedIndeces = fileList.getSelectedIndices();
                    for (int i = (selectedIndeces.length - 1); i >= 0; i--) {
                        fileListModel.remove(selectedIndeces[i]);
                        if (fileListModel.getSize() >= 1) {
                            fileList.setSelectedIndex(selectedIndeces[i] - 1);
                        }
                    }

                    if (fileListModel.getSize() == 0) {
                        fileListPanel.enableComponents(false);
                        buttonPanel.enableComponents(false);
                    }
                }
            });

            JPanel panel = new JPanel(new MigLayout("", "[grow,fill]", ""));
            panel.setBorder(BorderFactory.createEtchedBorder());
            panel.add(upButton, "wrap, grow");
            panel.add(downButton, "wrap, grow");
            panel.add(deleteButton, "wrap, grow");
            add(panel);
        }

        public void addFiles(File... files) {
            for (File file : files) {
                for (File f : FileUtils.getFileListing(file)) {
                    fileListModel.addElement(f);
                }
            }
        }

        public void clearFiles() {
            fileListModel.clear();
        }

        public List<File> getFiles() {
            List<File> files = new ArrayList<File>();
            Enumeration<?> enumeration = fileListModel.elements();
            while (enumeration.hasMoreElements()) {
                files.add((File) enumeration.nextElement());
            }
            return files;
        }

        public void updateFiles(File... files) {
            clearFiles();
            addFiles(files);

            FileRenamerFrame.this.pack();
        }

        public void enableComponents(boolean enabled) {
            upButton.setEnabled(enabled);
            downButton.setEnabled(enabled);
            deleteButton.setEnabled(enabled);
        }
    }

    class ButtonPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private JButton openButton;
        private JButton renameButton;
        private JButton clearButton;
        private JFileChooser fileChooser;

        public ButtonPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout());
            initFileChooser();
            initButtons();
        }

        private void initFileChooser() {
            fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }

        private void initButtons() {
            openButton = new JButton(rb.getString(ResourceBundleKey.LABEL_OPEN.toString()));
            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = fileChooser.showOpenDialog(FileRenamerFrame.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        fileListPanel.clearFiles();

                        File[] sourceFiles = fileChooser.getSelectedFiles();
                        fileListPanel.addFiles(sourceFiles);

                        fileListPanel.enableComponents(true);
                        buttonPanel.enableComponents(true);
                    }
                    FileRenamerFrame.this.pack();
                }
            });
            add(openButton);

            renameButton = new JButton(rb.getString(ResourceBundleKey.LABEL_RENAME.toString()));
            renameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectionPanel.validateForm()) {
                        try {
                            PreviewDialog previewDialog = new PreviewDialog(FileRenamerFrame.this,
                                    rb, selectionPanel.getReplacer(), fileListPanel.getFiles());
                            previewDialog.setVisible(true);
                        } catch (Exception ex) {
                            showErrorMessage(ex.getMessage());
                        }
                    }
                }
            });
            add(renameButton);

            clearButton = new JButton(rb.getString(ResourceBundleKey.LABEL_CLEAR.toString()));
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileListPanel.clearFiles();
                    fileListPanel.enableComponents(false);
                    buttonPanel.enableComponents(false);

                    FileRenamerFrame.this.pack();
                }
            });
            add(clearButton);
            
            add(new JLabel(rb.getString(ResourceBundleKey.LABEL_CREATED_BY.toString())));
        }

        public void enableComponents(boolean enabled) {
            renameButton.setEnabled(enabled);
            clearButton.setEnabled(enabled);
        }
    }
}
