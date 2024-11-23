#include <jni.h>
#include <iostream>
#include <vector>
#include "classfile/classFileParser.hpp"
#include "runtime/thread.hpp"
#include "oops/klass.hpp"
#include "enderwrapper_internal_Native.h"

using namespace std;

JNIEXPORT jbyteArray JNICALL Java_enderwrapper_internal_Native_getClassBytecode(JNIEnv* env, jobject thisObj, jclass clazz) {
    jobject globalClazz = env->NewGlobalRef(clazz);
    jclass javaLangClass = env->FindClass("java/lang/Class");
    jfieldID klassFieldID = env->GetFieldID(javaLangClass, "klass", "I");
    Klass* k = (Klass*) env->GetIntField(globalClazz, klassFieldID);
    env->DeleteGlobalRef(globalClazz);
    ClassFileParser* parser = k->klass_part()->class_file_parser();
    int length = parser->length();
    vector<u1> bytecodeData(length);
    memcpy(bytecodeData.data(), parser->content_begin(), length);
    jbyteArray bytecodeArray = env->NewByteArray(length);
    env->SetByteArrayRegion(bytecodeArray, 0, length, (const jbyte*) bytecodeData.data());
    return bytecodeArray;
}