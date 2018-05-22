/* * * * * * * *
 * Module for using an existing inverted file index for a multi-word query to 
 * find the top-matching documents in a large document collection
 *
 * created for COMP20007 Design of Algorithms - Assignment 1, 2018
 * by Un Leng Wat (860803) on 03/04/2018 for COMP20007 Assignment 1
 */

#include <stdio.h>
#include <assert.h>
#include "query.h"
#include "list.h"
#include "heap.h"

// Use min-heap to find the max n_results total scores and their associated 
// doc id 
void kthLargest(float* arr, int arr_length, int k);
// Given a doc_id, find in the doclists priority queue to see whether if that id
// appear in the queue for more than once. If yes, return the the accumulated
// extra score and update the doclists priority queue
float other_score(Heap* doc_h, int current_doc_id, Index* index, int* count);



// see Assignment spec: 'Task 1: Array-based accumulation approach'
void print_array_results(Index *index, int n_results, int n_documents) {

	// 1. Initialize an array of n_documents floating point numbers
	float score_list[n_documents];
	int i;
	for (i=0; i<n_documents; i++){
		score_list[i] = 0.0;
	}
	
	// 2. Iterate through each document list for every term in the index and add
	// the document score to the corresponding position in the array 

	for (i=0; i < index->num_terms; i++){
		
		Node *doclists_head = index->doclists[i]->head;		
		// traverse every doc through the doclist for every term 
		while (doclists_head != NULL){
			Document* doc = doclists_head->data;
			score_list[doc->id] += doc->score;
			doclists_head = doclists_head->next;
		}	
	}
	
	// 3. Use min-heap to find the max n_results total scores and their 
	// associated doc id 
	kthLargest(score_list, n_documents, n_results);

}

// Use min-heap to find the max n_results total scores and their associated 
// doc id 
void kthLargest(float arr[], int arr_length, int k){
    
    // Build a min-heap for the first k elements
    Heap *h = new_heap(k);
	int i;
	for (i=0; i<k; i++){
		heap_insert(h, arr[i], i);
	}
	

    // for the remaining (n-k) elements, if it is larger than the root of the
    // min-heap then the root will be replaced by it and then heapify the new 
    // root. Else, just move on. 
    int j;
    for (j=k; j<arr_length; j++){
    	// if = ?
        if (arr[j] > heap_peek_key(h)){
        	h->items[0].key = arr[j];
        	h->items[0].data = j;
        	siftdown(h, 0);
        }
    }

    // Sort and traverse the whole min-heap to get the kth largest number and 
    // their associated doc id 
    heap_sort(h, k);
    free_heap(h);
}



// see Assignment spec: 'Task 2: Priority queue-based multi-way merge approach'
void print_merge_results(Index *index, int n_results) {
	
	// Build a priority queue for the kth Largest Scores, ordered by the score 
	// and doc_id as data
	Heap* score_h = new_heap(n_results);
	

	// Build a priority queue for the terms’ doclist, ordered by the first 
	// doc_id of each doclist and the term number (the index of the term in
	// the term array) as data
	Heap* doc_h = new_heap(index->num_terms);
	int i;
	for (i=0; i<index->num_terms; i++){
		Document* first_doc = index->doclists[i]->head->data;
		float first_id = (float)first_doc->id;
		heap_insert(doc_h, first_id, i);
	}
	int null_num = 0;

	// Insert the score of the first k doc
	for(i=0; i<n_results; i++) {
		int term_index = heap_peek_min(doc_h);
		int current_doc_id = (int)heap_peek_key(doc_h);
		
		// retrieve the document at the front of the priority queue
		float accumulated_score = 0.0;
		Document* doc = index->doclists[term_index]->head->data;
		accumulated_score += doc->score;
		
		// update the doclist priority queue
		index->doclists[term_index]->head=index->doclists[term_index]->head->next;
		doc = index->doclists[term_index]->head->data;
		doc_h->items[0].key = (float)doc->id;
		siftdown(doc_h, 0);	
		
		// check if other term has that doc in their doclist. If yes, add it 
		// into the total score. Else, move on to another doc.
		accumulated_score += other_score(doc_h,current_doc_id, index, &null_num);
		
		//put the accumulated_score into the top score priority queue
		heap_insert(score_h, accumulated_score, current_doc_id);
	}
	
	
	// Calculate the score for the remaining (n-k) elements. If it is larger
	// than the root of the min-heap, replace. If not, move on.
	while (null_num < index->num_terms){
		int term_index = heap_peek_min(doc_h);
		int current_doc_id = (int)heap_peek_key(doc_h);
		
		// retrieve the document at the front of the priority queue
		float accumulated_score = 0.0;
		Document* doc = index->doclists[term_index]->head->data;
		accumulated_score += doc->score;
		
		// update the doclist priority queue
		Document* doc_last = index->doclists[term_index]->last->data;
		//printf("%d %d\n", doc->id, doc_last->id);
		if(doc->id == doc_last->id){
			null_num+=1;
			heap_remove_min(doc_h);
			break;
		}
		index->doclists[term_index]->head = index->doclists[term_index]->head->next;
		doc = index->doclists[term_index]->head->data;
		doc_h->items[0].key = (float)doc->id;
		siftdown(doc_h, 0);	
		
		// check if other term has that doc in their doclist. If yes, add it 
		// into the total score. Else, move on to another doc.
		accumulated_score+=other_score(doc_h,current_doc_id, index, &null_num);

		
		// compare with the root of min-heap
		float min_heap_root = heap_peek_key(score_h);
		if (accumulated_score > min_heap_root){
			score_h->items[0].key = accumulated_score;
        	score_h->items[0].data = current_doc_id;
        	siftdown(score_h, 0);
		}
		
	}


	// heap sort the min-heap and print the largest k scores and their doc id
	heap_sort(score_h, n_results);


	//print_heap(score_h);
	free_heap(doc_h);
	free_heap(score_h);
}

// Given a doc_id, find in the doclists priority queue to see whether if that id
// appear in the queue for more than once. If yes, return the the accumulated
// extra score and update the doclists priority queue
float other_score(Heap* doc_h, int current_doc_id, Index* index, int *count){
	float extra_score = 0.0;
	int i;
	for(i=0; i<index->num_terms; i++){
		if (doc_h->items[0].key == (float)current_doc_id){
			int term_index;
			term_index = doc_h->items[0].data;
			Document* doc = index->doclists[term_index]->head->data;
			extra_score += doc->score;
			
			// update the doclists priority queue
			Document* doc_cur = index->doclists[term_index]->head->data;
			Document* doc_last = index->doclists[term_index]->last->data;
			if(doc_cur->id != doc_last-> id){
				index->doclists[term_index]->head = index->doclists[term_index]->head->next;
				doc = index->doclists[term_index]->head->data;
				doc_h->items[0].key = (float)doc->id;
				siftdown(doc_h, 0);
			}
			else{
				heap_remove_min(doc_h);
				*count += 1;
			}
			
		}

	}
	return extra_score;
}

// 
