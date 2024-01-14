package util;

import java.util.*;

/**
 * 提供生成token的一些静态方法。
 * @author 贾聪毅
 */
public class TokenHelper {
    static final Random random = new Random();

    /**
     * 根据已有数据生成一个不重复的int类型的token。
     * @param existingInts 已经存在的token枚举集合。
     * @return 非重复的随机token。
     */
    public static int generateNonRepeatedRandomInt(Iterator<Integer> existingInts)
    {
        int result = random.nextInt();
        while (enumerationContains(existingInts,result))
        {
            result = random.nextInt();
        }
        return result;
    }

    /**
     * 判断迭代对象是否包含某个值。
     * @param iterator 枚举器。
     * @param value 要检测的值。
     * @return 包含返回true，否则返回false。
     * @param <T> 要比对的值类型。
     */
    static <T> boolean enumerationContains(Iterator<T> iterator, T value)
    {
        while (iterator.hasNext())
        {
            T currentValue = iterator.next();
            if(currentValue.equals(value)) return true;
        }
        return false;
    }
}
