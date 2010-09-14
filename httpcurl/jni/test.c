#include <netdb.h>
#include <sys/socket.h>
#include <stdio.h>

#define LOGD printf

int main(int argc, char* argv[])
{
	struct hostent *hptr;
    /* 调用gethostbyname()。调用结果都存在hptr中 */
    if ((hptr = gethostbyname("www.baidu.com")) == NULL)
	{
		LOGD("gethostbyname error for host:%s, errcode=%d\n", "www.baidu.com", h_errno);
		return 1; /* 如果调用gethostbyname发生错误，返回1 */
	}
	LOGD("resolvHost success");

	return 0;
}
