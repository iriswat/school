/* * * * * * *
 * Module for creating and manipulating binary min-heaps on integer indices
 *
 * created for COMP20007 Design of Algorithms 2013
 * by Andrew Turpin
 * modified by Matt Farrugia <matt.farrugia@unimelb.edu.au> and Un Leng Wat 
 */

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "heap.h"



// HELPER FUNCTIONS

// swap two heap items (the items at index i and index j)
// assumes h != NULL
void swap(Heap *h, int i, int j) {
	HeapItem t  = h->items[i];
	h->items[i] = h->items[j];
	h->items[j] = t;
}




// returns a pointer to a new, empty heap with a capacity for maximum_size items
Heap* new_heap(int size) {
	Heap *h = malloc(sizeof *h);
	assert(h);
	h->items = malloc(size * (sizeof *h->items));
	assert(h->items);
	h->cur_size = 0;
	h->max_size = size;
	return h;
}

// de-allocate all memory used by h
void free_heap(Heap *h) {
	assert(h != NULL);

	free(h->items);
	free(h);
}


// return the data of the smallest key in h
int heap_peek_min(Heap *h) {
	assert(h != NULL);
	assert(h->cur_size > 0);
	return h->items[0].data;
}


// move an item up the heap structure to re-establish heap order
void siftup(Heap *h, int i) {
	assert(h != NULL);
	int parent = (i - 1) / 2;
	while (h->items[i].key < h->items[parent].key) {
		swap(h, parent, i);
		i = parent;
		parent = (i - 1) / 2;
	}
}

// inserts data into heap with priority key
void heap_insert(Heap *h, float key, int data) {
	assert(h != NULL);
	assert(h->cur_size < h->max_size);

	h->items[h->cur_size].key  = key;
	h->items[h->cur_size].data = data;
	h->cur_size++;
	siftup(h, h->cur_size-1);
}



// return the key of the item with the smallest key in h
float heap_peek_key(Heap *h) {
	assert(h != NULL);
	assert(h->cur_size > 0);
	return h->items[0].key;
}



// find the index of the smallest child among the children of an item
// if the item has no children, return 0
int min_child(Heap *h, int i) {
	assert(h != NULL);
	int child = i * 2 + 1;
	if (child >= h->cur_size) {
		// no children
		return 0;
	} else if (child+1 >= h->cur_size || h->items[child].key < h->items[child+1].key) {
		// only child or first child is smallest child
		return child;
	} else {
		// second child exists and is smallest child
		return child+1;
	}
}

// move an item down the heap structure to re-establish heap order
void siftdown(Heap *h, int index) {
	assert(h != NULL);
	int child = min_child(h, index);
	while (child && h->items[child].key < h->items[index].key) {
		swap(h, child, index);
		index = child;
		child = min_child(h, index);
	}
}


// sort the heap and print the data in an descending order 
void heap_sort(Heap *h, int k) {
	assert(h != NULL);
	assert(h->cur_size > 0);
	// swap the root and the last node and then heapify
	while (h->cur_size > 0) {
		swap(h, 0, (h->cur_size)-1);
		h->cur_size -= 1;
		siftdown(h, 0);
	}
	h->cur_size = k;
	int i;
	for (i=0; i<k; i++){
		if (h->items[i].key > 0.0){
			printf("%6d %.6f\n", h->items[i].data, h->items[i].key);
		}
	}
}

// remove the item with the smallest key in h
void heap_remove_min(Heap *h) {
	assert(h != NULL);
	assert(h->cur_size > 0);

	h->items[0] = h->items[h->cur_size-1];
	h->cur_size -= 1;
	siftdown(h, 0);

}



// TESTING FUNCTIONS

// print the contents of a heap to stdout
void print_heap(Heap *h) {
	assert(h != NULL);

	int i;

	printf("printing heap\n-------------\n");
	printf("heap array:\n");
	printf("   i: ");
	for (i = 0; i < h->cur_size; i++) {
		printf("%5d ", i);
	}
	printf("\n");
	printf("data: ");
	for (i = 0; i < h->cur_size; i++) {
		printf("%5d ", h->items[i].data);
	}
	printf("\n");
	printf("keys: ");
	for (i = 0; i < h->cur_size; i++) {
		printf("%5.6f ", h->items[i].key);
	}

	printf("\n");
}
