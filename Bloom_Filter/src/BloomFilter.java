import java.util.BitSet;

/**
 * @BelongsPackage: PACKAGE_NAME
 * @Author: yca
 * @CreateTime: 2022-10-24  22:55
 * @Description:
 *      布隆过滤器
 */
public class BloomFilter {
    // bloomFilter 的位图大小
    private static final int Default_BitSize = 100000;

    // 哈希函数种子的提供数组，数组的大小就是哈希函数的数量
    private static final int[] Default_seeds = new int[]{3,5,7,11,13,17,19};

    // 使用BitSet数据结构来当作过滤器的位图
    private static BitSet bitSet;

    // 哈希函数组
    private final HashFunction[] hashFunctions;

    public BloomFilter(HashFunction[] hashFunctions) {
        this.hashFunctions = hashFunctions;
    }
}