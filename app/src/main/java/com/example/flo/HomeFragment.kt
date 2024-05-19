package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeMainAlbumImg1Iv.setOnClickListener {
//            initAlbumFragment(
//                binding.homeMainAlbumTitleTv.text.toString(),
//                binding.homeMainAlbumSingerTv.text.toString()
//            )
//        }
//        binding.homeMainAlbumImg2Iv.setOnClickListener {
//            initAlbumFragment(
//                binding.homeMainAlbumTitle2Tv.text.toString(),
//                binding.homeMainAlbumSinger2Tv.text.toString()
//            )
//        }

        //데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("Butter","방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_1))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        }


        // 더미데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)

        // 리사이클러뷰에 어댑터를 연결
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album){
                changeAlbumFragment(album)
            }

//            override fun onRemoveAlbum(position: Int) {
//                albumRVAdapter.removeItem(position)
//            }
        })


        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicatorAdapter = IndicatorVPAdapter(this)
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_first_album_default))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp4))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp3))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp5))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp6))
        binding.homePannelBackgroundVp.adapter = indicatorAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.Indicator;
        indicator.setViewPager(binding.homePannelBackgroundVp);
        autoSlide()

        return binding.root
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

    private var sliderHandler = Handler(Looper.getMainLooper())
    private var sliderRunnable: Runnable? = null

    private fun autoSlide() {
        sliderRunnable = Runnable {
            val viewPager = binding.homePannelBackgroundVp
            viewPager.adapter?.let { adapter ->
                viewPager.currentItem =
                    if (viewPager.currentItem < adapter.itemCount - 1) {
                        viewPager.currentItem + 1
                    } else {
                        0
                    }
            }
            sliderHandler.postDelayed(sliderRunnable!!, 3000)
        }
        sliderHandler.post(sliderRunnable!!)
    }

    private fun initAlbumFragment(titleTV : String, singerTV : String) {
        with(binding) {
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("albumTitle", titleTV)
                    putString("albumSinger", singerTV)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}

