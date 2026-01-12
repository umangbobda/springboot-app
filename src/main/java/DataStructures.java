import java.util.*;

public class DataStructures {
	
	/**
	 * Data Structures in Java
	 * 
	 * String
	 * Array
	 * ArrayList
	 * LinkedList
	 * Queue
	 * PrioriyQueue -- used for heaps
	 * Stack
	 * Map - HashMap and TreeMap
	 * Set - HashSet and TreeSet
	 * Heap
	 * Sorting
	 * Additional Data Structures - Follow the link https://www.interviewcake.com/data-structures-reference
	 * 
	 * Binary tree
	 * Binary Search Tree
	 * Graph
	 * Trie
	 *  
	 *  Algos
	 *  DFS
	 *  BFS
	 *  Binary Search
	 */
	
	public static void main (String args[]) {
		
		//Utilities
		
		int max = Integer.MAX_VALUE;
		int min = Integer.MIN_VALUE;
		Integer.max(max, min);
		Integer.valueOf(min);
		Integer.valueOf("1234");
		String.valueOf(2);

		
		Character.isLetter('c');
		Character.isDigit('3');
		Character.isLetterOrDigit('w');
		Character.isUpperCase('c');
		
		double dbl = 123.34;
		
		Math.abs(max);
		Math.ceil(dbl);
		Math.floor(dbl);
		Math.max(max, min);
		Math.min(max, min);
		double randomNum = Math.random();
	
		
		
		//String and String builder
		
		int pos = 0;
		int beginIndex=0;
		int endIndex = 4;
		int array[] = {'1','2','3'};
		char c[] = new char[] {'a','b','c'};
		String.valueOf(c);
		
		String str = "abc";
		str.toCharArray(); //get char array of a String
		str.charAt(pos); //get a char at the specific index
		str.length(); //string length
		str.substring(beginIndex); 
		str.substring(beginIndex, endIndex); // from begin to endIndex-1
		str.compareTo("hello");
		str.equals("abc");
		str.equalsIgnoreCase(str);
		str.indexOf('a');
		str.indexOf("abc");
		str.isBlank(); // if string is empty or contains white spaces
		str.isEmpty(); // iff string length is zero
		str.split("ab*");
		str.toLowerCase(); str.toUpperCase();
		str.lastIndexOf("a");
		str.replaceAll(".","");
		
		StringBuilder stb = new StringBuilder(str);
		stb.reverse();
		stb.append('d'); stb.append("qwer");
		stb.toString();
		stb.insert(0,"a");
		
		int num = Integer.valueOf("40");//string to integer
		String.valueOf(22);//integer to string
		
		Arrays.toString(c); //convert char array to string
		
		
		//Arrays
		int arr[] = new int[10];
		int arrVal[] = {1,2,3};
		int arr2d[][] = new int [5][5];
		int rows = arr2d.length;
		int cols = arr2d[0].length;
 		String arr2[] = new String[] {"a","b"};
		List<String> listStr = Arrays.asList(arr2);
		Arrays.sort(arr);
		int len = arr.length;
		
		
		
		//Lists
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> list2 = Arrays.asList(1, 3, 5, 7, 9);
		List<Integer> list3 = List.of(1,2,3); // returns unmodifiable list , max 10
		int index = 0;
		
		list.add(1);
		list.add(0,2); // adding at index 0;
		list.get(index);
		list.set(index, 3);
		list.indexOf(1);
		list.sort((a,b) -> (a-b));
		list.clear();
		list.isEmpty();
		list.size();
		list.contains(1);
		list.remove(index);
		Object arr3[] = list.toArray();
		int[] arr4 = list.stream().mapToInt(Integer::intValue).toArray();
		Integer arr5[] = list.toArray(new Integer[0]);
		list2.equals(list3);

		Collections.sort(list);
		Collections.sort(list, (a,b) -> b-a); // descending order
		
		
		//Linked List - its doubly linked list used in Queue interface. it implements all of the above functions
		LinkedList<Integer> linkedList = new LinkedList<>();
		//additional methods for linkedlist
		linkedList.addFirst(1);
		linkedList.addLast(1);
		linkedList.push(2);
		
		
		//Queue
		/**
		 * Queue is an Interface
		 * Used in BFS
		 * FIFO
		 */
		Queue<Integer> queue = new LinkedList<>(); 
		queue.add(1); // no push for queue only add
		queue.peek(); // check value
		queue.poll(); // remove the node, null if empty
		queue.isEmpty();
		
		//Prioriy Queue - also a Min Heap by default
		/**
		 * 	PriorityQueue doesn’t permit null.
			We can’t create a PriorityQueue of Objects that are non-comparable
			The head of this queue is the least element with respect to the specified ordering. 
			If multiple elements are tied for the least value, the head is one of those elements — ties are broken arbitrarily.
			Use cases - Scheduling, Dijkstra’s Algorithm, Prim’s Algorithms
			Enqueue and dequeue done in O(log n) using binary heaps
		 */
		int intervals [][] = {{1,2},{3,7}};
		PriorityQueue<int[]> pq1 = new PriorityQueue<>((a,b) -> a[0]-b[0]); //sort by start time
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(); // default comparator with lowest as head ( min heap)
		PriorityQueue<Integer> pq2 = new PriorityQueue<Integer>((a,b) -> b-a); // custom comparator - this makes it Max Heap
		pq.add(1); // no push for queue only add
		pq.peek(); // check value
		pq.poll(); // remove the node, null if empty
		pq.isEmpty();
		
		
		//Stacks - LIFO
		Stack<Integer> stack = new Stack<>(); // using Stack class is old way Use Dequeue
		stack.push(1); // adding at top of the stack
		stack.peek(); // get the val
		stack.pop(); // remove the val;
		stack.isEmpty();
		stack.size();
		
		Deque<Integer> stack2 = new ArrayDeque<Integer>(); // methods are same as above
		
		//Maps - HashMap, TreeMap, LinkedHashMap
		Map<String, Integer> hashMap = new HashMap<String, Integer>();
		Map<String, Integer> hashMap2 = new HashMap<String, Integer>(){
												{
													put("a",1);
													put("b",2);
												}
											};
		Map<String, String> map = Map.of("key1","value1", "key2", "value2"); // immutable maps , max 10
		String key = "a";
		hashMap.put("c", 3);
		hashMap.clear();
		hashMap.get(key);
		hashMap.size(); 
		hashMap.isEmpty();
		hashMap.containsKey(key);
		hashMap.keySet();
		hashMap.values();
		hashMap.entrySet();
		hashMap.remove(key);
		//equals check
		hashMap.equals(hashMap);
		hashMap.getOrDefault(key, 0); // returns default 0 if no key is present
		hashMap.compute(key, (k,v) -> v+=1); // computes the value for the key based on function passed

		Map<String, Integer> treeMap = new TreeMap<String, Integer>(); // sorted map
		Map<String, Integer> treeMap2 = new TreeMap<String, Integer>((a,b) -> a.compareToIgnoreCase(b)); // with comparator
		TreeMap<Integer,Integer> treeMap3 = new TreeMap<>();
		int floorKey = treeMap3.floorKey(10); //get lowerbound key less than 10
		//methods are same as HashMap
		
		Map<String, Integer> linkedHashMap = new  LinkedHashMap<String, Integer>(); // maintians insertion order

		
		//Set HashSet, TreeSet, LinkedHashSet
		Set<Integer> set = new HashSet<Integer>();
		Set<Integer> treeSet = new TreeSet<>(); // sorted set
		Set<Integer> linkedHashSet = new LinkedHashSet<Integer>(); // maintains insertion order
		int key1 = 1;
		
		set.add(1);
		set.remove(2);
		set.size(); set.clear(); set.isEmpty();
		set.contains(key1);
		set.toArray(new Integer[0]);
		
		//Heap
		/**
		 * A binary heap is a binary tree where the smallest value is always at the top.
		 */

		PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>(); // default comparator with lowest as head ( min heap)
		PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>((a,b) -> b-a); // custom comparator - this makes it Max Heap
		
		minHeap.add(1); minHeap.poll(); minHeap.peek(); minHeap.clear(); minHeap.isEmpty(); minHeap.contains(1);

		
		//DFS traverse in a graph
		
		class Tree {

			int n;
			ArrayList<Tree> list;
			HashSet<Tree> set = new HashSet<Tree>();
			
			public Tree(int n){
				this.n = n;

			}
			
			public void DFS(Tree root){
				System.out.print(root.n+"-");
				 set.add(root);
				 if(root.list != null){
					 
					 for(Tree child : root.list){
						 if(!set.contains(child))
							 DFS(child);
					 }
				 }
			}
			
			public void BFS(Tree root) {
				set.clear();
				if(root == null)
					return;
				Queue<Tree> queue = new ArrayDeque<Tree>();
				queue.add(root);
				
				while(!queue.isEmpty()) {
					
					Tree temp = queue.poll();
					set.add(temp);
					if(temp.list != null) {
						for(Tree child: temp.list) {
							if(!set.contains(child))
								queue.add(child);
						}
					}
								
				}
				
			}

			//Binary Search
			public int binarySearch(int nums[], int target) {
				int low = 0;
				int high = nums.length;
				int lowerBound = -1;

				while (low <= high) {
					int mid = (low + high) / 2;
					if (nums[mid] == target) {
						lowerBound = mid;
						high = mid - 1; // for upper low = mid+1
					}

					//below is regular binary search
					else if (nums[mid] > target)
						high = mid - 1;
					else
						low = mid + 1;
				}

				return lowerBound;
			}
			
		}

	}

}
