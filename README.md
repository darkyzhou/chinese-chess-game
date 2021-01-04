# 象棋游戏

由Java编写的联机象棋游戏，主要使用了Java Swing和Gson。功能并不算多，稳定性没有经过进一步打磨，因为本项目的诞生仅仅是为了在元旦假期的几天之内应付一个选修课大作业。

### 功能

- 通过网络和另一个玩家联机游戏
- 为自己的角色选择形象
- 悔棋
- 认输
- 使用聊天框进行文字聊天

### 代码架构

![](https://raw.githubusercontent.com/darkyzhou/chinese-chess-game/main/images/structure.png)

### 游戏截图

![](https://raw.githubusercontent.com/darkyzhou/chinese-chess-game/main/images/1.png)
![](https://raw.githubusercontent.com/darkyzhou/chinese-chess-game/main/images/2.png)
![](https://raw.githubusercontent.com/darkyzhou/chinese-chess-game/main/images/3.png)
![](https://raw.githubusercontent.com/darkyzhou/chinese-chess-game/main/images/4.png)

### 编译运行方法

运行下列指令：

```text
mvn clean package
```

然后运行 `dist/chess-game-1.0-SNAPSHOT.jar` 即可

### 游戏素材

- 棋盘图片、棋子图片、以及胜利或失败的图片均来源于网络，本人并不是作者
- 角色立绘来源于《三国杀》桌游，本人并不是作者
