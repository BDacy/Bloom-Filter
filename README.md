# Bloom-Filter
布隆过滤器的java实现

本项目参考两篇论文实现了BloomFilters的两种扩展
分别是
1. A_Shifting_BloomFilter
2. Spatial Bloom Filters

在下方连接给出了论文和这两种扩展布隆过滤器的介绍原理和实现
https://uestc.feishu.cn/drive/folder/fldcnFok8uqOf6QMUXSAm7MMgkg?from=space_shared_folder

对于上述两种bloomFilters 的简单改进代码在该项目的另一个分支中。

## 对于使用
如果想要使用的话，可以参考每种Filters对应的单元测试文件进行使用，

MockAndComPareTest中的测试数据相对较大，是随机生成的，生成不同分布的测试数据的代码存放在DataProvider模块中；
可以自己修改产生的数据量和测试的数据量，需要考虑内存的大小和磁盘的大小。

后续会放一个简单较为粗糙的项目介绍视频链接在下面：
