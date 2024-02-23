package com.example.tareaevaluable2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tareaevaluable2.R
import com.example.tareaevaluable2.model.Producto

class ProductosAdapter (var listaProductos: ArrayList<Producto>, var context: Context): RecyclerView.Adapter<ProductosAdapter.MyHolder>() {
    private var listaFiltrada: ArrayList<Producto> = ArrayList(listaProductos)

    private lateinit var listener: OnRecyclerProductoListener
    init {
        listener = context as OnRecyclerProductoListener
    }

    class MyHolder (item: View): RecyclerView.ViewHolder(item) {
        lateinit var imagen: ImageView
        lateinit var nombreProducto: TextView
        lateinit var precioProducto: TextView
        lateinit var botonAñadir: Button

        init {
            imagen = item.findViewById(R.id.item_imagen_main)
            nombreProducto = item.findViewById(R.id.item_nombre_main)
            precioProducto = item.findViewById(R.id.item_precio_main)
            botonAñadir = item.findViewById(R.id.item_botonAñadir_main)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val vista: View = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
        return MyHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaFiltrada[position]
        Glide.with(context).load(producto.imagen).into(holder.imagen)
        holder.nombreProducto.text = producto.nombre
        holder.precioProducto.text = producto.precio.toString() + "€."
        holder.botonAñadir.setOnClickListener() {
            listener.onProductoSelected(producto)
        }
    }

    fun añadirProducto (producto: Producto) {
        listaProductos.add(producto)
        listaFiltrada.add(producto)
        notifyItemInserted(listaFiltrada.size -1)
    }

    fun filtrarLista(categoria: String) {
        listaFiltrada =
            if (categoria.isEmpty()) {
                ArrayList(listaProductos)
            } else {
                ArrayList(
                    listaProductos.filter {
                        it.categoria.equals(categoria, ignoreCase = true)
                    }
                )
            }
        notifyDataSetChanged()
    }

    interface OnRecyclerProductoListener {
        fun onProductoSelected (producto: Producto)
    }

}