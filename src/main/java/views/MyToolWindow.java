package views;

import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MyToolWindow {
    private JPanel myToolWindowContent;

    public MyToolWindow(@NotNull ToolWindow toolWindow) {
        myToolWindowContent = new JPanel();
        myToolWindowContent.setLayout(new BoxLayout(myToolWindowContent, BoxLayout.Y_AXIS));
        String[] inputArray = {"Host", "Token"};

        // Add JTextField
        for (int i = 0; i < inputArray.length; i++) {
            JLabel label = new JLabel(inputArray[i]);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            myToolWindowContent.add(label);

            JTextField textField = new JTextField();
            textField.setName(inputArray[i]);
            Dimension preferredSize = new Dimension(250, 30);
            textField.setPreferredSize(preferredSize);
            textField.setMaximumSize(preferredSize);
            textField.setAlignmentX(Component.CENTER_ALIGNMENT);
            myToolWindowContent.add(textField);
        }

        // add Button
        JButton button = new JButton("Submit");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        myToolWindowContent.add(button);

        // if user click button get text from JTextField
        button.addActionListener(e -> {
            Component[] components = myToolWindowContent.getComponents();
            for (Component component : components) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    // if text field is empty show error message
                    if (textField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields");
                        return;
                    }

                    // if label is Host get text from JTextField and show it in message
                    if (textField.getName().equals("Host")) {
                        JOptionPane.showMessageDialog(null, textField.getText());
                    }

                    // if label is Token get text from JTextField and show it in message
                    if (textField.getName().equals("Token")) {
                        JOptionPane.showMessageDialog(null, "Token:" + textField.getText());
                    }
                }
            }
        });

    }

    public JComponent getContent() {
        return myToolWindowContent;
    }
}
