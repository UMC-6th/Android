package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, albumList.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumLockerRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumLockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])

        holder.binding.itemAlbumMoreIv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    // ViewHolder 클래스
    inner class ViewHolder(val binding: ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumImgIv.setImageResource(album.coverImg!!)
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
        }
    }
}
