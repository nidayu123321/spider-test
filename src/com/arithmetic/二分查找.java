package com.arithmetic;

/**
 * @author nidayu
 * @Description: 二分查找的前提必须是有序数组
 * @date 2015/10/14
 */
public class 二分查找 {
    public static int binarySearch(Integer[] srcArray, int des) {
        int low = 0;
        int high = srcArray.length - 1;

        while ((low <= high) && (low <= srcArray.length - 1)
                && (high <= srcArray.length - 1)) {
            int middle = low + ((high - low) >> 1);
            if (des == srcArray[middle]) {
                return middle;
            } else if (des < srcArray[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args){
        Integer[] src = new Integer[]{1, 2, 4, 6, 8, 9, 20, 23, 24, 25, 34, 56, 78};
        int index = binarySearch(src, 23);
        System.out.println(index);
    }
}
