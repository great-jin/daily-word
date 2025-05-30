<h1 align="center">Daily Word</h1>

## 一、系统介绍

「每日一词」是一款单词背诵的应用，与常见的单词软件不同，「每日一词」更倾向于协作，最大的特点即以游戏的视角打开英语的大门。

持续性学习对许多人而言并不是一个简单的事物，与其由程序当地起监督的角色，何不让朋友亦或是陌生人与你共同寓教于乐。

在「每日一词」中，单词背诵不在局限于自定的任务计划，而是以对局匹配的形式存在。

换而言之，你可以像游戏匹配一样，但对局的内容不再是游戏战斗而是单词背诵，这或许又另一层面的战斗。

针对不同的单词，系统设定了不同的对局类型，当然获胜所取得的积分也各不相同。系统以月为统计分割线，记录所有用户的答题积分并以排行榜形式展示。

显而易见，「每日一词」更侧重于多人协作，如果你喜欢单打独斗？没问题，系统同样也支持单人硬行，当然所取得结果也将仅你可见。



## 二、效果展示
「每日一词」应用效果图如下：
<table border="0" style="border: none;">
    <tr>
      <td><img src="/document/img/1.png"></td>
      <td><img src="/document/img/2.png"></td>
      <td><img src="/document/img/3.png"></td>
    </tr>
    <tr>
      <td><img src="/document/img/4.png"></td>
      <td><img src="/document/img/5.png"></td>
      <td><img src="/document/img/6.png"></td>
    </tr>
</table>



## 三、架构说明
「每日一词」基于 `Spring Boot 3.x` 以及 `Vue(Element-Plus)` 开发设计，经典 `RESTFUL` 模式的前后端分离系统。

系统核心功能包含下述四大模块：

### 1. 权限认证
基于 `Spring Security` 设计的认证管理模块，包含注册登录功能，实现系统资源访问的权限控制。

同时，实现了用户多端同时在线限制，当新登录会话建立，其它所有活动会话将会置为无效，避免同一用户多端匹配冲突。


### 2. 对象存储
对象存储模块其核心为系统用户头像提供服务，零依赖实现带认证以及过期时间的图片外链访问能力。

对于用户上传的头像，通过 `jpeg` 格式以及压缩方式降低了服务压力，使得在集成部署的前提下便能满足系统的图片访问需求。


### 3. 匹配模块
匹配模块也是整个系统的核心，也是「每日一词」所突出的一大特点。

在设计上为了达到实时的匹配，采用 `Netty Websocket` 作为匹配服务核心，得益于 `Netty` 强大的性能，为系统的对局匹配功能提供了可靠保障。

针对系统中的超时匹配连接，系统将在五分钟后自动关闭匹配，同时对于过长未提交对局的挂机用户将由后台通过定时任务定时关闭。

对于匹配数据的存储，系统基础数据以 `MySQL` 为介质存储，而对于用户答题数据，则基于 `MongoDB` 实现存储。


### 4. 翻译引擎
在答题匹配之外，通过 `AI` 模型支持了多种语言的互译功能。

翻译模型服务以 `API` 形式为「每日一词」提供服务，引擎服务同样完全开源，代码仓库: [translation-engine](https://github.com/great-jin/translation-engine)。



## 四、部署手册
### 1. 环境准备
如果你想自行部署，在开始前需要准备下述环境配置：

- JDK 17
- Node.js
- Nginx(可选，用于线上部署)
- MySQL 数据库
- MongoDB 数据库

在准备完 `MySQL` 之后，需要执行对应的初始化脚本，脚本内容位于 `document/sql`，在 `DML.sql` 中包含默认管理员账号：`Budai/123`。


### 2. 本地启动
#### Ⅰ) 后端服务
后端服务基于 `Spring Boot 3.x` 开发，因此 `JDK` 版本需为 `17` 或以上。

在数据库等环境准备完成之后，修改 `daily-word-client` 模块下的 `application-dev.yml` 配置文件，替换其中的数据库连接信息。

除基本数据库连接信息之外，配置文件涉及下述几处需进行调整：

- **template.email**: 配置注册以及修改密码邮件模板，文件在 `resources/template` 目录下;
- **resource**: 配置对局词典文件路径，文件在 `resources/dict` 目录下;
- **oss.home**: 配置 `OSS` 文件目录;
- **engine**: 配置翻译引擎 `API` 服务信息;

完成上述配置后即可运行 `DailyWordWebApplication` 启动后端服务，服务将运行于 `9056` 与 `9057` 端口。


#### Ⅱ) 前端服务
安装 `Node.js` 环境之后在 `daily-word-web` 模块下执行下述命令下载依赖及启动服务，默认端口为 `8080`。
```bash
npm install

npm run serve
```


### 3) 线上部署
#### Ⅰ) 后端服务
基本配置与本地启动同理，此处仅介绍如何部署，在项目根目录执行下述命令打包后端工程：
```bash
mvn package -Pprod 
```

命令执行完毕之后将在 `daily-word-client` 模块 `target` 目录下生成可执行 `jar` 文件。

将可执行文件上传至服务器，在同级路径下新建 `config` 目录，并将下述配置文件上传至此目录：

- `daily-word-auth` 中的 `application-auth.yml`;
- `daily-word-client` 中的 `application-prod.yml`;

完成后将工程 `document/bash` 下启停脚本置于 `jar` 可执行文件同级目录，通过下述命令授权：
```bash
chmod +x start.sh

chmod +x stop.sh
```

完成授权后便可通过脚本启停服务，注意服务器防火墙需放行 `9056` 与 `9057` 端口。
```bash
./start.sh

./stop.sh
```


#### Ⅱ) 前端服务
修改 `daily-word-web/.env.production` 配置文件，将其中 `IP` 地址替换为目标服务 `IP`。

完成后通过下述命令打包，完成后将在 `daily-word-web` 模块下生成 `dist` 目录，上传至服务器指定目录。
```bash
npm build
```

打包生成为的文件为静态资源文件，需要通过 `Nginx` 实现服务访问。

安装 `Nginx` 后将 `document/nginx/nginx.conf` 替换至 `Nginx` 配置目录 `conf` 下。

修改 `nginx.conf` 中的 `root` 目录为实际 `dist` 目录上传路径，同时替换其中 `IP` 地址。
