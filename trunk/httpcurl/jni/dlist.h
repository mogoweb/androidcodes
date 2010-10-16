#ifndef __DLIST_H__
#define __DLIST_H__

#ifdef __cplusplus
extern "C" {
#endif

typedef struct _DListNode {
	struct _DListNode *prev;
	struct _DListNode *next;
	void *data;
} DListNode;

typedef struct _DList {
	struct _DListNode *head;
	struct _DListNode *tail;
	int count;
} DList;

int dlist_init(DList* dlist);
int dlist_append_node(DList* dlist, DListNode* node);
int dlist_remove_node(DList* dlist, DListNode* node);
int dlist_free(DList* dlist);

#ifdef __cplusplus
}
#endif

#endif
