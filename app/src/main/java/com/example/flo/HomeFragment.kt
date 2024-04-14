package com.example.flo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeOneulMusicAlbum1st.setOnClickListener {
            initAlbumFragment(
                binding.homeOneulPlaySong1stTitle.text.toString(),
                binding.homeOneulPlaySong1stSinger.text.toString()
            )
        }
        binding.homeOneulMusicAlbum2nd.setOnClickListener {
            initAlbumFragment(
                binding.homeOneulPlaySong2ndTitle.text.toString(),
                binding.homeOneulPlaySong2ndSinger.text.toString()
            )
        }
        return binding.root
    }

    private fun initAlbumFragment(titleTV: String, singerTV: String) {
        with(binding) {
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("musicTitle", titleTV)
                    putString("musicSinger", singerTV)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment).addToBackStack(null).commit()
        }
    }
}
