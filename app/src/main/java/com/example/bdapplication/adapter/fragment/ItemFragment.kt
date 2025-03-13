package com.example.bdapplication.adapter.fragment

import android.os.Bundle
import android.util.Log
import android.view.InputQueue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bdapplication.R
import com.example.bdapplication.adapter.ItemAdapter
import com.example.bdapplication.database.DatabaseHelper
//import com.example.bdapplication.database.FirebaseRealtimeDatabaseHelper
import com.example.bdapplication.databinding.FragmentItemBinding
import com.example.bdapplication.model.Item
import org.json.JSONException


class ItemFragment : Fragment() {

    private lateinit var binding: FragmentItemBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var databaseHelper: DatabaseHelper
//    private lateinit var firebaseDatabaseHelper: FirebaseRealtimeDatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(layoutInflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())
//        firebaseDatabaseHelper = FirebaseRealtimeDatabaseHelper()
        requestQueue = Volley.newRequestQueue(context)

        setUpRecycler()

        binding.btnGetData.setOnClickListener {
            val url = "https://api.npoint.io/965e4b9a2d363c3e143c"
            val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
//                    databaseHelper.deleteAllItems()
                    databaseHelper.deleteAllItem()
                    val itemArray = response.getJSONArray("ingredients")
                    for(i in 0 until itemArray.length()){
                        val itemObject = itemArray.getJSONObject(i)
                        val item = Item(
                            0,
                            itemObject.getString("name"),
                            itemObject.getString("description")
                        )
                        databaseHelper.insert(item)
                    }

//                    firebaseDatabaseHelper.deleteAllItems()
//                    val itemArray = response.getJSONArray("ingredients")
//                    for(i in 0 until itemArray.length()){
//                        val itemObject = itemArray.getJSONObject(i)
//                        firebaseDatabaseHelper.insertItem(
//                            itemObject.getString("name"),
//                            itemObject.getString("description"))
//                    }


                } catch (e: JSONException) {
                    Toast.makeText(context, "Get data from url error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Log.e("Volley error", error.toString())
            })
            requestQueue.add(request)
        }

        /*TODO: DATA INPUT VALIDATION*/
        binding.btnAdd.setOnClickListener {
            val name = binding.etItemName.text.toString()
            val description = binding.etItemDescription.text.toString()
            databaseHelper.insert(Item(0, name, description))
//            firebaseDatabaseHelper.insertItem(name, description)
            setUpRecycler()
        }

        binding.btnUpdate.setOnClickListener {
            /*TODO: Validasi if the id is numeric or not*/
            val id = binding.etItemId.text.toString()
            val name = binding.etItemName.text.toString()
            val description = binding.etItemDescription.text.toString()
            databaseHelper.updateItem(Item(id.toInt(), name, description))
//            firebaseDatabaseHelper.updateItem(id, name, description)
            resetUI()
            setUpRecycler()
        }

        binding.btnDelete.setOnClickListener {
            val id = binding.etItemId.text.toString()
            databaseHelper.deleteItem(id)
//            firebaseDatabaseHelper.deleteItem(id)
            resetUI()
            setUpRecycler()
        }


        return binding.root
    }

    private fun resetUI() {
        binding.etItemId.text.clear()
        binding.etItemName.text.clear()
        binding.etItemDescription.text.clear()
    }

    private fun setUpRecycler() {
        val items = databaseHelper.getItems()
        itemAdapter = ItemAdapter(requireContext(), items)
        binding.rvItemList.adapter = itemAdapter
        binding.rvItemList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

//        firebaseDatabaseHelper.getItems { items ->
//            itemAdapter = ItemAdapter(requireContext(), items)
//            binding.rvItemList.adapter = itemAdapter
//            binding.rvItemList.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        }

    }


}