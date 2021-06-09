package school.xauat.eslearnapi;

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
        solution.zigzagLevelOrder(new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7))));

    }

    static class Solution {
        public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            int index = 0;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            List<List<Integer>> res = new ArrayList<>();
            while(!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> list = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    TreeNode cur = queue.poll();
                        queue.offer(cur.left);
                    queue.offer(cur.right);
                    list.add(cur.val);
                    if((index) % 2 != 0) {
                        Collections.reverse(list);
                    }
                }
                System.out.println(list);

                index++;
                res.add(list);
            }
            return res;
        }
    }
    public static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }

        @Override
        public String toString() {
            return "TreeNode{" +
                    "val=" + val +
                    '}';
        }
    }
}
