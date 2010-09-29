#include "timer_jni.h"
#include "timer.h"
#include "cutils/log.h"

void func(void *p)
{
    LOGD("timer1:timeout\n");
}

JNIEXPORT void JNICALL Java_com_whtr_example_mtimer_MainActivity_startTimer(JNIEnv *env, jobject thizz)
{
    LOGD("entering JNI startTimer");
    timer_list_init();
    start_tick_and_timer_check();

    VTOP_TimerId timer = create_rel_timer(func, TIMEOUT_AUTO_FREE);
    start_rel_timer(timer, 100 * 1000, 0);

    LOGD("leaving JNI startTimer");
}

