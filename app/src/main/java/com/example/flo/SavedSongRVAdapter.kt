package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSongBinding

class SavedSongRVAdapter(private val albumList: ArrayList<Song>) : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(song: Song)
        fun onRemoveSong(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }
    fun addItem(song: Song) {
        albumList.add(song)
        notifyItemInserted(albumList.size)
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = albumList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(position)
        }
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }
        holder.binding.itemSongMoreIv.setOnClickListener{mItemClickListener.onRemoveSong(position)}
    }

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSongTitleTv.text = song.title ?: ""
            binding.itemSongSingerTv.text = song.singer ?: ""
            // coverImg가 null이 아닌 경우 이미지 설정
            song.coverImg?.let { coverImg ->
                binding.itemSongImgIv.setImageResource(coverImg)
            } ?: run {
                // coverImg가 null일 때 기본 이미지 설정
                binding.itemSongImgIv.setImageResource(R.drawable.img_album_exp2)
            }
        }
    }
}
