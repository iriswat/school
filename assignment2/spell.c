/*  COMP20007 Assigment 2
 *
 *  Spelling Corrrection
 *
 *  Created by Un Leng Wat (860803) on 15/05/2018
 *  <uwat@student.unimelb.edu.au>
 */
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <time.h>

#include "spell.h"
#include "list.h"
#include "hashtbl.h"

// Maximum number of characters that a word can have
#define MAX_CHAR_LEN 256




/* Helper Function */
// Task 1
int levenshtein_distance(char *word1, char *word2, int m, int n);
int min(int a, int b, int c);
int word_length(char *word);

// Task 2
void print_linked_list(List *linked_list);
char *concatenate_two (char *left, char *right);
char *concatenate_three (char *left, char middle, char *right);
void slicing(char *a, char *b, int start, int end);
void get_all_edit1 (char *word, List *list);

// Task 3
HashTable *dict_to_hashtbl (List *dictionary);

// Task 4
void get_all_edit2 (char *word, List *edit1_list, List *list);
void get_all_edit3 (char *word, List *edit2_list, List *list);
void check_edit_1(char *word, HashTable *hash_table);
void check_edit_2 (char *word, HashTable *hash_table, List *list_edit1);
void check_edit_3 (char *word, HashTable *hash_table, List *edit2_list_raw);
void find_correction (HashTable *hash_table, List *candidate_list);
void union_linked_list(List *list1, List *list2, List *union_list);

/* Task 1: Computing the edit distance (levenshtein distance) between two words
 * Time Complexity: O(m*n) where m and n equal to the lengths of the two words
 */
void print_edit_distance(char *word1, char *word2) {
	// find out the word length of word1 and word2
	int m = 0;
	m = word_length(word1);
	int n = 0;
	n = word_length(word2);
	// compute their levenshtein_distance
	int d = levenshtein_distance(word1, word2, m, n);
	printf("%d\n", d);
}


/* Task 2: Enumerating all possible strings whose levenshtein distance is  
 * within 1 from the input word
 * Time Complexity: O(n)
 */
void print_all_edits(char *word) {
	List *edit1_list = new_list();
	get_all_edit1 (word, edit1_list);
	print_linked_list(edit1_list);
}




/* Task 3: Check the spelling of every word in the input document 
 * Print the word if it is correct. Else, print the word and a question mark 
 * at the end
 */
void print_checked(List *dictionary, List *document) {
	
	// convert the dictionary linked list into a hash table
	HashTable *hash_table = dict_to_hashtbl(dictionary);	
	
	// travserves through document and search them in the hash table
	Node *doc_temp = document->head;
	while (doc_temp != NULL){
		char* data = doc_temp->data;
		if (hash_table_has(hash_table, data)) {
			printf("%s\n", data);
		}
		else {
			printf("%s?\n", data);
		}
		doc_temp = doc_temp->next;
	}
	free_hash_table(hash_table);
}



/* Task 4: Check the spelling of every word in the input document linked list
 * If the word is correct, print it out. If not, check all the words that are 
 * 1 edit distance from it in the dictionary. Pick the word that has the highest
 * occurence probability and print it. If such word does not exist, repeat the 
 * step to check words that are 2 and 3 edit distance away from the input word.
 * If such word still does not exit after looking into words that are 3 edit 
 * distance from the word, print the word with a question mark at the end
 */
void print_corrected(List *dictionary, List *document) {
	
	// convert the dictionary linked list into a hash table
	HashTable *hash_table = dict_to_hashtbl(dictionary);
	
	
	// traverse through the whole document linked list
	Node *doc_temp = document->head;
	while (doc_temp != NULL){
		char* data = doc_temp->data;
		
		// If the word is spelled correctly, simply print it out 
		if(hash_table_has(hash_table, data)) {
			printf("%s\n", data);
		}
		
		// check words that are one edit distance from it 
		else {
			check_edit_1(data, hash_table);
		}
		
		// move to the next misspelled word
		doc_temp = doc_temp->next;
	}
	free_hash_table(hash_table);
}




/* Helper Function */

// Task 1

/* Count the characters length of a word */
int word_length (char *word) {
	int count = 0;
	int i;
	for(i = 0; word[i] != '\0'; i++){
		count+=1;
	}
	return count;
}

/* Find the minimum value of the three inputs */
int min(int a, int b, int c) {
	int temp;
	int min;
	temp = (a < b) ? a : b;
    min =  (c < temp) ? c : temp;
	return min;
}

/* Calculate the levenshtein distance between two words */
int levenshtein_distance(char *word1, char *word2, int m, int n) {
	
	// the table for storing the results to avoid recomputations
    int results[m+1][n+1];
 
    for (int i=0; i<=m; i++) {
        for (int j=0; j<=n; j++) {
        	
            // If either one of the strings is empty, the edit distance will 
            // equal to the other string's length
            if (i==0)
                results[i][j] = j;  
            else if (j==0)
                results[i][j] = i; 
 
            // If last characters are same, ignore the last character and move 
            // on to the remaining string
            else if (word1[i-1] == word2[j-1]) {
                results[i][j] = results[i-1][j-1];
            }
 
            // If last characters are different, consider all three options and
            // the edit distance will equal to the minimum plus one
            else {
                results[i][j] = 1 + min(results[i][j-1], results[i-1][j],
                                results[i-1][j-1]); 
            }
        }
    }
    return results[m][n];
}

// Task 2

/* Print a linked list */
void print_linked_list(List *linked_list) {
	Node* temp = linked_list->head;
	while (temp!=NULL) {
		char *data = temp->data;
		printf("%s\n", data);
		temp = temp->next;
	}
	
}

/* Concatenate two strings into one string */
char *concatenate_two (char *left, char *right) {
	char *product = malloc((1 + strlen(left)+strlen(right)) * sizeof (char));
	strcpy(product, left);
	strcat(product, right);
	return product;
}

/* Concatenate two strings and a character into one string */
char *concatenate_three (char *left, char middle, char *right) {
	char *word = malloc((1 + strlen(left)+strlen(right)+1)*sizeof (char));
	strcpy(word, left);
	int len = strlen(word);
	word[len]=middle;
	word[len+1]='\0';
	strcat(word, right);
	return word;
}

/* Find all the words that are one edit distance from the input word and put 
 * them into a linked list
 */
void get_all_edit1(char *word, List *list) {
	int length;
	length = word_length(word);
	int levenshtein_distance = 1;
	char *alphabets = "abcdefghijklmnopqrstuvwxyz";
	char left[MAX_CHAR_LEN];
	char right[MAX_CHAR_LEN];
	
	int i;
	for(i=0; i<length; i++){
		slicing(word, left, 0, i);
		slicing(word, right, i+(levenshtein_distance-1), length);
		
		// deletion
		char deleted[MAX_CHAR_LEN];
		slicing(right, deleted, 1, word_length(right));
		list_add_end(list, concatenate_two(left,deleted));
		
		// replacement
		int j;
		for(j = 0; alphabets[j] != '\0'; j++){
			list_add_end(list, concatenate_three(left,alphabets[j],deleted));
		}
		
		left[0] = '\0';
		right[0] = '\0';
	}
	
	// insertion
	for(i=0; i<length+1; i++){
		slicing(word, left, 0, i);
		slicing(word, right, i+(levenshtein_distance-1), length);
		int j;
		for(j = 0; alphabets[j] != '\0'; j++){
			list_add_end(list, concatenate_three(left,alphabets[j],right));
		}
		left[0] = '\0';
		right[0] = '\0';
	}
}

/* Slice a string based on its index */
void slicing(char *a, char *b, int start, int end) {
    int j = 0;
    int i;
    for (i=start; i<end; i++) {
        b[j] = a[i];
        j+=1;
    }
    b[j] = '\0';
}


// Task 3

/* Convert a sorted linked list (in descending occurence probability) into 
 * a hash table (order is preversed in storing them into the hash table as well)
 */
HashTable *dict_to_hashtbl (List *dictionary) {
	
	// build a hash table
	HashTable *hash_table = new_hash_table(list_size(dictionary));
	
	// travserves through dictionary and add them to hash table
	int order = 0;
	Node *temp = dictionary->head;	
	while (temp != NULL){
		order += 1;
		char* data = temp->data;
		hash_table_put(hash_table, data, order);
		temp = temp->next;
	}
	return hash_table;
}




// Task 4

/* Check if any words that are 1 edit distance from the input word exist in 
 * the hash table. If true, pick the lowest order one and print it out. Else,
 * check words that are 2 edit distance away from the input word.
 */
void check_edit_1(char *word, HashTable *hash_table) {
	
	// get all the words that have 1 edit distance in an linked list
	List *list_edit1 = new_list();
	get_all_edit1(word, list_edit1);
	
	
	// check if all any of the words is in the hash table 
	// if yes then store the word(s) in a new linked list 
	List *edit1_list_exist = new_list();
	Node* temp = list_edit1->head;
	while(temp!=NULL){
		char *data = temp->data;
		if (hash_table_has(hash_table, data)){
			list_add_end(edit1_list_exist, data);
		}
		temp = temp->next;
	}
	
	// if none of the words is in the hash table, check the words that are 2 
	// edit distance from the input word 
	if (list_is_empty(edit1_list_exist)){
		check_edit_2(word, hash_table, list_edit1);
	}
	// if there is word(s) in the hash table, find out the word that has the 
	// lowest order/highest occurence probability and print it out 
	else {
		find_correction(hash_table, edit1_list_exist);
	}
	
	free_list(edit1_list_exist);
	free_list(list_edit1);
}

/* Check if any words that are 2 edit distance from the input word exist in 
 * the hash table. If true, pick the lowest order one and print it out. Else,
 * check words that are 3 edit distance away from the input word.
 */
void check_edit_2 (char *word, HashTable *hash_table, List *list_edit1) {
	
	// get all the words that have 2 edit distance in an linked list, excludes
	// the words that are 1 edit distance from the word (which have already
	// been checked )
	List *edit2_list_raw = new_list();
	get_all_edit2(word, list_edit1, edit2_list_raw);
	List *edit2_list = new_list();
	union_linked_list(list_edit1, edit2_list_raw, edit2_list);
	
	// check if all any of the words is in the hash table 
	// if yes then store the word(s) in a new linked list 
	List *edit2_list_exist = new_list();
	Node* temp2 = edit2_list->head;
	while(temp2!=NULL){
		char *data2 = temp2->data;
		if (hash_table_has(hash_table, data2)){
			list_add_end(edit2_list_exist, data2);
		}
		temp2 = temp2->next;
	}
	
	// if none of the words is in the hash table, check the words that are 3 
	// edit distance from the input word 
	if (list_is_empty(edit2_list_exist)){
		check_edit_3(word, hash_table, edit2_list_raw);
	}
	// if there is word(s) in the hash table, find out the word that has the 
	// lowest order/highest occurence probability and print it out 
	else {
		find_correction(hash_table, edit2_list_exist);
	}
	
	free_list(edit2_list);
	free_list(edit2_list_raw);
	free_list(edit2_list_exist);
}


/* Check if any words that are 3 edit distance from the input word exist in 
 * the hash table. If true, pick the lowest order one and print it out. Else,
 * print the word out with a question mark at the end.
 */
void check_edit_3 (char *word, HashTable *hash_table, List *edit2_list_raw) {
	
	// get all the words that have 3 edit distance in an linked list
	List *edit3_list_raw = new_list();
	get_all_edit3(word, edit2_list_raw, edit3_list_raw);
	List *edit3_list = new_list();
	union_linked_list(edit2_list_raw, edit3_list_raw, edit3_list);
	
	
	// check if all any of the words is in the hash table 
	// if yes then store the word(s) in a new linked list 
	List *edit3_list_exist = new_list();
	Node* temp3 = edit3_list->head;
	while(temp3!=NULL){
		char *data3 = temp3->data;
		if (hash_table_has(hash_table, data3)){
			list_add_end(edit3_list_exist, data3);
		}
		temp3 = temp3->next;
	}


	// if none of the words is in the hash table, print the word with a question
	// mark at the end
	if (list_is_empty(edit3_list_exist)){
		printf("%s?\n", word);
	}
	// if there is word(s) in the hash table, find out the word that has the 
	// lowest order/highest occurence probability and print it out
	else {
		find_correction(hash_table, edit3_list_exist);
	}
	
	free_list(edit3_list);
	free_list(edit3_list_raw);
	free_list(edit3_list_exist);
}



/* Find all the words that are two edit distance from the input word and put 
 * them into a linked list
 */
void get_all_edit2 (char *word, List *edit1_list, List *list) {
	Node* temp = edit1_list->head;
	while(temp!=NULL){
		char* data = temp->data;
		get_all_edit1(data, list);
		temp = temp->next;
	}
}

/* Find all the words that are three edit distance from the input word and put 
 * them into a linked list
 */
void get_all_edit3 (char *word, List *edit2_list, List *list) {
	Node* temp = edit2_list->head;
	while(temp!=NULL){
		char* data = temp->data;
		get_all_edit1(data, list);
		temp = temp->next;
	}
}

/* Find the word that has the lowest order from a linked list and print it out 
 */
void find_correction (HashTable *hash_table, List *candidate_list){
	
	// traverse through the linked list and find the candidate word that
	// has the lowest order number (higher probability of occurrence)
	Node* temp2 = candidate_list->head;
	char* data = temp2->data;
	int min = hash_table_get(hash_table, data);
	char *correction; 
	
	while(temp2!=NULL){
		data = temp2->data;
		if(min >= hash_table_get(hash_table, data)) {
			min = hash_table_get(hash_table, data);
			correction = data;
		}
		temp2 = temp2->next;
	}
	printf("%s\n", correction);
}

/* Find the union element of two linked list */
void union_linked_list(List *list1, List *list2, List *union_list) {
	
	// build a hash table for list1
	HashTable *hash_table = dict_to_hashtbl(list1);
	
	// traverse through list2 and check whether it exists in the hash table
	// if true, ignore; else, add the word into a new linked list
	// traverse through the whole document linked list
	Node *temp = list2->head;
	while (temp != NULL){
		char* data = temp->data;
		
		// If it exists in the hash table, ignore; else, add it into a new 
		// linked list
		if(!hash_table_has(hash_table, data)) {
			list_add_end(union_list, data);
		}
		
		temp = temp->next;
	}
	
	free_hash_table(hash_table);
}