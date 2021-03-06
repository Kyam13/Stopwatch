package com.timerapplication.stopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.os.Handler
import android.media.RingtoneManager
import java.security.KeyStore

class MainActivity : AppCompatActivity() {

    // 1度だけ代入するものはvalを使う
    val handler = Handler()
    // 繰り返し代入するためvarを使う
    var timeValue = 0
    // startが偶数回押された時にstopになるようにするための判別変数
    var startchangeStop =false
    //timerの時につける変数
    var timerdecide =false
    //timerstop
    //var timerstop=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //as TextView という記述だとタイムパラメーターの推論ができないから
        //型を明示する 変数の後に型を書くやり方がいい
        val timeText:TextView = findViewById(R.id.timeText)
        val startandstopButton:Button = findViewById(R.id.startandstop)
        //val stopButton:Button = findViewById(R.id.stop)
        val resetButton:Button = findViewById(R.id.reset)
        val hourButton:Button = findViewById(R.id.hour)
        val minuteButton:Button = findViewById(R.id.min)
        val secondButton:Button = findViewById(R.id.sec)

        // 現在設定されている着信音を選択する
        val ringType = RingtoneManager.TYPE_NOTIFICATION
        val soundUri = RingtoneManager.getActualDefaultRingtoneUri(this, ringType)
        val ringtone = RingtoneManager.getRingtone(applicationContext, soundUri)

        //1秒ごとに処理を実行
        //Runnableクラスに対する無名オブジェクト
        val runnable = object : Runnable{
            override fun run() {
                if (timeValue>0 && timerdecide==true) {
                    timeValue=timeValue.dec()
                }
//                else if(timeValue==0&&timerdecide==true){
//                    timerdecide=false
//                }
                else{// if(timerstop==true){
                    timerdecide=false
                    timeValue=timeValue.inc()
                }
                // TextViewを更新
                // ?.letを用いて、nullではない場合のみ更新
                timeToText(timeValue)?.let {
                    // timeToText(timeValue)の値がlet内ではitとして使える
                    timeText.text = it
                }
                handler.postDelayed(this, 1000)
            }
        }


        // start and stop
        startandstopButton.setOnClickListener {
            if(startchangeStop==false) {
                handler.post(runnable)
                startchangeStop =true
            }
            else {
                handler.removeCallbacks(runnable)
                startchangeStop = false
            }
//            timerstop= if(timeValue==0){true }else{ false}
        }

        // reset
        resetButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            timeValue = 0
            // timeToTextの引数はデフォルト値が設定されているので、引数省略できる
            timeToText()?.let {
                timeText.text = it
            }
        }
        //hour
        hourButton.setOnClickListener {
            timerdecide=true
            timeValue +=3600
            timeToText(timeValue)?.let {
                timeText.text = it
            }
        }
        //minute
        minuteButton.setOnClickListener {
            timeValue +=60
            timerdecide=true
            timeToText(timeValue)?.let {
                timeText.text = it
            }
        }
        //second
        secondButton.setOnClickListener {
            timeValue +=1
            timerdecide=true
            timeToText(timeValue)?.let {
                timeText.text = it
            }
        }
//
    }
    // 数値を00:00:00形式の文字列に変換する関数
    // 引数timeにはデフォルト値0を設定、返却する型はnullableなString?型
    private fun timeToText(time: Int = 0): String? {
        // if式は値を返すため、そのままreturnできる
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
}
