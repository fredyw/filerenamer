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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.fredy.filerenamer.FileRenamerManager;
import org.fredy.filerenamer.replacer.Replacer;

/**
 * Preview dialog.
 * @author fredy
 */
public class PreviewDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final ResourceBundle rb;
    private FileListPanel fileListPanel;
    private ButtonPanel buttonPanel;
    private Replacer replacer;
    private List<File> fromFiles;
    private FileRenamerManager frm;
    private FileRenamerFrame frame;
    Map<File, File> result;
    private List<File> toFiles;

    public PreviewDialog(FileRenamerFrame frame, ResourceBundle rb, 
            Replacer replacer, List<File> files) {
        this.rb = rb;
        this.replacer = replacer;
        this.fromFiles = files;
        this.frm = new FileRenamerManager();
        this.frame = frame;
        setLayout(new MigLayout("", "[grow,fill]", "[grow,fill][]"));
        setTitle(rb.getString(ResourceBundleKey.LABEL_PREVIEW.toString()));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        initComponents();
        pack();
    }

    private void initComponents() {
        fileListPanel = new FileListPanel();
        add(fileListPanel, "wrap, grow");
        buttonPanel = new ButtonPanel();
        add(buttonPanel, "wrap, grow");
    }
    
    class FileListPanel extends JPanel {
        
        private static final long serialVersionUID = 1L;
        private JList fromFileList;
        private JList toFileList;
        private DefaultListModel fromListModel;
        private DefaultListModel toListModel;
        
        public FileListPanel() {
            setLayout(new MigLayout("", "[grow,fill][grow,fill]", "[][grow,fill]"));
            setBorder(BorderFactory.createEtchedBorder());
            initComponents();
        }
        
        private void initComponents() {
            add(new JLabel(rb.getString(ResourceBundleKey.LABEL_TO.toString())));
            add(new JLabel(rb.getString(ResourceBundleKey.LABEL_FROM.toString())), "wrap");

            fromListModel = new DefaultListModel();
            fromFileList = new JList(fromListModel);
            fromFileList.setBorder(BorderFactory.createEtchedBorder());
            JScrollPane fromScrollPane = new JScrollPane(fromFileList);
            add(fromScrollPane);

            toListModel = new DefaultListModel();
            toFileList = new JList(toListModel);
            toFileList.setBorder(BorderFactory.createEtchedBorder());
            JScrollPane toScrollPane = new JScrollPane(toFileList);
            add(toScrollPane);
            
            toFiles = new ArrayList<File>();
            result = frm.preview(replacer, fromFiles);
            for (Entry<File, File> entry : result.entrySet()) {
                fromListModel.addElement(entry.getKey());
                toListModel.addElement(entry.getValue());
                toFiles.add(entry.getValue());
            }
        }
    }
    
    class ButtonPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private JButton okButton;
        private JButton cancelButton;

        public ButtonPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout());
            initComponents();
        }
        
        private void initComponents() {
            okButton = new JButton(rb.getString(ResourceBundleKey.LABEL_OK.toString()));
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frm.rename(result);
                    frame.getFileListPanel().updateFiles(toFiles.toArray(new File[toFiles.size()]));
                    dispose();
                }
            }); 
            add(okButton);
            
            cancelButton = new JButton(rb.getString(ResourceBundleKey.LABEL_CANCEL.toString()));
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PreviewDialog.this.setVisible(false);
                }
            }); 
            add(cancelButton);
        }
    }
}
