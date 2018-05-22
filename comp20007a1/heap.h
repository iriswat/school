/* * * * * * *
 * Module for creating and manipulating binary min-heaps on integer indices
 *
 * created for COMP20007 Design of Algorithms 2013
 * by Andrew Turpin
 * modified by Matt Farrugia <matt.farrugia@unimelb.edu.au> and Un Leng Wat
 */


typedef struct item HeapItem; 
struct item {
	float key;  // the key for deciding position in heap
	int data;  // the data value associated with this key
};

typedef struct heap Heap;
struct heap {
	HeapItem *items;  // the underlying array
	int cur_size; // the number of items currently in the heap
	int max_size; // the maximum number of items allowed in the heap
};

// returns a pointer to a new, empty heap with a capacity for max_size items
Heap *new_heap(int size);

// inserts data into h with priotiry key
void heap_insert(Heap *h, float key, int data);

// sort the heap and print the data in an descending order  
void heap_sort(Heap *h, int k);

// return the item with the smallest key in h
int heap_peek_min(Heap *h);

// return the key of the item with the smallest key in h
float heap_peek_key(Heap *h);

// de-allocate all memory used by h
void free_heap(Heap *h);

// move an item down the heap structure to re-establish heap order
void siftdown(Heap *h, int index);

// remove the item with the smallest key in h
void heap_remove_min(Heap *h);

// FOR TESTING

// print the contents of a heap to stdout
void print_heap(Heap *h);
