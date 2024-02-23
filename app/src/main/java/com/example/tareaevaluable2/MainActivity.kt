package com.example.tareaevaluable2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tareaevaluable2.adapter.ProductosAdapter
import com.example.tareaevaluable2.databinding.ActivityMainBinding
import com.example.tareaevaluable2.model.Producto
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray

class MainActivity : AppCompatActivity(), ProductosAdapter.OnRecyclerProductoListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptadorProducto: ProductosAdapter
    private lateinit var listaProducto: ArrayList<Producto>
    private var listaProductosSeleccionados = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        listaProducto = ArrayList()
        adaptadorProducto = ProductosAdapter(listaProducto, this)

        supportActionBar?.title = "BOUTIQUE JOAQUINITO17"
        rellenarLista()
        rellenarSpinner()

        binding.listaProductos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.listaProductos.adapter = adaptadorProducto

        binding.spinnerCategorias.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val categoriaSeleccionada = parent!!.getItemAtPosition(position).toString()
                adaptadorProducto.filtrarLista(categoriaSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.carritoCompra -> {
                val datosRecibidos = intent.getSerializableExtra("listaCarrito") as? ArrayList<Producto>
                val intent = Intent (this, SecondActivity::class.java)
                Log.v("dat", datosRecibidos.toString())
                if (datosRecibidos != null) {
                    for (i in datosRecibidos.indices) {
                        val pro = datosRecibidos[i]
                        listaProductosSeleccionados.add(pro)
                    }
                    intent.putExtra("ListaProductos", ArrayList(listaProductosSeleccionados))
                } else {
                    intent.putExtra("ListaProductos", ArrayList(listaProductosSeleccionados))
                }

                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun rellenarLista() {
        var peticion: JsonObjectRequest = JsonObjectRequest("https://dummyjson.com/products",
            {
                var products: JSONArray = it.getJSONArray("products")
                for (i in 0 until products.length()) {
                    val product = products.getJSONObject(i)
                    val producto: Producto = Producto (
                        product.getString("title"),
                        product.getString("category"),
                        product.getString("thumbnail"),
                        product.getInt("price")
                    )
                    adaptadorProducto.añadirProducto(producto)
                }
            }, {
                Snackbar.make(
                    binding.root,
                    "Error en la lectura del archivo.",
                    Snackbar.LENGTH_SHORT
                ).show()
            })
        Volley.newRequestQueue(applicationContext).add(peticion)
    }

    fun rellenarSpinner() {
        val categoriasList = ArrayList<String>()

        val peticion = JsonArrayRequest(
            Request.Method.GET,
            "https://dummyjson.com/products/categories",
            null,
            { response ->
                for (i in 0 until response.length()) {
                    val cat = response.getString(i)
                    categoriasList.add(cat)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriasList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategorias.adapter = adapter
            },
            { error ->
                Snackbar.make(
                    binding.root,
                    "Error en la lectura de categorías: ${error.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            })

        Volley.newRequestQueue(applicationContext).add(peticion)
    }

    override fun onProductoSelected(producto: Producto) {
        listaProductosSeleccionados.add(producto)
    }
}

