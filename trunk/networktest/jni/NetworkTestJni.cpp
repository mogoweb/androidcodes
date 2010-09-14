#include <netdb.h>
#include <sys/socket.h>
#include <jni.h>
#include "NetworkTestJni.h"
#include "mylog.h"

JNIEXPORT void JNICALL Java_com_whtr_example_networktest_MainActivity_getHostName(JNIEnv* env, jobject thizz, jstring urlstr)
{
	LOGD("entering JNI getHostName");
	const char* url = env->GetStringUTFChars(urlstr, 0);

	struct hostent *hptr;
	/* 调用gethostbyname()。调用结果都存在hptr中 */
	if ((hptr = gethostbyname(url)) == NULL)
	{
		LOGD("gethostbyname error for host:%s, errcode=%d\n", url, h_errno);
		return; /* 如果调用gethostbyname发生错误，返回1 */
	}
	LOGD("resolvHost success");

	env->ReleaseStringUTFChars(urlstr, url);
	LOGD("leaving JNI getHostName");
}
