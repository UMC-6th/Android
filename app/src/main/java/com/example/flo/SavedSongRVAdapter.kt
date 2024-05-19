package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSongBinding

class SavedSongRVAdapter(private val albumList: ArrayList<Song>): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    interface MyItemClickListner{
        fun onItemClick(song: Song)
        fun onRemoveSong(position: Int)
    }

    private lateinit var mItemClickListener:MyItemClickListner
    fun setMyItemClickListner(itemClickListener:MyItemClickListner){
        mItemClickListener = itemClickListener
    }

    fun addItem(song: Song){
        albumList.add(song)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = albumList.size

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.itemSongMoreIv.setOnClickListener{mItemClickListener.onRemoveSong(position)}
    }

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemSongTitleTv.text= song.title
            binding.itemSongSingerTv.text= song.singer
            binding.itemSongImgIv.setImageResource(song.coverImg!!)
        }
    }
}