#include <jni.h>
#include "httpcurl.h"
#include "mylog.h"
//#include <cutils/log.h>

#include <string.h>
#include <unistd.h>
#include <curl/curl.h>

#include "fopen.h"
#include "lib507.h"

/* curl calls this routine to get more data */
static size_t write_callback(char *buffer, size_t size, size_t nitems, void *userp)
{
	LOGD("write_callback called");

	return size;
}

JNIEXPORT void JNICALL Java_com_whtr_example_httpcurl_MainActivity_loadUrl(JNIEnv *env, jobject thizz, jstring urlstr)
{
	LOGD("entering jni loadUrl");
#if 0
	CURL *handle;
	CURLM *multi_handle;

	handle = curl_easy_init();
#endif
	const char* url = env->GetStringUTFChars(urlstr, 0);
	LOGD("url: %s", url);
	loadUrl(url);
	//resolveHost(url);
#if 0
	curl_easy_setopt(handle, CURLOPT_URL, url);
	curl_easy_setopt(handle, CURLOPT_VERBOSE, 1L);
	curl_easy_setopt(handle, CURLOPT_WRITEFUNCTION, write_callback);

	/* init a multi stack */
	multi_handle = curl_multi_init();
	CURLMcode ret = curl_multi_add_handle(multi_handle, handle);
	LOGD("ret=%d", ret);

	int still_running = 0; /* keep number of running handles */

	/* we start some action by calling perform right away */
	while (CURLM_CALL_MULTI_PERFORM == curl_multi_perform(multi_handle, &still_running));

	LOGD("still_running=%d", still_running);
	//~while (still_running) {
		struct timeval timeout;
		int rc; /* select() return code */

		fd_set fdread;
		fd_set fdwrite;
		fd_set fdexcep;
		int maxfd;

		FD_ZERO(&fdread);
		FD_ZERO(&fdwrite);
		FD_ZERO(&fdexcep);

		/* set a suitable timeout to play around with */
		timeout.tv_sec = 1;
		timeout.tv_usec = 0;

		/* get file descriptors from the transfers */
		curl_multi_fdset(multi_handle, &fdread, &fdwrite, &fdexcep, &maxfd);
		LOGD("maxfd: %d", maxfd);

		/* In a real-world program you OF COURSE check the return code of the
		   function calls, *and* you make sure that maxfd is bigger than -1 so
		   that the call to select() below makes sense! */

		rc = select(maxfd+1, &fdread, &fdwrite, &fdexcep, &timeout);
		LOGD("rc=%d", rc);

		switch (rc) {
			case -1:
				/* select error */
				break;
			case 0:
				/* timeout, do something else */
				break;
			default:
				/* one or more of curl's file descriptors say there's data to read
				   or write */
				while (CURLM_CALL_MULTI_PERFORM == curl_multi_perform(multi_handle, &still_running));
				break;
		}
	//~}

	CURLMsg *msg; /* for picking up messages with the transfer status */
	int msgs_left; /* how many messages are left */

	/* See how the transfers went */
	while ((msg = curl_multi_info_read(multi_handle, &msgs_left))) {
		LOGD("read message");
		if (msg->msg == CURLMSG_DONE) {
			bool found = (msg->easy_handle == handle);

			if (found)
				LOGD("HTTP transfer completed with status %d\n", msg->data.result);
		}
	}

	curl_multi_cleanup(multi_handle);

	/* Free the CURL handles */
	curl_easy_cleanup(handle);
#endif
	env->ReleaseStringUTFChars(urlstr, url);


	LOGD("leaving jni loadUrl");
}

JNIEXPORT void JNICALL Java_com_whtr_example_httpcurl_MainActivity_multiLoadUrl(JNIEnv *env, jobject thizz, jstring urlstr)
{
	LOGD("entering jni multiLoadUrl");

	const char* url = env->GetStringUTFChars(urlstr, 0);
	LOGD("url: %s", url);
	test(url);
	env->ReleaseStringUTFChars(urlstr, url);

	LOGD("leaving jni multiLoadUrl");
}

