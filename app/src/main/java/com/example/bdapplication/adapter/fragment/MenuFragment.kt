package com.example.bdapplication.adapter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bdapplication.adapter.MenuAdapter
import com.example.bdapplication.database.DatabaseHelper
import com.example.bdapplication.databinding.FragmentMenuBinding
import com.example.bdapplication.model.Menu
import org.json.JSONException

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        requestQueue = Volley.newRequestQueue(context)

        setUpRecycler()

        // Fetch data from API
        binding.btnGetData.setOnClickListener {
            val url = "https://api.npoint.io/965e4b9a2d363c3e143c"
            val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    databaseHelper.deleteAllMenus()
                    val menuArray = response.getJSONArray("menus")
                    for (i in 0 until menuArray.length()) {
                        val menuObject = menuArray.getJSONObject(i)
                        val menu = Menu(
                            0,
                            menuObject.getString("name"),
                            menuObject.getDouble("price"),
                        )
                        databaseHelper.insertMenu(menu)
                    }
                    setUpRecycler()
                } catch (e: JSONException) {
                    Toast.makeText(context, "Error fetching menu data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                Log.e("Volley error", error.toString())
            })
            requestQueue.add(request)
        }

        // Add menu item
        binding.btnAdd.setOnClickListener {
            val name = binding.etMenuName.text.toString()
            val priceText = binding.etMenuPrice.text.toString()

            if (name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val price = priceText.toDouble()
                val menu = Menu(0, name, price)
                databaseHelper.insertMenu(menu)
                resetUI()
                setUpRecycler()
            }
        }

        // Update menu item
        binding.btnUpdate.setOnClickListener {
            val idText = binding.etMenuId.text.toString()
            val name = binding.etMenuName.text.toString()
            val priceText = binding.etMenuPrice.text.toString()

            if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val id = idText.toInt()
                val price = priceText.toDouble()
                val menu = Menu(id, name, price)
                databaseHelper.updateMenu(menu)
                resetUI()
                setUpRecycler()
            }
        }

        // Delete menu item
        binding.btnDelete.setOnClickListener {
            val idText = binding.etMenuId.text.toString()
            if (idText.isEmpty()) {
                Toast.makeText(context, "Please enter Menu ID to delete", Toast.LENGTH_SHORT).show()
            } else {
                databaseHelper.deleteMenu(idText)
                resetUI()
                setUpRecycler()
            }
        }

        return binding.root
    }

    private fun resetUI() {
        binding.etMenuId.text.clear()
        binding.etMenuName.text.clear()
        binding.etMenuPrice.text.clear()
    }

    private fun setUpRecycler() {
        val menus = databaseHelper.getMenus()
        menuAdapter = MenuAdapter(requireContext(), menus)
        binding.rvMenuList.adapter = menuAdapter
        binding.rvMenuList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
