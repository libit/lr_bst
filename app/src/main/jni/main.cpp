/*
 * JNI 接口文件
 * 作者：Libit
 */

#ifdef __cplusplus
extern "C" {
#endif
#include "utils.h"
#include <stdio.h>

const char *mainUrl = "http://115.29.140.222/lr_bst/user";
const char *debugKey = "b1e7076c386ec6d21be35a427ea7a18f";
const char *releaseKey = "78816b8bde1386b7ab6a7202402ebb1e";
const char *errInfo = "Good Bye.";

jint JNI_OnLoad(JavaVM *jvm, void *reserved)
{
    return JNI_VERSION_1_6;
}

jstring getMainUrl(JNIEnv *env)
{
    return env->NewStringUTF (mainUrl);
}

bool isKeyValid(JNIEnv *env, jstring key)
{
    return (compareJString (env, key, env->NewStringUTF (releaseKey)) || compareJString (env, key, env->NewStringUTF (debugKey)));
}

jstring Java_com_lrcall_utils_INativeInterface_getUrl(JNIEnv *env, jobject thiz, jstring key)
{
    if(!isKeyValid (env, key)){
        return env->NewStringUTF (errInfo);
    }
    return env->NewStringUTF (mainUrl);
}

// 对外接口，用户登录
jstring Java_com_lrcall_utils_INativeInterface_login(JNIEnv *env, jobject thiz, jstring number, jstring pwd, jstring agentId, jstring signKey, jstring key)
{
    if(!isKeyValid (env, key)){
        return env->NewStringUTF (errInfo);
    }

    // POST方式提交
    char *str = new char[128];
    sprintf (str, "%s/loginAjax", mainUrl);
    jstring url = env->NewStringUTF (str);
    delete str;

    char *params = new char[256];
    sprintf (params, "[username,%s];[password,%s];[platform,android];[agentId,%s];[signKey,%s]", getChars (env, number), getChars (env, pwd), getChars (env, agentId), getChars (env, signKey));
    jstring param = env->NewStringUTF (params);
    delete params;

    return doPostJMethod (env, thiz, url, param);
}

jstring Java_com_lrcall_utils_INativeInterface_getUpdate(JNIEnv *env, jobject thiz, jstring number, jstring pwd, jstring agentId, jstring signedData, jstring platformInfo, jstring key)
{
    if(!isKeyValid (env, key)){
        return env->NewStringUTF (errInfo);
    }

    // POST方式提交
    char *str = new char[128];
    sprintf (str, "%s/lrUpdateClientAjax", mainUrl);
    jstring url = env->NewStringUTF (str);
    delete str;

    char *params = new char[256];
    sprintf (params, "[username,%s];[password,%s];[agentId,%s];[signedData,%s];%s", getChars (env, number), getChars (env, pwd), getChars (env, agentId), getChars (env, signedData),
             getChars (env, platformInfo));
    jstring param = env->NewStringUTF (params);
    delete params;

    return doPostJMethod (env, thiz, url, param);
}

#ifdef __cplusplus
}
#endif
