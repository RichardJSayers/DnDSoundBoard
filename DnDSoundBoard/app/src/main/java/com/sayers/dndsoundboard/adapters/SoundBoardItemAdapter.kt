package com.sayers.dndsoundboard.adapters

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sayers.dndsoundboard.R
import com.sayers.dndsoundboard.databinding.SoundboardItemBinding
import com.sayers.dndsoundboard.helpers.FileUtils
import com.sayers.dndsoundboard.helpers.RealmRecyclerViewAdapter
import com.sayers.dndsoundboard.models.SoundBoardItem
import com.sayers.dndsoundboard.viewmodels.MainActivityViewModel
import io.realm.RealmResults
import java.io.IOException

class SoundBoardItemAdapter(private val activity: AppCompatActivity, var SoundBoardItems: RealmResults<SoundBoardItem>) : RealmRecyclerViewAdapter<SoundBoardItem?, SoundBoardItemAdapter.SoundBoardItemViewHolder?>(SoundBoardItems, true) {

    private lateinit var parentViewModel: MainActivityViewModel

    inner class SoundBoardItemViewHolder(var binding: SoundboardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundBoardItemViewHolder {
        val binding = SoundboardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        parentViewModel = ViewModelProvider(activity)[MainActivityViewModel::class.java]
        return SoundBoardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundBoardItemViewHolder, position: Int) {
        SoundBoardItems[position]?.let { soundBoardItem ->
            val view = holder.binding

            if (soundBoardItem.image.isNotEmpty()) {
                view.icon.setImageURI(Uri.parse(soundBoardItem.image))
                view.icon.visibility = View.VISIBLE
                view.iconAlt.visibility = View.GONE
            } else {
                view.iconAlt.text = soundBoardItem.name.first().toString()
                view.iconAlt.visibility = View.VISIBLE
                view.icon.visibility = View.GONE
            }

            view.text.text = soundBoardItem.name

            view.root.setOnClickListener{
                try {
                    val audioPath = FileUtils.getRealPathFromURI(activity, Uri.parse(soundBoardItem.sound))
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(audioPath)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                } catch (ex: IOException) {
                    Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            view.root.setOnLongClickListener {
                val popup = androidx.appcompat.widget.PopupMenu(activity, it)
                popup.gravity = Gravity.END
                popup.inflate(R.menu.delete_menu)
                popup.setOnMenuItemClickListener { item: MenuItem ->
                    when (item.itemId) {
                        R.id.action_delete -> parentViewModel.deleteSoundboardItem(soundBoardItem.id)
                    }
                    false
                }
                popup.show()
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return SoundBoardItems.size
    }
}