这是一个基于Java实现的智能游戏解决方案项目，目前包含数字华容道求解器（完全参考的 https://github.com/weiyinfu/pintu ）和魔方求解器两个主要模块。项目采用高效的算法策略，能够快速解决各类复杂的游戏难题。

## 项目特点

- 采用分治策略和高效算法，实现快速求解
- 支持多种游戏类型的解决方案
- 提供详细的解题步骤和状态展示
- 具有完善的可解性判断机制
- 高效的状态追踪和位置更新机制

## 主要功能模块

### 1. 数字华容道求解器

#### 功能特点
- 支持任意尺寸的数字华容道（N×N）求解
- 采用分治策略，逐步解决棋盘各个区域
- 使用黄金操作序列处理特殊情况
- 能够判断棋局是否有解
- 提供详细的移动步骤说明

#### 实现原理
求解器采用分治策略，按以下步骤解决问题：
1. 首先解决左上角区域（n-2行，n-2列）
2. 处理最后两行
3. 处理最后两列
4. 最后解决2x2的小正方形

### 2. 魔方求解器

#### 功能特点
- 支持标准三阶魔方的求解
- 采用层次化解法策略
- 提供详细的还原步骤
- 支持任意有效的初始状态

## 技术实现

- 使用Java语言开发，保证跨平台兼容性
- 采用面向对象设计，代码结构清晰
- 实现高效的状态表示和转换机制
- 使用广度优先搜索（BFS）等算法优化求解过程
- 完善的日志记录和状态展示系统

## 使用示例

### 数字华容道求解
```java
// 创建3x3棋盘
Board board = new Board(new int[]{1, 2, 3, 4, 0, 6, 7, 5, 8});

// 创建求解器实例
NumberPuzzleSolver solver = new NumberPuzzleSolver(board);

// 求解并获取结果
boolean isSolvable = solver.solve();
```

### 魔方求解
```java
// 创建魔方实例
RubiksCube cube = new RubiksCube();

// 创建求解器并求解
RubiksSolver solver = new RubiksSolver(cube);
solver.solve();
```

## 运行环境要求

- JDK 8或更高版本
- Maven 3.6或更高版本

## 注意事项

### 数字华容道
- 输入数组必须包含0到n²-1的数字（n为棋盘边长）
- 0代表空格位置
- 设置了最大步数限制（100000步），超过限制会终止求解

### 魔方
- 确保输入的魔方状态有效
- 遵循标准魔方颜色约定
- 注意旋转操作的正确性
