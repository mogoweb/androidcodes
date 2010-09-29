#ifndef __TIMER_H__
#define __TIMER_H__

#ifdef __cplusplus
extern "C" {
#endif

#define UTIMER_TICK	100 * 1000  /* 100 miliseconds */

typedef long long utime_t;

typedef enum {
	TIMEOUT_ONE_SHOT,
	TIMEOUT_LOOP,
	TIMEOUT_AUTO_FREE
} TimeoutType;

typedef struct _timer_handle {
	unsigned int ptr;
}* VTOP_TimerId;

typedef void (*pfTimeoutCallback)(void* args);

typedef struct _timer_node timer_node_t;
struct _timer_node {
	struct _timer_node *prev;
	struct _timer_node *next;

	pfTimeoutCallback timeout_callback;
	int flag;
	utime_t expire;
	int interval;	// if interval<=0, the timer is not active
	VTOP_TimerId id;
	void *param;	// temporarily not used
};

typedef struct _timer_list timer_list_t;
struct _timer_list {
	int element_count;
	timer_node_t *head;
};

/**
 * 初始化，获取timer list所需的资源
 */
int timer_list_init(void);

/**
 * use this funtion to release the memory resources for timer list
 */
void timer_list_destroy(void);

/**
 * 创建一个timer
 *
 * @return success:the timerID, error:0
 */
VTOP_TimerId create_rel_timer(pfTimeoutCallback callback, int flag);

/**
 * 启动一个timer, 参数duration表示持续时间, 用微秒度量
 *
 * @return success:0, error:-1
 */
int start_rel_timer(VTOP_TimerId timer_id, int duration, void *param);

/**
 * 停止一个timer
 *
 * @return success:0, error:-1
 */
int stop_rel_timer(VTOP_TimerId timer_id);

/**
 * 删除一个timer
 *
 * @return success:0, error:-1
 */
int free_rel_timer(VTOP_TimerId timer_id);

void start_tick_and_timer_check(void);

#ifdef __cplusplus
}
#endif

#endif
