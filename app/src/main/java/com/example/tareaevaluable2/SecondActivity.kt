package com.example.tareaevaluable2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            (intent.getSerializableExtra("ListaProductos") as ArrayList<Producto>)
        Log.v("cuenta", listaProductosSeleccionados.size.toString())
        adaptadorProductoSeleccionado = ProductoSeleccionadoAdapter(listaProductosSeleccionados, this)

        adaptadorProductoSeleccionado = ProductoSeleccionadoAdapter(listaProductosSeleccionados, this)

        binding.recyclerProductosSecond.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductosSecond.adapter = adaptadorProductoSeleccionado
        binding.textViewResultadoCompras.text = actualizarPrecio().toString()
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
                binding.textViewResultadoCompras.text = "0"
            }
            R.id.cancelar_Compra -> {
                Snackbar.make(
                    binding.root,
                    "El carrito está vacío.",
                    Snackbar.LENGTH_SHORT
                ).show()
                listaProductosSeleccionados.removeAll(listaProductosSeleccionados)
                listaProductosSeleccionados.clear()
                adaptadorProductoSeleccionado.notifyDataSetChanged()
                binding.textViewResultadoCompras.text = "0"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onProductoSeleccionadoSelected(producto: Producto) {

        listaProductosSeleccionados.remove(producto)
        adaptadorProductoSeleccionado.notifyDataSetChanged()

        var precioActual: Int = binding.textViewResultadoCompras.text.toString().toInt()
        var precioProducto: Int = producto.precio
        var resultado = precioActual - precioProducto

        binding.textViewResultadoCompras.text = resultado.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val listaCarrito = listaProductosSeleccionados.clone() as ArrayList<Producto>
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("listaCarrito", ArrayList(listaCarrito))
        startActivity(intent)
    }

    fun actualizarPrecio (): Int{
        for (i in 0 until adaptadorProductoSeleccionado.listaSeleccionado?.size!!){
            var producto = adaptadorProductoSeleccionado.listaSeleccionado.get(i)
            var precio =  producto.precio
            precioTotal += precio
        }
        return precioTotal
    }

}

