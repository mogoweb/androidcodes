#include "timer.h"

#include <assert.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>
#include "utils/threads.h"

static timer_list_t * z_timer_list = 0;
static utime_t* z_ujiffies = 0;
static bool z_is_tick_start = false;
static android_thread_id_t z_thread_tick;
static android_thread_id_t z_thread_check;

static int is_timer_list_empty(void)
{
	assert(z_timer_list != 0);

	return (z_timer_list->element_count == 0 && z_timer_list->head == 0);
}

int timer_list_init(void)
{
	if (z_timer_list == 0) {
		if ((z_timer_list = (timer_list_t*)malloc(sizeof(timer_list_t))) == 0) {
			return -1;
		}
		memset(z_timer_list, 0, sizeof(timer_list_t));
	}

	if (z_ujiffies == 0) {
		if ((z_ujiffies = (utime_t*)malloc(sizeof(utime_t))) == 0){
			return -1;
		}
		*z_ujiffies = 0;
	}

	return 0;
}

void timer_list_destroy(void)
{
	timer_node_t * ptmp = 0;

	while (z_timer_list->head != 0) {
		ptmp = z_timer_list->head;
		z_timer_list->head = ptmp->next;
		free(ptmp);
		z_timer_list->element_count--;
	}

	free(z_timer_list);
	z_timer_list = 0;
	free(z_ujiffies);
	z_ujiffies = 0;
}

VTOP_TimerId create_rel_timer(pfTimeoutCallback callback, int flag)
{
	if (z_timer_list == 0 || z_ujiffies == 0)
	{
		if (timer_list_init() < 0)
		{
			return 0;
		}
	}

	timer_node_t * pnew = (timer_node_t*)malloc(sizeof(timer_node_t));
	if (pnew == 0)
	{
		return 0;
	}
	memset(pnew, 0, sizeof(timer_node_t));

	pnew->timeout_callback = callback;
	pnew->flag = flag;
	pnew->id = (VTOP_TimerId)malloc(sizeof(struct _timer_handle));
	if (pnew->id == 0)
	{
		free(pnew);
		return 0;
	}
	pnew->id->ptr = (unsigned int)pnew;

	// insert the new node from list head
	if (z_timer_list->head != 0)
	{
		z_timer_list->head->prev = pnew;
	}
	pnew->next = z_timer_list->head;
	z_timer_list->head = pnew;
	z_timer_list->element_count++;

	return pnew->id;
}

//how to handle the parameter:void * param
int start_rel_timer(VTOP_TimerId timer_id, int duration, void *param)
{
	assert(timer_id != 0 && timer_id->ptr != 0);

	if (timer_id == 0 || timer_id->ptr == 0)
	{
		return -1;
	}

    timer_node_t* ptmp = (timer_node_t*)timer_id->ptr;
	ptmp->expire = *z_ujiffies + duration;
	ptmp->interval = duration;

	return 0;
}

int stop_rel_timer(VTOP_TimerId timer_id)
{
	assert(timer_id != 0 && timer_id->ptr != 0);

	if (timer_id == 0 || timer_id->ptr == 0)
	{
		return -1;
	}

	timer_node_t* ptmp = (timer_node_t*)timer_id->ptr;
	ptmp->expire = 0;
	ptmp->interval = 0;

	return 0;
}

int free_rel_timer(VTOP_TimerId timer_id)
{
	assert(timer_id != 0 && timer_id->ptr != 0 && z_timer_list->element_count != 0);

    if (timer_id == 0 || timer_id->ptr == 0 || z_timer_list->element_count == 0)
	{
		return -1;
	}

	timer_node_t* pcur = (timer_node_t*)timer_id->ptr;
	if (pcur->prev == 0) //the head node
	{
		z_timer_list->head = pcur->next;
	}
	else
	{
		pcur->prev->next = pcur->next;
	}

	if (pcur->next != 0) //if not the tail node
	{
		 pcur->next->prev = pcur->prev;
	}

	timer_id->ptr = 0;
    free(pcur->id);
	free(pcur);

	z_timer_list->element_count--;
}

/**
 * start to tick
 */
static void* start_tick(void* param)
{
    z_is_tick_start = 1;

	struct timeval o_tv;
	struct timeval tv;

	o_tv.tv_sec = 0;
	o_tv.tv_usec = UTIMER_TICK;

	while (1)
	{
		tv = o_tv;
		select(0, 0, 0, 0, &tv);
		*z_ujiffies += UTIMER_TICK;
	}

	return 0;
}

/**
 * check whether there is any timout event happen
 */
static void* timer_check(void* param)
{
    while (!z_is_tick_start)
    {
		;
    }

	struct timeval o_tv;
	struct timeval tv;
	o_tv.tv_sec = 0;
	o_tv.tv_usec = UTIMER_TICK;

	while (1)
	{
		tv = o_tv;
		select(0, 0, 0, 0, &tv);
		timer_node_t * ptmp = z_timer_list->head;
		for (; ptmp; ptmp = ptmp->next)
		{
			if (ptmp->interval <= 0)
			{
				continue;
			}

			if (ptmp->expire <= *z_ujiffies)
			{
				ptmp->timeout_callback(0);

				if (ptmp->flag == TIMEOUT_ONE_SHOT)
				{
					stop_rel_timer(ptmp->id);
				}
				else if (ptmp->flag == TIMEOUT_AUTO_FREE)
				{
					free_rel_timer(ptmp->id);
				}
				else if (ptmp->flag == TIMEOUT_LOOP)
				{
					ptmp->expire = *z_ujiffies + ptmp->interval;
				}
			}
		}
	}
}

void start_tick_and_timer_check(void)
{
    //~androidCreateThread(start_tick, 0);
    //~androidCreateThread(timer_check, 0);
    pthread_create(&z_thread_tick, 0, start_tick, 0);
    pthread_create(&z_thread_check, 0, timer_check, 0);
}
