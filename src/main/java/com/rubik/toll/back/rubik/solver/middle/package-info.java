/**
 * 魔方中层还原包，包含了完整的中层还原算法实现。中层还原是魔方复原的第二阶段，在底层完成后进行：
 *
 * <h2>中层棱块还原（MiddleLayerSolver）</h2>
 * 在完成底层还原后，需要将中层的四个棱块放置到正确位置。这个过程通过以下步骤完成：
 *
 * <h3>1. 棱块定位</h3>
 * <ul>
 *   <li>在顶层寻找目标棱块（非黄色面的棱块）</li>
 *   <li>将棱块旋转到目标位置的正上方</li>
 *   <li>判断棱块的朝向，选择合适的公式</li>
 * </ul>
 *
 * <h3>2. 棱块插入</h3>
 * <ul>
 *   <li>对于左插入使用：U' L' U L U F U' F'公式</li>
 *   <li>对于右插入使用：U R U' R' U' F' U F公式</li>
 *   <li>如果目标位置已被占用，先将错误棱块取出到顶层</li>
 * </ul>
 *
 * <h3>3. 特殊情况处理</h3>
 * <ul>
 *   <li>处理被卡在错误位置的棱块</li>
 *   <li>使用基本公式将其取出到顶层</li>
 *   <li>重新放置到正确位置</li>
 * </ul>
 *
 * 中层还原的关键在于正确识别棱块的目标位置和选择合适的插入公式。通过仔细观察棱块颜色与中心块的关系，
 * 可以准确判断应该使用左插入还是右插入公式。整个过程需要注意不破坏已经还原的底层结构。
 */
package com.rubik.toll.back.rubik.solver.middle;