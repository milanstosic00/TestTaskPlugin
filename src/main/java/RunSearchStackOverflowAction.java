import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class RunSearchStackOverflowAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        String selectedText = getSelectedText(project);
        System.out.println("Selected Text: " + selectedText);

        String url = "https://stackoverflow.com/search/?q=" + Utils.queryMaker(selectedText);

        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException ioException) {
            Messages.showMessageDialog("Failed to open browser.", "Error", Messages.getErrorIcon());
        }
    }

    private static String getSelectedText(Project project) {
        ToolWindow runToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Run");

        if (runToolWindow == null || !runToolWindow.isVisible()) return "";
        System.out.println("Tool window found");

        ContentManager contentManager = runToolWindow.getContentManager();
        for (Content content : contentManager.getContents()) {
            System.out.println("Inspecting content: " + content.getDisplayName());

            JComponent component = content.getComponent();

            ConsoleView consoleView = findConsoleViewImpl(component);
            if (consoleView instanceof ConsoleViewImpl) {
                System.out.println("ConsoleViewImpl found");
                Editor editor = ((ConsoleViewImpl) consoleView).getEditor();
                if (editor != null) {
                    return editor.getSelectionModel().getSelectedText();
                }
            }
        }
        return "";
    }

    private static ConsoleView findConsoleViewImpl(Component component) {
        if (component instanceof ConsoleViewImpl) {
            return (ConsoleViewImpl) component;
        } else if (component instanceof JComponent) {
            for (Component child : ((JComponent) component).getComponents()) {
                ConsoleView result = findConsoleViewImpl(child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
