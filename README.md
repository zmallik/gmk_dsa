

[Readme Syntax link](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax).

## union and Find

```
    weight = new int[n];
    parent = new int[n];
        
    void union(int[] edge){
        int ra = root(edge[0]);
        int rb = root(edge[1]);
        if(ra != rb){
            if(weight[rb] < weight[ra]){
                int t = rb;
                rb = ra;
                ra = t;
            }

            parent[ra] = rb;
            weight[rb] += weight[ra];
        }
    }

    int root(int i){
        while(parent[i] != i){
           parent[i] = parent[i];
           i = parent[i];
        }

        return i;
    }
```

time complexity:
First, we must account for the time needed for the initialization of the parent and size arrays, which is equal to O(n).
With the union-by-rank and path compression optimizations, both Find and Union operations take O(1) 
time on average (or a(n) time, where a is the inverse Ackermann function that grows really slowly and is considered practically constant).


[Link](https://www.hackerearth.com/practice/notes/disjoint-set-union-union-find/)


## Graph algorithms

Min Dis between twp nodes in a graph 
1)  Dijkstra’s Algorithm
Source Node: Before starting off with the Algorithm, we need to define a source node from which the shortest distances to every other node would be calculated.
Priority Queue: Define a Priority Queue which would contain pairs of the type {dist, node}, where ‘dist’ indicates the currently updated value of the shortest distance from the source to the ‘node’.
Dist Array: Define a dist array initialized with a large integer number at the start indicating that the nodes are unvisited initially. This array stores the shortest distances to all the nodes from the source node.
Parent Array: This array’s sole purpose is to store the parent of a particular node i.e., the node from where that node came while traversing through the graph by Dijkstra’s Algorithm.


## String algorithms
Z Algorithm

```
    private int[] z_function(String s) {
        int n = s.length(), l = 0, r = 0;
        int[] z = new int[n];
        for (int i = 1; i < n; ++i) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                ++z[i];
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }
```
[CPP](https://cp-algorithms.com/string/z-function.html)
[LC](https://leetcode.com/problems/find-the-occurrence-of-first-almost-equal-substring/solutions/5844897/java-c-python-z-function)
