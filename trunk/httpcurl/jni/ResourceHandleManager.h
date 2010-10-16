#ifndef __ResourceHandleManager_H__
#define __ResourceHandleManager_H__

#include "ResourceHandle.h"
#include "dlist.h"
#include <curl/curl.h>

class ResourceHandleManager {
public:
    static ResourceHandleManager* sharedInstance();
    void add(ResourceHandle*);
private:
    ResourceHandleManager();
    ~ResourceHandleManager();

    static int threadFunc(void* arg);

    CURLM* m_curlMultiHandle;
    CURLSH* m_curlShareHandle;
    DList *m_resourceHandleList;
    int m_runningJobs;
};

#endif
