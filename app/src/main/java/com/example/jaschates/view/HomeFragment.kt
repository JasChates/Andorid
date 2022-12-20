package com.example.jaschates.view

import android.annotation.SuppressLint
import android.app.LauncherActivity.ListItem
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.jaschates.R
import com.example.jaschates.adapter.RandomChatRecyclerAdapter
import com.example.jaschates.data.ChatRoomModel
import com.example.jaschates.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.createRandomChatRoomImage.setOnClickListener {
            startActivity(Intent(context, CreateChatRoomActivity::class.java))
        }
        createRecyclerView()
        return binding.root
    }

    private fun createRecyclerView() {
        database.child("randomChat").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomModel: ArrayList<ChatRoomModel> = arrayListOf()
                for (snapshot in snapshot.children) {
                    val item = snapshot.getValue(ChatRoomModel::class.java)
                    chatRoomModel.add(item!!)
                }

                val adapter = RandomChatRecyclerAdapter(chatRoomModel, requireContext())
                binding.homeRecycler.adapter = adapter
                binding.homeRecycler.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                adapter.setItemClickListener(object: RandomChatRecyclerAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {
                        val intent = Intent(context, RandomChatActivity::class.java)
                        startActivity(intent)
                    }
                })
            }


            override fun onCancelled(error: DatabaseError) {}


        })
    }
}
