# License Template

在 IntelliJ IDEA 的项目中选择模板创建 LICENSE 文件。可以参考 <https://choosealicense.com>（英文）或 <http://choosealicense.online>（中文）选择你的模板。

## 安装

在 IDEA 的 Plugin Marketplace 上搜索 “License Template” 安装，或下载 license-template.jar 后本地安装。

## 使用

选择文件夹（或文件），打开弹出菜单→New→LICENSE，选择模板后会在当前文件夹下创建 LICENSE 文件。如果当前文件夹下已存在 LICENSE 文件或选中的是多个文件夹（文件），则不会出现 “LICENSE” 选项。

![image](https://github.com/czy211/picture-library/blob/master/images/License%20Template.png)

默认使用当前年份和用户名替换了部分模板中的 year 和 name。

## 改动日志

### v1.1.0

新增模板：
- BSD 2-Clause License
- BSD 3-Clause License
- Creative Commons Zero v1.0 Universal
- Eclipse Public License 2.0
- GNU General Public License v2.0
- GNU Lesser General Public License v2.1
- GNU Lesser General Public License v3.0

### v1.0.3

修复未选择或多选文件（文件夹）时插件发生错误的 bug。

### v1.0.2

修复使用 “Search EveryWhere” 时插件发生错误的 bug。

### v1.0.1

- 提供英文的插件描述。
- 修改插件的 “since-build”。
- 在 plugin.xml 中添加 dependencies。

### v1.0.0

在项目中快速创建 LICENSE 文件。
