package com.example.tareaevaluable2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tareaevaluable2.adapter.ProductoSeleccionadoAdapter
import com.example.tareaevaluable2.databinding.ActivityMainBinding
import com.example.tareaevaluable2.databinding.ActivitySecondBinding
import com.example.tareaevaluable2.model.Producto
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity(), ProductoSeleccionadoAdapter.OnRecyclerProductoSecondListener {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var listaProductosSeleccionados: ArrayList<Producto>
    private lateinit var adaptadorProductoSeleccionado: ProductoSeleccionadoAdapter

    private var precioTotal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarSecond)

        supportActionBar?.title = "CARRITO"

        listaProductosSeleccionados =
            (intent.getSerializableExtra("ListaProductos") as? ArrayList<Producto>)!!

        adaptadorProductoSeleccionado = ProductoSeleccionadoAdapter(listaProductosSeleccionados, this)

        binding.recyclerProductosSecond.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductosSecond.adapter = adaptadorProductoSeleccionado
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.confirmacion_compra -> {
                Snackbar.make(
                    binding.root,
                    "Compra confirmada. Total: ${precioTotal} €.",
                    Snackbar.LENGTH_SHORT
                ).setAction("Cerrar") {
                    finish()
                }.show()
                listaProductosSeleccionados.removeAll(listaProductosSeleccionados)
                listaProductosSeleccionados.clear()
                adaptadorProductoSeleccionado.notifyDataSetChanged()
            }
            R.id.cancelar_Compra -> {
                Snackbar.make(
                    binding.root,
                    "Carrito vacío.",
                    Snackbar.LENGTH_SHORT
                ).show()
                listaProductosSeleccionados.removeAll(listaProductosSeleccionados)
                listaProductosSeleccionados.clear()
                adaptadorProductoSeleccionado.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onProductoSeleccionadoSelected(producto: Producto) {
        val posicion = adaptadorProductoSeleccionado.itemCount
        listaProductosSeleccionados.remove(producto)
        adaptadorProductoSeleccionado.notifyDataSetChanged()
    }
}

