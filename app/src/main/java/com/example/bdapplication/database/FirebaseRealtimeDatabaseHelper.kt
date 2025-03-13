//package com.example.bdapplication.database
//
//import com.example.bdapplication.model.Item
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class FirebaseRealtimeDatabaseHelper {
//    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference
//
//    fun insertItem(name: String, description: String) {
//        val id = db.push().key.toString()
//        db.child("ingredients").child(id).setValue(Item(id, name, description))
//    }
//
//    fun getItems(callback: (List<Item>) -> Unit) {
//        db.child("ingredients").addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val items = arrayListOf<Item>()
//                for (child in snapshot.children) {
//                    val item = child.getValue(Item::class.java)
//                    item?.let { items.add(item) }
//                }
//                callback(items)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                callback(emptyList())
//            }
//        })
//    }
//
//    fun updateItem(id: String, name: String, description: String){
//        db.child("ingredients").child(id).setValue(Item(id, name, description))
//    }
//
//    fun deleteItem(id: String) {
//        db.child("ingredients").child(id).removeValue()
//    }
//
//    fun deleteAllItems() {
//        db.child("ingredients").removeValue()
//    }
//
//}