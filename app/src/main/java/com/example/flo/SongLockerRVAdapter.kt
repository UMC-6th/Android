
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.Album
import com.example.flo.databinding.ItemSongBinding

class SongLockerRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<SongLockerRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onRemoveSong(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongLockerRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongLockerRVAdapter.ViewHolder, position: Int) {
//        val song = albumList[position]
//        holder.bind(song)

        holder.bind(albumList[position])

        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(position)
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemSongTitleTv.text = album.title
            binding.itemSongSingerTv.text = album.singer
            binding.itemSongImgIv.setImageResource(album.coverImg!!)
        }
    }
}
