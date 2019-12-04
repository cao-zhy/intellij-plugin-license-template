package com.github.czy211.licensetemplate;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.OpenFileAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Objects;

public class TemplateGroup extends ActionGroup {
    public TemplateGroup() {
        // 设置 ActionGroup 的图标
        this.getTemplatePresentation().setIcon(AllIcons.FileTypes.Text);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        String[] types = {
                "Apache License 2.0",
                "BSD 2-Clause License",
                "BSD 3-Clause License",
                "Creative Commons Zero v1.0 Universal",
                "Eclipse Public License 2.0",
                "GNU Affero General Public License v3.0",
                "GNU General Public License v2.0",
                "GNU General Public License v3.0",
                "GNU Lesser General Public License v2.1",
                "GNU Lesser General Public License v3.0",
                "MIT License",
                "Mozilla Public License 2.0",
                "The Unlicense",
                "CC0-1.0",
                "CC-BY-4.0",
                "CC-BY-SA-4.0",
                "SIL Open Font License 1.1"
        };
        Icon icon = AllIcons.FileTypes.Text;

        // 创建 AnAction 数组，对应不同协议类型的 LICENSE
        AnAction[] actions = new AnAction[types.length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new Template(types[i], "Create " + types[i], icon);
        }
        return actions;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // 显示或隐藏 ActionGroup
        e.getPresentation().setEnabledAndVisible(isShown(e.getDataContext()));
    }

    /**
     * 创建LICENSE文件
     *
     * @param fileName 当前弹出右键菜单的文件
     * @param content LICENSE 文件内容
     * @return LICENSE 文件路径
     */
    private String createFile(String fileName, String content) {
        String path;
        File file = new File(fileName);
        // file 是目录时在当前路径下创建 LICENSE 文件，是文件时，在其父目录下创建 LICENSE 文件
        path = (file.isDirectory() ? fileName : file.getParent()) + "/LICENSE";
        try (PrintWriter output = new PrintWriter(path)) {
            output.print(content);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return path;
    }

    /**
     * 替换模板中的 {$YEAR} 和 {$NAME}
     *
     * @param tempContext 模板文件内容
     * @return LICENSE 文件内容
     */
    private String replaceTemplate(String tempContext) {
        int year = LocalDate.now().getYear();
        String name = System.getProperty("user.name");
        return tempContext.replace("{$YEAR}", year + "").replace("{$NAME}", name);
    }

    /**
     * 从模板文件中读取内容
     *
     * @param fileName 模板文件路径
     * @return 模板文件内容
     */
    private String readFromTemplate(String fileName) {
        try {
            InputStream in = Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName))
                    .openStream();
            return StreamUtil.readText(in, "utf-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 是否显示 ActionGroup
     *
     * @param dataContext 数据上下文
     * @return 如果显示 ActionGroup，返回 true，否则返回 false
     */
    private boolean isShown(DataContext dataContext) {
        VirtualFile[] vfs = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        // 没有文件被选中或选中多个文件
        if (vfs == null || vfs.length != 1) {
            return false;
        }
        VirtualFile[] files = vfs[0].isDirectory() ? vfs[0].getChildren() : vfs[0].getParent().getChildren();
        for (VirtualFile f : files) {
            // 当前路径中已存在 LICENSE 文件
            if ("license".equalsIgnoreCase(f.getNameWithoutExtension())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 定义 Action 类
     */
    class Template extends AnAction {
        Template(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text,
                 @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description, @Nullable Icon icon) {
            super(text, description, icon);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
            String tempContext = readFromTemplate("templates/" + this.getTemplateText());
            String content = replaceTemplate(tempContext);
            DataContext dataContext = anActionEvent.getDataContext();
            String fileName = Objects.requireNonNull(PlatformDataKeys.VIRTUAL_FILE.getData(dataContext)).getPath();
            String path = createFile(fileName, content);
            Project project = Objects.requireNonNull(anActionEvent.getProject());
            // 打开 LICENSE 文件
            OpenFileAction.openFile(path, project);
        }
    }
}
