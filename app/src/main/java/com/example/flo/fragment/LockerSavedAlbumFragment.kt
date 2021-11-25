package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.adapter.LockerSavedAlbumRvAdapter
import com.example.flo.data.Album
import com.example.flo.data.SongDB
import com.example.flo.databinding.FragmentLockerSavedAlbumBinding

class LockerSavedAlbumFragment : Fragment() {

    lateinit var binding : FragmentLockerSavedAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val jwt = getJwt()
        val songDB = SongDB.getInstance(requireContext())!!
        val albumData = songDB.AlbumDao().getLikedAlbums(jwt) as ArrayList<Album>
        val mAdapter = LockerSavedAlbumRvAdapter(albumData)

        binding.lockerSavedAlbumRv.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun getJwt():Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf?.getInt("jwt", 0)!!
    }
}