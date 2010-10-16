#include "dlist.h"

#include <assert.h>
#include <stdio.h>

int dlist_init(DList* dlist)
{
	dlist->head = NULL;
	dlist->tail = NULL;
	dlist->count = 0;

	return 0;
}

int dlist_append_node(DList* dlist, DListNode* node)
{
	assert(dlist != NULL && node != NULL);
	if (dlist->tail)
	{
		dlist->tail->next = node;
		node->prev = dlist->tail->prev;
		node->next = dlist->head;
		dlist->head->prev = node;
		dlist->tail = node;
	}
	else
	{
		dlist->head = node;
		dlist->tail = node;
		node->prev = node;
		node->next = node;
	}

	return 0;
}

int dlist_remove_node(DList* dlist, DListNode* node)
{
	return 0;
}

int dlist_free(DList* dlist)
{

}
