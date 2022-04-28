package com.example.voicerecorder.listrecord


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.voicerecorder.R
import com.example.voicerecorder.database.RecordingItem
import com.example.voicerecorder.player.PlayerFragment
import com.example.voicerecorder.removeDialog.RemoveDialogFragment
import java.io.File
import java.lang.Exception
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path

class ListRecordAdapter:RecyclerView.Adapter<ListRecordAdapter.ViewHolder>() {
    var data = listOf<RecordingItem>()
    set(value) {
        field = value
        //notifYDataSetChanged
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var vName: TextView = itemView.findViewById(R.id.file_name_text)
        var vLength: TextView = itemView.findViewById(R.id.file_length_text)
        var cardView: CardView = itemView.findViewById(R.id.card_view)
    }

    companion object{
        fun from(parent:ViewGroup): ViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val view: View = layoutInflater.inflate(R.layout.list_item_record, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val recordingItem = data[position]
        val itemDuration: Long = recordingItem.length
        val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes)
        holder.vName.text = recordingItem.name
        holder.vLength.text = String.format("%02d:%02d",minutes, seconds)
        holder.cardView.setOnClickListener {
            val filePath = recordingItem.filePath
            val file = File(filePath)
            if(file.exists()){
                try{
                    playRecord(filePath, context)
                }catch(e:Exception){

                }
            }else{
                Toast.makeText(context, "Аудиофайл не найден", Toast.LENGTH_SHORT).show()
            }
        }
        holder.cardView.setOnLongClickListener {
            removeItemDialog(recordingItem, context)
            false
        }


    }

    override fun getItemCount(): Int = data.size

    private fun playRecord(filePath: String, context: Context? ){
        val playerFragment: PlayerFragment = PlayerFragment().newInstance(filePath)
        val transaction: FragmentTransaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        playerFragment.show(transaction, "dialog_playback")

    }
    private fun removeItemDialog(
        recordingItem: RecordingItem,
        context: Context?
    ) {
        val removeDialogFragment: RemoveDialogFragment =
            RemoveDialogFragment()
                .newInstance(
                    recordingItem.id,
                    recordingItem.filePath)
        val transaction: FragmentTransaction =
            (context as FragmentActivity)
                .supportFragmentManager
                .beginTransaction()
        removeDialogFragment.show(transaction, "dialog_remove")
    }
}
