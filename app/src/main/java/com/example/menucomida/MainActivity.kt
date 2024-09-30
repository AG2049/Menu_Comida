package com.example.menucomida

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import android.widget.SearchView

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<String>
    lateinit var foodNames: Array<String>
    // Lista temporal
    lateinit var filteredFoodNames: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Listas de datos
        foodNames = arrayOf("Tacos", "Sushi", "Pizza", "Paella", "Ceviche", "Curry", "Empanadas", "Moussaka", "Hamburguesa", "Pho")
        val comidaDescripcion = arrayOf(
            "Un plato tradicional de México hecho con tortillas y rellenos variados.",
            "Plato tradicional japonés de pescado crudo y arroz.",
            "Un plato italiano hecho con una base de pan, salsa de tomate y queso.",
            "Plato de arroz típico de la gastronomía española.",
            "Un plato peruano hecho con pescado crudo y marinado con cítricos.",
            "Plato picante de la India hecho con especias y carne o verduras.",
            "Una masa rellena de carne o verduras, típica de Argentina.",
            "Plato griego hecho a base de berenjenas y carne.",
            "Un sándwich de carne a la parrilla, típico de Estados Unidos.",
            "Sopa de fideos y carne, típica de Vietnam."
        )
        val imagenesdecomida = arrayOf(R.drawable.img_tacos, R.drawable.img_sushi, R.drawable.img_pizza, R.drawable.img_paella, R.drawable.img_ceviche, R.drawable.img_curry, R.drawable.img_empanadas, R.drawable.img_moussaka, R.drawable.img_hamburguesa, R.drawable.img_pho)

        val listView: ListView = findViewById(R.id.listView)
        val searchView: SearchView = findViewById(R.id.searchView)

        // Lista con platillos
        filteredFoodNames = ArrayList(foodNames.toList())

        // ArrayAdapter para mostrar los nombres de los platillos en el ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredFoodNames)
        listView.adapter = adapter

        // clic en cada ítem de la lista
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFoodName = filteredFoodNames[position]
            // funcion del array para tener la posición original
            val originalPosition = foodNames.indexOf(selectedFoodName)
            showFoodDialog(foodNames[originalPosition], comidaDescripcion[originalPosition], imagenesdecomida[originalPosition])
        }

        // Configuracion del SearchView para filtrar los platillos por nombre
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText ?: "")
                return true
            }
        })
    }

    // Funcion para mostrar el dialogo con detalles del platillo
    private fun showFoodDialog(name: String, description: String, image: Int) {
        // LinearLayout para contener la vista del diálogo
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        // ImageView para mostrar la imagen del platillo
        val foodImageView = ImageView(this).apply {
            setImageResource(image)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500)
        }

        // TextView para mostrar la descripción del platillo
        val foodDescription = TextView(this).apply {
            text = description
            textSize = 16f
            setPadding(0, 40, 0, 40)
        }

        // Boton de Compartir
        val shareButton = Button(this).apply {
            text = "Compartir"
            setOnClickListener {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$name: $description")
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Compartir con"))
            }
        }

        // Añadir la imagen, descripcion y boton al layout del dialogo
        dialogLayout.addView(foodImageView)
        dialogLayout.addView(foodDescription)
        dialogLayout.addView(shareButton)

        // Dialogo del boton
        val dialog = AlertDialog.Builder(this)
            .setTitle(name)
            .setView(dialogLayout)
            .setCancelable(true)
            .create()

        dialog.show()
    }

    // Funcion que filtra la lista según el texto de búsqueda
    private fun filterList(query: String) {
        filteredFoodNames.clear()
        if (query.isEmpty()) {
            filteredFoodNames.addAll(foodNames)
        } else {
            for (food in foodNames) {
                if (food.toLowerCase().contains(query.toLowerCase())) {
                    filteredFoodNames.add(food)
                }
            }
        }
        // Actualiza la nueva lista filtrada
        adapter.notifyDataSetChanged()
    }
}
