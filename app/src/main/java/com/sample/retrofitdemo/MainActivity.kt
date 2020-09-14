package com.sample.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        //getRequestWithQueryParameters()
        //getRequestWithPathParameters()
        uploadAlbum()


    }
    private fun getRequestWithQueryParameters(){
        val responseLiveData: LiveData<Response<Album>> = liveData {
            //val response = retService.getAlbums()  --We can use this for all data
            val response = retService.getSortedAlbums(3)


            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumList = it.body()?.listIterator()
            if (albumList!= null){
                while (albumList.hasNext()){
                    val albumsItem = albumList.next()
                    val result = " " + "Album id : ${albumsItem.id}}" +"\n"+
                            " " + "Album title : ${albumsItem.title}" +"\n"+
                            " " + "Album userid : ${albumsItem.userId}" +"\n\n\n"

                    textView.append(result)

                    Log.i("MyTag", albumsItem.title)
                }

            }
        })
    }
    private fun getRequestWithPathParameters(){
        val pathResponse : LiveData<Response<AlbumItem>> = liveData {
            val response = retService.getAlbum(3)
            emit(response)
        }
        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this,"Titla : $title" ,Toast.LENGTH_SHORT).show()
        })
    }
    private fun uploadAlbum(){
        val album = AlbumItem(0 , "My Title" ,3)
        val postResponse : LiveData<Response<AlbumItem>> = liveData {
            val response = retService.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedItem = it.body()
            val result = " " + "Album id : ${receivedItem?.id}}" +"\n"+
                    " " + "Album title : ${receivedItem?.title}" +"\n"+
                    " " + "Album userid : ${receivedItem?.userId}" +"\n\n\n"
            textView.text = result
        })
    }
}