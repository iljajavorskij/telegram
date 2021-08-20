package com.example.mymassenger.utilits

import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class AppVoiceRecorder {
    companion object{
        private val mMediaRecorder = MediaRecorder()
        private lateinit var mFile:File
        private lateinit var mMessageKey:String


        fun startRecorder(messageKey:String) = CoroutineScope(Dispatchers.IO).launch{
            try {
                mMessageKey = messageKey
                createFileForRecord()
                prapareMediaRecorder()
                mMediaRecorder.start()
            }catch (e:Exception){
                showToast(e.message.toString())
            }


        }

        private fun prapareMediaRecorder() {

            mMediaRecorder.reset()
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            mMediaRecorder.setOutputFile(mFile.absolutePath)
            mMediaRecorder.prepare()
        }

        private fun createFileForRecord() {
            mFile = File(APP_ACTIVITY.filesDir, mMessageKey)
            mFile.createNewFile()
        }


        fun stopRecorder(onSuccess:(file:File,messageKey:String) -> Unit){
            try {
                mMediaRecorder.stop()
                onSuccess(mFile,mMessageKey)
            }catch (e:Exception){
                showToast(e.message.toString())
            }

        }


        fun releaseRecording(){
            try {
                mMediaRecorder.release()
            }catch (e:Exception){
                showToast(e.message.toString())
            }

        }
    }
}