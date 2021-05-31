/*
Text editor GUI class

Author: Nathan "Nathcat" Baines
 */

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TextEditor {

    private static final JFrame root = new JFrame();
    private static final JTextArea editor = new JTextArea();
    private static Scanner inputReader;
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final JMenuBar menuBar = new JMenuBar();
    private static File openFile;

    public static void main(String[] args) {

        // Setup root window
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setTitle("Text Editor");
        root.setSize(new Dimension(500, 500));

        // Setup text editor component
        editor.setFont(new Font("Monospaced", Font.PLAIN, 18));
        editor.setLineWrap(true);

        root.add(editor);

        // Setup menu bar
        JMenu fileMenu = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem newFile = new JMenuItem("New");

        menuBar.setForeground(Color.WHITE);

        save.addActionListener(e -> {
            try {
                saveFile();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        open.addActionListener(e -> {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showOpenDialog(root);
            File file = fileChooser.getSelectedFile();
            openFile(file);
        });

        newFile.addActionListener(e -> {
            newFile();
        });

        fileMenu.add(save);
        fileMenu.add(open);
        fileMenu.add(newFile);

        menuBar.add(fileMenu);

        root.setJMenuBar(menuBar);

        // Show the window
        root.setVisible(true);
    }

    private static void openFile(File file) {
        // Read the file and build a content string
        try {
            inputReader = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        StringBuilder fileContentBuilder = new StringBuilder();

        while (inputReader.hasNextLine()) {
            fileContentBuilder.append(inputReader.nextLine()).append("\n");
        }

        inputReader.close();

        String fileContent = fileContentBuilder.toString();

        // Write the content to the editor
        editor.append(fileContent);

        // Change the title to include the chosen file name
        root.setTitle("Text Editor - " + file.getName());

        openFile = file;
    }

    private static void saveFile() throws IOException {
        // Write the file
        FileWriter fileWriter = new FileWriter(openFile.getPath());
        fileWriter.write(editor.getText());
        fileWriter.flush();
        fileWriter.close();

        JOptionPane.showMessageDialog(root, "File " + openFile.getName() + " has been saved.");
    }

    private static Object newFile() {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(root);
        try {
            if (!fileChooser.getSelectedFile().isDirectory()) {
                JOptionPane.showMessageDialog(root, "Please select a directory to place the new file in.");
                return null;
            }

            String fileName = JOptionPane.showInputDialog(root, "Enter the name of the new file.");

            File file = new File(fileChooser.getSelectedFile().getPath() + File.separator + fileName);

            boolean returnVal2 = file.createNewFile();

            openFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
