package school.xauat.eslearnapi;

import org.springframework.core.annotation.AnnotationUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：zsy
 * @date ：Created 2021/6/7 23:01
 * @description：
 */
public class Main {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.change(5, new int[]{1, 2, 5});
        System.out.println(solution.count);
    }

     static class Solution {
        int count = 0;
        int[] coins;
        public int change(int amount, int[] coins) {
            this.coins = coins;
            dfs(amount, 0);
            return count;
        }

        public void dfs(int curAmount, int index) {
            if(curAmount == 0) {
                count++;
                return;
            }
            if(index >= coins.length) return;
            int coin = coins[index];
            for(int i = 0; i * coin <= curAmount; i++) {
                int nextAmount = curAmount - i * coin;
                System.out.println(i + " " + coin);
                dfs(nextAmount, index + 1);
            }
        }
    }
}
