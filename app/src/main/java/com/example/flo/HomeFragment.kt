import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.Album
import com.example.flo.AlbumFragment
import com.example.flo.AlbumRVAdapter
import com.example.flo.BannerFragment
import com.example.flo.BannerVPAdapter
import com.example.flo.ImageFragment
import com.example.flo.ImageVPAdapter
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.Song
import com.example.flo.SongDatabase
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var sliderHandler: Handler
    private var sliderRunnable: Runnable? = null

    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("albumlist", albumDatas.toString())

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)


        return binding.root
    }

    interface OnPlayClickListener {
        fun onPlayClick(songData: Int)
    }

    private var listener: OnPlayClickListener? = null

    fun onPlayClick(album: Album) {
        Log.d("id찾기", "${album.id}")

        listener?.onPlayClick(album.id) // MainActivity로 AlbumId 전달
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.homeOneulMusicAlbum1st.setOnClickListener {
//            initAlbumFragment(
//                binding.homeOneulPlaySong1stTitle.text.toString(),
//                binding.homeOneulPlaySong1stSinger.text.toString()
//            )
//        }
//        binding.homeOneulMusicAlbum2nd.setOnClickListener {
//            initAlbumFragment(
//                binding.homeOneulPlaySong2ndTitle.text.toString(),
//                binding.homeOneulPlaySong2ndSinger.text.toString()
//            )
//        }

        // 데이터 리스트 생성 더미 데이터
        fun inputDummyAlbums(){
            val songDB = SongDatabase.getInstance(requireActivity())!!
            val songs = songDB.albumDao().getAlbums()

            if (songs.isNotEmpty()) return

            songDB.albumDao().insert(
                Album(
                    "IU 5th Album 'LILAC'",
                    "아이유 (IU)",
                    R.drawable.img_album_exp2,
                    1
                )
            )

            songDB.albumDao().insert(
                Album(
                    "Butter",
                    "방탄소년단 (BTS)",
                    R.drawable.img_album_exp,
                    2
                )
            )

            songDB.albumDao().insert(
                Album(
                    "iScreaM Vol.10: Next Level Remixes",
                    "에스파 (AESPA)",
                    R.drawable.img_album_exp3,
                    3
                )
            )

            songDB.albumDao().insert(
                Album(
                    "Map of the Soul Persona",
                    "뮤직 보이 (Music Boy)",
                    R.drawable.img_album_exp4,
                    4
                )
            )


            songDB.albumDao().insert(
                Album(
                    "Great!",
                    "모모랜드 (MOMOLAND)",
                    R.drawable.img_album_exp5,
                    5
                )
            )

            val songDBData = songDB.albumDao().getAlbums()
            Log.d("DB data", songDBData.toString())
        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayMusicAlbumRv.layoutManager= manager



        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                initAlbumFragment(album.title.toString(), album.singer.toString(), album.coverImg.toString().toInt())
            }

            override fun onPlayClick(album: Album) {
                Log.d("id찾기", "${album.id}")

                listener?.onPlayClick(album.id) // MainActivity로 AlbumId 전달
            }

//            override fun onRemoveAlbum(position: Int) {
//                albumRVAdapter.removeItem(position)
//            }
        })


        binding.homePannelAlbumImg1Iv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm , AlbumFragment())
                .commitAllowingStateLoss()
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ImageVPAdapter 생성 및 프래그먼트 추가
        val imageAdapter = ImageVPAdapter(this)
        imageAdapter.addFragment(ImageFragment(R.drawable.img_first_album_default))
        imageAdapter.addFragment(ImageFragment(R.drawable.img_album_exp3))
        imageAdapter.addFragment(ImageFragment(R.drawable.img_album_exp5))
        imageAdapter.addFragment(ImageFragment(R.drawable.img_album_exp6))

        // Handler 및 Runnable 초기화
        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = object : Runnable {
            override fun run() {
                val viewPager = binding.homePannelBackgroundVp
                val indicator = binding.homePannelIndicatorCi
                indicator.setViewPager(viewPager)

                // 현재 아이템 변경
                viewPager.currentItem =
                    if (viewPager.currentItem < imageAdapter.itemCount - 1) {
                        viewPager.currentItem + 1
                    } else {
                        0
                    }

                sliderHandler.postDelayed(this, 3000L) // 다음 슬라이드 예약
            }
        }

        // ViewPager2 설정
        binding.homePannelBackgroundVp.adapter = imageAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homePannelBackgroundVp.offscreenPageLimit = imageAdapter.itemCount

        sliderHandler.post(sliderRunnable!!)

    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment가 Destroy되기 전에 Runnable을 제거하여 메모리 누수를 방지합니다.
        sliderRunnable?.let { sliderHandler.removeCallbacks(it) }
    }

    private fun initAlbumFragment(titleTV: String, singerTV: String, toInt: Int) {
        with(binding) {
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("musicTitle", titleTV)
                    putString("musicSinger", singerTV)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}

