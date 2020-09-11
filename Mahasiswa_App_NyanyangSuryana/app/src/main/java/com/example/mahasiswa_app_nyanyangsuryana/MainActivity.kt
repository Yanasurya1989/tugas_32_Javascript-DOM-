package com.example.mahasiswa_app_nyanyangsuryana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mahasiswa_app_nyanyangsuryana.Adapter.DataAdapter
import com.example.mahasiswa_app_nyanyangsuryana.Config.NetworkModule
import com.example.mahasiswa_app_nyanyangsuryana.Model.action.ResponseActoin
import com.example.mahasiswa_app_nyanyangsuryana.Model.getdata.DataItem
import com.example.mahasiswa_app_nyanyangsuryana.Model.getdata.ResponseGetData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        showData()

    }

    private fun showData(){
        val listAnggota = NetworkModule.service().getData()
        listAnggota.enqueue(object : Callback<ResponseGetData>{

            override fun onResponse(
                call: Call<ResponseGetData>,
                response: Response<ResponseGetData>
            ) {
                if (response.isSuccessful){

                    val item = response.body()?.data

                    val adapter = DataAdapter(item, object : DataAdapter.OnClickListener{
                        override fun detail(item: DataItem?) {

                            val intent = Intent(applicationContext, InputActivity::class.java)
                            intent.putExtra("data", item)
                            startActivity(intent)

                        }

                        override fun hapus(item: DataItem?) {
                            hapusData(item?.idMahasiswa)
                        }

                    })

                    list.adapter = adapter

                }
            }

            override fun onFailure(call: Call<ResponseGetData>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun hapusData(idMahasiswa: String?) {
        val hapus = NetworkModule.service().deleteData(idMahasiswa ?: "")
        hapus.enqueue(object : Callback<ResponseActoin> {
            override fun onResponse(
                call: Call<ResponseActoin>,
                response: Response<ResponseActoin>
            ) {
                Toast.makeText(applicationContext, "Data berhasil dihapus", Toast.LENGTH_SHORT)
                    .show()
                showData()
            }

            override fun onFailure(call: Call<ResponseActoin>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        showData()
    }
}