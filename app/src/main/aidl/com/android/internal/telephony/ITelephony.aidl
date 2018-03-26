package com.android.internal.telephony;

interface ITelephony 
{
    void answerRingingCall();
    void silenceRinger();
    void call(String number);
}