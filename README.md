# 项目名称: 翱翔清单

## 项目简介
翱翔清单是一款基于 Nuxt.js 3 的待办清单软件，旨在为用户提供简便而强大的任务管理工具。支持用户进行任务的添加、编辑、删除以及统计等功能，同时提供番茄时钟功能来帮助用户更好地管理时间。

## 团队简介
终南一梦组是由来自软件学院2022级的同学们组成的开发小组。

组长：**刘黎可**

前端：**刘黎可**、徐薏茜、韩紫琪、于李源、傅钰

后端：**贾聪毅**、李振国

服务端：**张书畅**

## 项目结构
### 前端结构
```bash
|-- README.md                 # 项目文档
|-- api                       # 存放 API 相关配置
|-- app.vue                   # Nuxt.js 应用的根组件
|-- build                     # 编译配置文件
|-- components                # 存放 Vue 组件
|-- composables               # 存放可复用的逻辑模块
|-- layout                    # 存放布局组件
|-- node_modules              # 存放项目依赖的 Node.js 模块
|-- nuxt.config.ts            # Nuxt.js 配置文件
|-- package-lock.json         # npm 依赖版本锁定文件
|-- package.json              # 项目的 npm 包配置文件
|-- pages                     # 存放页面组件
|-- plugins                   # 存放插件
|-- public                    # 存放静态文件
|-- server                    # 存放服务器相关代码
|-- static                    # 静态文件目录，会被复制到输出目录
|-- store                     # Vuex 状态管理
|-- tsconfig.json             # TypeScript 配置文件
`-- yarn.lock                 # yarn 依赖版本锁定文件
```
### 后端结构
```bash
|-- references                # 存放 Java 依赖的 JAR 文件
|   |-- jackson-annotations-2.15.2.jar
|   |-- jackson-core-2.15.2.jar
|   |-- jackson-databind-2.15.2.jar
|   `-- jackson-datatype-jsr310-2.15.2.jar
`-- src                       # Java 源代码目录
    |-- Main.java             # 后端主程序入口
    |-- shared                # 存放共享的 Java 类
    |-- statistics            # 存放与统计功能相关的代码
    |-- sys                   # 存放系统管理相关代码
    |-- trans                 # 存放事务处理相关代码
    |-- unittest              # 单元测试代码
    |-- user                  # 存放用户管理相关代码
    `-- util                  # 存放工具类
```
### 服务端结构
```bash
`-- apache-tomcat-10.1.17      # Apache Tomcat 服务器
|-- BUILDING.txt           # 构建说明
|-- CONTRIBUTING.md        # 贡献指南
|-- LICENSE                # 许可证文件
|-- NOTICE                 # 注意事项
|-- README.md              # 服务器文档
|-- RELEASE-NOTES          # 发行说明
|-- RUNNING.txt            # 运行说明
|-- bin                    # 存放可执行文件
|-- conf                   # 存放配置文件
|-- lib                    # 存放 Tomcat 的 Java 类库
|-- logs                   # 存放日志文件
|-- temp                   # 存放临时文件
`-- webapps                # 存放 Web 应用程序
```

## 部署方式
### 前端部署
1. 克隆项目仓库：`git clone https://github.com/Lozumi/AoxiangTodo.git`
2. 进入前端目录：`cd ./front-end/`
3. 安装依赖：`npm install`
4. 运行项目：`npm run dev`
5. 通过浏览器访问：`http://localhost:3000`

### 后端部署
1. 克隆后端项目仓库：`git clone https://github.com/Lozumi/Backend.git`
2. 进入后端目录：`cd ./back-end/`
3. 编译并运行：`javac -cp .:./references/* src/Main.java && java -cp .:./references/*:src Main`
4. 后端服务将在默认端口启动。

### 服务端部署
1. 下载 [Apache Tomcat](http://tomcat.apache.org/) 并解压缩。
2. 将后端编译生成的 `.war` 文件拷贝到 Tomcat 的 `webapps` 目录。
3. 启动 Tomcat 服务：进入 Tomcat 的 `bin` 目录，执行 `./catalina.sh run`（Linux/Mac）或 `catalina.bat run`（Windows）。

## 使用方式
1. 访问项目首页：`http://localhost:3000`
2. 如未登录，系统自动跳转到登陆页面，按提示进行注册或登录。
3. 在侧边导航栏中选择相应功能模块，如最近待办、番茄时钟、数据统计等。
4. 根据页面提示和操作进行任务管理、番茄时钟等操作。
5. 注销时，点击右上角的注销按钮即可退出登录。

## 项目仓库
[https://github.com/Lozumi/AoxiangTodo](https://github.com/Lozumi/AoxiangTodo)

## 问题反馈
如果在使用过程中遇到任何问题，请前往项目仓库的 Issues 页面提出，我们将尽快解决。