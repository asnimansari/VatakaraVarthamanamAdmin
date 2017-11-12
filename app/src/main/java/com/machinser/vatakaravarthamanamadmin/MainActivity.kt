package com.machinser.vatakaravarthamanamadmin

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import java.util.*


class MainActivity : AppCompatActivity() {


    var arrylist:ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        populateListViewFromDB()
        fab.setOnClickListener {

            buildAlertBox("")
        }

        lv.setOnItemClickListener { adapterView, view, i, l ->

            buildAlertBox(arrylist?.get(i))
        }
    }

    private fun buildAlertBox(s:String?) {


        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL
        val layoutparams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutparams.setMargins(20,0,20,0)


        val title = EditText(this@MainActivity)
        val titleTv = TextView(this@MainActivity)
        titleTv.layoutParams = layoutparams
        title.maxLines = 1
        title.imeOptions  = EditorInfo.IME_ACTION_NEXT
        title.inputType = InputType.TYPE_CLASS_TEXT
        titleTv.text = "Notification Title"

        title.setText("വടകര വർത്തമാനം")
        title.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.colorPrimary))


        val body = EditText(this@MainActivity)
        val bodyTv = TextView(this@MainActivity)
        bodyTv.layoutParams = layoutparams
        body.maxLines = 5
        body.setLines(5)
        body.setText(s)
        bodyTv.text = "Notification Body"



        layout.addView(titleTv)
        layout.addView(title)

        layout.addView(bodyTv)
        layout.addView(body)
       var alert = AlertDialog.Builder(this@MainActivity)
               .setTitle("Send New Notification to VatakaraVarthamanam")
               .setView(layout)
               .setPositiveButton("SEND NOTIFICATION",null)
               .setNegativeButton("CANCEL",null)
               .create()

        alert.setCanceledOnTouchOutside(false)

        alert.setOnShowListener {

            val positive_button = (alert as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            val negative_button = (alert as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)

            positive_button.setOnClickListener {


                var body1 = body.text.toString().trim()
                var title1 = title.text.toString().trim()
                if (body1.length >0 && title1.length >0){
                    addToDB(body1,title1)
                    sendnotificaion(body1,title1)
                    populateListViewFromDB()
                    alert.dismiss()
                }
                else{
                    Toast.makeText(this@MainActivity,"ENTER PROPER TITLE AND MESSAGE",Toast.LENGTH_SHORT).show()
                }
            }
            negative_button.setOnClickListener {
                alert.dismiss()
            }
        }
        alert.show()


    }

    private fun sendnotificaion(body1: String, title1: String) {

        val notf = Notf()
        notf.title = title1
        notf.body = body1

        val builder = Retrofit.Builder()
                .baseUrl("http://mobile.msrgrid.com:1337")
                .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val pushRequest = retrofit.create(PushRequest::class.java)


        val responseOnPushCall = pushRequest.sendOrdinaryNotifiction(notf)
        responseOnPushCall.enqueue(object : Callback<ResponseOnPush> {
            override fun onResponse(call: Call<ResponseOnPush>, response: Response<ResponseOnPush>) {
                Log.e("Status Code", response.code().toString() + "")
                Toast.makeText(this@MainActivity, "Notification Has been Sent", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<ResponseOnPush>, t: Throwable) {

                Toast.makeText(this@MainActivity, "Notification  Has been Sent", Toast.LENGTH_SHORT).show()


            }
        })

    }

    fun addToDB(body: String,title:String){
        var db = MyDataBase(this@MainActivity)

        var cv = ContentValues()
        cv.put(MyDataBase.MESSAGE,body)
        cv.put(MyDataBase.TITLE,title)
        db.insertNotificaion(cv)
    }
    fun populateListViewFromDB(){
        var db = MyDataBase(this@MainActivity)
        arrylist = db.data
        if     (arrylist!=null) {
            Collections.reverse(arrylist)
            lv.adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_list_item_1, arrylist)

        }


    }
}
